package com.lapstr.lapstr;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button mSelectVideo;
    private Button mSelectCamera;
    private Button myCabinet;
    private Button singout;
    private ImageButton homeBtn;
    private ImageButton addBtn;
    private ImageButton cameraBtn;
    private ImageButton cabinetBtn;
    private ImageButton outBtn;
    private RecyclerView mBloglist;
    private DatabaseReference mDatabase;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private DatabaseReference mDatabaseLike;
    private boolean mProcessLike;


    static MediaController mediaC;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBloglist = (RecyclerView) findViewById(R.id.blog_list2);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("UsersVideo");

        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //mSelectVideo = (Button) findViewById(R.id.select_video);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ///////////Reverce blog list
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mBloglist.setLayoutManager(layoutManager);
        /////////


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

      //  getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
      //  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

     /*   mSelectCamera = (Button) findViewById(R.id.camera_activity);
        myCabinet = (Button) findViewById(R.id.cab);
        singout = (Button) findViewById(R.id.sign_out);

        mSelectVideo.setOnClickListener(new View.OnClickListener() {//Выбор видео для заливки
            @Override
            public void onClick(View view) {
                mSelectVideo.setOnClickListener(this);
                if (view == mSelectVideo) {
                    Intent SecAct99 = new Intent(getApplicationContext(), PostActivity.class);
                    startActivity(SecAct99);
                }
            }
        });


        mSelectCamera.setOnClickListener(new View.OnClickListener(){//открывает КАМЕРУ
            @Override
            public void onClick(View view) {
                mSelectCamera.setOnClickListener(this);
                if (view == mSelectCamera) {
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
        });  */

       /* BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_homepage) {

                }
                if (tabId == R.id.tab_selectVideo) {

                    Intent SecAct99 = new Intent(getApplicationContext(), PostActivity.class);
                    startActivity(SecAct99);

                }
                if (tabId == R.id.tab_cabinet) {

                    Intent SecAct = new Intent(getApplicationContext(), EditCabinet.class);
                    startActivity(SecAct);
                }
                if (tabId == R.id.tab_camera) {

                    Intent SecAct = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(SecAct);
                }

                if (tabId == R.id.tab_signout) {
                    signOut();
                }
            }
        });*/

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


  /*  public static Bitmap getBitmapFromURL(String src) { //для прорисовки аватарок
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
    }*/

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
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());

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

                        mDatabaseLike.child("Likes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int maxNum = (int) dataSnapshot.child(post_key).getChildrenCount();

                                if (mProcessLike) {

                                    if (dataSnapshot.child(post_key).hasChild(auth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child("Likes").child(post_key).child(auth.getCurrentUser().getUid()).removeValue();
                                        mDatabaseLike.child("Likes").child("count").child(post_key).setValue(maxNum-1);

                                        mProcessLike = false;

                                    } else {

                                        mDatabaseLike.child("Likes").child(post_key).child(auth.getCurrentUser().getUid()).setValue("Liked");
                                        mDatabaseLike.child("Likes").child("count").child(post_key).setValue(maxNum+1);
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
            post_video = (VideoView) mView.findViewById(R.id.post_video);
            awaClick = (ImageView) mView.findViewById(R.id.awko);
            nickClick = (TextView) mView.findViewById(R.id.post_name);
            mLikebtn = (ImageButton) mView.findViewById(R.id.like_btn);
            countLikes = (TextView) mView.findViewById(R.id.countlike2);
            countComments = (TextView) mView.findViewById(R.id.countcomments);
            play = (ImageButton) mView.findViewById(R.id.playButton);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("UsersVideo");
            auth = FirebaseAuth.getInstance();

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play.setVisibility(View.GONE);
                    post_video.setMediaController(mediaC);
                    mediaC.setAnchorView(mView);
                    post_video.requestFocus();
                    post_video.start();
                }
            });

            post_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    play.setVisibility(View.VISIBLE);
                }
            });
        }

        public void setmLikebtn(final String post_key){

            mDatabaseLike.child("Comments").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Map<String,Object> value2 = (Map<String, Object>) dataSnapshot.child("countComments").getValue();
                    try
                    {
                        String name2 = String.valueOf(value2.get(post_key));
                        countComments.setText(name2);
                    }
                    catch (Exception e){
                        countComments.setText("0");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            mDatabaseLike.child("Likes").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //вот тут
                    Map<String,Object> value = (Map<String, Object>) dataSnapshot.child("count").getValue();
                    try
                    {
                        String name1 = String.valueOf(value.get(post_key));
                        countLikes.setText(name1);
                    }
                    catch (Exception e){
                        countLikes.setText("0");
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
            Picasso.with(itemView.getContext()).load(imgur).into(im_d);

        }

        public void setTitle(String tit){

            TextView title = (TextView) mView.findViewById(R.id.post_title);
            title.setText(tit);

        }

        public void setName(String desc){

            TextView post_name = (TextView) mView.findViewById(R.id.post_name);
            post_name.setText(desc);
        }

        public void setDate(String vD){

            TextView date = (TextView) mView.findViewById(R.id.videoDate);
            date.setText(vD);
        }

        public void setTime(String vT){

            TextView tim = (TextView) mView.findViewById(R.id.videoTime);
            tim.setText(vT);
        }

        public void setImage(String video){

            post_video.setVideoPath(video);
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