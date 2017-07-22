package am.aca.orgflix.entity.valodik;

import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by karin on 7/22/2017.
 */
@Entity
@Table(name = "lists", schema = "orgflixtest", catalog = "")
public class ListsEntity {

    private byte isWatched;
    private byte isWished;
    private byte isPublic;


    @Basic
    @Column(name = "IS_WATCHED")
    public byte getIsWatched() {
        return isWatched;
    }

    public void setIsWatched(byte isWatched) {
        this.isWatched = isWatched;
    }

    @Basic
    @Column(name = "IS_WISHED")
    public byte getIsWished() {
        return isWished;
    }

    public void setIsWished(byte isWished) {
        this.isWished = isWished;
    }

    @Basic
    @Column(name = "IS_PUBLIC")
    public byte getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(byte isPublic) {
        this.isPublic = isPublic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListsEntity that = (ListsEntity) o;

        if (isWatched != that.isWatched) return false;
        if (isWished != that.isWished) return false;
        if (isPublic != that.isPublic) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) isWatched;
        result = 31 * result + (int) isWished;
        result = 31 * result + (int) isPublic;
        return result;
    }
}