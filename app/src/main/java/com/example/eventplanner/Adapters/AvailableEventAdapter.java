package com.example.eventplanner.Adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.Objects.Event;
import com.example.eventplanner.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AvailableEventAdapter extends RecyclerView.Adapter<AvailableEventAdapter.ViewHolder> {

    private ArrayList<Event> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView availableAt,typeOfEvent,eventSizeRange,eventProviderName,eventHourOfTheDay;
        private ImageView eventImage;
        public ViewHolder(View view) {
            super(view);
            availableAt = view.findViewById(R.id.eventDate);
            typeOfEvent = view.findViewById(R.id.eventType);
            eventSizeRange = view.findViewById(R.id.eventSizeRange);
            eventProviderName = view.findViewById(R.id.eventProvider);
            eventImage = view.findViewById(R.id.eventImage);
            eventHourOfTheDay=view.findViewById(R.id.hourOfTheDay);
        }

        public TextView getEventHourOfTheDay() {
            return eventHourOfTheDay;
        }

        public TextView getAvailableAt() {
            return availableAt;
        }

        public TextView getTypeOfEvent() {
            return typeOfEvent;
        }

        public TextView getEventSizeRange() {
            return eventSizeRange;
        }

        public TextView getEventProviderName() {
            return eventProviderName;
        }

        public ImageView getEventImage() {
            return eventImage;
        }
    }



    public AvailableEventAdapter(ArrayList<Event>  dataSet) {
        this.localDataSet = dataSet;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public AvailableEventAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.eventcard, viewGroup, false);
        return new AvailableEventAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AvailableEventAdapter.ViewHolder holder, final int position) {
        String eventType="";
       holder.getAvailableAt().setText(localDataSet.get(position).getEventAvailableAt());
       holder.getEventProviderName().setText(localDataSet.get(position).getEventProviderName());
     holder.getEventSizeRange().setText(localDataSet.get(position).getEventSizeInRange());
   holder.getEventHourOfTheDay().setText(localDataSet.get(position).getEventHourOfTheDay());
   if(localDataSet.get(position).getImageUrl().toString().contains("http")){
       Picasso.get().load(localDataSet.get(position).getImageUrl()).into(holder.getEventImage());
   }
   else {
       holder.getEventImage().setImageURI(localDataSet.get(position).getImageUrl());
   }
   for(int i=0; i<localDataSet.get(position).getEventType().size(); i++){
       eventType=eventType+localDataSet.get(position).getEventType().get(i)+",";
   }
        holder.getTypeOfEvent().setText(eventType);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    public  void updateData(ArrayList<Event> updatedItemList) {
      localDataSet=updatedItemList;
        notifyDataSetChanged();
    }

}


