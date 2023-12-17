package com.example.eventplanner.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.eventplanner.Objects.BookedEvent;
import com.example.eventplanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookedEventRowAdapter extends BaseAdapter {
    private final ArrayList<BookedEvent> bookedLists;
    public BookedEventRowAdapter(ArrayList<BookedEvent> lists) {
        this.bookedLists= lists;
    }
    @Override
    public int getCount() {
        return bookedLists.size();
    }
    @Override
    public Object getItem(int i) {
        return i;
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(viewGroup.getContext(), R.layout.booked_event_list_row,null);
        TextView name = v.findViewById(R.id.bookerName);
        TextView size = v.findViewById(R.id.eventSize);
        TextView date = v.findViewById(R.id.eventDate);
        TextView bookedDate = v.findViewById(R.id.bookedDate);
        ImageView imageView=v.findViewById(R.id.imageView);
name.setText(bookedLists.get(i).getName());
size.setText("Number Of Guest:\t"+bookedLists.get(i).getSize());
date.setText("Booked For:\t"+bookedLists.get(i).getDate());
bookedDate.setText(bookedLists.get(i).getBookedDate());
Picasso.get().load(bookedLists.get(i).getImageUrl()).into(imageView);
        return v;
    }
public ArrayList<BookedEvent> getData(){
        return bookedLists;
}
}
