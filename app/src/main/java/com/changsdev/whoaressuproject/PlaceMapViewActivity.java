package com.changsdev.whoaressuproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


//장소의 위치나 정보를 지도를 통해 보여줌
//해당 위치에 마커가 찍힘
public class PlaceMapViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String category; //장소의 카테고리
    private String placeName; //장소의 이름
    private Double lat,lng; //장소의 위도경도
    private String placeAddress; //장소의 주소

    private GoogleMap mMap; //구글맵 api사용
    private FragmentManager fragmentManager;
    private SupportMapFragment mapFragment; //지도를 표시하는 프래그먼트를 참조.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_map_view);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        placeName = intent.getStringExtra("placeName");
        lat = intent.getDoubleExtra("lat",0);
        lng = intent.getDoubleExtra("lng",0);
        placeAddress = intent.getStringExtra("placeAddress");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(placeName);


        fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);





    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        LatLng place = new LatLng(lat,lng ); //장소의 좌표
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(placeName); //장소의 이름
        markerOptions.snippet(placeAddress); //장소의 주소
        markerOptions.position(place);
        mMap.addMarker(markerOptions);


        mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
    }
}
