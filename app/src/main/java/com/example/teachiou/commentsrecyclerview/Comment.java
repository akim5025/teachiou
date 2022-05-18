package com.example.teachiou.commentsrecyclerview;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

public class Comment {
    private String time, answer, username, userImageID, docID;
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    boolean isTeacher;
    /**
        comment.put("answer", c.getAnswer());
        comment.put("username", c.getUsername());
        comment.put("userImageID", c.getUserImageID());
        comment.put("time", c.getTime());
     **/
    protected Comment(Parcel in) {
        answer = in.readString();
        docID = in.readString();
        time = in.readString();
        username = in.readString();
        userImageID = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
/**
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(answer);
        parcel.writeString(docID);
        parcel.writeString(username);
        parcel.writeString(time);
        parcel.writeString(userImageID);
    }
**/
    public Comment(String answer, String username, String userImageID) {
        this.username = username;
        this.answer = answer;
        this.docID = "";
        this.userImageID  = userImageID;
        this.time = String.valueOf(timestamp.getTime());
    }

    public Comment() {

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImageID() {
        return userImageID;
    }

    public void setUserImageID(String userImageID) {
        this.userImageID = userImageID;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }



}
