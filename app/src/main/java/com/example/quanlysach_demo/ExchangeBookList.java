package com.example.quanlysach_demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import com.example.quanlysach_demo.Database.Book;
import com.example.quanlysach_demo.Database.UserViewModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ExchangeBookList extends AppCompatActivity {
    private static List<Book> books;
    private int mOtherOwnerId;
    private int mOtherOwnerBookId;
    private Set<Integer> userBooks;
    private ListView mBookList;
    private UserViewModel mUserViewModel;
    private ArrayAdapter<Book> bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        //get list owner id
        mOtherOwnerId = getIntent().getIntExtra("otherOwnerId", -1);
        mOtherOwnerBookId = getIntent().getIntExtra("otherOwnerBookId", -1);

        //get user view model and load books
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        books = LoadBooks();

        //get list from display
        mBookList = findViewById(R.id.bookListDisplay);

        //create adapter and bind with list view
        this.bookAdapter = new ExchangeBookList.OwnedListAdapter(this, R.layout.customlist, new ArrayList<Book>());
        this.mBookList.setAdapter(bookAdapter);

        //clear adapter and add books
        bookAdapter.clear();
        bookAdapter.addAll(books);
        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.menu_search);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_search:
                Intent intentsearch = new Intent(this, Search.class);
                this.startActivity(intentsearch);
                break;
            case R.id.menu_profile:
                Intent intentprofile = new Intent(this, Profile.class);
                this.startActivity(intentprofile);
                break;
            case R.id.menu_booklist:
                Intent intentbooklist = new Intent(this, BookListActivity.class);
                this.startActivity(intentbooklist);
                break;
            case R.id.menu_wishlist:
                Intent intentwishlist = new Intent(this,WishListActivity.class);
                this.startActivity(intentwishlist);
                break;
            case R.id.menu_matches:
                Intent intentmatches = new Intent(this, Match.class);
                this.startActivity(intentmatches);
                break;
            case R.id.menu_users:
                Intent intentUsers = new Intent(this, UserListActivity.class);
                this.startActivity(intentUsers);
                break;
            case R.id.menu_logout:
                Toast.makeText(getApplicationContext(), "Logged Out!" , Toast.LENGTH_SHORT ).show();
                Intent intentlogout = new Intent(this, LoginActivity.class);
                this.startActivity(intentlogout);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    //load book pictures
    protected ArrayList<Book> LoadBooks()
    {
        //create array list for books
        ArrayList<Book> books = new ArrayList<Book>();

        List<Integer> booklist = mUserViewModel.getOwnedlist();
        Log.d("BookListActivityLog", "Book List Size: " + Integer.toString(booklist.size()));
        for (int i : booklist) {
            Log.d("BookListActivityLog", "Book List Item Book Id: " + Integer.toString(i));
            Book book = mUserViewModel.getBook(i);
            if (book != null) {
                books.add(book);
            }
        }

        return books;
    }

    //adapter for book display
    class OwnedListAdapter extends ArrayAdapter<Book> implements View.OnClickListener {
        public OwnedListAdapter(Context context, int resource, List<Book> objects) {
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
            Button removeBookListButton = convertView.findViewById(R.id.searchAddBookListButton);
            Button invisibleButton = convertView.findViewById(R.id.searchAddWishListButton);

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

            ExchangeBookList.OtherBookListButtonHandler buttonHandler = new ExchangeBookList.OtherBookListButtonHandler(book, getContext());

            //edit remove button
            removeBookListButton.setText("PICK");
            removeBookListButton.setOnClickListener(buttonHandler);
            invisibleButton.setVisibility(View.INVISIBLE);

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

        public void onClick(View v){
            Intent intent = new Intent(this.mContext, ExchangeActivity.class);
                intent.putExtra("ownerId", mUserViewModel.getCurrUserId());
                intent.putExtra("ownerBookId", mBook.getBookId());
                intent.putExtra("otherOwnerId", mOtherOwnerId);
                intent.putExtra("otherOwnerBookId", mOtherOwnerBookId);
                finish();
                this.mContext.startActivity(intent);
        }
    }
}
