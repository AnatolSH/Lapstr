package com.lapstr.lapstr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static com.lapstr.lapstr.MainActivity.getBitmapFromURL;

public class BlogSingleActivity extends AppCompatActivity {

    private EditText mPostComment;
    private Button mSubmitBtn;
    private ImageButton play;
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
    private TextView mBlogSingleTitle;
    private TextView mBlogLikes;
    private TextView mComm;
    private ImageView mBlogSingleAvatar;
    private DatabaseReference getmDatabaseCount;
    private DatabaseReference getmDatabaseCount2;
    private DatabaseReference mDatabaseLike;
    private Button mSinleDelBtn;
    private String post_video;
    private ImageButton mLikebtn;
    private boolean mProcessLike;
    private String post_name;

    private FirebaseAuth mAuth;

    MediaController mediaC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);

        mDatabase = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        getmDatabaseCount = FirebaseDatabase.getInstance().getReference().child("Likes").child("count");
        getmDatabaseCount2 = FirebaseDatabase.getInstance().getReference().child("Likes").child("countComments");

        mFirebaseInstance2 = FirebaseDatabase.getInstance();
        mFirebaseDatabase2 = mFirebaseInstance2.getReference("cabinet");
        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("blog_id");
        mBlogSingleVideo = (VideoView) findViewById(R.id.post_video);
        mBlogSingleName = (TextView) findViewById(R.id.post_name);
        mBlogSingleTitle = (TextView) findViewById(R.id.post_title);
        mBlogSingleAvatar = (ImageView) findViewById(R.id.awko);
        mSinleDelBtn = (Button) findViewById(R.id.delBtn);
        play = (ImageButton) findViewById(R.id.playButton2);
        mBlogLikes = (TextView) findViewById(R.id.countlike);
        mComm = (TextView) findViewById(R.id.countcom);
        mLikebtn = (ImageButton) findViewById(R.id.like_btn);
        //отображение
        mBloglist = (RecyclerView) findViewById(R.id.blog_list3);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));

        mediaC = new MediaController(this);


        authListener = new FirebaseAuth.AuthStateListener() { //если не авторизован, то открывает логин активити
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mStorage = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Likes").child("comments");

        mPostComment=(EditText) findViewById(R.id.vvodcomment);
        mSubmitBtn=(Button) findViewById(R.id.buttcomm);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
        addCabChangeListener();


        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_avatar = (String) dataSnapshot.child("awaurl").getValue();
                post_name = (String) dataSnapshot.child("name").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                post_video = (String) dataSnapshot.child("url").getValue();

                try {

                    mBlogSingleAvatar.setImageURI(Uri.parse(post_avatar));
                    mBlogSingleTitle.setText(post_title);
                    mBlogSingleAvatar.setImageBitmap(getBitmapFromURL(post_avatar));

                    if (mAuth.getCurrentUser().getUid().equals(post_uid)) {

                        mSinleDelBtn.setVisibility(View.VISIBLE);

                    }
                }
                catch (Exception e){finish();Intent mainIntent = new Intent(BlogSingleActivity.this, MainActivity.class);
                    startActivity(mainIntent);}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String,Object> value = (Map<String, Object>) dataSnapshot.child("count").getValue();
                try
                {
                    String name1 = String.valueOf(value.get(mPost_key));
                    mBlogLikes.setText(name1);
                }
                catch (Exception e){
                    mBlogLikes.setText("");
                }

                Map<String,Object> value2 = (Map<String, Object>) dataSnapshot.child("countComments").getValue();
                try
                {
                    String name2 = String.valueOf(value2.get(mPost_key));
                    mComm.setText(name2);
                }
                catch (Exception e){
                    mComm.setText("");
                }

                if(dataSnapshot.child(mPost_key).hasChild(mAuth.getCurrentUser().getUid())){

                    mLikebtn.setImageResource(R.mipmap.ic_favorite_red_48dp);

                } else {

                    mLikebtn.setImageResource(R.mipmap.ic_favorite_border_grey_48dp);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBlogSingleVideo.setVideoURI(Uri.parse(post_video));
                mBlogSingleVideo.setMediaController(mediaC);
                mediaC.setAnchorView(mBlogSingleVideo);
                mediaC.setPadding(0, 0, 0, 0);
                mBlogSingleVideo.start();
                mBlogSingleVideo.requestFocus();
                mBlogSingleName.setText(post_name);
            }
        });

        mLikebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProcessLike = true;

                mDatabaseLike.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int maxNum = (int) dataSnapshot.child(mPost_key).getChildrenCount();

                        if (mProcessLike) {

                            if (dataSnapshot.child(mPost_key).hasChild(mAuth.getCurrentUser().getUid())) {

                                mDatabaseLike.child(mPost_key).child(mAuth.getCurrentUser().getUid()).removeValue();
                                mDatabaseLike.child("count").child(mPost_key).setValue(maxNum-1);

                                mProcessLike = false;

                            } else {

                                mDatabaseLike.child(mPost_key).child(mAuth.getCurrentUser().getUid()).setValue("Liked");
                                mDatabaseLike.child("count").child(mPost_key).setValue(maxNum+1);
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

        mSinleDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(mPost_key).removeValue();
                mDatabaseLike.child(mPost_key).removeValue();
                getmDatabaseCount.child(mPost_key).removeValue();
                mDatabaseLike.child("comments").child(mPost_key).removeValue();
                mDatabaseLike.child("UsersVideo").child(post_name).child(mPost_key).removeValue();

                Intent mainIntent = new Intent(BlogSingleActivity.this, MainActivity.class);
                startActivity(mainIntent);
                getmDatabaseCount2.child(mPost_key).removeValue();
            }
        });

        mBlogSingleAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent singleBlogIntent = new Intent(BlogSingleActivity.this, UserSingleActivity.class);
                singleBlogIntent.putExtra("userName", mBlogSingleName.getText());
                startActivity(singleBlogIntent);

            }
        });
    }


    private void addCabChangeListener() { //метод чтения из бд Users
        mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot contSnap = dataSnapshot.child("users");
                Iterable<DataSnapshot> contShild = contSnap.getChildren();
                ArrayList<Cabinet> co = new ArrayList<>();
                for(DataSnapshot cont: contShild)
                {
                    Cabinet c = cont.getValue(Cabinet.class);
                    co.add(c);
                }

                FirebaseUser equ = FirebaseAuth.getInstance().getCurrentUser();

                for (int i = 0; i < co.size(); i++) {
                    if((co.get(i).getEmail()).equals(equ.getEmail()))
                    {
                        nick = co.get(i).getUserName(); //получаем текущий ник пользователя
                        urk = co.get(i).getUrl(); //получаем текущую ссылку на аватарку
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });}

    private void startPosting() {

        mDatabaseLike.child("comments").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int maxNum = (int) dataSnapshot.child(mPost_key).getChildrenCount();
                        mDatabaseLike.child("countComments").child(mPost_key).setValue(maxNum);
                    }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final String title_val = mPostComment.getText().toString().trim();

                DatabaseReference newPost = mFirebaseDatabase.child(mPost_key).push();
                newPost.child("awaurl").setValue(urk);
                newPost.child("name").setValue(nick);
                newPost.child("title").setValue(title_val);

            }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);

        FirebaseRecyclerAdapter<User, BlogSingleActivity.BlogViweHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, BlogSingleActivity.BlogViweHolder>(
                User.class,
                R.layout.comments_activity,
                BlogSingleActivity.BlogViweHolder.class,
                mFirebaseDatabase.child(mPost_key)

        ) {
            @Override
            protected void populateViewHolder(BlogSingleActivity.BlogViweHolder viewHolder, User model, int position) {

                viewHolder.setAwa(model.getAwaurl());
                viewHolder.setName(model.getName());
                viewHolder.setTitle(model.getTitle());

            }

        };

        mBloglist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViweHolder extends RecyclerView.ViewHolder{

        View mView;

        FirebaseAuth auth;

        public BlogViweHolder(View itemView) {
            super(itemView);

            mView = itemView;
            auth = FirebaseAuth.getInstance();

        }

        public void setAwa(String imgur){

            ImageView im_d = (ImageView) mView.findViewById(R.id.authorawatar);
            im_d.setImageBitmap(getBitmapFromURL(imgur));

        }

        public void setTitle(String tit){

            TextView title = (TextView) mView.findViewById(R.id.samcomment);
            title.setText(tit);

        }

        public void setName(String desc){

            TextView post_name = (TextView) mView.findViewById(R.id.nameauthor);
            post_name.setText(desc);
        }

    }

}
