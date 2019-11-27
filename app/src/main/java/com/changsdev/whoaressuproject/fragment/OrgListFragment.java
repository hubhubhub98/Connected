package com.changsdev.whoaressuproject.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.changsdev.whoaressuproject.Adapter.MyAdapter;
import com.changsdev.whoaressuproject.Adapter.OrgAdapter;
import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.model.ChatInfo;
import com.changsdev.whoaressuproject.model.TeamInfo;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrgListFragment extends Fragment {
    public OrgListFragment() {
        // Required empty public constructor
    }
    EditText SearchWard;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 조직 목록 리스트 버튼 동작
        View view = inflater.inflate(R.layout.fragment_org_list, container, false);
//        final EditText serchWard = (EditText)view.findViewById(R.id.serchWard);
//        String ward;
//        ImageButton serchOrgList = (ImageButton)view.findViewById(R.id.serchOrgList);
//        serchOrgList.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                serchWard.getText();
//            }
//        });
        // 전화번호 띄우기 기능
//        TextView button = (TextView) view.findViewById(R.id.org_name);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
//                intent.setAction(Intent.ACTION_DIAL);
//                intent.setData(Uri.parse("tel:01038405669"));
//                startActivity(intent);
//            }
//        });

        SearchWard = view.findViewById(R.id.serchWard);

        // 그리드 리사이클러뷰
       /* ArrayList<TeamInfo> orgItemList = new ArrayList<TeamInfo>(){{
            add(new TeamInfo("장학팀"));
            add(new TeamInfo("교무팀"));
            add(new TeamInfo("학사팀"));
            add(new TeamInfo("학생서비스팀"));
            add(new TeamInfo("컴퓨터학부"));
            add(new TeamInfo("전자정보공학부"));
            add(new TeamInfo("스포츠학부"));
            add(new TeamInfo("철학과"));
            add(new TeamInfo("글로벌미디어학부"));
            add(new TeamInfo("스마트시스템소프트웨어학부"));
        }};*/

        int numberofColumns = 2;
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler);
        RecyclerView.LayoutManager mGridLayoutManager;
        OrgAdapter myAdapter = new OrgAdapter(getActivity(),SearchWard);

        mRecyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(getActivity(),numberofColumns);


        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(myAdapter);
        return view;
    }

}
