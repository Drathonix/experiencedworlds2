package com.drathonix.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GlobalEvents {
    private static Map<Class<?>, List<Consumer<Object>>> listeners = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> void addListener(Class<T> clazz, Consumer<T> listener) {
        List<Consumer<Object>> list = listeners.computeIfAbsent(clazz,(k)->new ArrayList<>());
        list.add((Consumer<Object>) listener);
    }

    public static void post(Object event) {
        List<Consumer<Object>> consumers = listeners.getOrDefault(event.getClass(),new ArrayList<>());
        for (Consumer<Object> consumer : consumers) {
            consumer.accept(event);
        }
    }
}
