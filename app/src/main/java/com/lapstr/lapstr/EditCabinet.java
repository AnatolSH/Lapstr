package com.lapstr.lapstr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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


/**
 * Created by Anatole on 27.01.2017.
 */

public class EditCabinet extends AppCompatActivity{
    private TextView lineUserName;
    private TextView lineUserEmail;
    private EditText inputName;
    private Button btnSave;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase2;

    private FirebaseDatabase mFirebaseInstance;
    private String userId;
    private ImageView awa;
    private String nick;

    private RecyclerView rv;
    private ArrayList<User> us = new ArrayList<>();
    private ArrayList<User> yourList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcab_activity);
        lineUserName = (TextView) findViewById(R.id.txt_user5);
        lineUserEmail = (TextView) findViewById(R.id.lineuseremail);
        awa = (ImageView) findViewById(R.id.imageView11);
      //  inputName = (EditText) findViewById(R.id.name5);
      //  btnSave = (Button) findViewById(R.id.btn_save5);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("cabinet");
        mFirebaseDatabase2 = FirebaseDatabase.getInstance().getReference("uploadedVideo").child("contacts");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv=(RecyclerView)findViewById(R.id.blog_list99);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        addCabChangeListener();

    }


    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(yourList);
        rv.setAdapter(adapter);
    }

    private void addUserChangeListener() {//тут все ссылки для ленты + имена тех кто залил
        mFirebaseDatabase2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot contSnap = dataSnapshot;
                Iterable<DataSnapshot> contShild = contSnap.getChildren();
                for(DataSnapshot cont: contShild)
                {
                    User c = cont.getValue(User.class);
                    us.add(c);
                }

                for (int i = 0; i < us.size(); i++) {
                    if((us.get(i).getName()).equals(nick))
                    {
                        yourList.add(us.get(i));
                    }
                }
                initializeAdapter();
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void addCabChangeListener() {
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
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
                       // awa.setImageBitmap(getBitmapFromURL(co.get(i).getUrl()));
                        Picasso.with(getApplicationContext()).load(co.get(i).getUrl()).into(awa);
                        lineUserName.setText(co.get(i).getUserName());
                        lineUserEmail.setText(co.get(i).getEmail());
                        nick = co.get(i).getUserName();
                        break;
                    }
                }
                addUserChangeListener();
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


/*      btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
            //    String email = inputEmail.getText().toString();
                updateCabinet(name);
            }
        });*/

    }

    private void updateCabinet(String name) {
            FirebaseUser equ = FirebaseAuth.getInstance().getCurrentUser();
            String userId = equ.getUid();
            mFirebaseDatabase.child("users").child(userId).child("userName").setValue(name);
         //   mFirebaseDatabase.child("users").child(userId).child("email").setValue(email);
            addCabChangeListener();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}

