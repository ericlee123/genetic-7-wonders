package ai;

import game.Board;
import game.Card;
import game.Resource;

import java.util.*;

/**
 * Created by eric on 1/24/17.
 */
public class Player {

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

    private double[] _weights; // money military resources science vp
    private boolean _stupid;

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

        _weights = new double[5];
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

        _weights = copy.getWeights();
    }

    public double assess() {
//        double value = 0;
//        value += _money * _weights[0];
//        value += _military * _weights[1];
//        value += _resources.size() * _weights[2];
//
//        int scienceTotal = 0;
//        for (int i = 0; i < _science.length; i++) {
//            scienceTotal += _science[i];
//        }
//        value += scienceTotal * _weights[3];
//
//        value += _vp * _weights[4];
//
//        if (value < 0) {
//            System.out.println("money " + _money);
//            System.out.println("military " + _military);
//            System.out.println("resources " + _resources.size());
//            System.out.println("st " + scienceTotal);
//            System.out.println("vp " + _vp);
//            for (int i = 0; i < _weights.length; i++) {
//                System.out.print(_weights[i] + " ");
//            }
//        }

        return _weights[0] * calculateVP();
    }

    public void stupid() {
        _stupid = true;
    }

    public void chooseCard() {
        if (!_stupid) {
            boolean takeOne = false;
            double potential = -50000;
            for (int i = 0; i < _hand.size(); i++) {
                if (afford(_hand.get(i))) { // TODO: if player can't afford anything, burn for 3 coins
                    takeOne = true;
                    Player temp = new Player(this);
                    _hand.get(i).affect(temp);
                    if (temp.assess() > potential) {
                        potential = temp.assess();
                        _chosenCard = i;
                    }
                }
            }
            if (!takeOne) {
                _chosenCard = (int) (Math.random() * _hand.size()); // TODO: make this smarter later
                _burn = true;
            }
        } else {
            _chosenCard = (int) (Math.random() * _hand.size());
        }
    }

    public void flip() {
        if (_chosenCard == -1) {
            System.out.println("SOMEONE DIDN'T CHOOSE A CARD!!!!");
        }

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

    public boolean afford(Card card) {
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

    public void setWeights(double[] weights) {
        _weights = weights;
    }

    public double[] getWeights() {
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

    public int calculateVP() {
        int vp = 0;
        vp += _money / 3;
        vp += _vp;

        int minScience = _science[0];
        for (int i = 0; i < 3; i++) { // do this right later
            vp += Math.pow(_science[i], 2);
            minScience = (_science[i] < minScience) ? _science[i] : minScience;
        }
        vp += 7 * minScience;

        for (Card.Color color : _vpPerColor.keySet()) {
            _vp += _vpPerColor.get(color) * _colorFreq.get(color);
        }

        return vp;
    }
}