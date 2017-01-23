package com.phototrip;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class SelectMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Double Latitude;
    private Double Longitude;
    private Bitmap newbm;
    String geturi;
    String travel_id;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("定位不到經緯度，請利用上方搜尋框輸入地點按下搜尋鍵，選定要將照片擺放在地圖的地方，標記長按可以移動。");
        builder.setPositiveButton("確定",null);
        builder.show();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        bundle = this.getIntent().getExtras();
        travel_id = bundle.getString("travel_id");
        newbm = getIntent().getParcelableExtra("bitmap");
        geturi = getIntent().getStringExtra("geturi");
    }
    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.TFaddress);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null && !location.equals("") && location.length()!=0)
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("Marker").draggable(true));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        Latitude =marker.getPosition().latitude;
                        Longitude = marker.getPosition().longitude;
                        Toast.makeText(SelectMapsActivity.this,String.valueOf(Latitude)+","+String.valueOf(Longitude),Toast.LENGTH_SHORT).show();
                    }
                });

                Latitude = address.getLatitude();
                Longitude = address.getLongitude();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public  void onCheck(View view)
    {
        if(Longitude!=null && Latitude!=null)
        {
            Intent intent = new Intent();
            intent.putExtra("bitmap", newbm);
            intent.putExtra("Latitude", Latitude);
            intent.putExtra("Longitude", Longitude);
            intent.putExtra("geturi",geturi);
            bundle.putString("travel_id", travel_id);
            intent.putExtras(bundle);
            intent.setClass(this, CameraActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(this,"請先輸入您想要釘在畫面的位置",Toast.LENGTH_SHORT).show();
        }

    }
}
