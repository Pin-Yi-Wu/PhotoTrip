package com.phototrip;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;


public class AdepdActivity extends AppCompatActivity {
    RadioGroup rg1,rg2;
    RadioButton rb_breakfast,rb_lunch,rb_dinner,rb_dessert,rb_drink,rb_transportation,rb_stuff,rb_fun,rb_hotel,rb_others;
    private SQLiteDatabase mSQLiteDatabase= null;
    private static final String DATABASE_NAME = "w.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adepd);

        Bundle bundle = this.getIntent().getExtras();
        final String travel_id = bundle.getString("travel_id");
   //     Toast.makeText(this,travel_id,Toast.LENGTH_SHORT).show();

        mSQLiteDatabase=this.openOrCreateDatabase(DATABASE_NAME,
                MODE_PRIVATE, null);
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS spends (spend_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,txt TEXT,money INT,date DATE,travel_id TEXT)";
        mSQLiteDatabase.execSQL(CREATE_TABLE);

        rg1 = (RadioGroup) findViewById(R.id.first_group);
        rg2 = (RadioGroup) findViewById(R.id.second_group);
        rg1.clearCheck();
        rg2.clearCheck();
        rg1.setOnCheckedChangeListener(listener1);
        rg2.setOnCheckedChangeListener(listener2);

        rb_breakfast=(RadioButton)findViewById(R.id.rb_breakfast);
        rb_lunch=(RadioButton)findViewById(R.id.rb_lunch);
        rb_dinner= (RadioButton) findViewById(R.id.rb_dinner);
        rb_dessert= (RadioButton) findViewById(R.id.rb_dessert);
        rb_drink= (RadioButton) findViewById(R.id.rb_drink);
        rb_transportation= (RadioButton) findViewById(R.id.rb_transportation);
        rb_stuff= (RadioButton) findViewById(R.id.rb_stuff);
        rb_fun= (RadioButton) findViewById(R.id.rb_fun);
        rb_hotel= (RadioButton) findViewById(R.id.rb_hotel);
        rb_others= (RadioButton) findViewById(R.id.rb_others);

//        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/toto.ttf");
//        rb_breakfast.setTypeface(font);
        final EditText et_content = (EditText)findViewById(R.id.et_content);
        final EditText et_cash = (EditText)findViewById(R.id.et_cash);
        final EditText et_date = (EditText)findViewById(R.id.et_date);
        Button bt_check = (Button) findViewById(R.id.bt_check);
        final String[] checkname = {""};
        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(rb_breakfast.isChecked()==true)
              {
                  checkname[0] = "早餐";
              }else if(rb_lunch.isChecked()==true)
              {
                  checkname[0] = "午餐";
              }else if(rb_dinner.isChecked()==true)
              {
                  checkname[0] = "晚餐";
              }else if(rb_dessert.isChecked()==true)
              {
                  checkname[0] = "零食";
              }else if(rb_drink.isChecked()==true)
              {
                  checkname[0] = "飲料";
              }else if(rb_transportation.isChecked()==true)
              {
                  checkname[0] = "交通";
              }else if(rb_stuff.isChecked()==true)
              {
                  checkname[0] = "日用品";
              }else if(rb_fun.isChecked()==true)
              {
                  checkname[0] = "娛樂";
              }else if(rb_hotel.isChecked()==true)
              {
                  checkname[0] = "住宿";
              }else if(rb_others.isChecked()==true)
              {
                  checkname[0] = "其他";
              }else{}


                if(et_cash.getText().toString().length()==0)
                {
                    Toast.makeText(AdepdActivity.this,"請不要空金額",Toast.LENGTH_SHORT).show();

                } else if(checkname[0]=="")
                {
                    Toast.makeText(AdepdActivity.this,"請不要空選擇支出類型",Toast.LENGTH_SHORT).show();

                }  else if(et_date.getText().toString().length() == 0)
                {
                    Toast.makeText(AdepdActivity.this,"請不要空日期",Toast.LENGTH_SHORT).show();

                }else
                {
                    String INSERT_TABLE="INSERT INTO spends (name,txt,money,date,travel_id) VALUES ('"+checkname[0].toString()+"','"+et_content.getText().toString()+"','"+et_cash.getText().toString()+" ',' "+et_date.getText().toString()+"','"+travel_id+"')";
                    mSQLiteDatabase.execSQL(INSERT_TABLE);
                    Bundle bundle = new Bundle();
                    bundle.putString("travel_id", travel_id);
                    Intent intent = new Intent();
                    intent.putExtras(bundle);
                    intent.setClass(AdepdActivity.this,EpdalsActivity.class);
                    startActivity(intent);
                }


//                }
            }
        });

        ImageButton bt_adepdicon =  (ImageButton)findViewById(R.id.bt_adepdicon);
        bt_adepdicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(AdepdActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(AdepdActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                et_date.setText(year + "-" + (monthOfYear + 1) + "-"
                                        + dayOfMonth);
                                final int[] st_year = new int[1];
                                final int[] st_month = new int[1];
                                final int[] st_day = new int[1];
                                st_year[0]= year;
                                st_month[0]= monthOfYear+1;
                                st_day[0] = dayOfMonth;
                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
    }
    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg2.setOnCheckedChangeListener(null); // remove the listener before clearing so we don't throw that stackoverflow exception(like Vladimir Volodin pointed out)
                rg2.clearCheck(); // clear the second RadioGroup!
                rg2.setOnCheckedChangeListener(listener2); //reset the listener
            }
            switch(rg1.getCheckedRadioButtonId()){
                case R.id.rb_breakfast:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb01),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;
                case R.id.rb_lunch:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb02),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;
                case R.id.rb_dinner:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb03),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;

                case R.id.rb_dessert:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb04),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;

                case R.id.rb_drink:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb05),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                rg1.setOnCheckedChangeListener(null);
                rg1.clearCheck();
                rg1.setOnCheckedChangeListener(listener1);
            }

            switch(rg2.getCheckedRadioButtonId()){
                case R.id.rb_transportation:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;
                case R.id.rb_stuff:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb08),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;
                case R.id.rb_fun:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb07),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;
                case R.id.rb_hotel:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorAccent));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorWhite));

                    break;
                case R.id.rb_others:
                    rb_breakfast.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b02),null,null);
                    rb_breakfast.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_lunch.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b03),null,null);
                    rb_lunch.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dinner.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b04),null,null);
                    rb_dinner.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_dessert.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b05),null,null);
                    rb_dessert.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_drink.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b001),null,null);
                    rb_drink.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_transportation.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b06),null,null);
                    rb_transportation.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_stuff.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b07),null,null);
                    rb_stuff.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_fun.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b08),null,null);
                    rb_fun.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_hotel.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.b09),null,null);
                    rb_hotel.setTextColor(getResources().getColor(R.color.colorWhite));
                    rb_others.setCompoundDrawablesWithIntrinsicBounds(null,getDrawable(R.drawable.pb10),null,null);
                    rb_others.setTextColor(getResources().getColor(R.color.colorAccent));

                    break;
            }

        }
    };
}
