package com.troy.qatarevents;

import com.troy.qatarevents.utils.Utils;

import android.graphics.Bitmap;
 
public class Events {
    private String title, EventsIconUrl,dateTimePlace,catName;
    Utils utils;
    //Bitmap mIcon;
    //,Bitmap bitmap
    public Events(String tit,String eventsiconurl,String datetimeplace,String catname){
    	this.title = tit;
    	this.EventsIconUrl = eventsiconurl;
    	this.dateTimePlace = datetimeplace;
    	this.catName = catname;
    	//this.mIcon = bitmap;
    }
    
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String name) {
        this.title = name;
    }
 
    public String getEventsIconUrl() {
        return EventsIconUrl;
    }
 
    public void setEventsIconUrl(String EventsIconUrl) {
        this.EventsIconUrl = EventsIconUrl;
    }
 
    public String getDateTimePlace() {
        return dateTimePlace;
    }
 
    public void setDateTimePlace(String dateTimePlace) {
        this.dateTimePlace = dateTimePlace;
    }
    
    public void setCategoryName(String catName){
    	this.catName = catName;
    }
    
    public String getCategoryName(){
    	return catName;
    }
   /* public void setBitmapImage(String url){
    	this.mIcon = utils.getBitmapFromURL(url);
    }
    
    public Bitmap getBitmapImage(){
    	return mIcon;
    }*/
}