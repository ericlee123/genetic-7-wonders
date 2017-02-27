package ai;

import game.Player;

/**
 * Created by eric on 2/26/17.
 */
public class Weighted extends Strategy {
    public int chooseCard(Player p) {
        int bestCard = -1;
        double maxScore = Double.MIN_VALUE;
        for (int i = 0; i < p.getHand().size(); i++) {
            if (p.canAfford(p.getHand().get(i))) {
                Player clone = new Player(p);
                p.getHand().get(i).affect(clone);
                double score = clone.assess();
                if (score > maxScore) {
                    maxScore = score;
                    bestCard = i;
                }
            }
        }
        if (bestCard == -1) {
            p.setBurn(true);
            return (int) (Math.random() * p.getHand().size());
        }
        return bestCard;
    }
}
