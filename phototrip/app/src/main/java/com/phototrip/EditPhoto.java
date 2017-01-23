package com.phototrip;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;


public class EditPhoto extends AppCompatActivity {

    SQLiteDatabase mSQLiteDatabase;
    String DATABASE_NAME = "w.db";
    int photo_id;
    String travel_id;
    EditText et_name;
    EditText et_content;
    ImageView  imageView;
    Uri uri;
    String ImagePath;
    ImageButton imbt_check;
    ImageButton imbt_cancle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editphoto);

        travel_id = getIntent().getStringExtra("travel_id");
        photo_id = getIntent().getIntExtra("photo_id",0);

        mSQLiteDatabase=this.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
        final Cursor c = mSQLiteDatabase.rawQuery("Select * From photos Where photo_id =  " + photo_id, null);
        c.moveToFirst();
        ImagePath = "file://" + c.getString(6);
        uri = Uri.parse(ImagePath);
        et_name  = (EditText) findViewById(R.id.edet_name);
        et_content  = (EditText) findViewById(R.id.edet_content);
        imageView =  (ImageView)findViewById(R.id.edimg);
        imbt_check = (ImageButton)findViewById(R.id.edimbt_check);
        imbt_cancle =  (ImageButton)findViewById(R.id.edimbt_cancle);

        imageView.setImageURI(uri);
        et_name.setText(c.getString(1));
        et_content.setText(c.getString(2));

        imbt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit = "UPDATE photos SET name='"+et_name.getText().toString()+"',txt='"+et_content.getText().toString()+"' WHERE photo_id="+photo_id;
                mSQLiteDatabase.execSQL(edit);
                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(EditPhoto.this,MapsActivity.class);
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
    }
}
