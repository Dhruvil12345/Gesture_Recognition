package com.example.gesture_recognition;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button select_file_from_device;
    public static int RESULT_LOAD_IMAGE = 1;
    Button CaptureImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        select_file_from_device = (Button) findViewById(R.id.fileSelection);
        select_file_from_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
        CaptureImage = (Button)findViewById(R.id.capture_image);
        CaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MainActivity.this, ImageClass.class);
                startActivity(in);
            }
        });
    }
        public void onActivityResult(int requestCode,int resultCode,Intent data){
            super.onActivityResult(requestCode,resultCode,data);
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                cursor.close();
                Intent intent = new Intent(MainActivity.this,viewImage.class);
                intent.putExtra("uri",selectedImage.toString());
                startActivity(intent);
            }
        }
    }


