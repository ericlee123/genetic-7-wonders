package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 1/24/17.
 */
public class Board {

    private Resource _startingResource;
    private Card.Color _startingColor;

    public void initialize(Player p) {
        List<Resource> temp = new ArrayList<Resource>();
        temp.add(_startingResource);
        p.addResources(temp);
        p.getColorFreq().put(_startingColor, p.getColorFreq().get(_startingColor)+1);
    }

    public void setStartingResource(Resource r) {
        _startingResource = r;
    }

    public Resource getStartingResource() {
        return _startingResource;
    }

    public void setStartingColor(Card.Color c) {
        _startingColor = c;
    }

    public Card.Color getStartingColor() {
        return _startingColor;
    }

}
