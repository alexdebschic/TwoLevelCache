package TwoLevelCache;

import java.util.TreeMap;

public abstract class Strategy<KEY> {
    private TreeMap<KEY, Long> ObjectsPriority;

    Strategy() {
        this.ObjectsPriority = new TreeMap<KEY, Long>();
    }

    public abstract void put(KEY key);

    public abstract KEY GetReplaceKey();

    public void remove(KEY key) {
        if (ContainsKey(key)) {
            ObjectsPriority.remove(key);
        }
    }

    public boolean ContainsKey(KEY key) {return ObjectsPriority.containsKey(key);}

    public void clear() {ObjectsPriority.clear();}

    public TreeMap<KEY,Long> GetObjectsPriority(){return ObjectsPriority;}

}
