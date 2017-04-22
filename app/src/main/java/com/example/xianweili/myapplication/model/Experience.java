package com.example.xianweili.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by xianwei li on 4/16/2017.
 */

public class Experience implements Parcelable {
    public String id;
    public String company;
    public String title;
    public Date startDate;
    public Date endDate;
    public List<String> details;

    public Experience() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.company);
        dest.writeString(this.title);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
        dest.writeStringList(this.details);
    }


    protected Experience(Parcel in) {
        this.id = in.readString();
        this.company = in.readString();
        this.title = in.readString();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        this.details = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Experience> CREATOR = new Parcelable.Creator<Experience>() {
        @Override
        public Experience createFromParcel(Parcel source) {
            return new Experience(source);
        }

        @Override
        public Experience[] newArray(int size) {
            return new Experience[size];
        }
    };
}
