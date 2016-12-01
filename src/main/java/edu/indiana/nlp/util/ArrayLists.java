package edu.indiana.nlp.util;

import java.util.ArrayList;
import java.util.List;

public class ArrayLists {
    /**
     * Adds element to given List, or instantiates a new ArrayList if it doesn't exist
     *
     * @param list  List to add to, may be null
     * @param toAdd Element to add
     * @return List with element added
     */
    public static <T> List<T> add(List<T> list, T toAdd) {
        if (list == null) list = new ArrayList<>();
        list.add(toAdd);
        return list;
    }
}
