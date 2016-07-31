package com.example.hp.moviez.models;



// this is a basic object that can be parceled to transfer btn objects
public class Trailer {
    private String trailerName, trailerUrl, trailerImageUrl;

    //basic constructor
    public Trailer() {
    }




    public Trailer(String trailerName, String trailerUrl, String trailerImageUrl) {
        this.trailerName = trailerName;
        this.trailerUrl = trailerUrl;
        this.trailerImageUrl = trailerImageUrl;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getTrailerUrl() {
        return trailerUrl;
    }

    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }

    public String getTrailerImageUrl(){ return trailerImageUrl; }
    public void setTrailerImageUrl(String trailerImageUrl1){this.trailerImageUrl = trailerImageUrl; }

}
