package com.phototrip.mFragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.phototrip.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by fundot on 2016/11/27.
 */

public class DetailsFragment extends ListFragment {
    SQLiteDatabase mSQLiteDatabase;
    String DATABASE_NAME = "w.db";
    final ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();

    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savaInstanceState){

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_details,container,false);
//        String [] datasourse = {"a","v","a","s","a","v","a","s","a","v","a","s","a","v","a","s","a","v","a","s","a","v","a","s","a","v","a","s","a","v","a","s"};
//        ArrayAdapter<String> adapter =  new ArrayAdapter<String>(getActivity(),R.layout.rowlayout, R.id.txt_red,datasourse);
//        setListAdapter(adapter);
        mSQLiteDatabase=getActivity().openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
        show_listview();
        setRetainInstance(true);

        return  rootView;

    }
    public  void  onListItemClick(ListView l , View view , int position , long id) {

        HashMap<String, Object>  selected_item=list.get(position);
        String spend_id=(String) selected_item.get("spend_id");
        String travel_id=(String) selected_item.get("travel_id");
        final Cursor c = mSQLiteDatabase.rawQuery("Select * From spends Where travel_id = "+ travel_id+" AND spend_id = "+spend_id, null);
        c.moveToFirst();
      //  Toast.makeText(getActivity(),String.valueOf(c.getInt(0))+c.getString(1)+c.getString(2)+String.valueOf(c.getInt(3)),Toast.LENGTH_SHORT).show();
        try {
            Toast.makeText(getActivity(),c.getString(2),Toast.LENGTH_SHORT).show();
        }catch (Exception ex)
        {

        }
    }



    public void show_listview() {
        String[] name  = {" '早餐' "," '午餐' "," '晚餐' "," '零食' "," '飲料' "," '交通' "," '日用品' "," '娛樂' "," '住宿' "," '其他' "};

        Bundle bundle = getActivity().getIntent().getExtras();
        final String travel_id = bundle.getString("travel_id");
       // Toast.makeText(getActivity(),travel_id,Toast.LENGTH_SHORT).show();

        HashMap<String, Object> first_item = new HashMap<String, Object>();
        first_item.put("name","名稱");
        first_item.put("money","金額");
        first_item.put("date","日期");
        list.add(first_item);
        for(int i = 0 ; i<10 ;i++){
            final Cursor c = mSQLiteDatabase.rawQuery("Select * From spends Where travel_id = "+ travel_id +" AND name = "+name[i]+"ORDER BY  date  DESC", null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                HashMap<String, Object> item = new HashMap<String, Object>();
                item.put("spend_id",c.getString(0));
                item.put("name", c.getString(1));
                item.put("txt", c.getString(2));
                item.put("money", c.getInt(3));
                item.put("date", c.getString(4));
                item.put("travel_id", c.getString(5));
                list.add(item);
                c.moveToNext();
            }

    }





        final SimpleAdapter adapter
                = new SimpleAdapter(getActivity(),
                list,
                R.layout.adapter_details,
                new String[]{"name", "money", "date"},
                new int[]{R.id.tvd_name, R.id.tvd_cash, R.id.tvd_date});

            setListAdapter(adapter);


    }
}
