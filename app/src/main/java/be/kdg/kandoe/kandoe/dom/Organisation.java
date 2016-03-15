package be.kdg.kandoe.kandoe.dom;

import java.util.List;

/**
 * Created by Edward on 14/03/2016.
 */
public class Organisation {
    private Integer organisationId;
    private String organisationName;
    private String imageUrl;
    private User creator;
    private List<User> members;
    private List<Theme> themes;

    public Integer getOrganisationId() {
        return organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public User getCreator() {
        return creator;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<Theme> getThemes() {
        return themes;
    }

    public void setOrganisationId(Integer organisationId) {
        this.organisationId = organisationId;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setThemes(List<Theme> themes) {
        this.themes = themes;
    }
}
