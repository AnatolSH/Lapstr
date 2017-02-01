package com.lapstr.lapstr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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
    private ImageView awatarka;

    private static final String TAG = MainActivity.class.getSimpleName();
    //тут начинается перенос

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance;
    private FirebaseDatabase mFirebaseInstance2;
    private String userId;
    private String randId;
    private String nick;
    private String urk;
    private String val;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private static final int SELECT_VIDEO = 3;
    private TextView txtDetails;
    private VideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  txtDetails = (TextView) findViewById(R.id.txt_user);
        // awatarka = (ImageView) findViewById(R.id.imageView6);
        // videoview = (VideoView)findViewById(R.id.surface_view);
        mBloglist = (RecyclerView) findViewById(R.id.blog_list2);
        mBloglist.setHasFixedSize(true);
        mBloglist.setLayoutManager(new LinearLayoutManager(this));

        //mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance2 = FirebaseDatabase.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");
        mFirebaseDatabase2 = mFirebaseInstance2.getReference("cabinet");
        //  mFirebaseInstance.getReference("app_title").setValue("Realtime Database");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mSrorage = FirebaseStorage.getInstance().getReference();
        mSelectImage = (Button) findViewById(R.id.select_image);
        mProgressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        authListener = new FirebaseAuth.AuthStateListener() {
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

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a Video "), SELECT_VIDEO);
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                buttonStart.setOnClickListener(this);
                if (view == buttonStart) {
                    startVideo();
                }
            }
        });


        newActivity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                newActivity.setOnClickListener(this);
                if (view == newActivity) {
                    Intent SecAct = new Intent(getApplicationContext(), CameraActivity.class);
                    startActivity(SecAct);
                }
            }
        });

        myCabinet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myCabinet.setOnClickListener(this);
                if (view == myCabinet) {
                    Intent SecAct = new Intent(getApplicationContext(), EditCabinet.class);//верни кабинет
                    startActivity(SecAct);
                }
            }
        });

        singout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                singout.setOnClickListener(this);
                if (view == singout) {
                    signOut();
                }
            }
        });
        //  addUserChangeListener();
        addCabChangeListener();
    }
    public void signOut() {
        auth.signOut();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK)
        {
            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            Uri uri = data.getData();
            StorageReference filepath = mSrorage.child("Videos").child(uri.getLastPathSegment());
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(MainActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();

                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    String string_dwload = downloadUrl.toString();
                    createUser(nick, string_dwload, urk);
                }
            });
        }
        else{
            Intent SecAct = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(SecAct);}
    }


    private void addCabChangeListener() {
        mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot contSnap = dataSnapshot.child("users");
                Iterable<DataSnapshot> contShild = contSnap.getChildren();
                ArrayList<Cabinet> co = new ArrayList<>();
                for(DataSnapshot cont: contShild)
                {
                    //   co.clear();//??
                    Cabinet c = cont.getValue(Cabinet.class);
                    co.add(c);
                }

                FirebaseUser equ = FirebaseAuth.getInstance().getCurrentUser();

                for (int i = 0; i < co.size(); i++) {
                    if((co.get(i).getEmail()).equals(equ.getEmail()))
                    {
                        nick = co.get(i).getUserName();
                        urk = co.get(i).getUrl();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });}
    /*
        private void addUserChangeListener() {//тут все ссылки для ленты + имена тех кто залил
            mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot contSnap = dataSnapshot.child("contacts");
                    Iterable<DataSnapshot> contShild = contSnap.getChildren();
                    ArrayList<User> us = new ArrayList<>();
                    for(DataSnapshot cont: contShild)
                    {
                        User c = cont.getValue(User.class);
                        us.add(c);
                    }
                    //awatarka.setImageURI(Uri.parse(us.get(0).getAwaurl()));
                    awatarka.setImageBitmap(getBitmapFromURL(us.get(0).getAwaurl()));
                     txtDetails.setText(us.get(0).getName());
                    videoview.setVideoPath(us.get(0).getUrl());
                    videoview.setVideoURI(Uri.parse(us.get(0).getUrl()));
                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e(TAG, "Failed to read user", error.toException());
                }
            });
        }*/
    public static Bitmap getBitmapFromURL(String src) {
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
    private void createUser(String name, String url, String imurl) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        userId = mFirebaseDatabase.push().getKey();
        User user = new User(name, url, imurl);

        mFirebaseDatabase.child(userId).setValue(user);//ТУТ МЕНЯТЬ

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
            protected void populateViewHolder(BlogViweHolder viewHolder, User model, int position) {

                viewHolder.setTitle(model.getAwaurl());
                viewHolder.setDesc(model.getName());
                viewHolder.setImage(model.getUrl());

            }

        };

        mBloglist.setAdapter(firebaseRecyclerAdapter);
    }

    public static class BlogViweHolder extends RecyclerView.ViewHolder{

        View mView;

        public BlogViweHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String imgur){

            ImageView im_d = (ImageView) mView.findViewById(R.id.awko);
            im_d.setImageBitmap(getBitmapFromURL(imgur));

            //  TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            //    post_desc.setText(desc);
        }

        public void setDesc(String desc){

            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setImage(String video){

            VideoView post_video = (VideoView) mView.findViewById(R.id.post_video);
            post_video.setVideoPath(video);
            post_video.setVideoURI(Uri.parse(video));

            post_video.start();
            //post_video.setMediaController(new MediaController(this));
            post_video.requestFocus();

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