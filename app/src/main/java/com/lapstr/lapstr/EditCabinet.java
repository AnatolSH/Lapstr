package com.lapstr.lapstr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;


/**
 * Created by Anatole on 27.01.2017.
 */

public class EditCabinet extends AppCompatActivity{
    private ImageButton homeBtn;
    private ImageButton addBtn;
    private ImageButton cameraBtn;
    private ImageButton cabinetBtn;
    private ImageButton outBtn;
    private RecyclerView mBloglist;
    private DatabaseReference mDatabase;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth auth;

    static MediaController mediaC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcab_activity);
        mBloglist = (RecyclerView) findViewById(R.id.blog_list88);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("cabinet").child("users");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mediaC = new MediaController(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        homeBtn = (ImageButton) findViewById(R.id.imageButton2);
        addBtn = (ImageButton) findViewById(R.id.imageButton3);
        cameraBtn = (ImageButton) findViewById(R.id.imageButton4);
        cabinetBtn = (ImageButton) findViewById(R.id.imageButton5);
        outBtn = (ImageButton) findViewById(R.id.imageButton6);

        homeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                homeBtn.setOnClickListener(this);
                if (view == homeBtn) {
                    Intent SecAct = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(SecAct);
                }
            }
        });
        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                addBtn.setOnClickListener(this);
                if (view == addBtn) {
                    Intent SecAct = new Intent(getApplicationContext(), PostActivity.class);
                    startActivity(SecAct);
                }
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cameraBtn.setOnClickListener(this);
                if (view == cameraBtn) {
                    Intent SecAct = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(SecAct);
                }
            }
        });
        cabinetBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                cabinetBtn.setOnClickListener(this);
                if (view == cabinetBtn) {
                    Intent SecAct = new Intent(getApplicationContext(), EditCabinet.class);
                    startActivity(SecAct);
                }
            }
        });
        outBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                outBtn.setOnClickListener(this);
                signOut();
            }
        });

    }

    public void signOut() { //метод на разлогинивание
        auth.signOut();
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Cabinet, EditCabinet.BlogViweHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Cabinet, EditCabinet.BlogViweHolder>(

                Cabinet.class,
                R.layout.blog_row_for_user,
                EditCabinet.BlogViweHolder.class,
                mFirebaseDatabase

        ) {
            @Override
            protected void populateViewHolder(EditCabinet.BlogViweHolder viewHolder, Cabinet model, final int position) {

                final String[] blbabla = new String[1];

                mFirebaseDatabase.child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        blbabla[0] = dataSnapshot.child("uid").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                viewHolder.setAwa(model.getUrl());
                viewHolder.setName(model.getUserName());
                viewHolder.setCountVideo(model.getCountVideo());


                viewHolder.imageButton1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent singleBlogIntent = new Intent(EditCabinet.this, UserSingleActivity.class);
                        singleBlogIntent.putExtra("uid", blbabla[0]);
                        startActivity(singleBlogIntent);

                    }
                });
            }

        };

        mBloglist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViweHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageView awaClick;
        TextView nickClick;

        ImageButton imageButton1;

        public BlogViweHolder(View itemView) {
            super(itemView);

            mView = itemView;
            awaClick = (ImageView) mView.findViewById(R.id.awko88);
            nickClick = (TextView) mView.findViewById(R.id.post_name88);
            imageButton1 = (ImageButton) mView.findViewById(R.id.imageButton7);
        }

        public void setAwa(String imgur){

            ImageView im_d = (ImageView) mView.findViewById(R.id.awko88);
            Picasso.with(itemView.getContext()).load(imgur).into(im_d);

        }

        public void setName(String desc){

            TextView post_name = (TextView) mView.findViewById(R.id.post_name88);
            post_name.setText(desc);
        }

        public void setCountVideo(String co)
        {
            TextView countVideo = (TextView) mView.findViewById(R.id.post_title99);
            countVideo.setText(co);
        }

    }
}