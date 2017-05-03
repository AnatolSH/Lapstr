package com.lapstr.lapstr;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

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
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectVideo;
    private EditText mPostTitle;
    private Button mSubmitBtn;
    private Uri mVideoUri = null;
    private ProgressDialog mProgress;
    private DatabaseReference mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance2;
    private String nick;
    private String uID;
    private String cVideo;
    private FirebaseAuth mAuth;
    private String urk;
    private Locale myLocale;
    Context context;
    private String lang;
    private ProgressBar progressBar;

    private ImageButton homeBtn;
    private ImageButton addBtn;
    private ImageButton cameraBtn;
    private ImageButton cabinetBtn;
    private ImageButton outBtn;
    private Button singout;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference baza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");
        baza = FirebaseDatabase.getInstance().getReference().child("UsersVideo");
        mSelectVideo = (ImageButton) findViewById(R.id.videoSelect5);
        mFirebaseInstance2 = FirebaseDatabase.getInstance();
        mFirebaseDatabase2 = mFirebaseInstance2.getReference("cabinet");

        mPostTitle=(EditText) findViewById(R.id.titleField5);
        mSubmitBtn=(Button) findViewById(R.id.button3);
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    mSelectVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent= new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("video/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startPosting();

                progressBar.setVisibility(View.VISIBLE);

            }
        });
        addCabChangeListener();
        loadLocale();


       /* BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {


                if (tabId == R.id.tab_selectVideo) {

                }

                if (tabId == R.id.tab_homepage) {
                    Intent SecAct9 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(SecAct9);

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
                    Intent SecAct = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(SecAct);
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
                if (view == outBtn) {
                    Intent SecAct = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(SecAct);
                }
            }
        });

    }

    public void SignOut() { //метод на разлогинивание
        mAuth.signOut();
        finish();
    }

    public ProgressDialog loadingdialog;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadingdialog.dismiss();

        }
    };

    private void startPosting()  {

        final String title_val = mPostTitle.getText().toString().trim();

        StorageReference filepath = mStorage.child("Videos").child(mVideoUri.getLastPathSegment());

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
        final String dd = df.format(Calendar.getInstance().getTime());

        DateFormat dq = new SimpleDateFormat("HH:mm");
        final String dt = dq.format(Calendar.getInstance().getTime());


        filepath.putFile(mVideoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mProgress.dismiss();

                Uri downloadUrl=taskSnapshot.getDownloadUrl();
                DatabaseReference newPost = mFirebaseDatabase.push();
                String str = newPost.getKey();
                String uid = mAuth.getCurrentUser().getUid();
                DatabaseReference dataForUserBd = baza.child("Uploaded").child((mAuth.getCurrentUser().getUid())).child(str);
                DatabaseReference comm = baza.child("Comments").child("countComments").child(str);
                DatabaseReference loice = baza.child("Likes").child("count").child(str);
                DatabaseReference countVideo = mFirebaseDatabase2.child("users").child((mAuth.getCurrentUser().getUid()));

                newPost.child("awaurl").setValue(urk);
                newPost.child("name").setValue(nick);
                newPost.child("url").setValue(downloadUrl.toString());
                newPost.child("title").setValue(title_val);
                newPost.child("uid").setValue(uid);
                newPost.child("date").setValue(dd);
                newPost.child("time").setValue(dt);
                newPost.child("videoName").setValue(mVideoUri.getLastPathSegment().toString());

                dataForUserBd.child("name").setValue(nick);
                dataForUserBd.child("awaurl").setValue(urk);
                dataForUserBd.child("url").setValue(downloadUrl.toString());
                dataForUserBd.child("title").setValue(title_val);
                dataForUserBd.child("date").setValue(dd);
                dataForUserBd.child("time").setValue(dt);
                int sum = Integer.parseInt(cVideo);
                sum = sum + 1;
                String res = String.valueOf(sum);
                countVideo.child("countVideo").setValue(res);

                comm.setValue(0);
                loice.setValue(0);
            }
        });

       // mProgress.setMessage("Posting...");
        //mProgress.show();


        try {
            Thread.sleep(3500); //ТУТ ждёт 3.5 секунды
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent SecAct99 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(SecAct99);
    }

    private void addCabChangeListener() { //метод чтения из бд Users
        mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot contSnap = dataSnapshot.child("users");
                uID = dataSnapshot.getKey().toString();
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
                        nick = co.get(i).getUserName(); //получаем текущий ник пользователя
                        urk = co.get(i).getUrl(); //получаем текущую ссылку на аватарку
                        cVideo = co.get(i).getCountVideo();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });}

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mVideoUri= data.getData();
            mSelectVideo.setImageURI(mVideoUri);
        }
    }

    private void updateTexts()
    {
        mSubmitBtn.setText(R.string.subm_post);
        mPostTitle.setHint(R.string.post_title);

    }

    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
        lang = language;
    }
    public void changeLang(String lang)
    {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }


}