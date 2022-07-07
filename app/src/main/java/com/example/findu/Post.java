package com.example.findu;

import com.google.firebase.Timestamp;

public class Post {
    private String name;
    private int age;
    private String image;
    private String gender;
    private String user_id;
    private Timestamp time;
    private String note;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

//     userPost.put("name", name);
//     userPost.put("image", uri.toString());
//     userPost.put("age", age);
//     userPost.put("gender", gender);
//     userPost.put("user_id", currentUserId);
//     userPost.put("note", note);
//     userPost.put("time", FieldValue.serverTimestamp());
    public Post(String name, String image, int age, String gender, String user_id, String note, Timestamp time) {
        this.name = name;
        this.image = image;
        this.age = age;
        this.gender = gender;
        this.user_id = user_id;
        this.note = note;
        this.time = time;
    }
    public Post() {}

}
