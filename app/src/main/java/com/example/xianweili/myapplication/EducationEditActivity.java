package com.example.xianweili.myapplication;
import com.example.xianweili.myapplication.util.DateUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.text.TextUtils;
import android.app.Activity;
import android.widget.TextView;


import com.example.xianweili.myapplication.model.Education;

import java.util.Arrays;

/**
 * Created by xianwei li on 3/25/2017.
 */

public class EducationEditActivity extends AppCompatActivity {
    public static final String KEY_EDUCATION = "education";
    public static final String KEY_EDUCATION_ID = "education_ID";
    private Education education;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_edit);
        //add back button in education edit activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // get editable activity
        education = getIntent().getParcelableExtra(KEY_EDUCATION);
        if(education != null) {
            setupUI();
        }
        setTitle(education == null? "New education" : "Edit education");
    }

        private void setupUI(){
            ((EditText)findViewById(R.id.education_edit_school)).setText(education.school);
            ((EditText)findViewById(R.id.education_edit_major)).setText(education.major);
            ((EditText)findViewById(R.id.education_edit_start_date)).setText(DateUtils.dateToString(education.startDate));
            ((EditText)findViewById(R.id.education_edit_end_date)).setText(DateUtils.dateToString(education.endDate));
            ((EditText) findViewById(R.id.education_edit_courses)).setText(TextUtils.join("\n", education.courses));

            findViewById(R.id.education_edit_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(KEY_EDUCATION_ID,education.id);
                    setResult(Activity.RESULT_OK,resultIntent);
                    finish();
                }
            });
        }

    // add save menu to education edit layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    // click the menu items and call different methods
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.ic_save:
                saveAndExit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveAndExit() {
        if(education == null) {
            education = new Education();
        }
        education.school = ((EditText)
                findViewById(R.id.education_edit_school)).getText().toString();
        education.major = ((EditText)
                findViewById(R.id.education_edit_major)).getText().toString();
        education.startDate = DateUtils.stringToDate(((EditText)
                findViewById(R.id.education_edit_start_date)).getText().toString());
        education.endDate = DateUtils.stringToDate(((EditText)
                findViewById(R.id.education_edit_end_date)).getText().toString());
        education.courses = Arrays.asList(TextUtils.split(((EditText)
                findViewById(R.id.education_edit_courses)).getText().toString(), "\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_EDUCATION, education);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
