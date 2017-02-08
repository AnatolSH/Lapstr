package com.lapstr.lapstr;

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class BlogSingleActivity extends AppCompatActivity {

    private String mPost_key = null;
    private DatabaseReference mDatabase;

    private VideoView mBlogSingleVideo;
    private TextView mBlogSingleName;
    private TextView mBlogSingleTitle;
    private ImageView mBlogSingleAvatar;
    private ImageButton mBlogSingleLike;
    private DatabaseReference getmDatabaseCount;
    private DatabaseReference mDatabaseLike;
    private StorageReference mStorageReference;
    private Button mSinleDelBtn;
    private StorageReference mStorageReferenceVideo;
    private FirebaseStorage mFirebaseStorage;
    private String post_video;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_single);

        mDatabase = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");
        mDatabaseLike = FirebaseDatabase.getInstance().getReference().child("Likes");
        getmDatabaseCount = FirebaseDatabase.getInstance().getReference().child("Likes").child("count");
        mAuth = FirebaseAuth.getInstance();

        mPost_key = getIntent().getExtras().getString("blog_id");

        mBlogSingleVideo = (VideoView) findViewById(R.id.post_video);
        mBlogSingleName = (TextView) findViewById(R.id.post_name);
        mBlogSingleTitle = (TextView) findViewById(R.id.post_title);
        mBlogSingleAvatar = (ImageView) findViewById(R.id.awko);
        mBlogSingleLike = (ImageButton) findViewById(R.id.like_btn);
        mSinleDelBtn = (Button) findViewById(R.id.delBtn);
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReferenceFromUrl("gs://latstr-c8e2c.appspot.com");
        mStorageReferenceVideo = mStorageReference.child("Videos");

        //Toast.makeText(BlogSingleActivity.this, post_key, Toast.LENGTH_LONG).show();

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_avatar = (String) dataSnapshot.child("awaurl").getValue();
                String post_name = (String) dataSnapshot.child("name").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
                post_video = (String) dataSnapshot.child("url").getValue();

                mBlogSingleVideo.setVideoURI(Uri.parse(post_video));
                mBlogSingleVideo.start();
                mBlogSingleVideo.requestFocus();
                mBlogSingleName.setText(post_name);
                mBlogSingleTitle.setText(post_title);
                mBlogSingleAvatar.setImageURI(Uri.parse(post_avatar));
                mBlogSingleAvatar.setImageBitmap(getBitmapFromURL(post_avatar));

                if(mAuth.getCurrentUser().getUid().equals(post_uid)){

                    mSinleDelBtn.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mSinleDelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDatabase.child(mPost_key).removeValue();
                mDatabaseLike.child(mPost_key).removeValue();
                getmDatabaseCount.child(mPost_key).removeValue();

                Intent mainIntent = new Intent(BlogSingleActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });

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
}
