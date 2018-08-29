package TwoLevelCache;

import java.io.Serializable;

public class TwoLevelCache<KEY,VALUE extends Serializable> implements Cache<KEY,VALUE> {
    private MemoryLevelCache<KEY, VALUE> FirstLevelCache;
    private FileSystemCache<KEY, VALUE> SecondLevelCache;
    private Strategy<KEY> CacheStrategy;

    public TwoLevelCache(int MemorySize, int FileSystemSize, int StrategyFlag){
        this.FirstLevelCache = new MemoryLevelCache<KEY, VALUE>(MemorySize);
        this.SecondLevelCache = new FileSystemCache<KEY, VALUE>(FileSystemSize);
        this.CacheStrategy = StrategyType(1);
    }

    private Strategy<KEY> StrategyType(int StrategyName) {
        switch (StrategyName) {
            case 1:
                return new StrategyLFU<KEY>();
            default:
                return  new StrategyLFU<KEY>();
        }
    }

    @Override
    public void put(KEY key, VALUE value){
        if (FirstLevelCache.ContainsKey(key) || FirstLevelCache.IsFreeSpace()) {
            FirstLevelCache.put(key,value);
            System.out.println("Добавление объекта в кеш 1-го уровня");
        } else if (SecondLevelCache.ContainsKey(key) || SecondLevelCache.IsFreeSpace()) {
            SecondLevelCache.put(key,value);
            System.out.println("Добавление объекта в кеш 2-го уровня");
        } else {
            ReplaceObject(key, value);
        }

        if (!CacheStrategy.ContainsKey(key)) {
            CacheStrategy.put(key);
        }
    }

    public void ReplaceObject(KEY key, VALUE value) {
        KEY ReplaceKey = CacheStrategy.GetReplaceKey();
        if (FirstLevelCache.ContainsKey(ReplaceKey)) {
            FirstLevelCache.remove(ReplaceKey);
            FirstLevelCache.put(key, value);
            System.out.println("Замена объекта в кеше 1-го уровня");
        } else if (SecondLevelCache.ContainsKey(ReplaceKey)){
            SecondLevelCache.remove(ReplaceKey);
            SecondLevelCache.put(key,value);
            System.out.println("Замена объекта в кеше 2-го уровня");
        }

        CacheStrategy.remove(ReplaceKey);
    }

    @Override
    public VALUE get(KEY key){
        if (FirstLevelCache.ContainsKey(key)) {
            CacheStrategy.put(key);
            System.out.println("Объект находится в кеше 1-го уровня");
            return FirstLevelCache.get(key);
        } else if (SecondLevelCache.ContainsKey(key)) {
            CacheStrategy.put(key);
            System.out.println("Объект находится в кеше 2-го уровня");
            return SecondLevelCache.get(key);
        }

        System.out.println("Объект отсутствует в кеше");
        return null;
    }

    @Override
    public void remove(KEY key){
        if (FirstLevelCache.ContainsKey(key)) {
            FirstLevelCache.remove(key);
            System.out.println("Объект удалён из кеша 1-го уровня");
        }else if (SecondLevelCache.ContainsKey(key)) {
            SecondLevelCache.remove(key);
            System.out.println("Объект удалён из кеша 2-го уровня");
        }

        CacheStrategy.remove(key);
    }

    @Override
    public void clear(){
        FirstLevelCache.clear();
        SecondLevelCache.clear();
        CacheStrategy.clear();
    }

    @Override
    public int size(){
        return FirstLevelCache.size() + SecondLevelCache.size();
    }

    @Override
    public boolean ContainsKey(KEY key) {
        return FirstLevelCache.ContainsKey(key) || SecondLevelCache.ContainsKey(key);
    }

    @Override
    public boolean IsFreeSpace(){
        return FirstLevelCache.IsFreeSpace() || SecondLevelCache.IsFreeSpace();
    }

}
