package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.changsdev.whoaressuproject.fragment.ScrollableMapFragment;
import com.changsdev.whoaressuproject.model.PlaceVO;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

//클릭한 장소의 정보들을 보여줌
public class PlaceViewActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    public static final int INDICATE_MARKER_DIRECTLY = 9; //requestcode.
    public static final int PICK_FROM_ALBUM = 10; //requestCode

    private Uri imageUri;
    private ImageView placePhotoImageView;
    private EditText placeNameEdittext;
    private EditText placeLatEdittext;
    private EditText placeLngEdittext;
    private EditText placeAddressEdittext;
    private EditText placeKeywordEdittext;

    private Button updateBtn;
    private Button deleteBtn;
    private LinearLayout updateDeleteLayout;
    private LinearLayout googleMapSearchLayout;
    private GoogleMap mMap; //구글맵 api사용
    private FragmentManager fragmentManager;
    private ScrollableMapFragment mapFragment; //지도를 표시하는 프래그먼트를 참조.
    private Geocoder geocoder; //주소나 지역, 장소를 지리좌표(위도,경도)로 변환해주는 역할.

    private EditText placeSearchEdittext;
    private Button placeSearchBtn;
    private LinearLayout mainLayout;
    private LinearLayout googleMap;
    private Button openMapBtn;
    private ScrollView scrollView;
    private Button indicateMarkerBtn;

    String placeId;
    String category;
    String placeName;
    double lat,lng;
    String placeAddress;
    String placePhotoUrl;
    String placeKeyword;
    String placeUid;
    String placePid;

    View.OnClickListener myListener;

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
        placeUid = intent.getStringExtra("placeUid");
        placePid = intent.getStringExtra("placePid");
        placeId = intent.getStringExtra("placeId");

        actionBar.setTitle(placeName);

        placePhotoImageView = (ImageView)findViewById(R.id.place_photo_imageview);
        placeNameEdittext = (EditText)findViewById(R.id.placename_edittext);
        placeLatEdittext = (EditText)findViewById(R.id.place_lat_edittext);
        placeLngEdittext = (EditText)findViewById(R.id.place_lng_edittext);
        placeAddressEdittext = (EditText)findViewById(R.id.place_address_edittext);
        placeKeywordEdittext = (EditText)findViewById(R.id.place_keyword_edittext);
        updateBtn = (Button)findViewById(R.id.place_update_btn);
        deleteBtn = (Button)findViewById(R.id.place_delete_btn);
        updateDeleteLayout = (LinearLayout)findViewById(R.id.update_delete_btn_layout);
        googleMapSearchLayout = (LinearLayout)findViewById(R.id.google_map_search_layout);


        placeSearchBtn = (Button)findViewById(R.id.place_search_btn);
        placeSearchEdittext = (EditText)findViewById(R.id.place_search_editText);

        mainLayout = (LinearLayout)findViewById(R.id.mainLayout);
        openMapBtn = (Button)findViewById(R.id.open_map_btn);
        googleMap = (LinearLayout)findViewById(R.id.google_map);
        scrollView = (ScrollView)findViewById(R.id.scroll_view);
        indicateMarkerBtn = (Button)findViewById(R.id.indicate_marker_btn);

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

        myListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.place_update_btn:
                        updateBtn.setEnabled(false);
                        placeUpdate();
                        break;
                    case R.id.open_map_btn:
                        if(googleMap.getVisibility() == View.GONE){ //안보이는 상태라면
                            googleMap.setVisibility(View.VISIBLE); //보이게함
                        }else{ //보이는 상태라면
                            googleMap.setVisibility(View.GONE); //감춤
                        }
                        break;
                    case R.id.place_photo_imageview:
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        //갤러리액티비티를 실행하기위한 세팅.
                        startActivityForResult(Intent.createChooser(intent,"이미지를 선택하세요"),PICK_FROM_ALBUM);
                        //갤러리에 접근하자.
                        break;
                    case R.id.indicate_marker_btn:
                        Intent intent1 = new Intent(PlaceViewActivity.this,PlaceIndicateMarkerActivity.class);
                        startActivityForResult(intent1,INDICATE_MARKER_DIRECTLY);
                        break;
                    case R.id.place_delete_btn:
                        deleteBtn.setEnabled(false);
                        placeDelete();
                        break;


                }
            }
        };

        openMapBtn.setOnClickListener(myListener);


        //이 장소를 등록한 사람이 현재 로그인한 사람이라면 수정과 삭제를 할 수 있는 권한이 주어진다.
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(placeUid)){
            placeNameEdittext.setEnabled(true); placeNameEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
            placeLatEdittext.setEnabled(true); placeLngEdittext.setEnabled(true);
            placeAddressEdittext.setEnabled(true); placeAddressEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
            placeKeywordEdittext.setEnabled(true); placeKeywordEdittext.setInputType(InputType.TYPE_CLASS_TEXT);
            updateDeleteLayout.setVisibility(View.VISIBLE);
            googleMapSearchLayout.setVisibility(View.VISIBLE);
            indicateMarkerBtn.setVisibility(View.VISIBLE);

            //상호작용 할 수 있도록 함.
            indicateMarkerBtn.setOnClickListener(myListener);
            updateBtn.setOnClickListener(myListener);
            placePhotoImageView.setOnClickListener(myListener);
            deleteBtn.setOnClickListener(myListener);
        }




    }

    //장소 데이터 수정 처리
    private void placeUpdate(){
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
            updateBtn.setEnabled(true);
            return ;
        }

        try{
            lat = Double.valueOf(placeLat);
            lng = Double.valueOf(placeLng);
            //실수로 바꿔주는 과정에서 예외가 발생할수 있기때문에 예외처리
        }catch (Exception e){
            showToast("위도 경도 정보를 올바르게 입력하세요");
            updateBtn.setEnabled(true);
            return ;
        }

        final StorageReference storage = FirebaseStorage.getInstance().getReference().child("placeImages")
                .child(placePid);
        if(imageUri != null){ //갤러리를 통해 이미지를 다시세팅해줬을경우
            //수정처리
            storage.putFile(imageUri)
                    .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return storage.getDownloadUrl();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {//업로드 성공시 실행되는 메서드
                    if (task.isSuccessful()) {
                        // 파일 업로드에 성공했을시 데이터베이스에 장소데이터를 수정해준다.
                        Uri downloadUri = task.getResult();
                        PlaceVO place = new PlaceVO();
                        place.setAddress(placeAddress);
                        place.setCategory(category);
                        place.setKeywords(placeKeyword);
                        place.setLat(lat);
                        place.setLng(lng);
                        place.setName(placeName);
                        place.setRecommend(0); place.setPlacePhotoUrl(downloadUri.toString());
                        place.setPid(placePid); place.setUid(placeUid);
                        FirebaseDatabase.getInstance().getReference().child("places").child(placeId)
                                .setValue(place);
                        showToast("수정을 완료했습니다.");
                        finish();
                    } else {
                        // Handle failures
                        // ...
                        showToast("수정에 실패했습니다.");
                        updateBtn.setEnabled(true);
                    }
                }
            });
        }else{ //이미지가 그대로일경우
            PlaceVO place = new PlaceVO();
            place.setAddress(placeAddress);
            place.setCategory(category);
            place.setKeywords(placeKeyword);
            place.setLat(lat);
            place.setLng(lng);
            place.setName(placeName);
            place.setRecommend(0); place.setPlacePhotoUrl(placePhotoUrl); //이미지는 그대로
            place.setPid(placePid); place.setUid(placeUid);
            FirebaseDatabase.getInstance().getReference().child("places").child(placeId)
                    .setValue(place);
            showToast("수정을 완료했습니다.");
            finish();
        }
    }

    //장소 데이터 삭제처리
    private void placeDelete(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("확인메세지").setMessage("정말로 삭제하시겠습니까?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseDatabase.getInstance().getReference().child("places") //데이터베이스에서 해당 장소데이터를 지워준다.
                        .child(placeId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        FirebaseStorage.getInstance().getReference().child("placeImages").child(placePid)
                                .delete(); //이미지파일까지 확실하게 지워준다.
                        showToast("삭제에 성공했습니다");
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("삭제에 실패했습니다.");
                        deleteBtn.setEnabled(true);
                    }
                });
            }
        });
        builder.setNegativeButton("NO",null);
        deleteBtn.setEnabled(true);
        builder.create().show();
    }

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

                    placeSearchBtn.setEnabled(true);
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
                CameraPosition.Builder builder = new CameraPosition.Builder();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.target(location).zoom(16.0f).build()));
                //검색된 장소중 가장 가까운 장소의
                // 정보를 가져와서 각각의 Edittext에 넣어줌.


            }
        });





        LatLng place = new LatLng(lat,lng ); //장소의 좌표
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(placeName); //장소의 이름
        markerOptions.snippet(placeAddress); //장소의 주소
        markerOptions.position(place);
        mMap.addMarker(markerOptions);


        CameraPosition.Builder builder = new CameraPosition.Builder();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(builder.target(place).zoom(16.0f).build()));
        mMap.setOnMarkerClickListener(this);
    }

    private void showToast(String msg){ //msg를 화면에 출력한다.
        Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
    }


    //갤러리 액티비티로부터 얻어온 데이터를 처리하자
    //지도를 표시하는 액티비티로부터 얻어온 데이터를 처리하자
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
        }else if(requestCode == INDICATE_MARKER_DIRECTLY && resultCode == RESULT_OK){
            double lat = data.getDoubleExtra("lat",0);
            double lng = data.getDoubleExtra("lng",0);
            String address = data.getStringExtra("address");

            placeLngEdittext.setText(lng+"");
            placeLatEdittext.setText(lat+"");
            placeAddressEdittext.setText(address);

            showToast("위치정보를 얻어왔습니다.");
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        LatLng latlng = marker.getPosition();
        placeLatEdittext.setText(latlng.latitude+""); placeLngEdittext.setText(latlng.longitude+"");
        placeAddressEdittext.setText(marker.getSnippet());
        return false;
    }
}
