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
import android.widget.TextView;
import android.widget.Toast;

public class SignupPage extends AppCompatActivity {
    EditText Name,Email,Pass;
    Button submit;
    String nameHolder ,emailHolder , passHolder ;
    TextView t1;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2196F3")));
        Name = (EditText)findViewById(R.id.input_name);
        Email = (EditText)findViewById(R.id.input_email);
        Pass = (EditText)findViewById(R.id.input_password);
        submit = (Button)findViewById(R.id.btn_signup);
        t1 = (TextView)findViewById(R.id.link_login);

        nameHolder = Name.getText().toString();
        emailHolder = Email.getText().toString();
        passHolder = Pass.getText().toString();

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nextPage = new Intent(SignupPage.this,LoginPage.class);
                startActivity(nextPage);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameHolder = Name.getText().toString();
                emailHolder = Email.getText().toString();
                passHolder = Pass.getText().toString();

                if(nameHolder.equals("") || emailHolder.equals("") || passHolder.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"All fields are required",Toast.LENGTH_LONG).show();
                }
                else
                {
                    final ProgressDialog progressDialog = new ProgressDialog(SignupPage.this);
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
                                        Toast.makeText(getApplicationContext(),"Registration done Successfully",Toast.LENGTH_LONG).show();
                                        Intent nextPage = new Intent(SignupPage.this,LoginPage.class);
                                        startActivity(nextPage);
                                    }
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }
}
