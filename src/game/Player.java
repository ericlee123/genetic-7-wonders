package game;

import ai.Strategy;

import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class Player {

    public static final int NUM_WEIGHTS = 9;

    private int _money;
    private int _military;
    private int _militaryLosses;
    private Set<Resource> _resources;
    private int[] _science;
    private int _vp;

    private Map<Card.Color, Integer> _colorFreq;
    private Map<Card.Color, Integer> _vpPerColor;

    private int _chosenCard;
    private boolean _burn;
    private Board _board;
    private List<Card> _hand;
    private Set<String> _selected;
    private Player _leftNeighbor;
    private Player _rightNeighbor;
    private SevenWonders _game;

    private double[][] _weights; // (per age) money military resources science(gear compass slab wildcard) blueVP totalVP
    private Strategy _strategy;

    public Player() {
        _money = 2;
        _military = 0;
        _resources = new HashSet<Resource>();
        _science = new int[4]; // gear compass slab wildcard
        _vp = 0;

        _colorFreq = new HashMap<Card.Color, Integer>();
        _colorFreq.put(Card.Color.GRAY, 0);
        _colorFreq.put(Card.Color.BROWN, 0);
        _colorFreq.put(Card.Color.RED, 0);
        _colorFreq.put(Card.Color.BLUE, 0);
        _colorFreq.put(Card.Color.GREEN, 0);
        _colorFreq.put(Card.Color.YELLOW, 0);
        _colorFreq.put(Card.Color.PURPLE, 0);
        _vpPerColor = new HashMap<Card.Color, Integer>();
        _vpPerColor.put(Card.Color.GRAY, 0);
        _vpPerColor.put(Card.Color.BROWN, 0);
        _vpPerColor.put(Card.Color.RED, 0);
        _vpPerColor.put(Card.Color.BLUE, 0);
        _vpPerColor.put(Card.Color.GREEN, 0);
        _vpPerColor.put(Card.Color.YELLOW, 0);
        _vpPerColor.put(Card.Color.PURPLE, 0);

        _chosenCard = -1;
        _burn = false;
        _board = null;
        _hand = null;
        _selected = new HashSet<String>();
        _leftNeighbor = null;
        _rightNeighbor = null;

        _weights = new double[SevenWonders.NUM_AGES][NUM_WEIGHTS];
    }

    public Player(Player copy) {
        _money = copy.getMoney();
        _military = copy.getMilitary();
        _resources = new HashSet<Resource>();
        _resources.addAll(copy.getResources());
        _science = copy.getScience();
        _vp = copy.getVP();

        _colorFreq = new HashMap<Card.Color, Integer>();
        _colorFreq.putAll(copy.getColorFreq());
        _vpPerColor = new HashMap<Card.Color, Integer>();
        _vpPerColor.putAll(copy.getVPPerColor());

        _board = copy.getBoard();
        _leftNeighbor = copy.getLeftNeighbor();
        _rightNeighbor = copy.getRightNeighbor();
        _game = copy.getSevenWonders();

        _weights = copy.getWeights();
    }

    public double assess() {
        int age = _game.getAge();
        double score = 0;

        score += _weights[age][0] * _money;
        score += _weights[age][1] * _military;
        score += _weights[age][2] * _resources.size(); // TODO: do something better like measure variety or individual resources (blegh)

        for (int i = 0; i < _science.length; i++) {
            score += _weights[age][3+i] * _science[i];
        }

        score += _weights[age][7] * _vp;
        score += _weights[age][8] * calculateVP();

        return score;
    }

    public int calculateVP() {
        int vp = 0;
        vp += _money / 3;
        vp += _vp;

        int minScience = _science[0];
        for (int i = 0; i < 3; i++) { // TODO: do this right later
            vp += Math.pow(_science[i], 2);
            minScience = (_science[i] < minScience) ? _science[i] : minScience;
        }
        vp += 7 * minScience;

        for (Card.Color color : _vpPerColor.keySet()) {
            _vp += _vpPerColor.get(color) * _colorFreq.get(color);
        }

        return vp;
    }

    public void chooseCard() {
        if (_strategy == null) {
            System.out.println("it's null");
        }
        _chosenCard = _strategy.chooseCard(this);
    }

    public void flip() {
        if (_burn) {
            _hand.remove(_chosenCard);
            _chosenCard = -1;
            _money += 3;
        } else {
            _hand.get(_chosenCard).affect(this);
            _selected.add(_hand.get(_chosenCard).getName());
            _colorFreq.put(_hand.get(_chosenCard).getColor(), _colorFreq.get(_hand.get(_chosenCard).getColor()) + 1);
            _hand.remove(_chosenCard);
            _chosenCard = -1;
        }
    }

    public boolean canAfford(Card card) {
        for (String s : card.getFreeRequirements()) {
            if (_selected.contains(s)) {
                return true;
            }
        }
        if (_money < card.getMoneyCost()) {
            return false;
        }

        // TODO: do this correctly (recursively) and look for trades with neighbors
        Set<Resource> goods = new HashSet<Resource>();
        goods.addAll(_resources);
        List<Resource> cost = card.getCost();
        for (int i = 0; i < cost.size(); i++) {
            boolean paid = false;
            for (Resource g : goods) {
                if (g.equals(cost.get(i))) {
                    paid = true;
                }
            }
            if (!paid) {
                return false;
            }
        }
        return true;
    }

    // setters and getters

    public void setSevenWonders(SevenWonders game) {
        _game = game;
    }

    public SevenWonders getSevenWonders() {
        return _game;
    }

    public void setBurn(boolean burn) {
        _burn = burn;
    }

    public void setStrategy(Strategy strategy) {
        _strategy = strategy;
    }

    public Strategy getStrategy() {
        return _strategy;
    }

    public void setChosenCard(int card) {
        _chosenCard = card;
    }

    public int getChosenCard() {
        return _chosenCard;
    }

    public void setBoard(Board board) {
        _board = board;
    }

    public Board getBoard() {
        return _board;
    }

    public void setHand(List<Card> hand) {
        _hand = hand;
    }

    public List<Card> getHand() {
        return _hand;
    }

    public Map<Card.Color, Integer> getColorFreq() {
        return _colorFreq;
    }

    public Map<Card.Color, Integer> getVPPerColor() {
        return _vpPerColor;
    }

    public void addMoney(int change) {
        _money += change;
    }

    public int getMoney() {
        return _money;
    }

    public void addMilitary(int military) {
        _military += military;
    }

    public int getMilitary() {
        return _military;
    }

    public int getMilitaryLosses() {
        return _militaryLosses;
    }

    public void addResources(Collection<Resource> r) {
        _resources.addAll(r);
    }

    public Set<Resource> getResources() {
        return _resources;
    }

    public void addScience(int science) {
        _science[science]++;
    }

    public int[] getScience() {
        return _science;
    }

    public void addVP(int vp) {
        _vp += vp;
    }

    public int getVP() {
        return _vp;
    }

    public void setLeftNeighbor(Player leftNeighbor) {
        _leftNeighbor = leftNeighbor;
    }

    public Player getLeftNeighbor() {
        return _leftNeighbor;
    }

    public void setRightNeighbor(Player rightNeighbor) {
        _rightNeighbor = rightNeighbor;
    }

    public Player getRightNeighbor() {
        return _rightNeighbor;
    }

    public void setWeights(double[][] weights) {
        _weights = weights;
    }

    public double[][] getWeights() {
        return _weights;
    }

    public void compareMilitary(int age) {
        if (_military > _leftNeighbor.getMilitary()) {
            _vp += age*2 + 1;
        } else if (_military < _leftNeighbor.getMilitary()) {
            _militaryLosses++;
            _vp--;
        }

        if (_military > _rightNeighbor.getMilitary()) {
            _vp += age*2 + 1;
        } else if (_military < _rightNeighbor.getMilitary()) {
            _militaryLosses++;
            _vp--;
        }
    }

    // special card effects

    public void twoMoneyGrayLMR() {
        _money += 2 * _leftNeighbor.getColorFreq().get(Card.Color.GRAY);
        _money += 2 * _colorFreq.get(Card.Color.GRAY);
        _money += 2 * _rightNeighbor.getColorFreq().get(Card.Color.GRAY);
    }

    public void oneMoneyBrownLMR() {
        _money += _leftNeighbor.getColorFreq().get(Card.Color.BROWN);
        _money += _colorFreq.get(Card.Color.BROWN);
        _money += _rightNeighbor.getColorFreq().get(Card.Color.BROWN);
    }

    public void oneMoneyOneVPBrownM() {
        _money += _colorFreq.get(Card.Color.BROWN);
        _vpPerColor.put(Card.Color.BROWN, _vpPerColor.get(Card.Color.BROWN)+1);
    }

    public void twoMoneyTwoVPGrayM() {
        _money += 2 * _colorFreq.get(Card.Color.GRAY);
        _vpPerColor.put(Card.Color.GRAY, _vpPerColor.get(Card.Color.GRAY)+2);
    }

    public void oneMoneyOneVPYellowM() {
        _money += _colorFreq.get(Card.Color.YELLOW);
        _vpPerColor.put(Card.Color.YELLOW, _vpPerColor.get(Card.Color.YELLOW)+1);
    }

    public void oneVPBrownGrayPurpleM() {
        _vpPerColor.put(Card.Color.BROWN, _vpPerColor.get(Card.Color.BROWN)+1);
        _vpPerColor.put(Card.Color.GRAY, _vpPerColor.get(Card.Color.GRAY)+1);
        _vpPerColor.put(Card.Color.PURPLE, _vpPerColor.get(Card.Color.PURPLE)+1);
    }
}