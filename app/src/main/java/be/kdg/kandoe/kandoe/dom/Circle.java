package be.kdg.kandoe.kandoe.dom;

import android.content.Intent;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Edward on 14/03/2016.
 */
public class Circle implements Serializable {
    private Integer circleId;
    private List<User> users;
    private Theme themeResource;
    //private GameMode gameMode;
    private Integer turnTime;
    private Integer totalRounds;
    private List<Card> cards;
    private Integer maxCardsToSelect;
    private Integer minCardsToSelect;

    public Integer getMaxCardsToSelect() {
        return maxCardsToSelect;
    }

    public void setMaxCardsToSelect(Integer maxCardsToSelect) {
        this.maxCardsToSelect = maxCardsToSelect;
    }

    public Integer getMinCardsToSelect() {
        return minCardsToSelect;
    }

    public void setMinCardsToSelect(Integer minCardsToSelect) {
        this.minCardsToSelect = minCardsToSelect;
    }

    public Integer getCircleId() {
        return circleId;
    }

    public void setCircleId(Integer circleId) {
        this.circleId = circleId;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public Theme getThemeResource() {
        return themeResource;
    }

    public void setThemeResource(Theme themeResource) {
        this.themeResource = themeResource;
    }

    public Integer getTurnTime() {
        return turnTime;
    }

    public void setTurnTime(Integer turnTime) {
        this.turnTime = turnTime;
    }

    public Integer getTotalRounds() {
        return totalRounds;
    }

    public void setTotalRounds(Integer totalRounds) {
        this.totalRounds = totalRounds;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
}
