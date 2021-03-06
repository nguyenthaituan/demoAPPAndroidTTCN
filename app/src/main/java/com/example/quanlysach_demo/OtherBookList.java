package com.example.quanlysach_demo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.quanlysach_demo.Database.Book;
import com.example.quanlysach_demo.Database.UserViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class OtherBookList extends AppCompatActivity implements View.OnClickListener {
    private static List<Book> books;
    private int mUserID;
    private int mListOwnerID;
    private Set<Integer> userBooks;
    private ListView mBookList;
    private UserViewModel mUserViewModel;
    private ArrayAdapter<Book> bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_book_list);

        //get list owner id
        mListOwnerID = getIntent().getIntExtra("ownerID", -1);

        //get user view model and load books
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserID = mUserViewModel.getCurrUserId();
        LoadSet();
        books = LoadBooks();

        //get list from display
        mBookList = findViewById(R.id.otherBookListDisplay);

        //create adapter and bind with list view
        this.bookAdapter = new OtherBookListAdapter(this, R.layout.customlist, new ArrayList<Book>());
        this.mBookList.setAdapter(bookAdapter);

        //clear adapter and add books
        bookAdapter.clear();
        bookAdapter.addAll(books);
    }

    //onclick listener for for button
    @Override
    public void onClick(View v){

    }

    //load book pictures
    protected ArrayList<Book> LoadBooks()
    {
        //create array list for books
        ArrayList<Book> books = new ArrayList<Book>();

        List<Integer> otherList = mUserViewModel.getUserById(mListOwnerID).getOwnedList();
        Log.d("BookListActivityLog", "Book List Size: " + Integer.toString(otherList.size()));
        for (int i : otherList) {
            Log.d("BookListActivityLog", "Book List Item Book Id: " + Integer.toString(i));
            if (mUserViewModel.getBook(i) != null) {
                books.add(mUserViewModel.getBook(i));
            }
        }

        return books;
    }

    protected void LoadSet()
    {
        userBooks = new HashSet<Integer>();
        List<Integer> booklist = mUserViewModel.getOwnedlist();
        List<Integer> wishlist = mUserViewModel.getWishlist();
        for(int i : booklist)
        {
            if(!userBooks.contains(i))
            {
                userBooks.add(i);
            }
        }

        for(int i : wishlist)
        {
            if(!userBooks.contains(i))
            {
                userBooks.add(i);
            }
        }

    }

    //adapter for book display
    class OtherBookListAdapter extends ArrayAdapter<Book> implements View.OnClickListener {
        public OtherBookListAdapter(Context context, int resource, List<Book> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Book book = getItem(position);

            if(convertView == null) {
                convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.search_list_item, parent, false);
            }

            ImageView matchImage = convertView.findViewById(R.id.searchMatchImage);
            TextView matchUserId = convertView.findViewById(R.id.searchMatchUserId);
            TextView matchBookTitle = convertView.findViewById(R.id.searchMatchBookTitle);
            Button bookButton = convertView.findViewById(R.id.searchAddBookListButton);
            Button wishButton = convertView.findViewById(R.id.searchAddWishListButton);

            if (book.getBookPic() != null && !book.getBookPic().equals("")) {
                Picasso.get().load(book.getBookPic()).into(matchImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        System.out.println("Successfully loaded " + book.getBookPic());
                    }

                    @Override
                    public void onError(Exception e) {
                        System.out.println("Could not load " + book.getBookPic());
                    }

                });
            }

            //set book name
            matchUserId.setText(book.getBookName());

            //make author textView invisible
            matchBookTitle.setVisibility(View.INVISIBLE);

            OtherBookListButtonHandler buttonHandler = new OtherBookListButtonHandler(book, this.getContext());

            //add onclick listener to buttons
            bookButton.setOnClickListener(buttonHandler);
            wishButton.setOnClickListener(buttonHandler);
            wishButton.setText("Exchange");

            //make buttons invisible if book is already in a user list
            if(userBooks.contains(book.getBookId())){
                bookButton.setVisibility(View.INVISIBLE);
                wishButton.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        @Override
        public void onClick(View v) {

        }
    }

    class OtherBookListButtonHandler implements View.OnClickListener {
        private Book mBook;
        private Context mContext;

        public OtherBookListButtonHandler(Book b, Context context) {
            mBook = b;
            mContext = context;
        }

        @Override
        public void onClick(View v) {

            ViewGroup container = (ViewGroup)v.getParent();
            int bookID = -1;

            switch (v.getId()) {
                case R.id.searchAddBookListButton:
                    //add book to user book list
                    bookID = mBook.getBookId();
                    mUserViewModel.addOwnedList(bookID);
                    mUserViewModel.addOwnedUser(bookID, mUserID );

                    //make add and wish button invisible
                    v.setVisibility(View.INVISIBLE);
                    container.getChildAt(4).setVisibility(View.INVISIBLE);

                    Toast.makeText(getApplicationContext(), "Added book to Book List!", Toast.LENGTH_SHORT).show();
                    break;

                case R.id.searchAddWishListButton:
                    Intent intent = new Intent(this.mContext, ExchangeBookList.class);
                    intent.putExtra("otherOwnerId", mListOwnerID);
                    intent.putExtra("otherOwnerBookId", mBook.getBookId());
                    this.mContext.startActivity(intent);
                    break;

            }

        }
    }
}
