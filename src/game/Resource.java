package game;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eric on 1/24/17.
 */
public class Resource {

    public enum Type {
        CLAY, ORE, STONE, WOOD, GLASS, LOOM, PAPYRUS
    }

    private Set<Type> _types;

    public Resource() {
        _types = new HashSet<Type>();
    }

    public void addType(Type type) {
        _types.add(type);
    }

    public Set<Type> getTypes() {
        return _types;
    }

//    @Override
//    public int hashCode() {
//        String ughh = new String();
//
//    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Resource)) {
            return false;
        }

        Resource other = (Resource) o;
        for (Type t : other.getTypes()) {
            if (_types.contains(t)) {
                return true;
            }
        }
        return false;
    }
}
