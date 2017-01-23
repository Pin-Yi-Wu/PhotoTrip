package com.phototrip;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.phototrip.mFragments.AnalysisFragment;
import com.phototrip.mFragments.DetailsFragment;

public class EpdalsActivity extends AppCompatActivity {

    AnalysisFragment analysisFragment;
    DetailsFragment detailsFragment;
    FragmentTransaction transaction;
    private SQLiteDatabase mSQLiteDatabase= null;
    private static final String DATABASE_NAME = "w.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epdals);

        Bundle bundle = this.getIntent().getExtras();
        final String travel_id = bundle.getString("travel_id");

        mSQLiteDatabase=this.openOrCreateDatabase(DATABASE_NAME,
                MODE_PRIVATE, null);
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS spends (spend_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,txt TEXT,money INT,date TEXT,travel_id TEXT)";
        mSQLiteDatabase.execSQL(CREATE_TABLE);

        com.github.clans.fab.FloatingActionButton add_adepd = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.add_adepd);
        add_adepd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(EpdalsActivity.this, AdepdActivity.class);
                startActivity(intent);
            }
        });
        ImageButton bt_epepdals =  (ImageButton)findViewById(R.id.bt_epepdals);
        bt_epepdals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(EpdalsActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        final TextView tv_1  = (TextView) findViewById(R.id.textView);
        final TextView tv_2  = (TextView) findViewById(R.id.textView2);
        final RadioButton rb_1 = (RadioButton) findViewById(R.id.radioButton);
        final RadioButton rb_2 = (RadioButton) findViewById(R.id.radioButton2);

        detailsFragment = new DetailsFragment();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fram, detailsFragment);
        transaction.commit();

        rb_1.setChecked(true);
        rb_1.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
        tv_1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
        rb_2.setTextColor(getResources().getColor(R.color.colorChar10));
        tv_2.setBackgroundColor(getResources().getColor(R.color.colorChar10));

        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.radioButton:
                        rb_1.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                        rb_2.setTextColor(getResources().getColor(R.color.colorChar10));
                        tv_1.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                        tv_2.setBackgroundColor(getResources().getColor(R.color.colorChar10));
                        detailsFragment = new DetailsFragment();
                        transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fram, detailsFragment);
                        transaction.commit();


                        break;
                    case R.id.radioButton2:
                        rb_1.setTextColor(getResources().getColor(R.color.colorChar10));
                        rb_2.setTextColor(getResources().getColor(android.R.color.holo_blue_bright));
                        tv_1.setBackgroundColor(getResources().getColor(R.color.colorChar10));
                        tv_2.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_bright));
                        analysisFragment = new AnalysisFragment();
                        transaction =  getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fram, analysisFragment);
                        transaction.commit();

                        break;
                }
            }
        });







    }
}
