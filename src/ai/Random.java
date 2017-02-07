package ai;

import game.Player;

/**
 * Created by eric on 2/6/17.
 */
public class Random extends Strategy {
    public int chooseCard(Player p) {
        return (int) (Math.random() * p.getHand().size());
    }
}
