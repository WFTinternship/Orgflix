package am.aca.orgflix.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity for lists of watched and planned films
 */
@Entity
@Table(name = "LISTS")
public class List implements Serializable {
    @Id
    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "USER_ID")
    private User user;

    @Id
    @ManyToOne(cascade = {CascadeType.ALL}, targetEntity = Film.class)
    @JoinColumn(name = "FILM_ID")
    private Film film;

    @Column(name = "IS_PUBLIC")
    private boolean isPublic;

    @Column(name = "IS_WATCHED")
    private boolean isWatched;

    @Column(name = "IS_PLANNED")
    private boolean isPlanned;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public boolean isPlanned() {
        return isPlanned;
    }

    public void setPlanned(boolean planned) {
        isPlanned = planned;
    }
}
