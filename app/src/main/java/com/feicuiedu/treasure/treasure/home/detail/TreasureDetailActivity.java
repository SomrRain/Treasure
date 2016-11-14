package com.feicuiedu.treasure.treasure.home.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.feicuiedu.treasure.R;
import com.feicuiedu.treasure.components.TreasureView;
import com.feicuiedu.treasure.treasure.Treasure;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TreasureDetailActivity extends AppCompatActivity {
    private static final String KEY_TREASURE = "key_treasure";

    @BindView(R.id.iv_navigation)
    ImageView ivNavigation;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.treasureView)
    TreasureView treasureView;
    @BindView(R.id.tv_detail_description)
    TextView tvDetailDescription;
    private Treasure treasure;
    private BitmapDescriptor dot = BitmapDescriptorFactory.fromResource(R.drawable.treasure_expanded);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_detail);
        
    }
    public static void start(Context context,Treasure treasure) {
        Intent starter = new Intent(context, TreasureDetailActivity.class);
        starter.putExtra(KEY_TREASURE,treasure);
        context.startActivity(starter);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
        treasure = (Treasure) getIntent().getSerializableExtra(KEY_TREASURE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(treasure.getTitle());
        treasureView.bindTreasure(treasure);
        initMap();
        
    }

    private void initMap() {
        LatLng  latLng=new LatLng(treasure.getLatitude(),treasure.getLongitude());
        MapStatus  mapStatus=new MapStatus.Builder()
                .target(latLng)
                .overlook(-20)
                .zoom(18)
                .build();
        BaiduMapOptions  options= new  BaiduMapOptions()
                .mapStatus(mapStatus)
                .compassEnabled(false)
                .scrollGesturesEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(false)
                .rotateGesturesEnabled(false)
                .scaleControlEnabled(false);
        MapView  mapView=new MapView(this,options);
        frameLayout.addView(mapView,0);
        BaiduMap  map=mapView.getMap();
        MarkerOptions  marker= new MarkerOptions()
                .position(latLng)
                .anchor(0.5f,0.5f)
                .icon(dot);
        map.addOverlay(marker);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())){
            case  android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
