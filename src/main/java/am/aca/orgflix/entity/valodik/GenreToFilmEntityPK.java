package am.aca.orgflix.entity.valodik;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by karin on 7/22/2017.
 */
public class GenreToFilmEntityPK implements Serializable {
    private int genreId;
    private int filmId;

    @Column(name = "GENRE_ID")
    @Id
    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    @Column(name = "FILM_ID")
    @Id
    public int getFilmId() {
        return filmId;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GenreToFilmEntityPK that = (GenreToFilmEntityPK) o;

        if (genreId != that.genreId) return false;
        if (filmId != that.filmId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = genreId;
        result = 31 * result + filmId;
        return result;
    }
}
