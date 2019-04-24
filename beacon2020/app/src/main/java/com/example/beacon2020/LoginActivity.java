package com.example.beacon2020;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends Activity {
    private EditText Name;
    private  EditText Password;
    private TextView Info;
    private TextView goregister;
    private TextView forgotPassword;
    private Button Login;
    private int counter = 5;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = findViewById(R.id.etName);
        Password = findViewById(R.id.etPassword);
        Info = findViewById(R.id.tvInfo);
        Login = findViewById(R.id.btnLogin);
        goregister = findViewById(R.id.tvRegister);
        forgotPassword = findViewById(R.id.tvForgotPassword);
        Info.setText("");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        progressDialog = new ProgressDialog(this);

        if(user != null){
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validate(Name.getText().toString(), Password.getText().toString());
            }
        });
        goregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerPage = new Intent(LoginActivity.this, RegisterationActivity.class);
                startActivity(registerPage);
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PasswordActivity.class));
            }
        });


    }
    private void Validate(String userName, String userPassword){

        progressDialog.setMessage("Loading");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                  //  startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    checkEmailVerification();
                }else
                {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    counter--;
                    Info.setText("No of attempts remaining: " + counter);
                    progressDialog.dismiss();
                    if(counter == 0){
                        Login.setEnabled(false);
                    }
                }
            }
        });

    }
    private void checkEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));

        //email verification
//        if(emailflag){
//            finish();
//            Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//        }else {
//            Toast.makeText(this, "Please verify your email", Toast.LENGTH_SHORT).show();
//            firebaseAuth.signOut();
//        }
    }

}
