package am.aca.orgflix.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity for lists of watched and planned films
 */
@Entity (name = "LISTS")
@Table(name = "LISTS")
@AssociationOverrides({
        @AssociationOverride(name = "key.film",
                joinColumns = @JoinColumn(name = "FILM_ID")),
        @AssociationOverride(name = "key.user",
                joinColumns = @JoinColumn(name = "USER_ID"))
})
public class List implements Serializable {
    private ListID key = new ListID();

    private boolean isPublic;

    private boolean isWatched;

    private boolean isPlanned;

    @EmbeddedId
    public ListID getKey() {
        return key;
    }

    public void setKey(ListID listID) {
        this.key = listID;
    }

    @Transient
    public Film getFilm() {
        return this.getKey().getFilm();
    }

    public void setFilm(Film film) {
        this.getKey().setFilm(film);
    }

    @Transient
    public User getUser() {
        return this.getKey().getUser();
    }

    public void setUser(User user) {
        this.getKey().setUser(user);
    }

    @Column(name = "IS_PUBLIC")
    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Column(name = "IS_WATCHED")
    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    @Column(name = "IS_WISHED")
    public boolean isPlanned() {
        return isPlanned;
    }

    public void setPlanned(boolean planned) {
        isPlanned = planned;
    }
}

@Embeddable
class ListID implements Serializable {
    private Film film;
    private User user;

    @ManyToOne
    public Film getFilm() {
        return film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}