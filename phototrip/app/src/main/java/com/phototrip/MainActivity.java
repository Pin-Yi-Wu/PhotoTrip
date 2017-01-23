package com.phototrip;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mSQLiteDatabase= null;
    private static final String DATABASE_NAME = "w.db";
    ListView listView1;
    ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSQLiteDatabase=this.openOrCreateDatabase(DATABASE_NAME,
                MODE_PRIVATE, null);
        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS travel (travel_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,startday TEXT,stopday TEXT)";
        mSQLiteDatabase.execSQL(CREATE_TABLE);
        listView1 = (ListView) findViewById(R.id.listView1);
        com.github.clans.fab.FloatingActionButton add_travel = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.add_travel);
        add_travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] st_year = new int[1];
                final int[] ed_year = new int[1];
                final int[] st_month = new int[1];
                final int[] ed_month = new int[1];
                final int[] st_day = new int[1];
                final int[] ed_day = new int[1];
                LayoutInflater inflater = getLayoutInflater();
                final View layout = inflater.inflate(R.layout.dialog,(ViewGroup) findViewById(R.id.dialog));
                final EditText et_name = (EditText) layout.findViewById(R.id.et_name);
                final EditText et_start = (EditText) layout.findViewById(R.id.et_start);
                final EditText et_end = (EditText) layout.findViewById(R.id.et_end);
                et_start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int mYear, mMonth, mDay;
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        et_start.setText(year + "-" + (monthOfYear + 1) + "-"
                                                + dayOfMonth);
                                        st_year[0]= year;
                                        st_month[0]= monthOfYear+1;
                                        st_day[0] = dayOfMonth;
                                    }
                                }, mYear, mMonth, mDay);
                        dpd.show();
                    }
                });
                et_end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int mYear, mMonth, mDay;
                        final Calendar c = Calendar.getInstance();
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);
                        DatePickerDialog dpd = new DatePickerDialog(MainActivity.this,
                                new DatePickerDialog.OnDateSetListener() {
                                    public void onDateSet(DatePicker view, int year,
                                                          int monthOfYear, int dayOfMonth) {
                                        et_end.setText(year + "-" + (monthOfYear + 1) + "-"
                                                + dayOfMonth);
                                        ed_year[0]= year;
                                        ed_month[0]= monthOfYear+1;
                                        ed_day[0] = dayOfMonth;
                                    }
                                }, mYear, mMonth, mDay);
                        dpd.show();
                    }
                });

                new AlertDialog.Builder(MainActivity.this).setView(layout).setPositiveButton("取消", null).setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if(et_name.getText().toString().length() == 0 || et_start.getText().toString().length() == 0 || et_end.getText().toString().length() == 0)
                        {
                            Toast.makeText(MainActivity.this,"請勿空白",Toast.LENGTH_LONG).show();
                        }else
                        {
                            if(ed_year[0]>st_year[0] || ed_year[0]==st_year[0])
                            {
                                if(ed_month[0]>st_month[0] || ed_month[0]==st_month[0])
                                {
                                    if (ed_day[0]>st_day[0] || ed_day[0]==st_day[0] )
                                    {
                                        String INSERT_TABLE="INSERT INTO travel (name,startday,stopday) VALUES ('"+et_name.getText().toString()+"','"+et_start.getText().toString()+"','"+et_end.getText().toString()+"')";
                                        mSQLiteDatabase.execSQL(INSERT_TABLE);
                                        //Toast.makeText(MainActivity.this,INSERT_TABLE,Toast.LENGTH_LONG).show();
                                        list.clear();
                                        show_listview();
                                    }else { Toast.makeText(MainActivity.this,"開始日不應該大於結束日",Toast.LENGTH_LONG).show();}
                                }else { Toast.makeText(MainActivity.this,"開始日不應該大於結束日",Toast.LENGTH_LONG).show();}
                            }else { Toast.makeText(MainActivity.this,"開始日不應該大於結束日",Toast.LENGTH_LONG).show();}

                        }


                    }
                }).show();
            }
        });


        show_listview();

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object>  selected_item=list.get(position);
                String travel_id=(String) selected_item.get("travel_id");

                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,MapsActivity.class);

                startActivity(intent);

            }
        });
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("確定要刪除嗎?");
                builder.setPositiveButton("取消",null);
                builder.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        HashMap<String, Object>  selected_item=list.get(position);
                        String id=(String) selected_item.get("travel_id");
                        String DELETE_TABLE1="DELETE FROM travel where travel_id="+id;
                        mSQLiteDatabase.execSQL(DELETE_TABLE1);
                        list.clear();
                        show_listview();
                    }
                });
                builder.show();
                return true;
            }});


    }

    public void show_listview() {

       listView1.setEmptyView(findViewById(R.id.tv_no_msg));


        final Cursor c = mSQLiteDatabase.rawQuery("Select * From travel Where 1 ", null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("travel_id", c.getString(0));
            item.put("name", c.getString(1));
            item.put("startday", c.getString(2));
            item.put("stopday", c.getString(3));
            list.add(item);
            c.moveToNext();
        }
        final SimpleAdapter adapter
                = new SimpleAdapter(MainActivity.this,
                list,
                R.layout.adapter,
                new String[]{"name", "startday", "stopday"},
                new int[]{R.id.tv_name, R.id.tv_startday, R.id.tv_stopday});

        listView1.setAdapter(adapter);
    }

}
