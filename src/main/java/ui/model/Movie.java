package ui.model;

import java.util.Date;

public class Movie {

    private String movieID;
    private String title;
    private String genre;
    private int duration; // in minutes
    private double rating; // 0.0 - 10.0
    private String description;
    private Date releaseDate;
    private String language;
    private String posterPath; // ✅ ONLY ONE POSTER FIELD

    public Movie() {}

    public Movie(
            String movieID,
            String title,
            String genre,
            int duration,
            double rating,
            String description,
            Date releaseDate,
            String language,
            String posterPath) {
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.description = description;
        this.releaseDate = releaseDate;
        this.language = language;
        this.posterPath = posterPath;
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getFormattedDuration() {
        int hours = duration / 60;
        int mins = duration % 60;
        return hours + "h " + mins + "m";
    }

    @Override
    public String toString() {
        return "Movie{movieID='"
                + movieID
                + "', title='"
                + title
                + "', genre='"
                + genre
                + "', duration="
                + duration
                + "min, rating="
                + rating
                + "}";
    }
}
