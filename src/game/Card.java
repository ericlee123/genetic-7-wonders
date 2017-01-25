package game;

import ai.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eric on 1/24/17.
 */
public class Card {

    public enum Color {
        GRAY, BROWN, RED, BLUE, GREEN, YELLOW
    }

    private Color _color;
    private String _name;
    private List<Resource> _cost;
    private int _moneyCost;
    private Set<String> _freeRequirements;
    private Effect _effect;

    public Card() {
        _name = null;
        _cost = new ArrayList<Resource>();
        _moneyCost = 0;
        _freeRequirements = new HashSet<String>();
        _effect = null;
    }

    public void setColor(Color color) {
        _color = color;
    }

    public Color getColor() {
        return _color;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public void setCost(List<Resource> cost) {
        _cost = cost;
    }

    public List<Resource> getCost() {
        return _cost;
    }

    public void setMoneyCost(int moneyCost) {
        _moneyCost = moneyCost;
    }

    public int getMoneyCost() {
        return _moneyCost;
    }

    public void setFreeRequirements(Set<String> freeRequirements) {
        _freeRequirements = freeRequirements;
    }

    public Set<String> getFreeRequirements() {
        return _freeRequirements;
    }

    public void setEffect(Effect effect) {
        _effect = effect;
    }

    public void affect(Player player) {
        _effect.doThing(player);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Card)) {
            return false;
        }
        Card other = (Card) o;
        return _name.equals(other.getName());
    }

    public interface Effect {
        void doThing(Player p);
    }
}
