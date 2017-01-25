package game;

import ai.Player;

import java.io.File;
import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class SevenWonders {

    private List<Player> _players;

    private List<List<Card>> _ages;

    public SevenWonders(List<Player> players) {
        _players = players;
        _ages = new ArrayList<List<Card>>();
        _ages.add(new ArrayList<Card>());
    }

    public int[] run() {
        setPlayerPositions();
        generateDecks();
        playGame();
        return getResults();
    }

    private void setPlayerPositions() {
        for (int i = 0; i < _players.size(); i++) {
            int left = (i == 0) ? _players.size()-1 : i-1;
            int right = (i == _players.size()-1) ? 0 : i+1;

            _players.get(i).setLeftNeighbor(_players.get(left));
            _players.get(i).setRightNeighbor(_players.get(right));
        }
    }

    private void generateDecks() {
        try {
            buildAge(1);
//            buildAge(2);
//            buildAge(3);
        } catch (Exception e) {
            System.out.print("Could not build decks properly.");
            e.printStackTrace();
            return;
        }
    }

    private void buildAge(int age) throws Exception {
        _ages.add(new ArrayList<Card>());
        Scanner scan = new Scanner(new File("src/game/cards-age-" + age + ".txt"));
        int playerReq = scan.nextInt();
        while (playerReq <= _players.size()) {
            scan.nextLine();
            while (scan.hasNext() && !scan.hasNextInt()) {
                String cardName = scan.nextLine();
                Card c = makeCard(cardName);
                if (c == null) {
                    System.out.println("c is null");
                    throw new Exception();
                }
                _ages.get(age).add(c);
            }
            if (scan.hasNextInt()) {
                playerReq = scan.nextInt();
            } else {
                break;
            }
        }
    }

    private Card makeCard(String cardName) {
        Card c = new Card();
        c.setName(cardName);
        Resource r = new Resource();
        List<Resource> cost = new ArrayList<Resource>();
        Set<Resource> resources = new HashSet<Resource>();
        Card.Effect ce = null;

        if (cardName.equals("loom")) { // gray
            c.setColor(Card.Color.GRAY);
            r.addType(Resource.Type.LOOM);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("glassworks")) {
            c.setColor(Card.Color.GRAY);
            r.addType(Resource.Type.GLASS);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("press")) {
            c.setColor(Card.Color.GRAY);
            r.addType(Resource.Type.PAPYRUS);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("clay-pit")) { // brown
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r.addType(Resource.Type.CLAY);
            r.addType(Resource.Type.ORE);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("timber-yard")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r.addType(Resource.Type.STONE);
            r.addType(Resource.Type.WOOD);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("excavation")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r.addType(Resource.Type.STONE);
            r.addType(Resource.Type.CLAY);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("clay-pool")) {
            c.setColor(Card.Color.BROWN);
            r.addType(Resource.Type.CLAY);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("forest-cave")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r.addType(Resource.Type.WOOD);
            r.addType(Resource.Type.ORE);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("stone-pit")) {
            c.setColor(Card.Color.BROWN);
            r.addType(Resource.Type.STONE);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("lumber-yard")) {
            c.setColor(Card.Color.BROWN);
            r.addType(Resource.Type.WOOD);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("mine")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r.addType(Resource.Type.STONE);
            r.addType(Resource.Type.ORE);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("tree-farm")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r.addType(Resource.Type.WOOD);
            r.addType(Resource.Type.CLAY);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("ore-vein")) {
            c.setColor(Card.Color.BROWN);
            r.addType(Resource.Type.ORE);
            resources.add(r);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("stockade")) { // red
            c.setColor(Card.Color.RED);
            r.addType(Resource.Type.WOOD);
            cost.add(r);
            ce = (Player p) -> p.addMilitary(1);
        } else if (cardName.equals("guard-tower")) {
            c.setColor(Card.Color.RED);
            r.addType(Resource.Type.CLAY);
            cost.add(r);
            ce = (Player p) -> p.addMilitary(1);
        } else if (cardName.equals("barracks")) {
            c.setColor(Card.Color.RED);
            r.addType(Resource.Type.ORE);
            cost.add(r);
            ce = (Player p) -> p.addMilitary(1);
        } else if (cardName.equals("pawnshop")) { // blue
            c.setColor(Card.Color.BLUE);
            ce = (Player p) -> p.addVP(3);
        } else if (cardName.equals("altar")) {
            c.setColor(Card.Color.BLUE);
            ce = (Player p) -> p.addVP(2);
        } else if (cardName.equals("theater")) {
            c.setColor(Card.Color.BLUE);
            ce = (Player p) -> p.addVP(2);
        } else if (cardName.equals("baths")) {
            c.setColor(Card.Color.BLUE);
            r.addType(Resource.Type.STONE);
            cost.add(r);
            ce = (Player p) -> p.addVP(3);
        } else if (cardName.equals("workshop")) { // green
            c.setColor(Card.Color.GREEN);
            r.addType(Resource.Type.GLASS);
            cost.add(r);
            ce = (Player p) -> p.addScience(0);
        } else if (cardName.equals("apothecary")) {
            c.setColor(Card.Color.GREEN);
            r.addType(Resource.Type.LOOM);
            cost.add(r);
            ce = (Player p) -> p.addScience(1);
        } else if (cardName.equals("scriptorium")) {
            c.setColor(Card.Color.GREEN);
            r.addType(Resource.Type.GLASS);
            cost.add(r);
            ce = (Player p) -> p.addScience(2);
        } else if (cardName.equals("tavern")) { // yellow
            c.setColor(Card.Color.YELLOW);
            ce = (Player p) -> p.addMoney(5);
        } else {
            System.out.println("unsupported card -> " + cardName);
        } // ADD THE REST OF THE YELLOW

        c.setCost(cost);
        c.setEffect(ce);
        return c;
    }

    private void playGame() {
        for (int i = 1; i <= 1; i++) {

            // distribute cards
            int handSize = _ages.get(i).size() / _players.size();
            for (int j = 0; j < _players.size(); j++) {
                List<Card> hand = new ArrayList<Card>();
                for (int k = 0; k < handSize; k++) {
                    hand.add(_ages.get(i).get(j*handSize + k));
                }
                _players.get(j).setHand(hand);
            }

            // do age
            for (int j = 0; j < handSize; j++) {
                for (int k = 0; k < _players.size(); k++) {
                    _players.get(k).chooseCard();
                }
                List<Card> temp = _players.get(0).getHand();
                for (int k = 0; k < _players.size() - 1; k++) {
                    _players.get(k).setHand(_players.get(k+1).getHand());
                }
                _players.get(_players.size()-1).setHand(temp);
            }

            // compare military
            for (int j = 0; j < _players.size(); j++) {
                _players.get(j).compareMilitary(i);
            }
        }
    }

    private int[] getResults() {
        int[] scores = new int[_players.size()];
        for (int i = 0; i < _players.size(); i++) {
            scores[i] = _players.get(i).calculateVP();
        }
        return scores;
    }

}