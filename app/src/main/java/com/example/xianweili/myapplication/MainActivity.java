package com.example.xianweili.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xianweili.myapplication.model.BasicInfo;
import com.example.xianweili.myapplication.model.Education;
import com.example.xianweili.myapplication.util.DateUtils;
import com.example.xianweili.myapplication.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BasicInfo basicInfo;
    private List<Education> educations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fakeData();
        setupUI();
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        setupBasicInfoUI();
        setupEducations();
    }

    private void setupBasicInfoUI() {
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    private void setupEducations(){
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
        for (Education education: educations){
            educationsLayout.addView(getEducationView(education));
        }
    }

    private View getEducationView(Education education) {
        View view = getLayoutInflater().inflate(R.layout.education_list, null);
        String dateString = DateUtils.dateToString(education.startDate) + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) view.findViewById(R.id.education_school)).setText(education.school + " (" + dateString + ")");
        ((TextView) view.findViewById(R.id.education_courses)).setText(formatItems(education.courses));
        return view;
    }

    public static String formatItems(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (String item: items) {
            sb.append(' ').append('-').append(' ').append(item).append('\n');
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void fakeData() {
        basicInfo = new BasicInfo();
        basicInfo.name = "xianwei li";
        basicInfo.email = "abc@gmail.com";

        Education education1 = new Education();
        education1.school = "pitt";
        education1.major = "ME";
        education1.startDate = DateUtils.stringToDate("09/2014");
        education1.endDate = DateUtils.stringToDate("4/2016");
        education1.courses = new ArrayList<>();
        education1.courses.add("Database");
        education1.courses.add("Algorithms");
        education1.courses.add("Networks");

        Education education2 = new Education();
        education2.school = "NCEPU";
        education2.major = "IS";
        education2.startDate = DateUtils.stringToDate("09/2010");
        education2.endDate = DateUtils.stringToDate("4/2014");
        education2.courses = new ArrayList<>();
        education2.courses.add("information");

        educations = new ArrayList<>();
        educations.add(education1);
        educations.add(education2);

    }
}
