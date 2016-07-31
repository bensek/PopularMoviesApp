package com.example.hp.moviez.models;


public class Review {
    public String author;
    public String reviewDesc;

    public Review(){}

    public Review (String auth, String rev){
        this.author =  auth;
        this.reviewDesc = rev;
    }

    public String getAuthor(){
        return author;
    }

    public void setAuthor(String auth){
      this.author = auth;
    }

    public String getReviewDesc(){
        return reviewDesc;
    }

    public void setReviewDesc(String reviewDesc) {
        this.reviewDesc = reviewDesc;
    }
}
