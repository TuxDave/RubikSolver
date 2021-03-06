package com.tuxdave.solver.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class Algorithm implements Iterable<String> {
    private ArrayList<String> sequence = new ArrayList<String>();

    private final static String symbols = "([RLUBDFMSxyzrludbf]{1}(['2+-]{1})?)";

    /**
     * @param alg the sequence, syntax: "R L2 U'" keySensitive and spaces required
     */
    public Algorithm(String alg) {
        if (!alg.equals("")) {
            String[] s = alg.split(" ");
            for (String s1 : s) {
                if (s1.matches(symbols))
                    if (s1.charAt(0) != 'r' && s1.charAt(0) != 'l' && s1.charAt(0) != 'u' && s1.charAt(0) != 'd'
                            && s1.charAt(0) != 'f' && s1.charAt(0) != 'b')
                        if (s1.charAt(0) == 'M') {
                            if (s1.length() == 1) {
                                sequence.add("R");
                                sequence.add("L'");
                                sequence.add("x-");
                            } else {
                                sequence.add("R'");
                                sequence.add("L");
                                sequence.add("x+");
                            }
                        } else if (s1.charAt(0) == 'S') {
                            if (s1.length() == 1) {
                                sequence.add("F'");
                                sequence.add("B");
                                sequence.add("z+");
                            } else {
                                sequence.add("F");
                                sequence.add("B'");
                                sequence.add("z-");
                            }
                        } else
                            sequence.add(s1);
                    else {
                        if (s1.length() == 2 && s1.charAt(1) == '2') {
                            throw new IllegalArgumentException("transform es: y2 in y+ y+");
                        }
                        String add;
                        if (s1.length() == 1) {
                            add = "";
                        } else {
                            add = s1.charAt(1) + "";
                        }

                        switch (s1) {
                            case "r":
                                sequence.add("L");
                                sequence.add("x+");
                                break;
                            case "r'":
                                sequence.add("L'");
                                sequence.add("x-");
                                break;
                            case "l":
                                sequence.add("R");
                                sequence.add("x-");
                                break;
                            case "l'":
                                sequence.add("R'");
                                sequence.add("x+");
                                break;
                            case "f":
                                sequence.add("B");
                                sequence.add("z+");
                                break;
                            case "f'":
                                sequence.add("B'");
                                sequence.add("z-");
                                break;
                            case "b":
                                sequence.add("F");
                                sequence.add("z-");
                                break;
                            case "b'":
                                sequence.add("F'");
                                sequence.add("z+");
                                break;
                            case "u":
                                sequence.add("D");
                                sequence.add("y+");
                                break;
                            case "u'":
                                sequence.add("D'");
                                sequence.add("y-");
                                break;
                            case "d":
                                sequence.add("U");
                                sequence.add("y-");
                                break;
                            case "d'":
                                sequence.add("U'");
                                sequence.add("y+");
                                break;
                        }
                    }
                else
                    throw new IllegalArgumentException(
                            "Please respect the algorithm syntax!: " + alg + "   sym: " + s1);
            }
        }
    }

    public Algorithm() {
        this("");
    }

    public void reset() {
        sequence = new ArrayList<String>();
    }

    /**
     * @return a String like the costructor's required
     */
    public String getSequenceAsString() {
        String s = "";
        for (String s1 : sequence) {
            s += s1 + " ";
        }
        return s.substring(0, s.length() - 1);
    }

    public void add(String _move) {
        if (_move.matches(symbols)) {
            sequence.add(_move);
        } else {
            throw new IllegalArgumentException(_move + ": not allowed symbol!");
        }
    }

    /**
     * @return a String array containing each moves
     */
    public String[] getSequenceAsArray() {
        return (String[]) sequence.toArray();
    }

    /**
     * @return the number of moves
     */
    public int size() {
        return sequence.size();
    }

    /**
     * similar to size() but not includes the xyz as moves
     * 
     * @return the move number
     */
    public int getMoveLength() {
        shorten();
        int moves = 0;
        String solve = "";
        for (String temp : sequence) {
            if (temp.charAt(0) != 'y' && temp.charAt(0) != 'x' && temp.charAt(0) != 'z') {
                solve += temp + " ";
            }
        }
        try {
            solve = solve.substring(0, solve.length() - 1);
        } catch (StringIndexOutOfBoundsException ignored) {
            return 0;
        }
        for (String move : solve.split(" ")) {
            if (move.length() == 1) {
                moves++;
            } else {
                if (move.charAt(1) == '2') {
                    moves += /* 1 */2;
                } else {
                    moves++;
                }
            }
        }
        return moves;
    }

    /**
     * compare the start index with every elements till the second index included
     * 
     * @param start the index to compare with all others
     * @param _for  the index to reach (length) es: 1 = with only the next
     * @return true if the start is equals all the others
     */
    public boolean equalsThenNext(int start, int _for) {
        boolean equals = true;
        try {
            for (int i = start; i <= start + _for; i++) {
                if (!sequence.get(start).equals(sequence.get(i))) {
                    equals = false;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return equals;
    }

    public void shorten() {
        int size = size();
        Algorithm target = new Algorithm();
        int i = 0;
        String current = null;
        while (i < size) {// go untill it reach the trd last
            current = sequence.get(i);
            if (equalsThenNext(i, 3)) {
                i += 4;// -1 because the continue imcrements i
                continue;
            } else if (equalsThenNext(i, 2)) {
                if (current.length() == 2) {
                    if (current.charAt(1) == '2')
                        target.add(current);
                    else if (current.charAt(1) == '+' || current.charAt(1) == '-')
                        target.add(current.charAt(0) + (current.charAt(1) == '+' ? "-" : "+"));
                    else
                        target.add("" + current.charAt(0));
                } else {
                    target.add(current + "'");
                }
                i += 3;
                continue;
            } else if (equalsThenNext(i, 1)) {
                if (current.length() == 2) {
                    if (current.charAt(1) == '\'')
                        target.add(current.charAt(0) + "2");
                    else {
                        target.add(current);
                        target.add(current);
                    }
                } else {
                    target.add(current + "2");
                }
                i += 2;
                continue;
            } else {
                target.add(current);
                i++;
            }
        }
        // for (; i < size; i++) {
        // target.add(sequence.get(i));
        // }
        sequence = target.sequence;
    }

    /**
     * it allows to use the for-each<String> to iterate all the moves
     * 
     * @return
     */
    @Override
    public Iterator<String> iterator() {
        return sequence.iterator();
    }

    @Override
    public String toString() {
        shorten();
        String s = "";
        int size1 = size();
        for (int i = 0; i < size1; i++) {
            s += sequence.get(i) + (i == size1 - 1 ? "" : " ");
        }
        return s;
    }
}
