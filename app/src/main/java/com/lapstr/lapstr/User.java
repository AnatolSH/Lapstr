package com.lapstr.lapstr;

/**
 * Created by Anatole on 25.01.2017.
 */

import android.net.Uri;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.StorageReference;

@IgnoreExtraProperties
public class User {


    //dsaas
    public String name;
    public String url;

    public User() {
    }

    public User(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
