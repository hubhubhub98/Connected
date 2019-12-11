package com.changsdev.whoaressuproject.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.changsdev.whoaressuproject.MainActivity;
import com.changsdev.whoaressuproject.PlaceListActivity;
import com.changsdev.whoaressuproject.PlaceWriteActivity;
import com.changsdev.whoaressuproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlaceFragment extends Fragment implements MainActivity.OnBackPressedListener{

    /*LinearLayout universityOrgLayout;
    LinearLayout restaurantLayout;
    LinearLayout medicalLayout;
    LinearLayout convLayout;
    LinearLayout bankAtmPostLayout;
    LinearLayout beautyShopLayout;*/

    LinearLayout categorysLayout;
    public PlaceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_place, container, false);

        categorysLayout = v.findViewById(R.id.categorys_linearlayout);
        int childCount = categorysLayout.getChildCount();
        for(int a=0; a<childCount; a++){
            View view = categorysLayout.getChildAt(a);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TextView categoryText = ((TextView)((LinearLayout)view).getChildAt(1));
                    Intent intent = new Intent(inflater.getContext(), PlaceListActivity.class);
                    intent.putExtra("category",categoryText.getText().toString());
                    startActivity(intent); //선택한 카테고리 정보와함께 설정해준 액티비티로 이동
                }
            });
        }

        return v;
    }

    ////////// back 버튼 2번 클릭 시 앱 종료 //////////
    @Override
    public void onBack() {
        // 리스너를 설정하기 위해 Activity 를 받아옴
        MainActivity activity = (MainActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제
        activity.setOnBackPressedListener(null);
    }

    // Fragment 호출 시 반드시 호출되는 오버라이드 메소드
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity)context).setOnBackPressedListener(this);
    }
}
