package com.example.teachiou;

import android.os.Parcel;
import android.os.Parcelable;

public class classListItem implements Parcelable {

    private String className, classSubject, docID;

    public static final Parcelable.Creator<classListItem> CREATOR = new Parcelable.Creator<classListItem>() {

        @Override
        public classListItem createFromParcel(Parcel parcel) {
            return new classListItem(parcel);
        }

        @Override
        public classListItem[] newArray(int size) {
            return new classListItem[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }

    public classListItem(Parcel parcel) {
        className = parcel.readString();
        classSubject = parcel.readString();
        docID = parcel.readString();
    }

    public classListItem(String className, String classSubject, String docID) {
        this.className = className;
        this.classSubject = classSubject;
        this.docID = docID;
    }
    public classListItem(String className, String classSubject) {
        this.className = className;
        this.classSubject = classSubject;
        this.docID = "No docID yet";
    }
    public classListItem() {
        this.className = "";
        this.classSubject = "";
        this.docID = "No docID yet";
    }

    public String toString() {
        return className + " at " + classSubject;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassSubject() {
        return classSubject;
    }

    public void setClassSubject(String itemLocation) {
        this.classSubject = itemLocation;
    }
    public String getDocID() {
        return docID;
    }
    public void setDocID(String docID) {
        this.docID = docID;
    }

}
