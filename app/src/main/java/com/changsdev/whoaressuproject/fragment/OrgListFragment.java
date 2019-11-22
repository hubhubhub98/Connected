package com.changsdev.whoaressuproject.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.changsdev.whoaressuproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrgListFragment extends Fragment {
    public OrgListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 조직 목록 리스트 버튼 동작
        View view = inflater.inflate(R.layout.fragment_org_list, container, false);
        final EditText serchWard = (EditText)view.findViewById(R.id.serchWard);
        String ward;
        // 전화번호 띄우기 기능
        //Button button = (Button) view.findViewById(R.id.button);
        //button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
//                intent.setAction(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:01038405669"));
//                startActivity(intent);
//            }
//        });
        ImageButton serchOrgList = (ImageButton)view.findViewById(R.id.serchOrgList);
        serchOrgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serchWard.getText();
            }
        });
        return view;
    }

}
