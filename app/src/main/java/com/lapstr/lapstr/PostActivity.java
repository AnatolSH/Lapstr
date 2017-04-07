package com.lapstr.lapstr;

/**
 * Created by Anatole on 04.02.2017.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {

    private ImageButton mselectImage;
    private EditText mPostTitle;
    private Button mSubmitBtn;
    private Uri mVideoUri = null;
    private ProgressDialog mProgress;
    private DatabaseReference mFirebaseDatabase2;
    private FirebaseDatabase mFirebaseInstance2;
    private String nick;
    private FirebaseAuth mAuth;
    private String urk;
    private Locale myLocale;
    Context context;
    private String lang;

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
        mselectImage = (ImageButton) findViewById(R.id.videoSelect5);
        mFirebaseInstance2 = FirebaseDatabase.getInstance();
        mFirebaseDatabase2 = mFirebaseInstance2.getReference("cabinet");

        mPostTitle=(EditText) findViewById(R.id.titleField5);
        mSubmitBtn=(Button) findViewById(R.id.button3);
        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mselectImage.setOnClickListener(new View.OnClickListener() {
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
            }
        });
        addCabChangeListener();
        loadLocale();

    }



    private void startPosting() {

        mProgress.setMessage("Posting...");
        mProgress.show();

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
                DatabaseReference dataForUserBd = baza.child("Uploaded").child(nick).child(str);
                DatabaseReference comm = baza.child("Comments").child("countComments").child(str);
                DatabaseReference loice = baza.child("Likes").child("count").child(str);

                newPost.child("awaurl").setValue(urk);
                newPost.child("name").setValue(nick);
                newPost.child("url").setValue(downloadUrl.toString());
                newPost.child("title").setValue(title_val);
                newPost.child("uid").setValue(uid);
                newPost.child("date").setValue(dd);
                newPost.child("time").setValue(dt);

                dataForUserBd.child("name").setValue(nick);
                dataForUserBd.child("awaurl").setValue(urk);
                dataForUserBd.child("url").setValue(downloadUrl.toString());
                dataForUserBd.child("title").setValue(title_val);
                dataForUserBd.child("date").setValue(dd);
                dataForUserBd.child("time").setValue(dt);

                comm.setValue(0);
                loice.setValue(0);
            }
        });

        Intent SecAct99 = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(SecAct99);
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
            mselectImage.setImageURI(mVideoUri);
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