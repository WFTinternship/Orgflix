package am.aca.orgflix.entity.valodik;

import javax.persistence.*;

/**
 * Created by karin on 7/22/2017.
 */
@Entity
@Table(name = "film_to_genre", schema = "orgflix", catalog = "")
@IdClass(FilmToGenreEntityPK.class)
public class FilmToGenreEntity {
    private int genreId;
    private int filmId;

    @Id
    @Column(name = "GENRE_ID")
    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    @Id
    @Column(name = "FILM_ID")
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

        FilmToGenreEntity that = (FilmToGenreEntity) o;

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
