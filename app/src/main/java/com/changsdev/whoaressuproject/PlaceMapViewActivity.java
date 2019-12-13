package com.changsdev.whoaressuproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


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
    private Geocoder geocoder; //주소나 지역, 장소를 지리좌표(위도,경도)로 변환해주는 역할.

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
        geocoder = new Geocoder(this, Locale.KOREA);

        LatLng place = new LatLng(lat,lng ); //장소의 좌표


        try {
            List<Address> addressList = null;
            addressList = geocoder.getFromLocation(
                    lat,lng,10);
            if(addressList == null || addressList.size() == 0){
                showToast("장소를 찾지 못했습니다");
                return ;
            }

            MarkerOptions marker = new MarkerOptions();
            marker.title(placeName);
            marker.snippet(placeAddress); //세부적인 컨텐츠내용
            marker.position(place);
            mMap.addMarker(marker);


            mMap.moveCamera(CameraUpdateFactory.newLatLng(place));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
