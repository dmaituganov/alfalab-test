package org.dmaituganov.alfalab.test.task2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class Grouper {
    public static <T extends NamedObject> Map<String, Collection<T>> groupByName(Collection<T> objects) {
        return objects.stream().collect(Collectors.groupingBy(
            NamedObject::getName,
            Collectors.toCollection(ArrayList::new)
        ));
    }
}
