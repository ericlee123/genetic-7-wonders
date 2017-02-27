package ai;

import game.Player;

/**
 * Created by eric on 2/5/17.
 */
public abstract class Strategy {
    /**
     * Needs to set burn variable if necessary.
     * @param p
     * @return index of chosen card
     */
    public abstract int chooseCard(Player p);
}
