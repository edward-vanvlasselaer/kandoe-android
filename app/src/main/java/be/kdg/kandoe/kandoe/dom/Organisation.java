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
}
