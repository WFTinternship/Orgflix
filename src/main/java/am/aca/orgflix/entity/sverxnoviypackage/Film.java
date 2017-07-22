package am.aca.orgflix.entity.sverxnoviypackage;

import javax.persistence.*;

/**
 * Created by karin on 7/22/2017.
 */
@Entity
@Table(name = "films")
public class Film {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String title;
    @Column
    private int prodYear;
    @Column
    private boolean hasOscar;
    @Column
    private String image;
    @Column
    private String director;
    @Column
    private int rate_1star;
    @Column
    private int rate_2star;
    @Column
    private int rate_3star;
    @Column
    private int rate_4star;
    @Column
    private int rate_5star;

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

    public boolean isHasOscar() {
        return hasOscar;
    }

    public void setHasOscar(boolean hasOscar) {
        this.hasOscar = hasOscar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

}
