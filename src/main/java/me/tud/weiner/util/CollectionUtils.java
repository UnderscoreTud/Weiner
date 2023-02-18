package me.tud.weiner.util;

import java.util.*;

public class CollectionUtils {

    @SafeVarargs
    public static <E> List<E> listOfNonNullables(E... elements) {
        List<E> list = new ArrayList<>(elements.length);
        for (E element : elements) {
            if (element != null)
                list.add(element);
        }
        return list;
    }

    public static <E> List<E> listOfNonNullables(List<E> list) {
        List<E> newList = new ArrayList<>(list.size());
        for (E element : list) {
            if (element != null)
                newList.add(element);
        }
        return newList;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] merge(T[]... arrays) {
        List<T> list = new LinkedList<>();
        for (T[] array : arrays)
            Collections.addAll(list, array);
        return (T[]) list.toArray(new Object[0]);
    }

}
