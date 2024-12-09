package com.drathonix.experiencedworlds.common.fairness;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * An extension of the HashSet class that also maintains an ArrayList of any object.
 * @param <T>
 */
public class ArrayHashSet<T> extends HashSet<T> implements List<T>{
    private final ArrayList<T> list = new ArrayList<>();
    @Override
    public boolean add(T t) {
        if(super.add(t)){
            list.add(t);
            return true;
        }
        return false;
    }

    @Override
    public boolean remove(Object o) {
        if(super.remove(o)){
            list.remove(o);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> c) {
        for (T t : c) {
            add(t);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        list.addAll(index, c);
        return super.addAll(c);
    }

    @Override
    public T get(int index) {
        return list.get(index);
    }

    @Override
    public T set(int index, T element) {
        T t = list.set(index, element);
        super.remove(t);
        return t;
    }

    @Override
    public void add(int index, T element) {
        if(super.add(element)) {
            list.add(index, element);
        }

    }

    @Override
    public T remove(int index) {
        T t = list.remove(index);
        super.remove(t);
        return t;
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<T> listIterator(int index) {
        return list.listIterator(0);
    }

    @NotNull
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
}
