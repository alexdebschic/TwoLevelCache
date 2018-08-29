package TwoLevelCache;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MemoryLevelCache<KEY, VALUE> implements Cache<KEY, VALUE>  {
    private Map<KEY, VALUE> CacheObjects;
    private int MaxSize;

    MemoryLevelCache(int size) {
        this.CacheObjects = new HashMap<KEY, VALUE>(size);
        this.MaxSize = size;
    }

    @Override
    public void put(KEY key, VALUE value) {
        CacheObjects.put(key, value);
    }

    @Override
    public VALUE get(KEY key){
        return CacheObjects.get(key);
    }

    @Override
    public void remove(KEY key){
        for(Iterator<Map.Entry<KEY, VALUE>> it = CacheObjects.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<KEY, VALUE> entry = it.next();
            if (entry.getKey().equals(key)) {
                it.remove();
            }
        }
    }

    @Override
    public void clear(){
        CacheObjects.clear();
    }

    @Override
    public int size(){
        return CacheObjects.size();
    }

    @Override
    public boolean ContainsKey(KEY key) {return CacheObjects.containsKey(key);}

    @Override
    public boolean IsFreeSpace() {
        return size() < MaxSize;
    }

}
