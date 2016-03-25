package be.kdg.kandoe.kandoe.util;

import java.util.Comparator;

import be.kdg.kandoe.kandoe.dom.Card;

/**
 * Created by Edward on 25/03/2016.
 */
public class CustomComparator implements Comparator<Card> {
    @Override
    public int compare(Card lhs, Card rhs) {
        return lhs.getCardId().compareTo(rhs.getCardId());
    }
}