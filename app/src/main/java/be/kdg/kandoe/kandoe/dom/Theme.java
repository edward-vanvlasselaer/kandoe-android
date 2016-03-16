package be.kdg.kandoe.kandoe.dom;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Edward on 14/03/2016.
 */
public class Theme implements Serializable{
    private Integer themeId;
    private String name;
    private String description;
    private String tags;
    private List<Theme> subThemes;
    private Integer mainThemeId;
    private Circle circle;

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Integer getThemeId() {
        return themeId;
    }

    public void setThemeId(Integer themeId) {
        this.themeId = themeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<Theme> getSubThemes() {
        return subThemes;
    }

    public void setSubThemes(List<Theme> subThemes) {
        this.subThemes = subThemes;
    }

    public Integer getMainThemeId() {
        return mainThemeId;
    }

    public void setMainThemeId(Integer mainThemeId) {
        this.mainThemeId = mainThemeId;
    }
}
