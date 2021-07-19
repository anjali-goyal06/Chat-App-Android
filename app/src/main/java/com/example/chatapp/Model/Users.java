package com.example.chatapp.Model;

import android.util.Log;

public class Users {
    String profilePic;
    String emailId;
    String username;
    String password;
    String userId;
    String about;

    public Users(){};

    public Users(String username,String emailId,String password){
        this.emailId = emailId;
        this.username = username;
        this.password = password;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Users(String profilePic, String emailId, String username, String password, String userId, String lastMessage, String about) {
        this.profilePic = profilePic;
        this.emailId = emailId;
        this.username = username;
        this.password = password;
        this.userId = userId;
        this.about = about;
    }

    public String getProfilePic() {
        Log.i("gggggggggggggggggggggkkkkkkkkkkkk","abc = "+ profilePic);
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
