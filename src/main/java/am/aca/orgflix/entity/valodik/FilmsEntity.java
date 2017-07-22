package am.aca.orgflix.entity.valodik;

import javax.persistence.*;

/**
 * Created by karin on 7/22/2017.
 */
@Entity
@Table(name = "films", schema = "orgflixtest", catalog = "")
public class FilmsEntity {
    private int id;
    private String title;
    private short prodYear;
    private byte hasOscar;
    private String imageRef;
    private String director;
    private Integer rate1Star;
    private Integer rate2Star;
    private Integer rate3Star;
    private Integer rate4Star;
    private Integer rate5Star;

    @Id
    @Column(name = "ID")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "TITLE")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "PROD_YEAR")
    public short getProdYear() {
        return prodYear;
    }

    public void setProdYear(short prodYear) {
        this.prodYear = prodYear;
    }

    @Basic
    @Column(name = "HAS_OSCAR")
    public byte getHasOscar() {
        return hasOscar;
    }

    public void setHasOscar(byte hasOscar) {
        this.hasOscar = hasOscar;
    }

    @Basic
    @Column(name = "IMAGE_REF")
    public String getImageRef() {
        return imageRef;
    }

    public void setImageRef(String imageRef) {
        this.imageRef = imageRef;
    }

    @Basic
    @Column(name = "DIRECTOR")
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Basic
    @Column(name = "RATE_1STAR")
    public Integer getRate1Star() {
        return rate1Star;
    }

    public void setRate1Star(Integer rate1Star) {
        this.rate1Star = rate1Star;
    }

    @Basic
    @Column(name = "RATE_2STAR")
    public Integer getRate2Star() {
        return rate2Star;
    }

    public void setRate2Star(Integer rate2Star) {
        this.rate2Star = rate2Star;
    }

    @Basic
    @Column(name = "RATE_3STAR")
    public Integer getRate3Star() {
        return rate3Star;
    }

    public void setRate3Star(Integer rate3Star) {
        this.rate3Star = rate3Star;
    }

    @Basic
    @Column(name = "RATE_4STAR")
    public Integer getRate4Star() {
        return rate4Star;
    }

    public void setRate4Star(Integer rate4Star) {
        this.rate4Star = rate4Star;
    }

    @Basic
    @Column(name = "RATE_5STAR")
    public Integer getRate5Star() {
        return rate5Star;
    }

    public void setRate5Star(Integer rate5Star) {
        this.rate5Star = rate5Star;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilmsEntity that = (FilmsEntity) o;

        if (id != that.id) return false;
        if (prodYear != that.prodYear) return false;
        if (hasOscar != that.hasOscar) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (imageRef != null ? !imageRef.equals(that.imageRef) : that.imageRef != null) return false;
        if (director != null ? !director.equals(that.director) : that.director != null) return false;
        if (rate1Star != null ? !rate1Star.equals(that.rate1Star) : that.rate1Star != null) return false;
        if (rate2Star != null ? !rate2Star.equals(that.rate2Star) : that.rate2Star != null) return false;
        if (rate3Star != null ? !rate3Star.equals(that.rate3Star) : that.rate3Star != null) return false;
        if (rate4Star != null ? !rate4Star.equals(that.rate4Star) : that.rate4Star != null) return false;
        if (rate5Star != null ? !rate5Star.equals(that.rate5Star) : that.rate5Star != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (int) prodYear;
        result = 31 * result + (int) hasOscar;
        result = 31 * result + (imageRef != null ? imageRef.hashCode() : 0);
        result = 31 * result + (director != null ? director.hashCode() : 0);
        result = 31 * result + (rate1Star != null ? rate1Star.hashCode() : 0);
        result = 31 * result + (rate2Star != null ? rate2Star.hashCode() : 0);
        result = 31 * result + (rate3Star != null ? rate3Star.hashCode() : 0);
        result = 31 * result + (rate4Star != null ? rate4Star.hashCode() : 0);
        result = 31 * result + (rate5Star != null ? rate5Star.hashCode() : 0);
        return result;
    }
}
