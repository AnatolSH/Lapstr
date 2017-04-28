package com.lapstr.lapstr;

/**
 * Created by Anatole on 27.01.2017.
 */

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.storage.StorageReference;

@IgnoreExtraProperties
public class Cabinet {


    //dsaas
    public String userName;
    public String email;
    public String url;
    public String uid;
    public String countVideo;

    public Cabinet() {
    }

    public Cabinet(String userName, String email, String url, String uid, String countVideo) {
        this.userName = userName;
        this.email = email;
        this.url = url;
        this.uid = uid;
        this.countVideo = countVideo;
    }

    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return this.userName; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return this.email; }

    public void setUrl(String url) { this.url = url; }
    public String getUrl() { return this.url; }

    public void setUid(String uid) { this.uid = uid; }
    public String getUid() { return this.uid; }

    public void setCountVideo(String countVideo) { this.countVideo = countVideo; }
    public String getCountVideo() { return this.countVideo; }
}