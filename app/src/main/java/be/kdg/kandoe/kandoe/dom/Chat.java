package be.kdg.kandoe.kandoe.dom;

import java.io.Serializable;

/**
 * Created by claudiu on 18/03/16.
 */
public class Chat implements Serializable {
    private Integer id;
    private String message;
    //private LocalDateTime timestamp;
    private int circle;
    private User user;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCircle() {
        return circle;
    }

    public void setCircle(int circle) {
        this.circle = circle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
