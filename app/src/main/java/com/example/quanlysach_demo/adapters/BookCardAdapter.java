package com.example.quanlysach_demo.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.quanlysach_demo.R;
import com.example.quanlysach_demo.models.Book;

public class BookCardAdapter extends ArrayAdapter<Book> {

    public BookCardAdapter(Context context) {
        super(context, 0);
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;

        if (contentView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            contentView = inflater.inflate(R.layout.item_tourist_spot_card, parent, false);
            holder = new ViewHolder(contentView);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }

        Book spot = getItem(position);

        holder.name.setText(spot.name);
        holder.city.setText(spot.city);
        Glide.with(getContext()).load(spot.url).into(holder.image);

        return contentView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView city;
        public ImageView image;

        public ViewHolder(View view) {
            this.name =  view.findViewById(R.id.item_tourist_spot_card_name);
            this.city =  view.findViewById(R.id.item_tourist_spot_card_city);
            this.image = view.findViewById(R.id.item_tourist_spot_card_image);
        }
    }
}
