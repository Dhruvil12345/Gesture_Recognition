package com.example.gesture_recognition;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginPage extends AppCompatActivity {
    private EditText email,pass;
    private Button login;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
        email = (EditText)findViewById(R.id.login_email);
        pass = (EditText)findViewById(R.id.login_pass);
        login = (Button)findViewById(R.id.btn_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailHolder = email.getText().toString();
                String passHolder = pass.getText().toString();

                if(emailHolder.equals("Admin") && passHolder.equals("Admin"))
                {
                    final ProgressDialog progressDialog = new ProgressDialog(LoginPage.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setMessage("Loading...");
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FFD4D9D0")));
                    progressDialog.setIndeterminate(false);
                    progressDialog.show();
                    progressStatus = 0;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(progressStatus < 100)
                            {
                                progressStatus +=1;
                            }
                            try{
                                Thread.sleep(10000);
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.setProgress(progressStatus);
                                    if(progressStatus == 100)
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Login Successfully",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginPage.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }).start();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
