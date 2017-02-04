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
    public String awaurl;
    public String title;

    public User() {
    }

    public User(String name, String url, String awaurl, String title) {
        this.name = name;
        this.url = url;
        this.awaurl = awaurl;
        this.title = title;
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }

    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return this.url; }

    public void setAwaurl(String awaurl) { this.awaurl = awaurl; }
    public String getAwaurl() { return this.awaurl; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return this.title; }
}
