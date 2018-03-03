package com.tim.fypapp.Model;

/**
 * Created by Tim on 08/02/2018.
 */

public class UserInformation {

    private String fullName;
    private String email;
    private String password;

    public UserInformation(){

    }

    public UserInformation(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
