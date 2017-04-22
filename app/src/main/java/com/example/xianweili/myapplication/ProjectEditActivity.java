package com.example.xianweili.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xianweili.myapplication.model.Project;
import com.example.xianweili.myapplication.util.DateUtils;

import java.util.Arrays;

/**
 * Created by xianwei li on 4/16/2017.
 */

public class ProjectEditActivity extends EditBaseActivity<Project> {
    public static final String KEY_PROJECTS = "projects";
    public static final String KEY_PROJECT_ID = "project id";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_project_edit;
    }

    @Override
    protected Project initializeData() {
        return getIntent().getParcelableExtra(KEY_PROJECTS);
    }

    @Override
    protected void setupUIForEdit(@NonNull final Project data) {
        ((EditText)findViewById(R.id.project_edit_name)).setText(data.name);
        ((EditText)findViewById(R.id.project_edit_start_date)).setText(DateUtils.dateToString(data.startDate));
        ((EditText)findViewById(R.id.project_edit_end_date)).setText(DateUtils.dateToString(data.endDate));
        ((EditText) findViewById(R.id.project_edit_details)).setText(TextUtils.join("\n", data.details));

        findViewById(R.id.project_edit_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(KEY_PROJECT_ID, data.id);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    protected void setupUIForCreate() {
        findViewById(R.id.project_edit_delete).setVisibility(View.GONE);
    }

    @Override
    protected void saveAndExit(Project data) {
        if (data == null) {
            data = new Project();
        }
        data.name = ((EditText)findViewById(R.id.project_edit_name)).getText().toString();
        data.startDate = DateUtils.stringToDate(((EditText)findViewById(R.id.project_edit_start_date)).getText().toString());
        data.endDate = DateUtils.stringToDate(((EditText)findViewById(R.id.project_edit_end_date)).getText().toString());
        data.details = Arrays.asList(TextUtils.split(((EditText)findViewById(R.id.project_edit_details)).getText().toString(),"\n"));

        Intent resultIntent = new Intent();
        resultIntent.putExtra(KEY_PROJECTS,data);
        setResult(Activity.RESULT_OK,resultIntent);
    }
}
