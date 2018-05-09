package com.neeraj.sudoku;

import java.util.*;

public class SudokuSolver {
    static Grid solve(Grid g){
        Grid gCpy1,solution;
        int r,c,minLocRow=-1,minLocColumn=-1;
        ArrayList<Integer> minConstraints=null,constraints;
        if(g.fillCertainLocations()==false){
            return null;
        }
        if(g.getNumFilledCells()==81){
            return g;       /*solution found*/
        }
        for(r=0;r<9;r++){               //get location with minimum
            for(c=0;c<9;c++){           //number of constraints
                if(g.isLocEmpty(r, c)){
                    constraints=g.getConstraintsAtLoc(r, c);
                    if(minConstraints==null){
                        minConstraints=constraints;
                        minLocRow=r;
                        minLocColumn=c;
                    }
                    else if(constraints.size()<minConstraints.size()){
                        minConstraints=constraints;
                        minLocRow=r;
                        minLocColumn=c;
                    }
                }
            }
        }
        for(int val:minConstraints){
            /*fill val into grid's copy and recurse*/
            gCpy1=g.copy();
            gCpy1.fastInsert(val, minLocRow, minLocColumn);    
            solution=SudokuSolver.solve(gCpy1);
            if(solution!=null){
               return solution; 
            }
        }
        return null;
    }
    static Grid randomizedSolve(Grid g){
        Grid gCpy1,solution;
        Random random = new Random();
        int r,c,minLocRow=-1,minLocColumn=-1,i,val;
        ArrayList<Integer> minConstraints=null,constraints;
        if(g.fillCertainLocations()==false){
            return null;
        }
        if(g.getNumFilledCells()==81){
            return g;
        }
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                if(g.isLocEmpty(r, c)){
                    constraints=g.getConstraintsAtLoc(r, c);
                    if(minConstraints==null){
                        minConstraints=constraints;
                        minLocRow=r;
                        minLocColumn=c;
                    }
                    else if(constraints.size()<minConstraints.size()){
                        minConstraints=constraints;
                        minLocRow=r;
                        minLocColumn=c;
                    }
                }
            }
        }
        /*the code below makes this function
        different from the solve function.
        the value to be inserted is selected in 
        randomized order from the minConstraints
        ArrayList
        */
        while(minConstraints.size()>0){
            i = random.nextInt(minConstraints.size());
            val = minConstraints.get(i);
            minConstraints.remove(i);
            gCpy1=g.copy();
            gCpy1.fastInsert(val, minLocRow, minLocColumn);
            solution=SudokuSolver.solve(gCpy1);
            if(solution!=null){
               return solution; 
            }
        }
        return null;
    }
    static Grid generateRandomSudoku(double fillFraction){
        /*fills an empty grid with random values 
        and empties sum of the locations so that
        numFilledLocations/81 is approx. equal to
        fillFraction
        */
        Grid g = new Grid();
        Random random = new Random();
        g=SudokuSolver.randomizedSolve(g);
        for(int r=0;r<9;r++){
            for(int c=0;c<9;c++){
                if(random.nextDouble()<(1-fillFraction)){
                    g.clearCell(g.getCellAtLoc(r, c));
                }
            }
        }
        return g;
    }
}