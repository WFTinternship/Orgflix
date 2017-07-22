package am.aca.orgflix.entity.sverxnoviypackage;

import javax.persistence.*;

/**
 * Created by karin on 7/22/2017.
 */
@Entity
@Table(name = "lists")
public class UserFilm {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_watched")
    private boolean isWatched;

    @Column(name = "is_wished")
    private boolean isWished;

    @Column(name = "is_public")
    private boolean isPublic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isWatched() {
        return isWatched;
    }

    public void setWatched(boolean watched) {
        isWatched = watched;
    }

    public boolean isWished() {
        return isWished;
    }

    public void setWished(boolean wished) {
        isWished = wished;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

//    @JoinColumn
//    @ManyToOne
//    private User user;
//
//    @JoinColumn
//    @OneToOne
//    private Film film;


}
