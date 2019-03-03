package com.example.webscrapping;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {

    private List<ModalClass> modalClassList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, time, description,relatedsector,relatedschemes,likesnumber;
        public ImageView contentimg;
        public RelativeLayout like,comment,reportasspam;
        public Button recommend,dismiss;

        public MyViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            time = view.findViewById(R.id.timestamp);
            description = view.findViewById(R.id.descriptiontext);
            contentimg = view.findViewById(R.id.content_img);
            like = view.findViewById(R.id.likelayout);
            comment = view.findViewById(R.id.commentlayout);
            reportasspam = view.findViewById(R.id.reportasspam);
            relatedschemes = view.findViewById(R.id.relatedschemes);
            relatedsector = view.findViewById(R.id.relatedsector);
            likesnumber = view.findViewById(R.id.likecount);
            recommend = view.findViewById(R.id.recommendtoda);
            dismiss = view.findViewById(R.id.dismiss);

        }

    }


    public SuggestionAdapter(List<ModalClass> modalClassList, Context context) {
        this.modalClassList = modalClassList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_post_suggestion_complain, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ModalClass modalClass = modalClassList.get(position);
        String desc = modalClass.getRelatedsector();
        holder.relatedsector.setText(modalClass.getRelatedsector());
        holder.relatedschemes.setText(modalClass.getRelatedscheme());
        holder.description.setText(desc);
        holder.time.setText(modalClass.getTimestamp());
        holder.likesnumber.setText(String.valueOf(modalClass.getLikes()));
        holder.recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("MPNode").child("hajipur")
//                        .child("MPrecommendation");
//
//                HashMap<String,Object> map = new HashMap<>();
//                map.put("related-sector",modalClass.getRelatedsector());
//                map.put("related-scheme",modalClass.getRelatedscheme());
//                map.put("")

                Intent recommendworkintwnt = new Intent(context,RecommendWork.class);
                context.startActivity(recommendworkintwnt);



            }
        });

        holder.dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("complaints").child(modalClass.getUid());
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mDatabase.setValue(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

//        String imgurl = modalClass.getImglink();
//
//        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(imgurl);
//        Glide.with(context).load(ref).into(holder.contentimg);


    }

    @Override
    public int getItemCount() {
        return modalClassList.size();
    }
}
