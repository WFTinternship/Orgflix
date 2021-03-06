package am.aca.orgflix.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Film entity class
 */
public class Film {
    private int id;
    private String title;
    private int prodYear;
    private boolean hasOscar;
    private String image;
    private String director;
    private int rate_1star;
    private int rate_2star;
    private int rate_3star;
    private int rate_4star;
    private int rate_5star;
    private List<Cast> casts;
    private List<Genre> genres;

    public Film() {
        hasOscar = false;
        rate_1star = 0;
        rate_2star = 0;
        rate_3star = 0;
        rate_4star = 0;
        rate_5star = 0;
        genres = new ArrayList<>();
        casts = new ArrayList<>();
        image = "0/0.jpg";
    }

    public Film(String title, int prodYear) {
        this();
        this.title = title;
        this.prodYear = prodYear;
    }

    public Film(String title, int prodYear, String image) {
        this(title, prodYear);
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        return getId() == film.getId() && getProdYear() == film.getProdYear() && getTitle().equals(film.getTitle());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getTitle().hashCode();
        result = 31 * result + getProdYear();
        return result;
    }

    @Override
    public String toString() {
        return "Film{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", prodYear=" + prodYear +
                ", hasOscar=" + hasOscar +
                ", image='" + image + '\'' +
                ", director='" + director + '\'' +
                ", rate_1star=" + rate_1star +
                ", rate_2star=" + rate_2star +
                ", rate_3star=" + rate_3star +
                ", rate_4star=" + rate_4star +
                ", rate_5star=" + rate_5star +
                ", casts=" + casts +
                ", genres=" + genres +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getProdYear() {
        return prodYear;
    }

    public void setProdYear(int prodYear) {
        this.prodYear = prodYear;
    }

    public boolean isHasOscar() {
        return hasOscar;
    }

    public void setHasOscar(boolean hasOscar) {
        this.hasOscar = hasOscar;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getRate_1star() {
        return rate_1star;
    }

    public void setRate_1star(int rate_1star) {
        this.rate_1star = rate_1star;
    }

    public int getRate_2star() {
        return rate_2star;
    }

    public void setRate_2star(int rate_2star) {
        this.rate_2star = rate_2star;
    }

    public int getRate_3star() {
        return rate_3star;
    }

    public void setRate_3star(int rate_3star) {
        this.rate_3star = rate_3star;
    }

    public int getRate_4star() {
        return rate_4star;
    }

    public void setRate_4star(int rate_4star) {
        this.rate_4star = rate_4star;
    }

    public int getRate_5star() {
        return rate_5star;
    }

    public void setRate_5star(int rate_5star) {
        this.rate_5star = rate_5star;
    }

    public List<Cast> getCasts() {
        return casts;
    }

    public void setCasts(List<Cast> casts) {
        this.casts = casts;
    }

    public void addCast(Cast cast) {
        this.casts.add(cast);
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void addGeners(Genre gener) {
        genres.add(gener);
    }
}
