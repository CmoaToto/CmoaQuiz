package fr.cmoatoto.quiz.lib.backoffice.pojo;

import java.io.Serializable;

public class Player implements Serializable {

    private String idGoogle;

    private String displayName;

    private String image;

    public Player() {
    }

    public Player(com.google.android.gms.games.Player person) {
        this.idGoogle = person.getPlayerId();
        this.displayName = person.getDisplayName();
        this.image = person.hasIconImage() && person.getIconImageUrl() != null ? person.getIconImageUrl().split("\\?")[0] : null;
    }

    public String getIdGoogle() {
        return idGoogle;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getImage() {
        return image;
    }

    public boolean isUsable() {
        return idGoogle != null && displayName != null;
    }

}
