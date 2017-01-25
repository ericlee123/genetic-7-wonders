package simulate;

import ai.Player;
import game.SevenWonders;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 1/24/17.
 */
public class Nature {
    public static void main(String[] args) {

        List<Player> players = new ArrayList<Player>();
        for (int i = 0; i < 6; i++) {
            players.add(new Player());
        }

        SevenWonders game = new SevenWonders(players);
        int[] results = game.run();

        for (int i = 0; i < results.length; i++) {
            System.out.println(i + " -> " + results[i]);
        }
    }
}
