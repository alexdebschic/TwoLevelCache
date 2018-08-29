package TwoLevelCache;

import java.util.Map;

public class StrategyLFU<KEY> extends Strategy<KEY> {
    @Override
    public void put(KEY key) {
        long Priority = 1;
        if (GetObjectsPriority().containsKey(key)) {
            Priority = GetObjectsPriority().get(key) + 1;
        }
        GetObjectsPriority().put(key, Priority);
    }

    @Override
    public KEY GetReplaceKey() {
        KEY ReplaceKey = GetObjectsPriority().firstKey();
        long MinPriority = GetObjectsPriority().get(ReplaceKey);
        for (Map.Entry<KEY, Long> entry : GetObjectsPriority().entrySet()) {
            KEY key = entry.getKey();
            Long Priority = entry.getValue();
            if (Priority < MinPriority) {
                MinPriority = Priority;
                ReplaceKey = key;
            }
        }
        return ReplaceKey;
    }
}
