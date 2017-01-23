package com.phototrip;


import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by fundot on 2016/12/12.
 */

public class CustomSwipeAdaptet extends PagerAdapter {
    private String  testtitle = "test1,test2,test3";
    private String testcontent = "con1,con2,con3";
    private int[] image_resources = {R.drawable.adepd,R.drawable.camera,R.drawable.sos};
    String path = "/storage/emulated/0/DCIM/100ANDRO/DSC_0733.JPG";
    String path2 = "/storage/emulated/0/DCIM/100ANDRO/DSC_0734.JPG";
    String path3 = "/storage/emulated/0/DCIM/100ANDRO/DSC_0735.JPG";
    String ImagePath = "file://" + path;
    String ImagePath2 = "file://" + path2;
    String ImagePath3 = "file://" + path3;
    String im_path =ImagePath+","+ImagePath2+","+ImagePath3;
    private Context ctx;
    private LayoutInflater layoutInflater;
    public CustomSwipeAdaptet(Context ctx)
    {
        this.ctx = ctx;
    }
    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==(LinearLayout)object);
    }
    public  void  setvalue(String imagepath , String title ,String content)
    {
        im_path = imagepath;
        testtitle = title;
        testcontent  = content;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        String[] spim_path = im_path.split(",");
        Uri uri = Uri.parse(spim_path[position]);
        layoutInflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.image_view);
        TextView textView =(TextView)item_view.findViewById(R.id.image_count);
        //imageView.setImageResource(image_resources[position]);
        imageView.setImageURI(uri);
        String[] spl_testtitle = testtitle.split(",");
        textView.setText("標題:"+spl_testtitle[position]);
        //textView.setText("標題:"+titles[position]);
        TextView  content = (TextView) item_view.findViewById(R.id.content);
        String[] spl_context = testcontent.split(",");
        content.setText("內容"+spl_context[position]);
        //content.setText("內容:"+content_null[position]);
        container.addView(item_view);
        return  item_view;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    {
        container.removeView((LinearLayout)object);
    }
}
