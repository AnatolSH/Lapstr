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

    public Cabinet() {
    }

    public Cabinet(String userName, String email) {
        this.userName = userName;
        this.email = email;
    }

    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return this.userName; }

    public void setEmail(String email) { this.email = email; }
    public String getEmail() { return this.email; }

}

