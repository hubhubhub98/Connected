package com.changsdev.whoaressuproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.changsdev.whoaressuproject.model.PlaceVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PlaceListActivity extends AppCompatActivity {

    private String category;
    private RecyclerView recyclerView;
    private Button placeWriteBtn;
    private TextView categoryNameTextView;
    private EditText placeSearchEdittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_list);

        placeWriteBtn = (Button)findViewById(R.id.place_write_btn);
        categoryNameTextView = (TextView)findViewById(R.id.category_name_textview);
        placeSearchEdittext = (EditText)findViewById(R.id.place_search_editText);

        Intent intent = getIntent();
        category = intent.getStringExtra("category");
        categoryNameTextView.setText(category);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide(); //액션바는 숨겨주자.


        recyclerView = (RecyclerView)findViewById(R.id.place_list_recyclerview);
        recyclerView.setAdapter(new PlaceListRecyclerViewAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.place_write_btn:
                        Intent intent = new Intent(PlaceListActivity.this,PlaceWriteActivity.class);
                        intent.putExtra("category",category);
                        startActivity(intent);

                }
            }
        };

        placeWriteBtn.setOnClickListener(myListener);


    }


    class PlaceListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private List<PlaceVO> placeList;

        PlaceListRecyclerViewAdapter(){
            placeList = new ArrayList<>();
            getList("");

            placeSearchEdittext.addTextChangedListener(new TextWatcher() { //Edittext에 리스너 등록해주기
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence s, int i, int i1, int i2) {// 해당 에딧텍스트 값이 변경될떄마다 호출
                    String result = s.toString(); //에딧텍스트에 입력한 값을 얻어온다.
                    getList(result);


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }

        public void getList(final String keyword){
            FirebaseDatabase.getInstance().getReference().child("places").orderByChild("category")
                    .equalTo(category)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //선택한 카테고리에 해당하는 장소목록을 데이터베이스로부터 가져온다.
                            placeList.clear();
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                PlaceVO place = snapshot.getValue(PlaceVO.class);
                                if (place.getName().contains(keyword) || place.getAddress().contains(keyword)
                                        || place.getKeywords().contains(keyword)) {
                                    //장소의 이름이나 주소나 키워드에 에딧텍스트에 입력해준 값이 포함되어있다면
                                    //해당 장소는 리스트에 담아준다.
                                    placeList.add(place);
                                }
                            }

                            notifyDataSetChanged(); //변경된 데이터를 리사이클러뷰에 반영해준다. 일종의 새로고침.
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_info,parent,false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            final CustomViewHolder customViewholder = (CustomViewHolder)holder;

            //이미지경로를통해 해당 이미지뷰에 이미지를 로딩시킨다.
            Glide.with(customViewholder.itemView.getContext()).load(placeList.get(position).getPlacePhotoUrl())
                    .apply(new RequestOptions().centerCrop())
                    .into(customViewholder.placePhotoImageView);

            customViewholder.placeNameTextView.setText(placeList.get(position).getName());

            customViewholder.placeMapImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PlaceListActivity.this,PlaceMapViewActivity.class);
                    intent.putExtra("category",placeList.get(position).getCategory());
                    intent.putExtra("placeName",placeList.get(position).getName());
                    intent.putExtra("lat",placeList.get(position).getLat());
                    intent.putExtra("lng",placeList.get(position).getLng());
                    intent.putExtra("placeAddress",placeList.get(position).getAddress());
                    startActivity(intent);
                }
            });

            customViewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PlaceListActivity.this,PlaceViewActivity.class);
                    intent.putExtra("category",placeList.get(position).getCategory());
                    intent.putExtra("placeName",placeList.get(position).getName());
                    intent.putExtra("lat",placeList.get(position).getLat());
                    intent.putExtra("lng",placeList.get(position).getLng());
                    intent.putExtra("placeAddress",placeList.get(position).getAddress());
                    intent.putExtra("placePhotoUrl",placeList.get(position).getPlacePhotoUrl());
                    intent.putExtra("placeKeyword",placeList.get(position).getKeywords());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return placeList.size();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder{
            ImageView placePhotoImageView;
            TextView placeNameTextView;
            ImageView placeMapImageView;
            public CustomViewHolder(@NonNull View itemView) {
                super(itemView);
                placePhotoImageView = itemView.findViewById(R.id.place_photo_imageview);
                placeNameTextView = itemView.findViewById(R.id.place_name_textview);
                placeMapImageView = itemView.findViewById(R.id.place_map_imageView);
            }
        }
    }

}
