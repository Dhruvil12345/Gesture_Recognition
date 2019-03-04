package com.example.gesture_recognition;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MysqlDatabaseHandlerClasses extends AsyncTask<String,String,String> {
    String text = "";
    String img_name ="";
    String image = "";
    String created_at = "";

    public MysqlDatabaseHandlerClasses(String img_name,String image,String created_at){
        this.img_name = img_name;
        this.image = image;
        this.created_at = created_at;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String type = arg0[0];
        String link = "http://192.168.0.102/upload_image.php";
        //Toast.makeText(getApplicationContext(), "Request Received", Toast.LENGTH_LONG).show();
        if (type.equals("upload_image")) {
            //String img_name2 = (String) arg0[1];
            //String image2 = (String) arg0[2];
            //String created_at2 = (String) arg0[3];
            try {
                String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(img_name, "UTF-8") + "&" +
                        URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(image, "UTF-8") + "&" +
                        URLEncoder.encode("created_at", "UTF-8") + "=" + URLEncoder.encode(created_at, "UTF-8");
                URL url = new URL(link);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                bufferedWriter.write(data);
                bufferedWriter.flush();
                int statusCode = httpURLConnection.getResponseCode();
                if (statusCode == 200) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null)
                        sb.append(line).append("\n");

                    text = sb.toString();
                    bufferedWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return text;
    }
}
