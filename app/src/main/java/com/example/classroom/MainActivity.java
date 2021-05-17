package com.example.classroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.classroom.Classroom;
import com.google.api.services.classroom.ClassroomScopes;
import com.google.api.services.classroom.model.Course;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    SignInButton signin;
    Button listBtn;
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    ClassroomServiceHelper mClassroomServiceHelper;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signin = findViewById(R.id.sign_in_button);
        signin.setOnClickListener(v -> {
            if (v.getId() == R.id.sign_in_button) {
                signIn();
            }
        });


    }

    private void signIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestScopes(new Scope(ClassroomScopes.CLASSROOM_COURSES_READONLY))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN_IN);

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            handleSignInResult(data);
        }
    }

    private void handleSignInResult(Intent data) {
        // GoogleSignInAccount account = task.getResult(ApiException.class);
        GoogleSignIn.getSignedInAccountFromIntent(data)
                .addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount account) {
                        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(MainActivity.this,Collections.singleton(ClassroomScopes.CLASSROOM_COURSES_READONLY));

                        credential.setSelectedAccount(account.getAccount());

                        Classroom classroom = new Classroom.Builder(
                                AndroidHttp.newCompatibleTransport(),
                                new GsonFactory(),
                                credential)
                                .setApplicationName("My Classroom tutorial")
                                .build();

                        mClassroomServiceHelper = new ClassroomServiceHelper(classroom);

                        // Signed in successfully, show authenticated UI.

                        course();
                        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                        startActivity(intent);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void course(){
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Classroom List getting");
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        mClassroomServiceHelper.listCourses().addOnSuccessListener(new OnSuccessListener<List<Course>>() {
            @Override
            public void onSuccess(List<Course> courses) {
                Log.d(TAG,"Classroom"+courses.toString()+".");

                progressDialog.dismiss();


            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Check your Classroom",Toast.LENGTH_LONG).show();
                    }
                });
    }
}