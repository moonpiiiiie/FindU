package com.example.findu;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.UUID;

public class Post {
    private String name;
    private int age;
    private String image;
    private String gender;
    private String user_id;
    private Timestamp time;
    private String note;
    private String category;
    private String post_id;


    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

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
    public Post(String name, String image, int age, String gender, String user_id, String note, Timestamp time, String category) {
        this.name = name;
        this.image = image;
        this.age = age;
        this.gender = gender;
        this.user_id = user_id;
        this.note = note;
        this.time = time;
        this.category = category;
        this.post_id = UUID.randomUUID().toString();

    }
    public Post() {}

}
