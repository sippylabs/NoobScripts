package oldschool.scripts.common.utils;

import org.powerbot.script.Filter;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {
    public static <T> List<T> where(List<T> list, Filter<T> filter) {
        ArrayList<T> results = new ArrayList<T>();
        for (T item : list) {
            if (filter.accept(item)) {
                results.add(item);
            }
        }
        return results;
    }

    public static <T> T first(List<T> list, Filter<T> filter) {
        for (T item : list) {
            if (filter.accept(item)) {
                return item;
            }
        }
        return null;
    }
}