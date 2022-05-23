package com.techeer.f5.jmtmonster.domain.review.domain;

public interface Updatable<T> {
    T getColumn();
    void update(T column);
}
