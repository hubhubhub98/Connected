package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.changsdev.whoaressuproject.fragment.ScrollableMapFragment;
import com.changsdev.whoaressuproject.model.PlaceVO;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PlaceWriteActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final int PICK_FROM_ALBUM = 10; //requestCode
    private ImageView placePhotoImageView;
    private Uri imageUri;
    private EditText placeNameEdittext;
    private EditText placeLatEdittext;
    private EditText placeLngEdittext;
    private EditText placeAddressEdittext;
    private EditText placeKeywordEdittext;
    private Button placeWriteBtn;

    private String category;

    private GoogleMap mMap; //구글맵 api사용
    private FragmentManager fragmentManager;
    private ScrollableMapFragment mapFragment; //지도를 표시하는 프래그먼트를 참조.
    private Geocoder geocoder; //주소나 지역, 장소를 지리좌표(위도,경도)로 변환해주는 역할.

    private Button openMapBtn;
    private Button placeSearchBtn;
    private EditText placeSearchEdittext;

    private LinearLayout googleMap;

    private LinearLayout mainLayout;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_write);

        placePhotoImageView = (ImageView)findViewById(R.id.place_photo_imageview);
        placeNameEdittext = (EditText)findViewById(R.id.placename_edittext);
        placeLatEdittext = (EditText)findViewById(R.id.place_lat_edittext);
        placeLngEdittext = (EditText)findViewById(R.id.place_lng_edittext);
        placeAddressEdittext = (EditText)findViewById(R.id.place_address_edittext);
        placeKeywordEdittext = (EditText)findViewById(R.id.place_keyword_edittext);
        placeWriteBtn = (Button)findViewById(R.id.place_write_btn);
        openMapBtn = (Button)findViewById(R.id.open_map_btn);
        placeSearchBtn = (Button)findViewById(R.id.place_search_btn);
        placeSearchEdittext = (EditText)findViewById(R.id.place_search_editText);
        googleMap = (LinearLayout)findViewById(R.id.google_map);
        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);


        Intent intent = getIntent(); //인텐트를 통해서 장소에대한 카테고리정보를 얻어온다
        ActionBar actionBar = getSupportActionBar();
        category = intent.getStringExtra("category");
        actionBar.setTitle(category);

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


        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.place_write_btn:
                        placeWrite();
                        break;
                    case R.id.place_photo_imageview:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        //갤러리액티비티를 실행하기위한 세팅.
                        startActivityForResult(Intent.createChooser(intent,"이미지를 선택하세요"),PICK_FROM_ALBUM);
                        //갤러리에 접근하자.
                        break;
                    case R.id.open_map_btn:
                        if(googleMap.getVisibility() == View.GONE){ //안보이는 상태라면
                            googleMap.setVisibility(View.VISIBLE); //보이게함
                            final Snackbar snackbar = Snackbar.make(mainLayout,"검색된 장소의 마커를 누르면\n" +
                                    "정보를 얻을 수 있습니다.",Snackbar.LENGTH_INDEFINITE);
                            snackbar.setAction("확인", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    snackbar.dismiss();
                                }
                            });
                            snackbar.show();
                        }else{ //보이는 상태라면
                            googleMap.setVisibility(View.GONE); //감춤
                        }

                }
            }
        };

        placePhotoImageView.setOnClickListener(myListener);
        placeWriteBtn.setOnClickListener(myListener);
        openMapBtn.setOnClickListener(myListener);



    }

    //장소를 등록하는 처리
    private void placeWrite(){
        final String placeName = placeNameEdittext.getText().toString().trim();
        String placeLat = placeLatEdittext.getText().toString().trim();
        String placeLng = placeLngEdittext.getText().toString().trim();
        final String placeAddress = placeAddressEdittext.getText().toString().trim();
        final String placeKeyword = placeKeywordEdittext.getText().toString().trim();


        final double lat,lng;
        if(placeName == null || placeName.equals("") ||
            placeLat == null || placeLat.equals("") ||
            placeLng == null || placeLng.equals("") ||
            placeAddress == null || placeAddress.equals("") ||
            placeKeyword == null || placeKeyword.equals("")){
            showToast("빈 칸 없이 입력해주세요");
            return ;
        }

        try{
            lat = Double.valueOf(placeLat);
            lng = Double.valueOf(placeLng);
            //실수로 바꿔주는 과정에서 예외가 발생할수 있기때문에 예외처리
        }catch (Exception e){
            showToast("위도 경도 정보를 올바르게 입력하세요");
            return ;
        }

        if(imageUri == null){
            imageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.standard_place);
        }// 이미지를 선택하지않았을땐 기본건물이미지(standard_place.png)로 세팅.


        final String pid = UUID.randomUUID().toString();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("placeImages")
                .child(pid);


        //storage에 파일업로드처리
        storageReference.putFile(imageUri)
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return storageReference.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {//업로드 성공시 실행되는 메서드
                        if (task.isSuccessful()) {
                            // 업로드에 성공했을시 데이터베이스에 장소데이터를 추가해준다.
                            Uri downloadUri = task.getResult();
                            PlaceVO place = new PlaceVO();
                            place.setAddress(placeAddress);
                            place.setCategory(category);
                            place.setKeywords(placeKeyword);
                            place.setLat(lat);
                            place.setLng(lng);
                            place.setName(placeName);
                            place.setRecommend(0); place.setPlacePhotoUrl(downloadUri.toString());
                            place.setPid(pid); place.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            FirebaseDatabase.getInstance().getReference().child("places").push().setValue(place);
                            showToast("업로드를 완료했습니다.");
                            finish();
                        } else {
                            // Handle failures
                            // ...
                            showToast("업로드에 실패했습니다.");
                        }
                    }
        });




    }


    //갤러리 액티비티로부터 얻어온 데이터를 처리하자
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_ALBUM && resultCode == RESULT_OK){
            imageUri = data.getData(); //선택한 이미지의 경로를 가지고 온다.
            System.out.println("imageUri : "+imageUri.toString());
            try{
                InputStream in = getContentResolver().openInputStream(data.getData());

                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();

                placePhotoImageView.setImageBitmap(img); //해당이미지뷰에 이미지 세팅하기.

            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }


    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }


    // 장소를 검색하면 그 장소에 마커가 찍히도록 처리하는 역할
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        geocoder = new Geocoder(this, Locale.KOREA);


        placeSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                placeSearchBtn.setEnabled(false);
                String str=placeSearchEdittext.getText().toString();
                str = "숭실대학교 "+str;
                List<Address> addressList = null;
                try {
                    // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
                    addressList = geocoder.getFromLocationName(
                            str, // 주소
                            10); // 최대 검색 결과 개수
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                if(addressList == null || addressList.size() == 0){
                    placeSearchBtn.setEnabled(true);
                    showToast("장소를 찾지 못했습니다");
                    return ;
                }

                mMap.clear();

                String address = null;
                String latitude = null;
                String longitude = null;
                LatLng location = null;
                for(int a = addressList.size()-1; a>=0; a--){
                    String[] splitStr = addressList.get(a).toString().split(",");

                    address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소
                    System.out.println(address); //주소

                    latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
                    longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

                    location = null;
                    try{
                        location = new LatLng(Double.valueOf(latitude),Double.valueOf(longitude));
                    }catch (Exception e){
                        placeSearchBtn.setEnabled(true);
                        showToast("데이터를 받아오지 못했습니다.\n장소명을 정확히 입력해주시기 바랍니다.");
                        return ;
                    }

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title("찾은 결과");
                    markerOptions.snippet(address); //세부적인 컨텐츠내용
                    markerOptions.position(location);
                    mMap.addMarker(markerOptions);
                    //검색된 장소들을 마커로 표시




                }

                placeLatEdittext.setText(latitude); placeLngEdittext.setText(longitude);
                placeAddressEdittext.setText(address);
                placeSearchBtn.setEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
                //검색된 장소중 가장 가까운 장소의
                // 정보를 가져와서 각각의 Edittext에 넣어줌.


            }
        });

        /*지도에서 숭실대학교가 먼저 보이도록 함. */
        LatLng soongsil = new LatLng(37.496606, 126.957408 ); //숭실대학교 좌표
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("숭실대학교");
        markerOptions.position(soongsil);
        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(soongsil));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

        mMap.setOnMarkerClickListener(this);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latlng = marker.getPosition();
        placeLatEdittext.setText(latlng.latitude+""); placeLngEdittext.setText(latlng.longitude+"");
        placeAddressEdittext.setText(marker.getSnippet());
        return false;
    }
}
