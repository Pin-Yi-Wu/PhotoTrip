package com.phototrip.mFragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.phototrip.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by fundot on 2016/11/27.
 */

public class AnalysisFragment extends ListFragment {
    SQLiteDatabase mSQLiteDatabase;
    String DATABASE_NAME = "w.db";
    final ArrayList<HashMap<String,Object>> list=new ArrayList<HashMap<String,Object>>();
    PieChart pieChart;
    float[] percent = new float[10];
    String[] name  = {"早餐","午餐","晚餐","零食","飲料","交通","日用品","娛樂","住宿","其他"};
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savaInstanceState){

        ViewGroup rootViewz = (ViewGroup)inflater.inflate(R.layout.fragment_analysis,container,false);
        pieChart = (PieChart) rootViewz.findViewById(R.id.chart);

        mSQLiteDatabase=getActivity().openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
        show_listview();
        pieChart();
        setRetainInstance(true);
        return  rootViewz;

    }
    public  void  onListItemClick(ListView l , View view , int position , long id)
    {
        ViewGroup viewGroup = (ViewGroup)view;
//        TextView r  = (TextView)viewGroup.findViewById(R.id.txt_blue);
//        Toast.makeText(getActivity(), r.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void show_listview() {

        Bundle bundle = getActivity().getIntent().getExtras();
        final String travel_id = bundle.getString("travel_id");
    //    Toast.makeText(getActivity(),travel_id,Toast.LENGTH_SHORT).show();

        float[] sum  = new float[10];

        float getmoney;
        final Cursor c = mSQLiteDatabase.rawQuery("Select * From spends Where travel_id =  " + travel_id, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {

            for(int i=0 ; i<10;i++)
            {
                if(c.getString(1).equals(name[i]))
                {
                    getmoney= c.getInt(3);
                    sum[i]+= getmoney;
                }//如果名子是早餐，取得早餐的錢做加總
            }

            c.moveToNext();
        }
        float k;
        String g ;


        for(int i=0; i<9; i++)
        {
            for(int j=i+1; j<10; j++)
            {
                if(sum[i] < sum[j]) {
                    k=sum[i];
                    sum[i]=sum[j];
                    sum[j]=k;

                    g=name[i];
                    name[i]=name[j];
                    name[j]=g;
                }
            }

        }  //排加總順序，由大排到小

        float op=0 ;
        for(int p =0 ; p<10 ;p++)
        {
            op+=sum[p];
        }   //所有項目(早餐","午餐","晚餐","零食","飲料","交通","日用品","娛樂","住宿","其他")金錢總額


        for(int y =0 ;y<10 ;y++)
        {
            percent[y] = sum[y]/op*100;

        }   //舉例 : 早餐項目加總 除 所有項目金額加總 ，算所佔的% 並且排到陣列


        HashMap<String, Object> first_item = new HashMap<String, Object>();
        first_item.put("image","");
        first_item.put("name","名稱");
        first_item.put("money","總金額");
        first_item.put("percent","百分比");
        list.add(first_item);

        int[] image = {R.drawable.crown04};
        HashMap<String, Object> first_icon_item = new HashMap<String, Object>();
        first_icon_item.put("image",image[0]);
        first_icon_item.put("name",name[0]);
        first_icon_item.put("money",(int)sum[0]);
        first_icon_item.put("percent",(int)(Math.floor(percent[0]*100))/100.0 +"%");
        list.add(first_icon_item);

        for(int i =1 ;i<10 ; i++)
        {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("name",name[i]);
            item.put("money",(int)sum[i]);
            item.put("percent",(int)(Math.floor(percent[i]*100))/100.0 +"%");
            list.add(item);
        }


        final SimpleAdapter adapter
                = new SimpleAdapter(getActivity(),
                list,
                R.layout.adapter_analysis,
                new String[]{"image","name", "money","percent"},
                new int[]{R.id.imageView2,R.id.tvds_name, R.id.tvds_cash,R.id.tv_persent});

        setListAdapter(adapter);

    }
    public  void pieChart()
    {


        float[] size  = new float[10];

        ArrayList<Entry> entries = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            for(int j=0 ; j<10 ;j++)
            {
                size[j] = percent[j];
            }
            if(size[i]!=0)
            {
                entries.add(new Entry(size[i], i));
            }
        }

        PieDataSet dataset = new PieDataSet(entries, "");

        ArrayList<String> labels = new ArrayList<String>();
        for(int h= 0 ;h<10 ;h++)
        {
            labels.add(name[h]);
        }
        PieData data = new PieData(labels, dataset);
        dataset.setColors((new int[]{getResources().getColor(R.color.colorChar6), getResources().getColor(R.color.colorChar12),
                getResources().getColor(R.color.colorChar13), getResources().getColor(R.color.colorChar14),
                getResources().getColor(R.color.colorChar5), getResources().getColor(R.color.colorChar16),
                getResources().getColor(R.color.colorChar7), getResources().getColor(R.color.colorTab),
                getResources().getColor(R.color.colorChar9), getResources().getColor(R.color.colorChar10)}));
        dataset.setValueTextColor(getResources().getColor(R.color.colorWhite));
        pieChart.setDescription("");
        data.setValueTextSize(12);

        pieChart.getLegend().setEnabled(false);
        pieChart.setData(data);
        pieChart.animateY(3000);
    }
}