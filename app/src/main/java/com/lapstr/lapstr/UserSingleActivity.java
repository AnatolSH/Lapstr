package com.lapstr.lapstr;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

import static com.lapstr.lapstr.MainActivity.getBitmapFromURL;

/**
 * Created by Anatole on 22.02.2017.
 */

public class UserSingleActivity extends AppCompatActivity {
    private EditText mPostComment;
    private Button mSubmitBtn;
    private String nick;
    private String urk;
    private StorageReference mStorage;
    private DatabaseReference mFirebaseDatabase;
    private RecyclerView mBloglist;
    private FirebaseAuth.AuthStateListener authListener;
    private DatabaseReference mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance2;

    private String mPost_key = null;
    private DatabaseReference mDatabase;

    private VideoView mBlogSingleVideo;
    private TextView mBlogSingleName;

    private TextView mBlogLikes;
    private TextView mComm;
    private ImageView mBlogSingleAvatar;
    private DatabaseReference getmDatabaseCount;
    private DatabaseReference getmDatabaseCount2;
    private DatabaseReference mDatabaseLike;
    private boolean mProcessLike;
    private DatabaseReference baza;
    private String post_name;

    public void setUserVideos(String userVideos) {
        this.userVideos = userVideos;
    }

    private String userVideos;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_single);

        mDatabase = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        getmDatabaseCount = FirebaseDatabase.getInstance().getReference().child("Likes").child("count");
        getmDatabaseCount2 = FirebaseDatabase.getInstance().getReference().child("Likes").child("countComments");
        mFirebaseInstance2 = FirebaseDatabase.getInstance();
        mFirebaseDatabase2 = mFirebaseInstance2.getReference("cabinet");
        baza = FirebaseDatabase.getInstance().getReference("Likes").child("UsersVideo");
        mAuth = FirebaseAuth.getInstance();

       // mPost_key = getIntent().getExtras().getString("blog_id");
        userVideos = getIntent().getExtras().getString("userName");
        mBlogSingleName = (TextView) findViewById(R.id.post_name0);
        mBlogSingleAvatar = (ImageView) findViewById(R.id.awko0);

        //отображение
        mBloglist = (RecyclerView) findViewById(R.id.blog_list30);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));


        authListener = new FirebaseAuth.AuthStateListener() { //если не авторизован, то открывает логин активити
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mStorage = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Likes").child("comments");

//nenkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk
        baza.child(userVideos).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> us = new ArrayList<>();
                DataSnapshot contSnap = dataSnapshot;
                Iterable<DataSnapshot> contShild = contSnap.getChildren();
                for(DataSnapshot cont: contShild)
                {
                    User c = cont.getValue(User.class);
                    us.add(c);
                }

                    String post_avatar = us.get(0).getAwaurl();
                    post_name = us.get(0).getName();

                    mBlogSingleName.setText(post_name);
                    mBlogSingleAvatar.setImageBitmap(getBitmapFromURL(post_avatar));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);

        DatabaseReference usersVideoDB = mFirebaseInstance2.getReference("Likes").child("UsersVideo").child(userVideos);

        FirebaseRecyclerAdapter<User, BlogViweHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, BlogViweHolder>(

                User.class,
                R.layout.for_single_blog,
                BlogViweHolder.class,
                usersVideoDB

        ) {
            @Override
            protected void populateViewHolder(BlogViweHolder viewHolder, User model, int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setImage(model.getUrl());
                viewHolder.setTitle(model.getTitle());
                viewHolder.setmLikebtn(post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent singleBlogIntent = new Intent(UserSingleActivity.this, BlogSingleActivity.class);
                        singleBlogIntent.putExtra("blog_id", post_key);
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

                                    if (dataSnapshot.child(post_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        mDatabaseLike.child("count").child(post_key).setValue(maxNum-1);

                                        mProcessLike = false;

                                    } else {

                                        mDatabaseLike.child(post_key).child(mAuth.getCurrentUser().getUid()).setValue("Liked");
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
        TextView countLikes;
        TextView countComments;
        DatabaseReference mDatabaseLike;
        FirebaseAuth auth;

        public BlogViweHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mLikebtn = (ImageButton) mView.findViewById(R.id.like_btn11);
            countLikes = (TextView) mView.findViewById(R.id.countlike211);
            countComments = (TextView) mView.findViewById(R.id.countcomments11);
            mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
            auth = FirebaseAuth.getInstance();

            mDatabaseLike.keepSynced(true);
        }

        public void setmLikebtn(final String post_key){

            mDatabaseLike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //вот тут
                    Map<String,Object> value = (Map<String, Object>) dataSnapshot.child("count").getValue();
                    String name1 = String.valueOf(value.get(post_key));

                    Map<String,Object> value2 = (Map<String, Object>) dataSnapshot.child("countComments").getValue();
                    String name2 = String.valueOf(value2.get(post_key));

                    if(name1.equals("null") || name1.equals("0")){ countLikes.setText("");}
                    else
                    {
                        try {
                            countLikes.setText(name1);
                        }catch (Exception e){}
                    }

                    if(name2.equals("null") || name2.equals("0")){ countComments.setText("");}
                    else
                    {
                        try {
                            countComments.setText(name2);
                        }catch (Exception e){}
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

        public void setTitle(String tit){

            TextView title = (TextView) mView.findViewById(R.id.post_title11);
            title.setText(tit);

        }

        public void setImage(String video){

            VideoView post_video = (VideoView) mView.findViewById(R.id.post_video11);
            post_video.setVideoPath(video);
            post_video.setVideoURI(Uri.parse(video));

            post_video.start();
            post_video.requestFocus();

        }
    }
}

