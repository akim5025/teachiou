package com.example.teachiou;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Calendar;
import java.util.Date;


public class Question implements Parcelable {
    private String body, title, answer, docID, imageID, time, username;
    private boolean isAnswered;
    private Date currentTime = Calendar.getInstance().getTime();


    protected Question(Parcel in) {
        body = in.readString();
        title = in.readString();
        answer = in.readString();
        docID = in.readString();
        imageID = in.readString();
        time = in.readString();
        username = in.readString();
        isAnswered = in.readByte() != 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(body);
        parcel.writeString(title);
        parcel.writeString(answer);
        parcel.writeString(docID);
        parcel.writeString(imageID);
        parcel.writeString(username);
        parcel.writeString(time);
        parcel.writeByte((byte) (isAnswered ? 1 : 0));
    }

    public Question(String body, String title, String answer, String docID, String imageID, boolean isAnswered) {
        this.username = "";
        this.body = body;
        this.title = title;
        this.answer = answer;
        this.docID = docID;
        this.imageID = imageID;
        this.time = currentTime.toString();
        this.isAnswered = isAnswered;
    }

    public Question(String body, String title, String answer, String docID, boolean isAnswered) {
        this.body = body;
        this.username = "";
        this.title = title;
        this.answer = answer;
        this.docID = docID;
        this.time = currentTime.toString();
        this.isAnswered = isAnswered;
    }

    public Question(String body, String title) {
        this.body = body;
        this.username = "";
        this.title = title;
        this.answer = "";
        this.docID = "";
        this.time = currentTime.toString();
        this.isAnswered = false;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getImageID() {
        return imageID;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) { isAnswered = answered; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }
}
