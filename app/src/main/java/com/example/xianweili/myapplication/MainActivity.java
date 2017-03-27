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
    private static final int REQ_CODE_EDIT_EDUCATION = 100;
    private BasicInfo basicInfo;
    private List<Education> educations;

    private static final String MODEL_EDUCATIONS = "educations";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
        setupUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQ_CODE_EDUCATION_EDIT && resultCode == Activity.RESULT_OK) {
//            Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
//            educations.add(education);
//            setupEducations();
//        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQ_CODE_EDIT_EDUCATION:
                    String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                    if (educationId != null) {
                        deleteEducation(educationId);
                    } else {
                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                        updateEducation(education);
                    }
                    break;
            }
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);
        ImageButton addEducationBtn = (ImageButton) findViewById(R.id.add_education_btn);
        addEducationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                startActivityForResult(intent,REQ_CODE_EDIT_EDUCATION);
            }
        });
        //setupBasicInfoUI();
        setupEducations();
    }

    private void setupBasicInfoUI() {
        ((TextView) findViewById(R.id.name)).setText(basicInfo.name);
        ((TextView) findViewById(R.id.email)).setText(basicInfo.email);
    }

    private void setupEducations(){
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
        educationsLayout.removeAllViews();
        for (Education education :educations){
            educationsLayout.addView(getEducationView(education));
        }
    }

    private View getEducationView(final Education education){
        View view = getLayoutInflater().inflate(R.layout.education_list,null);
        String dateString = DateUtils.dateToString(education.startDate) +"-"+DateUtils.dateToString(education.endDate);
        ((TextView) view.findViewById(R.id.education_school)).setText(education.school + "(" + dateString + ")");
        ((TextView) view.findViewById(R.id.education_courses)).setText(formatItems(education.courses));

        view.findViewById(R.id.edit_education_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION,education);
                startActivityForResult(intent,REQ_CODE_EDIT_EDUCATION);
            }
        });
        return view;
    }


    private void loadData() {
//        BasicInfo savedBasicInfo = ModelUtils.read(this,
//                MODEL_BASIC_INFO,
//                new TypeToken<BasicInfo>(){});
//        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this,
                MODEL_EDUCATIONS,
                new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

//        List<Experience> savedExperience = ModelUtils.read(this,
//                MODEL_EXPERIENCES,
//                new TypeToken<List<Experience>>(){});
//        experiences = savedExperience == null ? new ArrayList<Experience>() : savedExperience;
//
//        List<Project> savedProjects = ModelUtils.read(this,
//                MODEL_PROJECTS,
//                new TypeToken<List<Project>>(){});
//        projects = savedProjects == null ? new ArrayList<Project>() : savedProjects;
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

    private void updateEducation(Education education) {
        boolean found = false;
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, education.id)) {
                found = true;
                educations.set(i, education);
                break;
            }
        }

        if (!found) {
            educations.add(education);
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }

    private void deleteEducation(@NonNull String educationId) {
        for (int i = 0; i < educations.size(); ++i) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }

        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
        setupEducations();
    }
}
