package com.phototrip;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class CameraActivity extends AppCompatActivity {
    private ImageView image;
    private ImageButton imbt_check;
    private ImageButton imbt_cancle;
    private Bitmap newbm;
    private Double Latitude;
    private Double Longitude;
    SQLiteDatabase mSQLiteDatabase;
    String DATABASE_NAME = "w.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        mSQLiteDatabase=this.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
        final EditText et_name  = (EditText) findViewById(R.id.et_name);
        final EditText et_content  = (EditText) findViewById(R.id.et_content);
        final String time = "0";
        Bundle bundle = this.getIntent().getExtras();
        final String travel_id = bundle.getString("travel_id");
        image = (ImageView) findViewById(R.id.img);
        String geturi ="";
        if(getIntent().hasExtra("bitmap")) {
            newbm = getIntent().getParcelableExtra("bitmap");
            Latitude = getIntent().getDoubleExtra("Latitude",0);
            Longitude =  getIntent().getDoubleExtra("Longitude",0);
            geturi = getIntent().getStringExtra("geturi");
            image.setImageBitmap(newbm);

        }
        imbt_check = (ImageButton) findViewById(R.id.imbt_check);
        imbt_cancle = (ImageButton) findViewById(R.id.imbt_cancle);
        final String finalGeturi = geturi;
        imbt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String INSERT_TABLE="INSERT INTO photos (name,txt,time,latitude,longitude,picture,travel_id) VALUES (" +
                        "'"+et_name.getText().toString()+" ','"+et_content.getText().toString()+"','"+time.toString()+" ','"+Latitude+"','"+Longitude+"',' "+ finalGeturi +"','"+travel_id+"')";
                mSQLiteDatabase.execSQL(INSERT_TABLE);
                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(CameraActivity.this,MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        imbt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //image.setImageDrawable(getResources().getDrawable(R.drawable.camera));
    }



}
