package game;

import java.io.File;
import java.io.FileNotFoundException;
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
    }

    public void run() {
        setPlayerPositions();
        generateDecks();
        assignBoards();
        playGame();
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
        buildAges();
        shuffle();
    }

    private void buildAges() {

        for (int age = 0; age < 3; age++) {

            Scanner scan = null;
            try {
                scan = new Scanner(new File("src/game/cards-age-" + (age + 1) + ".txt"));
            } catch (FileNotFoundException e) {
                System.out.println("error reading in cards-age-" + (age+1) + ".txt");
                System.exit(1);
            }

            _ages.add(new ArrayList<Card>());
            int playerReq = scan.nextInt();
            while (playerReq <= _players.size()) {
                scan.nextLine();
                while (scan.hasNext() && !scan.hasNextInt()) {
                    String cardName = scan.nextLine();
                    Card c = makeCard(cardName);
                    if (c == null) { // TODO: remove this after all cards are implemented
//                    System.out.println("can't make " + cardName);
                    } else {
                        _ages.get(age).add(c);
                    }
                }
                if (scan.hasNextInt()) {
                    playerReq = scan.nextInt();
                } else {
                    break;
                }
            }

            // guild cards
            if (age == 2) {
                // add numPlayers+2 guild cards to deck
            }
        }
    }

    private void shuffle() {
        for (int i = 0; i < _ages.size(); i++) {
            List<Card> shuffled = new ArrayList<Card>();
            while (_ages.get(i).size() > 0) {
                int index = (int) (Math.random() * _ages.get(i).size());
                shuffled.add(_ages.get(i).remove(index));
            }
            _ages.get(i).addAll(shuffled);
        }
    }

    private Card makeCard(String cardName) {
        Card c = new Card();
        c.setName(cardName);
        Resource r0 = new Resource();
        Resource r1 = new Resource();
        Resource r2 = new Resource();
        Resource r3 = new Resource();
        Resource r4 = new Resource();
        Resource r5 = new Resource();
        Resource r6 = new Resource();
        Set<Resource> resources = new HashSet<Resource>();
        List<Resource> cost = new ArrayList<Resource>();
        List<String> fr = new ArrayList<String>();
        Card.Effect ce = null;

        // ========== AGE 1 ==========
        if (cardName.equals("loom")) { // gray
            c.setColor(Card.Color.GRAY);
            r0.addType(Resource.Type.LOOM);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("glassworks")) {
            c.setColor(Card.Color.GRAY);
            r0.addType(Resource.Type.GLASS);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("press")) {
            c.setColor(Card.Color.GRAY);
            r0.addType(Resource.Type.PAPYRUS);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("clay-pit")) { // brown
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.CLAY);
            r0.addType(Resource.Type.ORE);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("timber-yard")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.STONE);
            r0.addType(Resource.Type.WOOD);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("excavation")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.STONE);
            r0.addType(Resource.Type.CLAY);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("clay-pool")) {
            c.setColor(Card.Color.BROWN);
            r0.addType(Resource.Type.CLAY);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("forest-cave")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.WOOD);
            r0.addType(Resource.Type.ORE);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("stone-pit")) {
            c.setColor(Card.Color.BROWN);
            r0.addType(Resource.Type.STONE);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("lumber-yard")) {
            c.setColor(Card.Color.BROWN);
            r0.addType(Resource.Type.WOOD);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("mine")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.STONE);
            r0.addType(Resource.Type.ORE);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("tree-farm")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.WOOD);
            r0.addType(Resource.Type.CLAY);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("ore-vein")) {
            c.setColor(Card.Color.BROWN);
            r0.addType(Resource.Type.ORE);
            resources.add(r0);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("stockade")) { // red
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.WOOD);
            cost.add(r0);
            ce = (Player p) -> p.addMilitary(1);
        } else if (cardName.equals("guard-tower")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.CLAY);
            cost.add(r0);
            ce = (Player p) -> p.addMilitary(1);
        } else if (cardName.equals("barracks")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.ORE);
            cost.add(r0);
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
            r0.addType(Resource.Type.STONE);
            cost.add(r0);
            ce = (Player p) -> p.addVP(3);
        } else if (cardName.equals("workshop")) { // green
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.GLASS);
            cost.add(r0);
            ce = (Player p) -> p.addScience(0);
        } else if (cardName.equals("apothecary")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.LOOM);
            cost.add(r0);
            ce = (Player p) -> p.addScience(1);
        } else if (cardName.equals("scriptorium")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.GLASS);
            cost.add(r0);
            ce = (Player p) -> p.addScience(2);
        } else if (cardName.equals("tavern")) { // yellow
            c.setColor(Card.Color.YELLOW);
            ce = (Player p) -> p.addMoney(5);
        } else if (cardName.equals("marketplace")) {
            // TODO: implement these
            return null;
        } else if (cardName.equals("west-trading-post")) {
            return null;
        } else if (cardName.equals("east-trading-post")) {
            return null;
        }

        // ========== AGE 2 ==========
        else if (cardName.equals("brickyard")) { // brown
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            resources.add(r0);
            resources.add(r1);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("sawmill")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.WOOD);
            resources.add(r0);
            resources.add(r1);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("quarry")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.STONE);
            resources.add(r0);
            resources.add(r1);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("foundry")) {
            c.setColor(Card.Color.BROWN);
            c.setMoneyCost(1);
            r0.addType(Resource.Type.ORE);
            r1.addType(Resource.Type.ORE);
            resources.add(r0);
            resources.add(r1);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("stables")) { // red
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.WOOD);
            r2.addType(Resource.Type.ORE);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("apothecary");
            ce = (Player p) -> p.addMilitary(2);
        } else if (cardName.equals("walls")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.STONE);
            r2.addType(Resource.Type.STONE);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            ce = (Player p) -> p.addMilitary(2);
        } else if (cardName.equals("archery-range")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.WOOD);
            r2.addType(Resource.Type.ORE);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("workshop");
            ce = (Player p) -> p.addMilitary(2);
        } else if (cardName.equals("training-ground")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.ORE);
            r1.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.WOOD);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            ce = (Player p) -> p.addMilitary(2);
        } else if (cardName.equals("aqueduct")) { // blue
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.STONE);
            r2.addType(Resource.Type.STONE);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("baths");
            ce = (Player p) -> p.addVP(5);
        } else if (cardName.equals("temple")) {
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.GLASS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("altar");
            ce = (Player p) -> p.addVP(3);
        } else if (cardName.equals("courthouse")) {
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("scriptorium");
            ce = (Player p) -> p.addVP(4);
        } else if (cardName.equals("statue")) {
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.ORE);
            r1.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.WOOD);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("theater");
            ce = (Player p) -> p.addVP(4);
        } else if (cardName.equals("dispensary")) { // green
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.ORE);
            r1.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.GLASS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("apothecary");
            ce = (Player p) -> p.addScience(1);
        } else if (cardName.equals("laboratory")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.PAPYRUS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("workshop");
            ce = (Player p) -> p.addScience(0);
        } else if (cardName.equals("library")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.STONE);
            r2.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("scriptorium");
            ce = (Player p) -> p.addScience(2);
        } else if (cardName.equals("school")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.PAPYRUS);
            cost.add(r0);
            cost.add(r1);
            ce = (Player p) -> p.addScience(2);
        } else if (cardName.equals("caravansery")) { // yellow
            c.setColor(Card.Color.YELLOW);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.WOOD);
            cost.add(r0);
            cost.add(r1);
            fr.add("marketplace");
            r2.addType(Resource.Type.WOOD);
            r2.addType(Resource.Type.STONE);
            r2.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.CLAY);
            resources.add(r2);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("forum")) {
            c.setColor(Card.Color.YELLOW);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            cost.add(r0);
            cost.add(r1);
            fr.add("trading-post");
            r2.addType(Resource.Type.GLASS);
            r2.addType(Resource.Type.LOOM);
            r2.addType(Resource.Type.PAPYRUS);
            resources.add(r2);
            ce = (Player p) -> p.addResources(resources);
        } else if (cardName.equals("bazar")) {
            c.setColor(Card.Color.YELLOW);
            ce = (Player p) -> p.twoMoneyGrayLMR();
        } else if (cardName.equals("vineyard")) {
            c.setColor(Card.Color.YELLOW);
            ce = (Player p) -> p.oneMoneyBrownLMR();
        }

        // ========== AGE 3 ==========
        else if (cardName.equals("haven")) { // yellow
            c.setColor(Card.Color.YELLOW);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("forum");
            ce = (Player p) -> p.oneMoneyOneVPBrownM();
        } else if (cardName.equals("chamber-of-commerce")) {
            c.setColor(Card.Color.YELLOW);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.PAPYRUS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            ce = (Player p) -> p.twoMoneyTwoVPGrayM();
        } else if (cardName.equals("lighthouse")) {
            c.setColor(Card.Color.YELLOW);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.GLASS);
            cost.add(r0);
            cost.add(r1);
            fr.add("caravansery");
            ce = (Player p) -> p.oneMoneyOneVPYellowM();
        } else if (cardName.equals("arena")) {
            // TODO: implement this
            return null;
        } else if (cardName.equals("arsenal")) { // red
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.WOOD);
            r2.addType(Resource.Type.ORE);
            r3.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            ce = (Player p) -> p.addMilitary(3);
        } else if (cardName.equals("siege-workshop")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.CLAY);
            r3.addType(Resource.Type.WOOD);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("laboratory");
            ce = (Player p) -> p.addMilitary(3);
        } else if (cardName.equals("circus")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.STONE);
            r2.addType(Resource.Type.STONE);
            r3.addType(Resource.Type.ORE);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("training-ground");
            ce = (Player p) -> p.addMilitary(3);
        } else if (cardName.equals("fortifications")) {
            c.setColor(Card.Color.RED);
            r0.addType(Resource.Type.ORE);
            r1.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.ORE);
            r3.addType(Resource.Type.STONE);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("walls");
            ce = (Player p) -> p.addMilitary(3);
        } else if (cardName.equals("senate")) { // blue
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.WOOD);
            r2.addType(Resource.Type.STONE);
            r3.addType(Resource.Type.ORE);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("library");
            ce = (Player p) -> p.addVP(6);
        } else if (cardName.equals("gardens")) {
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.WOOD);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("statue");
            ce = (Player p) -> p.addVP(5);
        } else if (cardName.equals("town-hall")) {
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.STONE);
            r2.addType(Resource.Type.ORE);
            r3.addType(Resource.Type.GLASS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("statue");
            ce = (Player p) -> p.addVP(6);
        } else if (cardName.equals("pantheon")) {
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.ORE);
            r3.addType(Resource.Type.GLASS);
            r4.addType(Resource.Type.PAPYRUS);
            r5.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            cost.add(r4);
            cost.add(r5);
            fr.add("temple");
            ce = (Player p) -> p.addVP(7);
        } else if (cardName.equals("palace")) {
            c.setColor(Card.Color.BLUE);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.WOOD);
            r3.addType(Resource.Type.CLAY);
            r4.addType(Resource.Type.GLASS);
            r5.addType(Resource.Type.PAPYRUS);
            r6.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            cost.add(r4);
            cost.add(r5);
            cost.add(r6);
            ce = (Player p) -> p.addVP(8);
        } else if (cardName.equals("lodge")) { // green
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.CLAY);
            r1.addType(Resource.Type.CLAY);
            r2.addType(Resource.Type.PAPYRUS);
            r3.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("dispensary");
            ce = (Player p) -> p.addScience(1);
        } else if (cardName.equals("academy")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.STONE);
            r1.addType(Resource.Type.STONE);
            r2.addType(Resource.Type.STONE);
            r3.addType(Resource.Type.GLASS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("school");
            ce = (Player p) -> p.addScience(1);
        } else if (cardName.equals("university")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.WOOD);
            r2.addType(Resource.Type.PAPYRUS);
            r3.addType(Resource.Type.GLASS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("library");
            ce = (Player p) -> p.addScience(2);
        } else if (cardName.equals("study")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.PAPYRUS);
            r2.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            fr.add("school");
            ce = (Player p) -> p.addScience(0);
        } else if (cardName.equals("observatory")) {
            c.setColor(Card.Color.GREEN);
            r0.addType(Resource.Type.ORE);
            r1.addType(Resource.Type.ORE);
            r2.addType(Resource.Type.GLASS);
            r2.addType(Resource.Type.LOOM);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            fr.add("laboratory");
            ce = (Player p) -> p.addScience(0);
        } else if (cardName.equals("shipowners-guild")) { // purple
            c.setColor(Card.Color.PURPLE);
            r0.addType(Resource.Type.WOOD);
            r1.addType(Resource.Type.WOOD);
            r2.addType(Resource.Type.WOOD);
            r3.addType(Resource.Type.GLASS);
            r4.addType(Resource.Type.PAPYRUS);
            cost.add(r0);
            cost.add(r1);
            cost.add(r2);
            cost.add(r3);
            cost.add(r4);
            ce = (Player p) -> p.oneVPBrownGrayPurpleM();
        } else if (cardName.equals("workers-guild")) { // TODO: implement all of these

        } else if (cardName.equals("craftsman-guild")) {

        } else if (cardName.equals("magistrates-guild")) {

        } else if (cardName.equals("strategists-guild")) {

        } else if (cardName.equals("scientists-guild")) {

        } else if (cardName.equals("traders-guild")) {

        } else if (cardName.equals("philosophers-guild")) {

        } else if (cardName.equals("builders-guild")) {

        } else if (cardName.equals("spies-guild")) {

        }

        else {
            System.out.println("unsupported card -> " + cardName);
        }

        c.setCost(cost);
        c.setEffect(ce);
        return c;
    }

    private void assignBoards() {

        Scanner scan = null;
        try {
            scan = new Scanner(new File("src/game/boards.txt"));
        } catch (FileNotFoundException e) {
            System.out.println("error reading in boards.txt");
            System.exit(1);
        }

        List<Board> boards = new ArrayList<Board>();
        while (scan.hasNext()) {
            boards.add(makeBoard(scan.nextLine()));
        }
        for (int i = 0; i < _players.size(); i++) {
//            int index = (int) (Math.random() * boards.size());
            int index = i;
            _players.get(i).setBoard(boards.get/*remove*/(index));
            _players.get(i).getBoard().initialize(_players.get(i));
        }
    }

    private Board makeBoard(String boardName) {
        Board b = new Board();
        Resource r = new Resource();
        if (boardName.equals("rhodes")) {
            r.addType(Resource.Type.ORE);
            b.setStartingColor(Card.Color.BROWN);
        } else if (boardName.equals("alexandria")) {
            r.addType(Resource.Type.GLASS);
            b.setStartingColor(Card.Color.GRAY);
        } else if (boardName.equals("ephesus")) {
            r.addType(Resource.Type.PAPYRUS);
            b.setStartingColor(Card.Color.GRAY);
        } else if (boardName.equals("babylon")) {
            r.addType(Resource.Type.CLAY);
            b.setStartingColor(Card.Color.BROWN);
        } else if (boardName.equals("olympia")) {
            r.addType(Resource.Type.WOOD);
            b.setStartingColor(Card.Color.BROWN);
        } else if (boardName.equals("halicarnassus")) {
            r.addType(Resource.Type.LOOM);
            b.setStartingColor(Card.Color.GRAY);
        } else if (boardName.equals("giza")) {
            r.addType(Resource.Type.STONE);
            b.setStartingColor(Card.Color.BROWN);
        }
        b.setStartingResource(r);
        return b;
    }

    private void playGame() {
        for (int i = 0; i < 3; i++) {

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
            for (int j = 0; j < handSize-1; j++) {
                for (int k = 0; k < _players.size(); k++) {
                    _players.get(k).chooseCard();
                }
                for (int k = 0; k < _players.size(); k++) {
                    _players.get(k).flip();
                }

                // TODO: counterclockwise on age 2
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
}