package am.aca.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/26/2017.
 */
public class Film {
    private int id;
    private String title;
    private int prodYear;
    private boolean hasOscar;
    private int rate_1star;
    private int rate_2star;
    private int rate_3star;
    private int rate_4star;
    private int rate_5star;
    private List<Director> directors;
    private List<Genre> geners;

    public Film() {
        hasOscar = false;
        rate_1star = 0;
        rate_2star = 0;
        rate_3star = 0;
        rate_4star = 0;
        rate_5star = 0;
        geners = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Film film = (Film) o;

        if (getId() != film.getId()) return false;
        if (getProdYear() != film.getProdYear()) return false;
        return getTitle().equals(film.getTitle());
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
                ", rate_1star=" + rate_1star +
                ", rate_2star=" + rate_2star +
                ", rate_3star=" + rate_3star +
                ", rate_4star=" + rate_4star +
                ", rate_5star=" + rate_5star +
                ", geners=" + geners +
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

    public int getProdYear() {
        return prodYear;
    }

    public void setProdYear(int prodYear) {
        this.prodYear = prodYear;
    }

    public boolean isHasOscar() { return hasOscar;}

    public void setHasOscar(boolean hasOscar) {
        this.hasOscar = hasOscar;
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

    public List<Director> getDirectors() {
        return directors;
    }

    public void addDirector(Director director) {
        this.directors.add(director);
    }

    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }

    public List<Genre> getGeners() {
        return geners;
    }

    public void addGeners(Genre gener) {
        geners.add(gener);
    }
}
