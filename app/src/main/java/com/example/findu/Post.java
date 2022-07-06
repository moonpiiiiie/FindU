package com.example.findu;

import com.google.firebase.firestore.FieldValue;

public class Post {
    private String name;
    private int age;

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public FieldValue getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(FieldValue timestamp) {
        this.timestamp = timestamp;
    }

    private String image_uri;
    private String gender;
    private String user_id;
    private FieldValue timestamp;
    private String notes;
//     userPost.put("name", name);
//     userPost.put("image", uri.toString());
//     userPost.put("age", age);
//     userPost.put("gender", gender);
//     userPost.put("user_id", currentUserId);
//     userPost.put("note", note);
//     userPost.put("time", FieldValue.serverTimestamp());
    public Post(String name, String image_uri, int age, String gender, String user_id, String notes, FieldValue timestamp) {
        this.name = name;
        this.image_uri = image_uri;
        this.age = age;
        this.gender = gender;
        this.user_id = user_id;
        this.notes = notes;
        this.timestamp = timestamp;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }



}
