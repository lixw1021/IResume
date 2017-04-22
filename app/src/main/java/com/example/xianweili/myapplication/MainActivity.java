package com.example.xianweili.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xianweili.myapplication.model.BasicInfo;
import com.example.xianweili.myapplication.model.Education;
import com.example.xianweili.myapplication.model.Experience;
import com.example.xianweili.myapplication.model.Project;
import com.example.xianweili.myapplication.util.DateUtils;
import com.example.xianweili.myapplication.util.ImageUtils;
import com.example.xianweili.myapplication.util.ModelUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String MODEL_EDUCATIONS = "educations";
    private static final String MODEL_EXPERIENCES = "experiences";
    private static final String MODEL_PROJECTS = "projects";
    private static final String MODEL_BASIC_INFORMATION = "basic_information";

    private static final int EEQ_CODE_BASIC_INFORMATION_EDIT = 99;
    private static final int REQ_CODE_EDUCATION_EDIT = 100;
    private static final int REQ_CODE_EXPERIENCE_EDIT = 101;
    private static final int REQ_CODE_PROJECT_EDIT = 102;
    private BasicInfo basicInfo;
    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
        setupUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case EEQ_CODE_BASIC_INFORMATION_EDIT:
                    BasicInfo basicInfo = data.getParcelableExtra(BasicInforEditActivity.KEY_BASIC_INFORMATION);
                    updateBasicInfo(basicInfo);
                    break;

                case REQ_CODE_EDUCATION_EDIT:
                    String educationId = data.getStringExtra(EducationEditActivity.KEY_EDUCATION_ID);
                    if (educationId != null) {
                        deleteEducation(educationId);
                    } else {
                        Education education = data.getParcelableExtra(EducationEditActivity.KEY_EDUCATION);
                        updateEducation(education);
                    }
                    setupEducations();
                    break;

                case REQ_CODE_EXPERIENCE_EDIT:
                    String experienceId = data.getStringExtra(ExperienceEditActivity.KEY_EXPERIENCE_ID);
                    if (experienceId != null) {
                        deleteExperience(experienceId);
                    } else {
                        Experience experience = data.getParcelableExtra(ExperienceEditActivity.KEY_EXPERIENCE);
                        updateExperience(experience);
                    }
                    setupExperiences();
                    break;

                case REQ_CODE_PROJECT_EDIT:
                    String projectId = data.getStringExtra(ProjectEditActivity.KEY_PROJECT_ID);
                    if (projectId != null) {
                        deleteProject(projectId);
                    } else {
                        Project project = data.getParcelableExtra(ProjectEditActivity.KEY_PROJECTS);
                        updateProject(project);
                    }
                    setupProjects();
                    break;
            }
        }
    }

    private void setupUI() {
        setContentView(R.layout.activity_main);

        //define register listener to add_education button
        findViewById(R.id.add_education_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EducationEditActivity.class); // click to launch education edit activity
                startActivityForResult(intent,REQ_CODE_EDUCATION_EDIT);
            }
        });

        findViewById(R.id.add_experience_btn).setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                startActivityForResult(intent,REQ_CODE_EXPERIENCE_EDIT);
            }
        });

        findViewById(R.id.add_project_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
            }
        });

        setupBasicInfoUI();
        setupEducations();
        setupExperiences();
        setupProjects();
    }

    private void setupBasicInfoUI() {
        ((TextView) findViewById(R.id.name)).setText(TextUtils.isEmpty(basicInfo.name) ? "Your Name" : basicInfo.name );
        ((TextView) findViewById(R.id.email)).setText(TextUtils.isEmpty(basicInfo.email) ? "Your Email" : basicInfo.email );

        ImageView userPicture = (ImageView) findViewById(R.id.user_picture);
        if (basicInfo.imageUri != null) {
            ImageUtils.loadImage(this, basicInfo.imageUri, userPicture);
        } else {
            userPicture.setImageResource(R.drawable.user_ghost);
        }

        findViewById(R.id.edit_basic_info).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicInforEditActivity.class);
                intent.putExtra(BasicInforEditActivity.KEY_BASIC_INFORMATION, basicInfo);
                startActivityForResult(intent,EEQ_CODE_BASIC_INFORMATION_EDIT);
            }
        });

    }
    private void updateBasicInfo(BasicInfo basicInfo) {
        ModelUtils.save(this,MODEL_BASIC_INFORMATION,basicInfo);
        this.basicInfo = basicInfo;
        setupBasicInfoUI();
    }

    private void setupEducations(){
        LinearLayout educationsLayout = (LinearLayout) findViewById(R.id.education_list);
        educationsLayout.removeAllViews();
        for (Education education: educations){
            educationsLayout.addView(getEducationView(education));
        }
    }
    private View getEducationView(final Education education) {
        View view = getLayoutInflater().inflate(R.layout.education_list, null);
        String dateString = DateUtils.dateToString(education.startDate) + " ~ " + DateUtils.dateToString(education.endDate);
        ((TextView) view.findViewById(R.id.education_school)).setText(education.school + " (" + dateString + ")");
        ((TextView) view.findViewById(R.id.education_courses)).setText(formatItems(education.courses));
        ((TextView) view.findViewById(R.id.education_edit_major)).setText(education.major);
        // add listener to edit education button
        ImageButton editEducationBtn = (ImageButton) view.findViewById(R.id.edit_education_btn);
        editEducationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EducationEditActivity.class);
                intent.putExtra(EducationEditActivity.KEY_EDUCATION, education);
                startActivityForResult(intent, REQ_CODE_EDUCATION_EDIT);
            }
        });

        return view;
    }
    private void updateEducation(Education education) {
        boolean found = false;
        for(int i = 0; i < educations.size(); i++) {
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
    }
    private void deleteEducation(String educationId) {
        for (int i = 0; i < educations.size(); i++) {
            Education e = educations.get(i);
            if (TextUtils.equals(e.id, educationId)) {
                educations.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_EDUCATIONS, educations);
    }

    private void setupExperiences() {
        LinearLayout experiencesLayout = (LinearLayout) findViewById(R.id.experience_list);
        experiencesLayout.removeAllViews();
        for (Experience experience : experiences) {
            experiencesLayout.addView(getExperienceView(experience));
        }
    }
    private View getExperienceView(final Experience experience) {
        View view = getLayoutInflater().inflate(R.layout.experience_item, null);
        String dateString = DateUtils.dateToString(experience.startDate) + "~" + DateUtils.dateToString(experience.endDate);
        ((TextView) view.findViewById(R.id.experience_company)).setText(experience.company + " (" + dateString + ")");
        ((TextView) view.findViewById(R.id.experience_details)).setText(formatItems(experience.details));
        ((TextView) view.findViewById(R.id.experience_title)).setText(experience.title);

        view.findViewById(R.id.edit_experience_btn).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExperienceEditActivity.class);
                intent.putExtra(ExperienceEditActivity.KEY_EXPERIENCE, experience);
                startActivityForResult(intent, REQ_CODE_EXPERIENCE_EDIT);
            }
        });

        return view;
    }
    private void updateExperience(Experience experience) {
        boolean found = false;
        for (int i = 0; i < experiences.size(); i++) {
            Experience e = experiences.get(i);
            Log.i(e.id,experience.id);
            if(TextUtils.equals(e.id, experience.id)) {
                found = true;
                experiences.set(i,experience);
                break;
            }
        }

        if (!found) {
            experiences.add(experience);
        }

        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
    }
    private void deleteExperience(String experienceId) {
        for (int i = 0; i < experiences.size(); i++) {
            Experience e = experiences.get(i);
            if (TextUtils.equals(e.id, experienceId)) {
                experiences.remove(i);
                break;
            }
        }
        ModelUtils.save(this, MODEL_EXPERIENCES, experiences);
    }

    private void setupProjects(){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.projects_list);
        linearLayout.removeAllViews();
        for (int i = 0; i < projects.size(); i++){
            linearLayout.addView(getProjectView(projects.get(i)));
        }
    }
    private View getProjectView(final Project project) {
        View view = getLayoutInflater().inflate(R.layout.project_item,null);
        String dataString = DateUtils.dateToString(project.startDate) + "1" + DateUtils.dateToString(project.endDate);
        ((TextView) view.findViewById(R.id.project_name)).setText(project.name + " " +dataString);
        ((TextView) view.findViewById(R.id.project_details)).setText(formatItems(project.details));

        view.findViewById(R.id.edit_project_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProjectEditActivity.class);
                intent.putExtra(ProjectEditActivity.KEY_PROJECTS,project);
                startActivityForResult(intent, REQ_CODE_PROJECT_EDIT);
            }
        });

        return view;

    }
    private void updateProject(Project project) {
        boolean found = false;
        for (int i = 0; i < projects.size(); i++) {
            Project e = projects.get(i);
            if (TextUtils.equals(e.id, project.id)) {
                found = true;
                projects.set(i, project);
            }
        }
        if (!found) {
            projects.add(project);
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
    }
    private void deleteProject(String projectId) {
        for (int i = 0; i < projects.size(); i++) {
            String eId = projects.get(i).id;
            if (eId.equals(projectId)) {
                projects.remove(i);
            }
        }

        ModelUtils.save(this, MODEL_PROJECTS, projects);
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

    private void loadData() {
//        Project project = new Project();
//        project.name = "pitt research";
//        project.startDate = DateUtils.stringToDate("10/2014");
//        project.endDate = DateUtils.stringToDate("05/2016");
//        project.details = new ArrayList<>();
//        project.details.add("1111");
//        project.details.add("2222");
//
//        Project project1 = new Project();
//        project1.name = "pitt research1";
//        project1.startDate = DateUtils.stringToDate("11/2016");
//        project1.endDate = DateUtils.stringToDate("05/2017");
//        project1.details = new ArrayList<>();
//        project1.details.add("3333");
//        project1.details.add("4444");
//
//        projects = new ArrayList<>();
//        projects.add(project);
//        projects.add(project1);

        BasicInfo savedBasicInfo = ModelUtils.read(this, MODEL_BASIC_INFORMATION, new TypeToken<BasicInfo>(){} );
        basicInfo = savedBasicInfo == null ? new BasicInfo() : savedBasicInfo;

        List<Education> savedEducation = ModelUtils.read(this,MODEL_EDUCATIONS, new TypeToken<List<Education>>(){});
        educations = savedEducation == null ? new ArrayList<Education>() : savedEducation;

        List<Experience> savedExperiences = ModelUtils.read(this,MODEL_EXPERIENCES, new TypeToken<List<Experience>>(){});
        experiences = savedExperiences == null ? new ArrayList<Experience>() : savedExperiences;

        List<Project> savedProjects = ModelUtils.read(this, MODEL_PROJECTS, new TypeToken<List<Project>>(){});
        projects = savedProjects == null? new ArrayList<Project>() : savedProjects;
    }
}
