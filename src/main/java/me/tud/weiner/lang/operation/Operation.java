package me.tud.weiner.lang.operation;

public interface Operation<L, R, T> {

    T calculate(L left, R right);

}
