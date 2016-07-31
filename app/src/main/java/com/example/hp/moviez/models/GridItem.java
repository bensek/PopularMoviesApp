package com.example.hp.moviez.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by HP on 7/8/2016.
 */
public class GridItem implements Parcelable {
    private String image;
    private String title;
    private String overview;
    private String year;
    private String rating;
    private String id;
    private long _ID;
    private int isFavorite;
    private List<Trailer> trailersList;
    private List<Review> reviewsList;

    public List<Trailer> getTrailersList() {
        return trailersList;
    }

    public void setTrailersList(List<Trailer> trailersList) {
        this.trailersList = trailersList;
    }

    public GridItem(){
        super();
    }

    public GridItem(Parcel in){
        _ID = in.readLong();
        isFavorite = in.readInt();
        image = in.readString();
        title = in.readString();
        overview = in.readString();
        year = in.readString();
        rating = in.readString();
        id = in.readString();
        in.readList(trailersList, null);
        in.readList(reviewsList, null);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(_ID);
        dest.writeInt(isFavorite);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(year);
        dest.writeString(rating);
        dest.writeString(id);
        dest.writeList(trailersList);
        dest.writeList(reviewsList);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){

        @Override
        public GridItem createFromParcel(Parcel source) {
            return new GridItem(source);
        }

        @Override
        public GridItem[] newArray(int size) {
            return new GridItem[size];
        }
    };



    public String getImage(){
        return image;
    }
    public void setImage(String image){
        this.image = image;
    }

    public String getTitle(){
        return  title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getOverview(){
        return  overview;
    }
    public void setOverview(String overview){
        this.overview = overview;
    }

    public String getYear(){
        return  year;
    }
    public void setYear(String year){
        this.year = year;
    }

    public String getRating(){
        return rating;
    }
    public void setRating(String rating){
        this.rating = rating;
    }

    public String getId(){ return id; }
    public void setId(String id){this.id = id; }


    @Override
    public int describeContents() {
        return 0;
    }


    public List<Review> getReviewsList() {
        return reviewsList;
    }

    public void setReviewsList(List<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    public int isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public long get_ID() {
        return _ID;
    }

    public void set_ID(long _ID) {
        this._ID = _ID;
    }

}
