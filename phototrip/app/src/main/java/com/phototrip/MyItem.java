package com.phototrip;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by fundot on 2016/10/26.
 */

public class MyItem implements ClusterItem {
        private final LatLng mPosition;
        Bitmap  bitmap ;
        String title ;
        String snippet;
        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);

        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }

    public void photo (Bitmap bit,String tit,String sni)
    {
        bitmap = bit;
        title = tit ;
        snippet=  sni;
    }

}
