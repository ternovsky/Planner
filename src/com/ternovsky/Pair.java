package com.ternovsky;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 03.11.12
 * Time: 21:38
 * To change this template use File | Settings | File Templates.
 */
public class Pair<T, U> {

    private T first;
    private U second;

    public Pair() {
    }

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public U getSecond() {
        return second;
    }

    public void setSecond(U second) {
        this.second = second;
    }
}
