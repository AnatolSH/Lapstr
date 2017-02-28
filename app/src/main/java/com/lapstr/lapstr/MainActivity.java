package com.lapstr.lapstr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mSelectImage;
    private StorageReference mSrorage;
    private ProgressDialog mProgressDialog;
    private Button buttonStart;
    private Button newActivity;
    private Button myCabinet;
    private Button singout;
    private RecyclerView mBloglist;
    private DatabaseReference mDatabase;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mRootDatabase;

    private String userId;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseLike;
    private boolean mProcessLike;


    static MediaController mediaC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBloglist = (RecyclerView) findViewById(R.id.blog_list2);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        mDatabaseLike.keepSynced(true);

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");
        mRootDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSrorage = FirebaseStorage.getInstance().getReference();
        mSelectImage = (Button) findViewById(R.id.select_image);
        mProgressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mediaC = new MediaController(this);

        authListener = new FirebaseAuth.AuthStateListener() { //если не авторизован, то открывает логин активити
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonStart = (Button) findViewById(R.id.start_btn);
        newActivity = (Button) findViewById(R.id.new_activity);
        myCabinet = (Button) findViewById(R.id.cab);
        singout = (Button) findViewById(R.id.sign_out);
        ////////

        mSelectImage.setOnClickListener(new View.OnClickListener() {//Выбор видео для заливки
            @Override
            public void onClick(View view) {
                mSelectImage.setOnClickListener(this);
                if (view == mSelectImage) {
                    Intent SecAct99 = new Intent(getApplicationContext(), PostActivity.class);
                    startActivity(SecAct99);
                }
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener(){ //кнопка старт которая теперь не нужна
            @Override
            public void onClick(View view) {
                buttonStart.setOnClickListener(this);
                if (view == buttonStart) {
                    startVideo();
                }
            }
        });


        newActivity.setOnClickListener(new View.OnClickListener(){//открывает КАМЕРУ
            @Override
            public void onClick(View view) {
                newActivity.setOnClickListener(this);
                if (view == newActivity) {
                    Intent SecAct = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(SecAct);
                }
            }
        });

        myCabinet.setOnClickListener(new View.OnClickListener(){ //открывает КАБИНЕТ
            @Override
            public void onClick(View view) {
                myCabinet.setOnClickListener(this);
                if (view == myCabinet) {
                    Intent SecAct = new Intent(getApplicationContext(), EditCabinet.class);
                    startActivity(SecAct);
                }
            }
        });

        singout.setOnClickListener(new View.OnClickListener(){ //Разлогинивается
            @Override
            public void onClick(View view) {
                singout.setOnClickListener(this);
                if (view == singout) {
                    signOut();
                }
            }
        });
    }
    public void signOut() { //метод на разлогинивание
        auth.signOut();
        finish();
    }


    public static Bitmap getBitmapFromURL(String src) { //для прорисовки аватарок
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private void startVideo() {
        // videoview.setMediaController(new MediaController(this));
        //  videoview.requestFocus();
        //    videoview.start();
    }


    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);

        FirebaseRecyclerAdapter<User, BlogViweHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, BlogViweHolder>(

                User.class,
                R.layout.blog_row,
                BlogViweHolder.class,
                mFirebaseDatabase

        ) {
            @Override
            protected void populateViewHolder(BlogViweHolder viewHolder, User model, final int position) {

                final String post_key = getRef(position).getKey();
                final String[] blbabla = new String[1];

                mFirebaseDatabase.child(getRef(position).getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        blbabla[0] = dataSnapshot.child("name").getValue().toString();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

                    viewHolder.setAwa(model.getAwaurl());
                    viewHolder.setName(model.getName());
                    viewHolder.setImage(model.getUrl());
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setmLikebtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent singleBlogIntent = new Intent(MainActivity.this, BlogSingleActivity.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
                        startActivity(singleBlogIntent);

                    }
                });

                viewHolder.awaClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent singleBlogIntent = new Intent(MainActivity.this, UserSingleActivity.class);
                        singleBlogIntent.putExtra("userName", blbabla[0]);
                        startActivity(singleBlogIntent);

                    }
                });

                viewHolder.nickClick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent singleBlogIntent = new Intent(MainActivity.this, UserSingleActivity.class);
                        singleBlogIntent.putExtra("userName", blbabla[0]);
                        startActivity(singleBlogIntent);

                    }
                });

                viewHolder.mLikebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mProcessLike = true;

                            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int maxNum = (int) dataSnapshot.child(post_key).getChildrenCount();

                                    if (mProcessLike) {

                                        if (dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())) {

                                            mDatabaseLike.child(post_key).child(auth.getCurrentUser().getUid()).removeValue();
                                            mDatabaseLike.child("count").child(post_key).setValue(maxNum-1);

                                            mProcessLike = false;

                                        } else {

                                            mDatabaseLike.child(post_key).child(auth.getCurrentUser().getUid()).setValue("Liked");
                                            mDatabaseLike.child("count").child(post_key).setValue(maxNum+1);
                                            mProcessLike = false;

                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                });

            }

        };

        mBloglist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViweHolder extends RecyclerView.ViewHolder{

        View mView;

        ImageButton mLikebtn;
        ImageView awaClick;
        TextView nickClick;
        TextView countLikes;
        TextView countComments;
        DatabaseReference mDatabaseLike;
        FirebaseAuth auth;
        ImageButton play;
        VideoView post_video;

        public BlogViweHolder(View itemView) {
            super(itemView);

            mView = itemView;
            awaClick = (ImageView) mView.findViewById(R.id.awko);
            nickClick = (TextView) mView.findViewById(R.id.post_name);
            mLikebtn = (ImageButton) mView.findViewById(R.id.like_btn);
            countLikes = (TextView) mView.findViewById(R.id.countlike2);
            countComments = (TextView) mView.findViewById(R.id.countcomments);
            play = (ImageButton) mView.findViewById(R.id.playButton);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            auth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play.setVisibility(View.GONE);
                    post_video.setMediaController(mediaC);
                    mediaC.setAnchorView(mView);
                    post_video.start();
                    post_video.requestFocus();
                }
            });
        }

        public void setmLikebtn(final String post_key){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //вот тут
                    Map<String,Object> value = (Map<String, Object>) dataSnapshot.child("count").getValue();
                    try
                    {String name1 = String.valueOf(value.get(post_key));
                        countLikes.setText(name1);
                    }
                    catch (Exception e){
                        countLikes.setText("0");
                    }
                    Map<String,Object> value2 = (Map<String, Object>) dataSnapshot.child("countComments").getValue();
                    try
                    {
                        String name2 = String.valueOf(value2.get(post_key));
                        countComments.setText(name2);
                    }
                    catch (Exception e){
                        countComments.setText("0");
                    }

                    if(dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())){

                        mLikebtn.setImageResource(R.mipmap.ic_favorite_red_48dp);

                    } else {

                        mLikebtn.setImageResource(R.mipmap.ic_favorite_border_grey_48dp);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        public void setAwa(String imgur){

            ImageView im_d = (ImageView) mView.findViewById(R.id.awko);
            im_d.setImageBitmap(getBitmapFromURL(imgur));

        }

        public void setTitle(String tit){

            TextView title = (TextView) mView.findViewById(R.id.post_title);
            title.setText(tit);

        }

        public void setName(String desc){

            TextView post_name = (TextView) mView.findViewById(R.id.post_name);
            post_name.setText(desc);
        }

        public void setImage(String video){

            post_video = (VideoView) mView.findViewById(R.id.post_video);
            post_video.setVideoPath(video);
            post_video.setVideoURI(Uri.parse(video));


        }

    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}