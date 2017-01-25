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
    private int _militaryVP;
    private Set<Resource> _resources;
    private int[] _science;
    private int _vp;

    private Map<Card.Color, Integer> _colorFreq;
    private Board _board;
    private List<Card> _hand;
    private Player _leftNeighbor;
    private Player _rightNeighbor;

    private double[] _weights; // money military resources science vp

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

        _board = null;
        _hand = null;
        _leftNeighbor = null;
        _rightNeighbor = null;

        _weights = new double[5];
    }

    public Player(Player copy) {
        _money = copy.getMoney();
        _military = copy.getMilitary();
        _resources = copy.getResources();
        _science = copy.getScience();
        _vp = copy.getVP();

        _colorFreq = copy.getColorFreq();

        _leftNeighbor = copy.getLeftNeighbor();
        _rightNeighbor = copy.getRightNeighbor();

        _weights = copy.getWeights();
    }

    public double assess() {
        double value = 0;
        value += _money * _weights[0];
        value += _military * _weights[1];
        value += _resources.size() * _weights[2];

        int scienceTotal = 0;
        for (int i = 0; i < _science.length; i++) {
            scienceTotal += _science[i];
        }
        value += scienceTotal * _weights[3];

        value += _vp * _weights[4];
        return value;
    }

    public void chooseCard() {

        int index = 0;
        double potential = 0;
        for (int i = 0; i < _hand.size(); i++) {
            Player temp = new Player(this);
            _hand.get(i).affect(temp);
            if (temp.assess() > potential) {
                potential = temp.assess();
                index = i;
            }
        }

        _hand.get(index).affect(this);
        _colorFreq.put(_hand.get(index).getColor(), _colorFreq.get(_hand.get(index).getColor())+1);
        _hand.remove(index);
    }

    public void setHand(List<Card> hand) {
        _hand = hand;
    }

    public List<Card> getHand() {
        return _hand;
    }

    public void twoGrayLMR() {
        _money += 2 * _leftNeighbor.getColorFreq().get(Card.Color.GRAY);
        _money += 2 * _colorFreq.get(Card.Color.GRAY);
        _money += 2 * _rightNeighbor.getColorFreq().get(Card.Color.GRAY);
    }

    public void oneBrownLMR() {
        _money += _leftNeighbor.getColorFreq().get(Card.Color.BROWN);
        _money += _colorFreq.get(Card.Color.BROWN);
        _money += _rightNeighbor.getColorFreq().get(Card.Color.BROWN);
    }

    public Map<Card.Color, Integer> getColorFreq() {
        return _colorFreq;
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

    public void addMilitaryVP(int militaryVP) {
        _militaryVP += militaryVP;
    }

    public int getMilitaryVP() {
        return _militaryVP;
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
            _militaryVP += age*2 + 1;
        } else if (_military < _leftNeighbor.getMilitary()) {
            _militaryVP--;
        }

        if (_military > _rightNeighbor.getMilitary()) {
            _militaryVP += age*2 + 1;
        } else if (_military < _rightNeighbor.getMilitary()) {
            _militaryVP--;
        }
    }

    public int calculateVP() {
        int vp = 0;
        vp += _militaryVP;
        vp += _money / 3;
        // calculate board
        vp += _vp;

        int minScience = _science[0];
        for (int i = 0; i < 3; i++) { // do this right later
            vp += Math.pow(_science[i], 2);
            minScience = (_science[i] < minScience) ? _science[i] : minScience;
        }
        vp += 7 * minScience;

        return vp;
    }
}