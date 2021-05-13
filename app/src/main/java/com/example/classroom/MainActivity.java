package com.example.classroom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    SignInButton signin;
    Button listBtn;
    int RC_SIGN_IN = 0;
    GoogleSignInClient mGoogleSignInClient;
    protected ClassroomServiceHelper mClassroomServiceHelper;

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

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            listBtn = findViewById(R.id.listBtn);
            listBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        makeSignInClient(new Scope(ClassroomScopes.CLASSROOM_COURSES), new Scope(ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS));
//        checkAlreadyLogin();
//    }
//
//    public void checkAlreadyLogin() {
//        Task<GoogleSignInAccount> task = mGoogleSignInClient.silentSignIn();
//        if (task.isSuccessful()) {
//            makeClassroomHelper();
//            updateUI(true);
//        } else {
//            task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
//                @Override
//                public void onSuccess(GoogleSignInAccount googleSignInAccount) {
//                    updateUI(true);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
    //                updateUI(false);
     //           }
       //     });
        //}
    //}

//    private void updateUI(boolean b) {
//    }
//
//    private void makeSignInClient(Scope scope, Scope scope1) {
//    }
//
//    protected void makeClassroomHelper() {
//        Set<String> scopes = new HashSet<>();
//        scopes.add(ClassroomScopes.CLASSROOM_COURSES);
//        scopes.add(ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS);
//        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//        GoogleAccountCredential credential =
//                GoogleAccountCredential.usingOAuth2(
//                        this,  scopes);
//        credential.setSelectedAccount(googleSignInAccount.getAccount());
//        Classroom service = new Classroom.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
//                .setApplicationName("GRExample")
//                .build();
//
//        mClassroomServiceHelper = new ClassroomServiceHelper(service);
//    }
//



    private void signIn() {
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
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(MainActivity.this,ResultActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.v("ERROR", "signInResult:failed code=" + e.getStatusCode());
        }


//        protected void makeSignInClient(Scope scope1, Scope scope2) {
//            GoogleSignInOptions signInOptions =
//                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                            .requestEmail()
//                            .requestScopes(scope1)
//                            .requestScopes(scope2)
//                            .build();
//            mGoogleSignInClient = GoogleSignIn.getClient(this, signInOptions);
//        }
//
//        protected void makeClassroomHelper() {
//            Set<String> scopes = new HashSet<>();
//            scopes.add(ClassroomScopes.CLASSROOM_COURSES);
//            scopes.add(ClassroomScopes.CLASSROOM_COURSEWORK_STUDENTS);
//            GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);
//            GoogleAccountCredential credential =
//                    GoogleAccountCredential.usingOAuth2(
//                            this,  scopes);
//            credential.setSelectedAccount(googleSignInAccount.getAccount());
//            Classroom service = new Classroom.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential)
//                    .setApplicationName("GRExample")
//                    .build();
//
//            mClassroomServiceHelper = new ClassroomServiceHelper(service);
//        }





    }
}