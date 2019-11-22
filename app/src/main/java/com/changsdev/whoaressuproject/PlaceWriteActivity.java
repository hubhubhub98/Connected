package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.changsdev.whoaressuproject.model.PlaceVO;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.util.UUID;

public class PlaceWriteActivity extends AppCompatActivity {

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


        Intent intent = getIntent(); //인텐트를 통해서 장소에대한 카테고리정보를 얻어온다
        ActionBar actionBar = getSupportActionBar();

        /*
        String category = intent.getStringExtra("category");
        우선은 테스트를 위해서 category를 무족건 '맛집'으로 설정해놓겠음
        */
        category = "맛집";
        actionBar.setTitle(category);


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

                }
            }
        };

        placePhotoImageView.setOnClickListener(myListener);
        placeWriteBtn.setOnClickListener(myListener);




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
                            place.setRecommend(0);
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
}
