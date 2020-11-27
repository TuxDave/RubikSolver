package com.tuxdave.solver.extra;

/**
 * Logs cube movement
 * 
 * @author TuxDave MoveListener
 */
public interface MoveListener {

    /**
     * called on every move of the linked cube
     */
    public void onMove(char move, boolean cw);

    /**
     * called on every reOrientation
     * 
     * @param direction the direction r+/r-/u+/u-/f+/f-
     */
    public void onRotate(String direction);
}