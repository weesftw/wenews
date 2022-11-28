package com.weesftw.common.chain;

public enum SubscriberPhase {

    FIRST(-1000, -11337, -720),
    SECURITY(19000, 18337, 19200),
    LAST(29000, 28337, 59200);

    private final int order;
    private final int before;
    private final int after;

    SubscriberPhase(int order, int before, int after) {
        this.order = order;
        this.before = before;
        this.after = after;
    }

    public int order() {
        return order;
    }

    public int before() {
        return before;
    }

    public int after() {
        return after;
    }
}