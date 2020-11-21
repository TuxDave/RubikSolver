package com.tuxdave.solver.logic;

import java.util.ArrayList;
import java.util.Iterator;

public class Algorithm implements Iterable<String> {
    private ArrayList<String> sequence = new ArrayList<String>();

    private final static String symbols = "([RLUBDF]{1}(['2]{1})?)";

    /**
     * @param alg the sequence, syntax: "R L2 U'" keySensitive and spaces required
     */
    public Algorithm(String alg) {
        String[] s = alg.split(" ");
        for (String s1 : s) {
            if (s1.matches(symbols))
                sequence.add(s1);
            else
                throw new IllegalArgumentException("Please respect the algorithm syntax!");
        }
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

    /**
     * @return a String array containing each moves
     */
    public String[] getSequenceAsArray() {
        return (String[]) sequence.toArray();
    }

    /**
     * it allows to use the for-each<String> to iterate all the moves
     * @return
     */
    @Override
    public Iterator<String> iterator() {
        return sequence.iterator();
    }
}
