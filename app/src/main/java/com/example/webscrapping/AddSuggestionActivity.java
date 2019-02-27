package com.example.webscrapping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AddSuggestionActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner related_sector;
    private Spinner related_schemes;
    private TextView mDescriptionText,pdffile;
    private Button mUploadpdf,mSubmit,suggestion_to_mp,suggestion_to_da;
    private ImageView mContentimg;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefrence1,mRefrence2,mRefrence3;
    private List<String> sectorlist,schemelist;
    private int SELECT_FILE = 1;
    private Uri filePath,pathHolder;
    FirebaseStorage storage;
    StorageReference storageReference;
    int flag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ur_complain);

        related_schemes = findViewById(R.id.relatedshemesspinner);
        related_sector  = findViewById(R.id.relatedsectorspinner);
        mDescriptionText = findViewById(R.id.descriptiontext);
        mUploadpdf = findViewById(R.id.upload_pdf);
        mSubmit = findViewById(R.id.submit);
        suggestion_to_da = findViewById(R.id.suggest_da);
        suggestion_to_mp = findViewById(R.id.suggest_mp);
        mContentimg = findViewById(R.id.content_img);
        pdffile = findViewById(R.id.pdffilename);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mRefrence1 = mDatabase.getReference().child("sectors");
        mRefrence3 = mDatabase.getReference().child("suggestions");

        addListtoSpinner();

        mContentimg.setOnClickListener(this);
        mUploadpdf.setOnClickListener(this);
        mSubmit.setOnClickListener(this);
        suggestion_to_mp.setOnClickListener(this);
        suggestion_to_da.setOnClickListener(this);



    }

    private void addListtoSpinner() {

        sectorlist = new ArrayList();
        schemelist = new ArrayList();


        try {
            mRefrence1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sectorlist.add(dataSnapshot.getKey());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (Exception e){}
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sectorlist);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        related_sector.setAdapter(adp1);

        final String sector = String.valueOf(related_sector.getSelectedItem());

        mRefrence1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (sector.equals(ds.getKey())) {
                        mRefrence2 = mDatabase.getReference().child("sectors").child(sector);
                        mRefrence2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
                                    schemelist.add(ds1.getValue().toString());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, sectorlist);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        related_sector.setAdapter(adp2);


    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.content_img :
                uploadimg();
                break;

            case R.id.upload_pdf :
                uploadpdf();
                break;

            case R.id.submit :
                checkdatafilled();
                break;

            case R.id.suggest_da :
                flag=1;
                break;

            case R.id.suggest_mp :
                flag=2;
                break;

        }

    }

    private void uploadimg() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

    private void uploadpdf() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/msword,application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 7);

    }

    private void checkdatafilled() {

        if(filePath != null || pathHolder != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            StorageReference ref1 = storageReference.child("pdf/"+ UUID.randomUUID().toString());
                            ref1.putFile(pathHolder).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Toast.makeText(AddSuggestionActivity.this, "Uploaded pdf", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(AddSuggestionActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                            progressDialog.dismiss();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddSuggestionActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded image"+(int)progress+"%");
                        }
                    });

        }


        if (related_sector.getSelectedItem() != null && related_schemes.getSelectedItem() != null
                && (mDescriptionText.getText() != null || mDescriptionText.getText().toString().trim() != "")
                && pdffile.getText().toString() != null){

            final HashMap<String, Object> suggestionmap = new HashMap<>();
            suggestionmap.put("related-sector", related_sector.getSelectedItem().toString());
            suggestionmap.put("related-scheme", related_schemes.getSelectedItem().toString());
            suggestionmap.put("description", mDescriptionText.getText());
            suggestionmap.put("likes", "");
            suggestionmap.put("comments", "");
            suggestionmap.put("likesBy", "");
            suggestionmap.put("commentsBy", "");
            suggestionmap.put("imglink", filePath);
            suggestionmap.put("pdflink", pathHolder);
            suggestionmap.put("suggestion-type",flag);
            // complaintsmap.put("UUID",currentuseruid);
            String key = mRefrence3.push().getKey();
            suggestionmap.put("uid",key);

            mRefrence3.child(key).setValue(suggestionmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    mDescriptionText.setText("");
                    pdffile.setText("Upload pdf file related to complaint");
                    mContentimg.setImageBitmap(null);
                    mContentimg.setBackgroundResource(R.drawable.ic_camera_alt_black_24dp);
                    related_schemes.setSelection(0);
                    related_sector.setSelection(0);
                    flag = 0;
                    suggestionmap.clear();

                    finish();
                }
            });

        }

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == 7){
                pathHolder = data.getData();
                String PathHolder = data.getData().getPath();
                pdffile.setText(PathHolder);
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            filePath = data.getData();
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mContentimg.setImageBitmap(bm);
    }
}
