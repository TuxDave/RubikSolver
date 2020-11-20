package com.tuxdave.solver.extra;

/**Logs cube movement
 * @author TuxDave 
 * MoveListener
 */
public interface MoveListener {

    /**
     * called on every move of the linked cube
     */
    public void onMove(char move, boolean cw);
}