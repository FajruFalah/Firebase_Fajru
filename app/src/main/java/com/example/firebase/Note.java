package com.example.firebase;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 10120054
 * Fajru Falah
 * IF-2
 */

public class Note implements Parcelable{
    private String title;
    private String content;
    private String timestamp;
    private String docId; // Add the docId field

//    public Note(String title, String content, String timestamp) {
//    }

    public Note() {
        // Default constructor required for Firebase Realtime Database.
        // Do not remove this empty constructor.
    }

    public Note(String title, String content, String timestamp, String docId) {
        this.title = title;
        this.content = content;
        this.timestamp = timestamp;
        this.docId = docId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getDocId() {
        return docId;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(timestamp);
        dest.writeString(docId);

    }

    public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private Note(Parcel in) {

        title = in.readString();
        content = in.readString();
        timestamp =in.readString();
        docId = in.readString();
    }
}
