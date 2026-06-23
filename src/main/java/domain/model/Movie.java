package domain.model;

import java.util.Date;

public class Movie {
    private Long movieId;
    private String title;
    private String genre;
    private int duration;
    private double rating;
    private String description;
    private Date releaseDate;
    private String language;
    private String posterPath;

    public Movie() {}

    public Movie(Long movieId, String title, String genre, int duration, double rating,
                 String description, Date releaseDate, String language, String posterPath) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.rating = rating;
        this.description = description;
        this.releaseDate = releaseDate;
        this.language = language;
        this.posterPath = posterPath;
    }

    public String getFormattedDuration() {
        int hours = duration / 60;
        int mins = duration % 60;
        return hours + "h " + mins + "m";
    }

    public Long getMovieId() { return movieId; }
    public void setMovieId(Long movieId) { this.movieId = movieId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }
}
