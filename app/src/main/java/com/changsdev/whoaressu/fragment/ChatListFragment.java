package com.changsdev.whoaressu.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.changsdev.whoaressu.DTO.ChatInfo;
import com.changsdev.whoaressu.R;
import com.changsdev.whoaressu.Adapter.MyAdapter;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {


    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_chat_list, container, false);
        RecyclerView mRecyclerView = v.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<ChatInfo> chatInfoArrayList = new ArrayList<>();
        chatInfoArrayList.add(new ChatInfo("장학팀", "5,000원"));
        chatInfoArrayList.add(new ChatInfo("교무팀", "4,600원"));
        chatInfoArrayList.add(new ChatInfo("학사팀", "4,000원"));

        MyAdapter myAdapter = new MyAdapter(chatInfoArrayList);

        mRecyclerView.setAdapter(myAdapter);

        return v;


    }

}
