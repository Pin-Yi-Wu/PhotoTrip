package com.phototrip;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import static android.Manifest.permission.*;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.phototrip.mFragments.MakerFragment;
import android.location.LocationListener;
import java.io.FileNotFoundException;
import java.util.HashMap;
import android.support.v4.app.ActivityCompat;
import static android.Manifest.permission.*;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final int REQUEST_EXTERNAL_STORAGE = 0;
    private GoogleMap mMap;
   // private DisplayMetrics mPhone;
    private final static int CAMERA = 66;
    private LocationManager locationManger;
    private LocationListener locationListner;
    private Double Latitude;
    private Double Longitude;
    SQLiteDatabase mSQLiteDatabase;
    String DATABASE_NAME = "w.db";
    String travel_id;
    ClusterManager<MyItem> mClusterManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSQLiteDatabase=this.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);

        String CREATE_TABLE="CREATE TABLE IF NOT EXISTS photos (photo_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,txt TEXT,time TEXT,latitude Double,longitude Double,picture TEXT,travel_id TEXT)";
        mSQLiteDatabase.execSQL(CREATE_TABLE);

        String CREATE_sos_TABLE="CREATE TABLE IF NOT EXISTS sos (sos_id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT,sostel TEXT)";
        mSQLiteDatabase.execSQL(CREATE_sos_TABLE);

        final Bundle bundle = this.getIntent().getExtras();
        travel_id = bundle.getString("travel_id");

        mSQLiteDatabase=this.openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE, null);
        final Cursor c = mSQLiteDatabase.rawQuery("Select * From travel Where travel_id = "+travel_id, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            TextView map_tv_title = (TextView) findViewById(R.id.map_tv_title);
            map_tv_title.setText(c.getString(1) + "\n" + c.getString(2) + "~" + c.getString(3));
            c.moveToNext();
        }

     //   mPhone = new DisplayMetrics();
     //   getWindowManager().getDefaultDisplay().getMetrics(mPhone);


        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Switch switch1 = (Switch) findViewById(R.id.switch1);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });
        FloatingActionButton fab_vwtravl = (FloatingActionButton) findViewById(R.id.fab_vwtravl);
        fab_vwtravl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        FloatingActionButton fab_sos = (FloatingActionButton) findViewById(R.id.fab_sos);
        fab_sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Cursor c = mSQLiteDatabase.rawQuery("Select * From sos Where  1 " , null);
                c.moveToFirst();
                if(c.getString(0).equals(""))
                {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                    LinearLayout layout = new LinearLayout(MapsActivity.this);
                    TextView tvMessage = new TextView(MapsActivity.this);
                    TextView tvname = new TextView(MapsActivity.this);
                    TextView nontext = new TextView(MapsActivity.this);
                    final EditText etInput = new EditText(MapsActivity.this);
                    final EditText etInputname = new EditText(MapsActivity.this);
                    nontext.setText("\n");
                    tvMessage.setText("電話號碼");
                    tvname.setText("姓名");
                    etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                    etInput.setSingleLine();
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(nontext);
                    layout.addView(tvname);
                    layout.addView(etInputname);

                    layout.addView(tvMessage);
                    layout.addView(etInput);
                    alert.setTitle("緊急聯絡人");
                    alert.setView(layout);
                    alert.setNegativeButton("儲存", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String INSERT_TABLE="INSERT INTO sos (name,sostel) VALUES ('"+etInputname.getText().toString()+"','"+etInput.getText().toString()+"')";
                            mSQLiteDatabase.execSQL(INSERT_TABLE);

                        }
                    });

                    alert.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });

                    alert.show();
                }else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                    LinearLayout layout = new LinearLayout(MapsActivity.this);
                    Button button = new Button(MapsActivity.this);
                    button.setText(c.getString(1)+","+c.getString(2));
                    layout.setOrientation(LinearLayout.VERTICAL);
                    layout.addView(button);
                    alert.setView(layout);
                    alert.setTitle("緊急聯絡人");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Uri uri = Uri.parse("tel:" + c.getString(2).toString());
                               Intent intent = new Intent(Intent.ACTION_CALL, uri);
                        if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(intent);
                        }
                    });
                    alert.setNegativeButton("修改", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(MapsActivity.this);
                            LinearLayout layout = new LinearLayout(MapsActivity.this);
                            TextView tvMessage = new TextView(MapsActivity.this);
                            TextView tvname = new TextView(MapsActivity.this);
                            TextView nontext = new TextView(MapsActivity.this);
                            final EditText etInput = new EditText(MapsActivity.this);
                            final EditText etInputname = new EditText(MapsActivity.this);
                            nontext.setText("\n");
                            tvMessage.setText("電話號碼");
                            tvname.setText("姓名");
                            etInput.setInputType(InputType.TYPE_CLASS_NUMBER);
                            etInput.setSingleLine();
                            layout.setOrientation(LinearLayout.VERTICAL);
                            layout.addView(nontext);
                            layout.addView(tvname);
                            layout.addView(etInputname);

                            layout.addView(tvMessage);
                            layout.addView(etInput);
                            alert.setTitle("緊急聯絡人");
                            alert.setView(layout);
                            alert.setNegativeButton("儲存", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String UPDATE_TABLE="UPDATE sos SET name='"+etInputname.getText().toString()+"',sostel='"+etInput.getText().toString()+"' WHERE sos_id="+'1';
                                    mSQLiteDatabase.execSQL(UPDATE_TABLE);

                                }
                            });

                            alert.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.cancel();
                                }
                            });

                            alert.show();

                        }
                    });

                    alert.setPositiveButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();
                        }
                    });
                    alert.show();
                }

            }
        });
        FloatingActionButton fab_epdals = (FloatingActionButton) findViewById(R.id.fab_epdals);
        fab_epdals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MapsActivity.this,EpdalsActivity.class);

                startActivity(intent);

            }
        });
        FloatingActionButton fab_adepd = (FloatingActionButton) findViewById(R.id.fab_adepd);
        fab_adepd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("travel_id", travel_id);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                intent.setClass(MapsActivity.this,AdepdActivity.class);

                startActivity(intent);

            }
        });
        FloatingActionButton fab_camera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab_camera.setOnClickListener(new View.OnClickListener() {


            public static final int REQUEST_EXTERNAL_STORAGE = 1;

            @Override
            public void onClick(View v) {

                getloc();
                //requestCode進行呼叫，原因為拍照完畢後返回程式後則呼叫onActivityResult
                ContentValues value = new ContentValues();
                value.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        value);

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri.getPath());
                startActivityForResult(intent, CAMERA);


            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Button zoom_plus = (Button) findViewById(R.id.zoom_plus);
        zoom_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomBy(1));
            }
        });
        Button zoom_cut = (Button) findViewById(R.id.zoom_cut);
        zoom_cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomBy(-1));
            }
        });
        Button  bt_earth = (Button) findViewById(R.id.bt_earth);
        bt_earth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomBy(-50));
            }
        });


        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera


        setUpClusterer();
        final Cursor c = mSQLiteDatabase.rawQuery("Select * From photos Where travel_id =  " + travel_id, null);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if(marker.getTitle()!=null )
                {
                    String title = marker.getTitle().toString();
                    String[] spl_title =  title.split(",");

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));
                    marker.hideInfoWindow();


                    FragmentTransaction transaction;
                    transaction = getSupportFragmentManager().beginTransaction();
                    MakerFragment makerFragment =  new MakerFragment();
                    // makerFragment.test(spl_title[2],spl_title[1],marker.getSnippet().toString());
                    makerFragment.uri_path(spl_title[2],spl_title[1],marker.getSnippet(),Integer.parseInt(spl_title[0]),travel_id);
                    makerFragment.show(transaction,"sa");

                    //new MakerFragment().show(transaction,"sa");
                }else{
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 4));
                }


                return true;
            }
        });
        c.moveToFirst();
        while (!c.isAfterLast()) {

            ContentResolver cr = this.getContentResolver();
            String ImagePath = "file://" + c.getString(6);
            Uri uri = Uri.parse(ImagePath);
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                //縮小為0.8倍
                float scaleWidth = (float) 0.04;
                float scaleHeight = (float) 0.06;
                // 取得想要缩放的matrix參數
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的圖片
                matrix.postRotate(90);
                Bitmap testbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                    CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);
                    MyItem testItem = new MyItem(c.getDouble(4), c.getDouble(5));
                    testItem.photo(testbm,c.getInt(0)+","+c.getString(1)+","+ImagePath,c.getString(2));
                    mClusterManager.addItem(testItem);
                    mClusterManager.setRenderer(renderer);



            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }

            c.moveToNext();
        }



    }



    private void setUpClusterer() {

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.635501, 120.308989), 1));
        mClusterManager = new ClusterManager<MyItem>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //藉由requestCode判斷是否為開啟相機或開啟相簿而呼叫的，且data不為null


        if ((requestCode == CAMERA) && data != null) {
            //取得照片路徑uri
            Uri uri = data.getData();
            ContentResolver cr = this.getContentResolver();

            try {
                //讀取照片，型態為Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //轉換為圖片指定大小
                //獲得圖片的寬高
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                //縮小為0.8倍
                float scaleWidth = (float) 0.07;
                float scaleHeight = (float) 0.07;
                // 取得想要缩放的matrix參數
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                // 得到新的圖片
                matrix.postRotate(90);
                //照片旋轉90度
                Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                //重新載入 imageView


                if (Latitude != null && Longitude != null) {
                    Bundle bundle = this.getIntent().getExtras();
                    final String travel_id = bundle.getString("travel_id");
                    String geturi = getPath(uri);
                    Intent intent = new Intent();
                    intent.putExtra("bitmap", newbm);
                    intent.putExtra("Latitude", Latitude);
                    intent.putExtra("Longitude", Longitude);
                    intent.putExtra("geturi",geturi);
                    bundle.putString("travel_id", travel_id);
                    intent.putExtras(bundle);
                    intent.setClass(MapsActivity.this, CameraActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    getloc();
                    Toast.makeText(MapsActivity.this, "還沒抓到座標!", Toast.LENGTH_SHORT).show();
                    Bundle bundle = this.getIntent().getExtras();
                    final String travel_id = bundle.getString("travel_id");
                    String geturi = getPath(uri);
                    Intent intent = new Intent();
                    intent.putExtra("bitmap", newbm);
                    intent.putExtra("geturi",geturi);
                    bundle.putString("travel_id", travel_id);
                    intent.putExtras(bundle);
                    intent.setClass(MapsActivity.this, SelectMapsActivity.class);
                    startActivity(intent);

                }

                //判斷照片為橫向或者為直向，並進入ScalePic判斷圖片是否要進行縮放
//                if(bitmap.getWidth()>bitmap.getHeight())ScalePic(bitmap,
//                        mPhone.heightPixels);
//                else ScalePic(bitmap,mPhone.widthPixels);
            } catch (FileNotFoundException e) {
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getloc() {
        locationManger = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Toast.makeText(MapsActivity.this, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
                Latitude = location.getLatitude();
                Longitude = location.getLongitude();

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location location = locationManger.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location location1 = locationManger.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if(location != null)
                {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                }else if(location1 != null)
                {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();
                }
                else
                {
                    Toast.makeText(MapsActivity.this, "無法定位座標", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProviderDisabled(String provider) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
                Toast.makeText(MapsActivity.this,"請到設定頁面開啟定位",Toast.LENGTH_LONG).show();
            }
    };


    if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
    }
    locationManger.requestLocationUpdates("gps", 0, 0, locationListner);
    }
    public String getPath(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


}
