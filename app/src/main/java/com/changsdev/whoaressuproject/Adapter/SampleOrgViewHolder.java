package com.changsdev.whoaressuproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.fragment.OrgListFragment;

public class SampleOrgViewHolder extends RecyclerView.ViewHolder {
    public TextView orgName;
    private SparseBooleanArray selectedItems = new SparseBooleanArray(0);


    public SampleOrgViewHolder(View view){
        super(view);
        this.orgName = (TextView)view.findViewById(R.id.org_name);

//        // 아이템 클릭 이벤트 처리
//        view.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//                int position = getAdapterPosition();
//                if(selectedItems.get(position,false)){
//                    selectedItems.put(position,false);
//                    view.setBackgroundColor(Color.WHITE);
//                }else{
//                    selectedItems.put(position,true);
//                    view.setBackgroundColor(Color.BLUE);
//                }
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

    }
}
