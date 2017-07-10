package am.aca.orgflix.entity;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Film entity class
 */
@Entity(name = "FILMS")
public class Film {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;
    @Column(name = "TITLE", columnDefinition = "VARCHAR(50)", nullable = false)
    private String title;
    @Column(name = "PROD_YEAR", nullable = false)
    private int prodYear;
    @Column(name = "HAS_OSCAR", nullable = false)
    @ColumnDefault("false")
    private boolean hasOscar;
    @Column(name = "IMAGE_REF", columnDefinition = "VARCHAR(250)")
    private String image;
    @Column(name = "DIRECTOR", columnDefinition = "VARCHAR(250)")
    private String director;
    @Column(name = "RATE_1STAR")
    @ColumnDefault("0")
    private int rate_1star;
    @Column(name = "RATE_2STAR")
    @ColumnDefault("0")
    private int rate_2star;
    @Column(name = "RATE_3STAR")
    @ColumnDefault("0")
    private int rate_3star;
    @Column(name = "RATE_4STAR")
    @ColumnDefault("0")
    private int rate_4star;
    @Column(name = "RATE_5STAR")
    @ColumnDefault("0")
    private int rate_5star;
    @ManyToMany(cascade = CascadeType.REFRESH)
    @JoinTable(name = "FILM_TO_CAST",
            joinColumns = @JoinColumn(name = "FILM_ID"),
            inverseJoinColumns = @JoinColumn(name = "ACTOR_ID"))
    private List<Cast> casts;
    @ElementCollection(targetClass = Genre.class)
    @CollectionTable(name = "GENRE_TO_FILM", joinColumns = @JoinColumn(name = "FILM_ID"))
    @Column(name = "GENRE_ID")
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
        image = "00000";
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

    public void addGeners(Genre gener) {
        genres.add(gener);
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}
