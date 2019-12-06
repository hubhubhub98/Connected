package com.changsdev.whoaressuproject.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.changsdev.whoaressuproject.R;
import com.changsdev.whoaressuproject.activity_chatroom;
import com.changsdev.whoaressuproject.model.TeamInfo;
import com.changsdev.whoaressuproject.model.UserVO;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrgAdapter<orgItemList> extends RecyclerView.Adapter<SampleOrgViewHolder> {

    private final ArrayList<UserVO> orgItemList;
    private Context context;
    private EditText SearchWard;


    public OrgAdapter(Context context, EditText SearchWard){
        this.orgItemList = new ArrayList<>();
        this.context = context;
        this.SearchWard = SearchWard;
//        ShowDialog();

        getList("");
        SearchWard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String result = s.toString();
                getList(result);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

//    private void ShowDialog() {
//        final Dialog dialog = new Dialog(context,
//                android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_create);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.gravity = Gravity.CENTER;
//
//        dialog.getWindow().setAttributes(lp);
//
//
//        dialog.show();
//    }

    public void getList(final String result){
        FirebaseDatabase.getInstance().getReference().child("users").orderByChild("org")
                .equalTo(true)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        orgItemList.clear();
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            UserVO user = snapshot.getValue(UserVO.class);
                            if(user.getUserName().contains(result)){
                                orgItemList.add(user);
                            }
                        }

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
        viewHolder.orgName.setText(""+orgItemList.get(position).getUserName());
        viewHolder.callingNum.setText(""+orgItemList.get(position).getUserPhoneNumber());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(context,
                        android.R.style.Theme_Translucent_NoTitleBar);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_create);
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                lp.gravity = Gravity.CENTER;
                dialog.getWindow().setAttributes(lp);

                TextView team = dialog.findViewById(R.id.team);
                final TextView callNumber = dialog.findViewById(R.id.callNumber);
                ImageView chatting = (ImageView)dialog.findViewById(R.id.chatting);
                ImageView calling = (ImageView)dialog.findViewById(R.id.calling);
                team.setText(orgItemList.get(position).getUserName()+"");
                callNumber.setText(orgItemList.get(position).getUserPhoneNumber()+"");

                chatting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(view.getContext(), activity_chatroom.class);
                        context.startActivity(intent);
                    }
                });
                calling.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent();
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+callNumber.getText().toString()));
                        view.getContext().startActivity(intent);
                    }
                });
//                builder.setView(layout);
//                AlertDialog dialog = builder.create();
                dialog.show();

//                Toast.makeText(context,orgItemList.get(position).getUserName()+"",Toast.LENGTH_SHORT).show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                View layout = inflater.inflate(R.layout.dialog_create,null);






            }
        });

    }


}
