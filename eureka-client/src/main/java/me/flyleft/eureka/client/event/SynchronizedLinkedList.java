package me.flyleft.eureka.client.event;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.function.UnaryOperator;

public class SynchronizedLinkedList<E> extends LinkedList<E> {

    private final transient LinkedList<E> list;

    private final transient Object mutex;

    SynchronizedLinkedList(LinkedList<E> list) {
        this.list = list;
        mutex = this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        synchronized (mutex) {
            return list.equals(o);
        }
    }

    @Override
    public int hashCode() {
        synchronized (mutex) {
            return list.hashCode();
        }
    }

    @Override
    public E get(int index) {
        synchronized (mutex) {
            return list.get(index);
        }
    }

    @Override
    public E set(int index, E element) {
        synchronized (mutex) {
            return list.set(index, element);
        }
    }

    @Override
    public boolean add(E e) {
        synchronized (mutex) {
            return list.add(e);
        }
    }



    @Override
    public void add(int index, E element) {
        synchronized (mutex) {
            list.add(index, element);
        }
    }

    @Override
    public E remove(int index) {
        synchronized (mutex) {
            return list.remove(index);
        }
    }

    @Override
    public boolean remove(Object o) {
        synchronized (mutex) {
            return list.remove(o);
        }
    }

    @Override
    public int indexOf(Object o) {
        synchronized (mutex) {
            return list.indexOf(o);
        }
    }

    @Override
    public int lastIndexOf(Object o) {
        synchronized (mutex) {
            return list.lastIndexOf(o);
        }
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        synchronized (mutex) {
            return list.addAll(index, c);
        }
    }


    @Override
    public E getFirst() {
        synchronized (mutex) {
            return list.getFirst();
        }
    }

    @Override
    public E getLast() {
        synchronized (mutex) {
            return list.getLast();
        }
    }

    @Override
    public E removeFirst() {
        synchronized (mutex) {
            return list.removeFirst();
        }
    }

    @Override
    public E removeLast() {
        synchronized (mutex) {
            return list.removeLast();
        }
    }

    @Override
    public void addFirst(E e) {
        synchronized (mutex) {
            list.addFirst(e);
        }
    }

    @Override
    public void addLast(E e) {
        synchronized (mutex) {
            list.addLast(e);
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        synchronized (mutex) {
            list.replaceAll(operator);
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        synchronized (mutex) {
            list.sort(c);
        }
    }

    @Override
    public Object[] toArray() {
       return list.toArray();
    }

}
