package com.phototrip.mFragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.phototrip.EditPhoto;
import com.phototrip.R;


import static android.content.Context.MODE_PRIVATE;


public class MakerFragment extends DialogFragment {
//    ViewPager viewPager;
//    CustomSwipeAdaptet adapter;
    SQLiteDatabase mSQLiteDatabase;
    String DATABASE_NAME = "w.db";
    ImageView  imageView ;
    TextView tv_title;
    TextView tv_content;
    String titles;
    String contents;
    String path ;
    Uri uri;
    Button bt_delete;
    Button bt_edit;
    int photo_id ;
    String travel_ids;
    public View onCreateView(LayoutInflater inflater , ViewGroup container , Bundle savaInstanceState){
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        mSQLiteDatabase=getActivity().openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);


        View v = inflater.inflate(R.layout.fragment_maker, container, false);
        bt_edit = (Button)v.findViewById(R.id.bt_edit);
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("photo_id", photo_id);
                intent.putExtra("travel_id",travel_ids);
                intent.setClass(getActivity(), EditPhoto.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        bt_delete = (Button) v.findViewById(R.id.bt_delete);
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("提示");
                builder.setMessage("確定要刪除嗎?");
                builder.setPositiveButton("取消",null);
                builder.setNegativeButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        String DELETE_TABLE="DELETE FROM photos WHERE photo_id ="+photo_id;
                       mSQLiteDatabase.execSQL(DELETE_TABLE);
                        getActivity().finish();


                        //getDialog().cancel();
                    }
                });
                builder.show();
            }
        });
        tv_title = (TextView) v.findViewById(R.id.tv_phtitle);
        tv_content = (TextView) v.findViewById(R.id.tv_phcotent);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        imageView.setImageURI(uri);
        tv_title.setText(titles);
        tv_content.setText(contents);
//        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
//        adapter= new CustomSwipeAdaptet(getActivity());
//        test("","","");
//        viewPager.setAdapter(adapter);

        return v;

    }
    public  void test(String imagepath , String title ,String content)
    {
//        adapter.setvalue(imagepath,title,content);

    }
    public void uri_path(String imagepath,String title , String content,int id,String travel_id)
    {
        photo_id = id;
        travel_ids =travel_id;
        titles = title;
        contents = content;
        path = imagepath;
        uri = Uri.parse(path);
    }
}
