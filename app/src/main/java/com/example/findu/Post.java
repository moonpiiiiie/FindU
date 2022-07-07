package com.example.findu;

public class Post {
    private String name;
    private int age;
    private String gender;
    private String postImageUrl;
    private String notes;
    private  String userId;

    public Post(String name, int age, String notes, String postImageUrl, String gender, String userId) {
        this.name = name;
        this.age = age;
        this.notes = notes;
        this.postImageUrl = postImageUrl;
        this.gender = gender;
        this.userId = userId;
    }

    public String getPostImageUrl() {
        return postImageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPostImageUrl(String postImageUrl) {
        this.postImageUrl = postImageUrl;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
