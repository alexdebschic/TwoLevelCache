package TwoLevelCache;

public interface Cache <KEY, VALUE>{
    void put(KEY key, VALUE value);

    VALUE get(KEY key);

    void remove(KEY key);

    void clear();

    int size();

    boolean ContainsKey(KEY key);

    boolean IsFreeSpace();
}
