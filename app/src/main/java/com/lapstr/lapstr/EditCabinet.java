package com.lapstr.lapstr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private FirebaseDatabase mFirebaseInstance;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editcab_activity);
        lineUserName = (TextView) findViewById(R.id.txt_user5);
        lineUserEmail = (TextView) findViewById(R.id.lineuseremail);
        inputName = (EditText) findViewById(R.id.name5);
        btnSave = (Button) findViewById(R.id.btn_save5);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("cabinet");

        addUserChangeListener();
    }

    private void addUserChangeListener() {
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
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
                        lineUserName.setText(co.get(i).getUserName());
                        lineUserEmail.setText(co.get(i).getEmail());
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });


      btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
            //    String email = inputEmail.getText().toString();
                updateCabinet(name);
            }
        });

    }

    private void updateCabinet(String name) {
            FirebaseUser equ = FirebaseAuth.getInstance().getCurrentUser();
            String userId = equ.getUid();
            mFirebaseDatabase.child("users").child(userId).child("userName").setValue(name);
         //   mFirebaseDatabase.child("users").child(userId).child("email").setValue(email);
            addUserChangeListener();
    }
}

