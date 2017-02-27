package ai;

import game.Player;

/**
 * Created by eric on 2/6/17.
 */
public class Random extends Strategy {
    public int chooseCard(Player p) {
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.canAfford(p.getHand().get(i))) {
                return i;
            }
        }
        p.setBurn(true);
        return (int) (Math.random() * p.getHand().size());
    }
}
