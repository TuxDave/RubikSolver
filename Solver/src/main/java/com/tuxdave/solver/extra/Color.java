package com.tuxdave.solver.extra;

public enum Color {
    WHITE(0), BLUE(1), YELLOW(2), GREEN(3), ORANGE(4), RED(5);

    private int value;

    private Color(int v) {
        value = v;
    }

    public int getValue() {
        return value;
    }

    public static Color getColorByNumber(int n) {
        if (n >= 0 && n <= 5) {
            for (Color p : Color.values()) {
                if (n == p.getValue()) {
                    return p;
                }
            }
        } else {
            throw new IllegalArgumentException("n is not in range 0-5");
        }
        return null;
    }
}
