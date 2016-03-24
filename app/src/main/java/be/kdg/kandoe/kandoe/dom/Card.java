package be.kdg.kandoe.kandoe.dom;

import java.io.Serializable;

/**
 * Created by Edward on 8/03/2016.
 */
public class Card implements Serializable {
    private Integer cardId;
    private String cardName;
    private String description;
    private String imageUrl;
    private Integer score = 0;
    private Integer userId;
    private int circleId;
    private int selectorId;
    private int position;

    public int getSelectorId() {
        return selectorId;
    }

    public void setSelectorId(int selectorId) {
        this.selectorId = selectorId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getCircleId() {
        return circleId;
    }

    public void setCircleId(int circleId) {
        this.circleId = circleId;
    }

    public int getSelector() {
        return selectorId;
    }

    public void setSelector(int selector) {
        this.selectorId = selector;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
