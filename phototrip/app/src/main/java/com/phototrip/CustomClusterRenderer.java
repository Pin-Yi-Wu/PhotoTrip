package com.phototrip;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by fundot on 2016/10/29.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<MyItem> {
    private final Context mContext;
    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }
    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions) {
       markerOptions.title(item.title).icon(BitmapDescriptorFactory.fromBitmap(item.bitmap)).snippet(item.snippet);
    }

}
