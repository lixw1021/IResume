package com.example.xianweili.myapplication.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by xianwei li on 3/25/2017.
 */

public class Education implements Parcelable {
    public String id;
    public String school;
    public String major;
    public Date startDate;
    public Date endDate;
    public List<String> courses;

    public Education() {
        id = UUID.randomUUID().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.school);
        dest.writeString(this.major);
        dest.writeLong(this.startDate != null ? this.startDate.getTime() : -1);
        dest.writeLong(this.endDate != null ? this.endDate.getTime() : -1);
        dest.writeStringList(this.courses);
    }

    protected Education(Parcel in) {
        this.id = in.readString();
        this.school = in.readString();
        this.major = in.readString();
        long tmpStartDate = in.readLong();
        this.startDate = tmpStartDate == -1 ? null : new Date(tmpStartDate);
        long tmpEndDate = in.readLong();
        this.endDate = tmpEndDate == -1 ? null : new Date(tmpEndDate);
        this.courses = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Education> CREATOR = new Parcelable.Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel source) {
            return new Education(source);
        }

        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };
}
