package com.tuxdave.solver.extra;

public enum Position {
    FRONT("front"), UP("up"), BACK("back"), DOWN("down"), RIGHT("right"), LEFT("left");

    private String value;

    private Position(String s) {
        value = s;
    }

    public String getValue() {
        return value;
    }
}
