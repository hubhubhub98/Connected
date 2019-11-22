package com.changsdev.whoaressuproject.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.model.TeamInfo;

public class OrgAdapter<orgItemList> extends RecyclerView.Adapter<SampleOrgViewHolder> {

    private final ArrayList<TeamInfo> orgItemList;
    private Context context;



    public OrgAdapter(Context context, ArrayList<TeamInfo> orgItemList){
        this.orgItemList = orgItemList;
        this.context = context;
    }
    @Override
    public int getItemCount() {
        return this.orgItemList.size();
    }
    @Override
    public SampleOrgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sampleItemLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_org_list_recycler,parent,false);
        return new SampleOrgViewHolder(sampleItemLayout);
    }

    @Override
    public void onBindViewHolder(SampleOrgViewHolder viewHolder, final int position) {
        viewHolder.orgName.setText(""+orgItemList.get(position).name);

        viewHolder.orgName.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(orgItemList.get(position).name));
                context.startActivity(intent);
            }
        });
    }


}
