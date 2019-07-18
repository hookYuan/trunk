package yuan.core.cache;

/**
 * Created by wanglei on 2016/11/27.
 */

public interface ICache {
    public void put(String key, Object value);

    public Object get(String key);

    public void remove(String key);

    public boolean contains(String key);

    public void clear();

}
