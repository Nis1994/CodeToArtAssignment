package app.movie.com.movieapplication.models;

public class MovieListItemData {

    private String posterPath;
    private Boolean adult;
    private String releaseDate;
    private String originalTitle;

    public MovieListItemData(String posterPath, Boolean adult, String releaseDate, String originalTitle) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.releaseDate = releaseDate;
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
}
