package com.weesftw.common.chain;

import java.sql.SQLException;

public interface SubscriberFilter<T> {

    default int getOrder() { return 0; }
    void doFilter(T message) throws SQLException;
}
