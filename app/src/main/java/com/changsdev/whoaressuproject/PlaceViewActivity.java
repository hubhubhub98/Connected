package com.changsdev.whoaressuproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.changsdev.whoaressuproject.fragment.ScrollableMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

//클릭한 장소의 정보들을 보여줌
public class PlaceViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ImageView placePhotoImageView;
    private EditText placeNameEdittext;
    private EditText placeLatEdittext;
    private EditText placeLngEdittext;
    private EditText placeAddressEdittext;
    private EditText placeKeywordEdittext;

    private GoogleMap mMap; //구글맵 api사용
    private FragmentManager fragmentManager;
    private ScrollableMapFragment mapFragment; //지도를 표시하는 프래그먼트를 참조.

    private LinearLayout googleMap;
    private Button openMapBtn;
    private ScrollView scrollView;

    String category;
    String placeName;
    double lat,lng;
    String placeAddress;
    String placePhotoUrl;
    String placeKeyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_view);

        Intent intent = getIntent();
        ActionBar actionBar = getSupportActionBar();
        category = intent.getStringExtra("category");
        placeName = intent.getStringExtra("placeName");
        lat = intent.getDoubleExtra("lat",0);
        lng = intent.getDoubleExtra("lng",0);
        placeAddress = intent.getStringExtra("placeAddress");
        placePhotoUrl = intent.getStringExtra("placePhotoUrl");
        placeKeyword = intent.getStringExtra("placeKeyword");

        actionBar.setTitle(placeName);

        placePhotoImageView = (ImageView)findViewById(R.id.place_photo_imageview);
        placeNameEdittext = (EditText)findViewById(R.id.placename_edittext);
        placeLatEdittext = (EditText)findViewById(R.id.place_lat_edittext);
        placeLngEdittext = (EditText)findViewById(R.id.place_lng_edittext);
        placeAddressEdittext = (EditText)findViewById(R.id.place_address_edittext);
        placeKeywordEdittext = (EditText)findViewById(R.id.place_keyword_edittext);

        openMapBtn = (Button)findViewById(R.id.open_map_btn);
        googleMap = (LinearLayout)findViewById(R.id.google_map);
        scrollView = (ScrollView)findViewById(R.id.scroll_view);

        fragmentManager = getSupportFragmentManager();
        mapFragment = (ScrollableMapFragment) fragmentManager.findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        mapFragment.setListener(new ScrollableMapFragment.OnTouchListener() {
            @Override
            public void onActionDown() {
                scrollView.requestDisallowInterceptTouchEvent(true); //스크롤뷰에게 이벤트 가로채지말라고 요청
            }

            @Override
            public void onActionUp() {
                scrollView.requestDisallowInterceptTouchEvent(false);
            }
        });



        Glide.with(this).load(placePhotoUrl)
                .apply(new RequestOptions().centerCrop())
                .into(placePhotoImageView);
        //해당 장소의 이미지를 이미지뷰에 로딩한다.
        placeNameEdittext.setText(placeName);
        placeLatEdittext.setText(lat+""); placeLngEdittext.setText(lng+"");
        placeAddressEdittext.setText(placeAddress);
        placeKeywordEdittext.setText(placeKeyword);

        openMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(googleMap.getVisibility() == View.GONE){ //안보이는 상태라면
                    googleMap.setVisibility(View.VISIBLE); //보이게함
                }else{ //보이는 상태라면
                    googleMap.setVisibility(View.GONE); //감춤
                }
            }
        });



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
