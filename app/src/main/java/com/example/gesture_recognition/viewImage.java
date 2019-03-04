package com.example.gesture_recognition;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

public class viewImage extends Activity {

    ImageView imageView;
    Bitmap bitmap;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        imageView = (ImageView)findViewById(R.id.viewimage);
        Intent intent = getIntent();
        bitmap = (Bitmap)intent.getParcelableExtra("Image");
        //Bundle extras = getIntent().getExtras();
        //Uri fileUri = Uri.parse(extras.getString("uri"));
        imageView.setImageBitmap(bitmap);
    }
    /*public void getImageFromActivity(Uri uri,String path){
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
            Toast.makeText(getApplicationContext(),path,Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
