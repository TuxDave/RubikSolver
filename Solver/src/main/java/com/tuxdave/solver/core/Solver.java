package com.tuxdave.solver.core;

public class Solver {
    Cube core;

    public Cube getCoreCube(){
        return core;
    }
    public void setCoreCube(Cube _new){
        this.core = _new;
    }

    /**
     * construct the Solver
     * @param _c the cube to solve
     */
    public Solver(Cube _c){
        setCoreCube(_c);
    }
}
