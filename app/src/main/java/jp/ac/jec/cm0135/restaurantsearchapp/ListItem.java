package jp.ac.jec.cm0135.restaurantsearchapp;

public class ListItem {
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    ListItem(String name, String access, String photoUrl) {
        this.name = name;
        this.access = access;
        this.photoUrl = photoUrl;
    }

    private String photoUrl;
    private String name;
    private String access;
}
