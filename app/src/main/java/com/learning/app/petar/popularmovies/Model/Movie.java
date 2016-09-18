package com.learning.app.petar.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.NumberFormat;

/**
 * Created by Petar on 16.9.2016..
 */
public class Movie implements Parcelable {
    private String mPoster;
    private String mOverview;
    private String mReleaseDate;
    private String mTitle;
    private String mBackdrop;
    private double mAverageVote;
    private long mId;
    public Movie(){}

    //PARCELABLE DATA
    protected Movie(Parcel in) {
        mPoster = in.readString();
        mOverview= in.readString();
        mReleaseDate = in.readString();
        mTitle= in.readString();
        mBackdrop= in.readString();
        mAverageVote= in.readDouble();
        mId = in.readLong();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPoster);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mTitle);
        parcel.writeString(mBackdrop);
        parcel.writeDouble(mAverageVote);
        parcel.writeLong(mId);
    }

    //get and set
    public String getImage() {
        return mPoster;
    }

    public void setImage(String image) {
        mPoster = image;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public void setBackdrop(String backdrop) {
        mBackdrop = backdrop;
    }

    public String getAverageVote()
    {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(1);
        return formatter.format(mAverageVote);
    }

    public void setAverageVote(double averageVote) {
        mAverageVote = averageVote;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }
}

