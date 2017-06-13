package am.aca.entity;

/**
 * Created by karine on 6/3/2017
 */
public enum Genre {
    FAMILY (1, "Family"),
    HISTORY (2, "History"),
    MUSIC (3, "Music"),
    MYSTERY (4, "Music"),
    SCI_FI (5, "Sci-Fi"),
    THRILLER (6, "Music"),
    WESTERN (7, "Music"),
    ACTION (8, "Music"),
    ANIMATION (9, "Music"),
    COMEDY (10, "Music"),
    DOCUMENTARY (11, "Music"),
    ADVENTURE (12, "Music"),
    BIOGRAPHY (13, "Music"),
    CRIME (14, "Music"),
    DRAMA (15, "Music"),
    FANTASY (16, "Music"),
    HORROR (17, "Music"),
    MUSICAL (18, "Music"),
    ROMANCE (19, "Music"),
    SPORT (20, "Music"),
    WAR (21, "Music");

    Genre(final int value, final String title) {
        this.value = value;
        this.title = title;
    }

    public int getValue() {
        return value;
    }

    public String getTitle() {
        return title;
    }

    private int value;
    private String title;
}
