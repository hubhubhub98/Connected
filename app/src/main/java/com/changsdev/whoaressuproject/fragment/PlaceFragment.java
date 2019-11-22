package com.changsdev.whoaressuproject.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.changsdev.whoaressuproject.PlaceWriteActivity;
import com.changsdev.whoaressuproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceFragment extends Fragment {

    private Button placeWriteBtn;

    public PlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place, container, false);
        placeWriteBtn = (Button)v.findViewById(R.id.place_write_btn);

        placeWriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(inflater.getContext(), PlaceWriteActivity.class);
                intent.putExtra("category","맛집"); //테스트용으로 맛집만 추가해보자
                startActivity(intent);
            }
        });
        return v;
    }

}
