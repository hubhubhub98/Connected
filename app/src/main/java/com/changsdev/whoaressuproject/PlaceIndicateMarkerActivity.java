package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.changsdev.whoaressuproject.fragment.ScrollableMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PlaceIndicateMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap; //구글맵 api사용
    private FragmentManager fragmentManager;
    private SupportMapFragment mapFragment; //지도를 표시하는 프래그먼트를 참조.
    private Geocoder geocoder; //주소나 지역, 장소를 지리좌표(위도,경도)로 변환해주는 역할.

    private LinearLayout mainLayout;

    private double lat;
    private double lng;
    private String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_indicate_marker);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("직접마커표시하기");
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        fragmentManager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        final Snackbar snackbar = Snackbar.make(mainLayout,"원하는 위치를 터치하시면 마커를 표시할 수 있습니다."
                ,Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("확인", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.place_indicate_marker_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.complete_btn:
                if(lat == 0 || lng == 0 || address == null || address.equals("")){
                    showToast("지도에 마커를 표시해주세요.");
                    return true;
                }
                Intent intent = new Intent();
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("address",address);
                setResult(RESULT_OK,intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this, Locale.KOREA);


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                lat = latLng.latitude;
                lng = latLng.longitude;
                List<Address> addressList = null;
                try {
                    addressList = geocoder.getFromLocation(lat,lng,10);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addressList == null || addressList.size() == 0){
                    showToast("터치한 장소의 정보를 얻지 못했습니다.");
                    return ;
                }

                mMap.clear();

                String[] splitStr = addressList.get(0).toString().split(",");
                address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("찾은 결과");
                markerOptions.snippet(address); //세부적인 컨텐츠내용
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);

                showToast("위치정보를 얻었습니다");
            }
        });



        /*지도에서 숭실대학교가 먼저 보이도록 함. */
        LatLng soongsil = new LatLng(37.496606, 126.957408 ); //숭실대학교 좌표
        CameraPosition.Builder builder = new CameraPosition.Builder();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.target(soongsil).zoom(16.0f).build()));

    }

    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }
}
