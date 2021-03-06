package com.example.classroom;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.services.classroom.model.Course;
import com.google.api.services.classroom.model.CourseWork;

import java.util.List;

/**
 * Created by seungbeomkim on 2019. 4. 16..
 */

public class ThirdPartyCoursesActivity extends ThirdPartyLoginActivity {
    private static final String TAG = "TPCActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdparty_courses);

        Button getBtn = findViewById(R.id.getBtn);
        getBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get courses.
                mClassroomServiceHelper.listCourses()
                        .addOnSuccessListener(new OnSuccessListener<List<Course>>() {
                            @Override
                            public void onSuccess(List<Course> courses) {
                                if(true) {
                                    Course course = courses.get(0);

                                    // Get courseworks in selected course.
                                    mClassroomServiceHelper.listCourseWorks(course.getId())
                                            .addOnSuccessListener(new OnSuccessListener<List<CourseWork>>() {
                                                @Override
                                                public void onSuccess(List<CourseWork> courseWorks) {
                                                    Log.d(TAG, "CourseWork size : " + courseWorks.size());
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });


        Button getCancelableBtn = findViewById(R.id.getCancelableBtn);
        getCancelableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Get course...");
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();

                        // Cancel task.
                        cts.cancel();
                    }
                });
                dialog.show();

                cts = new CancellationTokenSource();

                // Get courses.
                mClassroomServiceHelper.listCoursesForCancalable(cts.getToken())
                        .addOnSuccessListener(new OnSuccessListener<List<Course>>() {
                            @Override
                            public void onSuccess(List<Course> courses) {
                                if(courses.size() > 0) {
                                    Course course = courses.get(0);

                                    mClassroomServiceHelper.listCourseWorksForCancelable(course.getId(), cts.getToken())
                                            .addOnSuccessListener(new OnSuccessListener<List<CourseWork>>() {
                                                @Override
                                                public void onSuccess(List<CourseWork> courseWorks) {
                                                    if(dialog != null && dialog.isShowing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    if(dialog != null && dialog.isShowing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            });

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
    }

    @Override
    protected void afterLogout() {

    }

    @Override
    protected void updateUI(boolean login) {

    }
}
