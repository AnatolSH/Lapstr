package com.lapstr.lapstr;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Anatole on 06.02.2017.
 */

@IgnoreExtraProperties
public class Likes {


    //dsaas
    public String val;

    public Likes() {
    }

    public Likes(String val) {
        this.val = val;
    }

    public void setVal(String val) { this.val = val; }
    public String getVal() { return this.val; }

}

