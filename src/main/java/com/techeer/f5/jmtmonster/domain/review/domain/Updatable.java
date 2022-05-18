package com.techeer.f5.jmtmonster.domain.review.domain;

import javax.transaction.Transactional;

public interface Updatable<T> {
    T getColumn();
    void update(T column);
}
