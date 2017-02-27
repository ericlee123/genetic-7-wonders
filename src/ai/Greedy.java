package ai;

import game.Player;

/**
 * Created by eric on 2/5/17.
 */
public class Greedy extends Strategy {
    public int chooseCard(Player p) {
        int chosen = -1;
        int maxVP = 0;
        for (int i = 0; i < p.getHand().size(); i++) {
            Player temp = new Player(p);
            if (temp.canAfford(p.getHand().get(i))) {
                p.getHand().get(i).affect(temp);
                if (p.calculateVP() > maxVP) {
                    maxVP = p.calculateVP();
                    chosen = i;
                }
            }
        }
        if (chosen == -1) {
            p.setBurn(true);
            return (int) (Math.random() * p.getHand().size());
        }
        return chosen;
    }
}
