package com.example.webscrapping;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.MyViewHolder> {

    private List<ModalClass> modalClassList;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView username, time, description,relatedsector,relatedschemes,likesnumber;
        public ImageView contentimg;
        public RelativeLayout like,comment,share;

        public MyViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            time = view.findViewById(R.id.timestamp);
            description = view.findViewById(R.id.descriptiontext);
            contentimg = view.findViewById(R.id.content_img);
            like = view.findViewById(R.id.likelayout);
            comment = view.findViewById(R.id.commentlayout);
            share = view.findViewById(R.id.sharelayout);
            relatedschemes = view.findViewById(R.id.relatedschemes);
            relatedsector = view.findViewById(R.id.relatedsector);
            likesnumber = view.findViewById(R.id.likenumber);

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

//        String imgurl = modalClass.getImglink();
//
//        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(imgurl);
//        Glide.with(context).load(ref).into(holder.contentimg);


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Suggestions").child(modalClass.getUid());
                 mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                         for (DataSnapshot ds : dataSnapshot.getChildren()){

                             //TODO update likesBy by Adding current user uid and update the like node by children count of likes by node

                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError databaseError) {

                     }
                 });
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO update comment node and show all comments in a dialog or expandable list view

            }
        });
    }

    @Override
    public int getItemCount() {
        return modalClassList.size();
    }
}
