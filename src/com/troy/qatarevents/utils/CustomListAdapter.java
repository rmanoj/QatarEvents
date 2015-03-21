package com.troy.qatarevents.utils;

import java.util.List;

import com.troy.qatarevents.Events;
import com.troy.qatarevents.R;
import com.troy.qatarevents.utils.imagefromurl.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class CustomListAdapter extends BaseAdapter {
    Context context;
    List<Events> rowItems;
    ImageLoader imgLoader;
    Utils utils;
    public CustomListAdapter(Context context, List<Events> items) {
        this.context = context;
        this.rowItems = items;
        imgLoader = new ImageLoader(context.getApplicationContext());
    }
     
    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDateTimePlace;
        TextView cat_line;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
         
        LayoutInflater mInflater = (LayoutInflater) 
            context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.listfilter_events_layout, null);
            holder = new ViewHolder();
            holder.txtDateTimePlace = (TextView) convertView.findViewById(R.id.event_date_time_place);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.event_title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.event_icon);
	  		holder.cat_line = (TextView) convertView.findViewById(R.id.cat_line);
	  		holder.cat_line.setVisibility(View.GONE);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
         
        Events rowItem = (Events) getItem(position);
        holder.txtDateTimePlace.setText(rowItem.getDateTimePlace());
        holder.txtTitle.setText(rowItem.getTitle());
        int loader = R.drawable.ic_launcher;
        imgLoader.DisplayImage(rowItem.getEventsIconUrl(), loader, holder.imageView);
       // holder.imageView.setImageBitmap(rowItem.getBitmapImage());
        
        //convertView.setOnI
        return convertView;
    }
 
    @Override
    public int getCount() {     
        return rowItems.size();
    }
 
    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }
 
    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}