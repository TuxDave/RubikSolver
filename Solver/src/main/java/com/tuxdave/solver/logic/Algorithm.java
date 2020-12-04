package com.tuxdave.solver.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Algorithm implements Iterable<String> {
    private ArrayList<String> sequence = new ArrayList<String>();

    private final static String symbols = "([RLUBDFxyz]{1}(['2+-]{1})?)";

    /**
     * @param alg the sequence, syntax: "R L2 U'" keySensitive and spaces required
     */
    public Algorithm(String alg) {
        if (!alg.equals("")) {
            String[] s = alg.split(" ");
            for (String s1 : s) {
                if (s1.matches(symbols))
                    sequence.add(s1);
                else
                    throw new IllegalArgumentException("Please respect the algorithm syntax!");
            }
        }
    }

    public Algorithm() {
        this("");
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
     * compare the start index with every elements till the second index included
     * 
     * @param start the index to compare with all others
     * @param _for  the index to reach (length) es: 1 = with only the next
     * @return true if the start is equals all the others
     */
    public boolean equalsThenNext(int start, int _for) {
        boolean equals = true;
        for (int i = start; i <= start + _for; i++) {
            if (!sequence.get(start).equals(sequence.get(i))) {
                equals = false;
            }
        }
        return equals;
    }

    public void shorten() {
        int size = size();
        Algorithm target = new Algorithm();
        int i = 0;
        String current = null;
        while (i <= size - 4) {// go untill it reach the trd last
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
        String s = "";
        int size1 = size();
        for (int i = 0; i < size1; i++) {
            s += sequence.get(i) + (i == size1 - 1 ? "" : " ");
        }
        // shorten();
        s = "";
        size1 = size();
        for (int i = 0; i < size1; i++) {
            s += sequence.get(i) + (i == size1 - 1 ? "" : " ");
        }
        return s;
    }
}
