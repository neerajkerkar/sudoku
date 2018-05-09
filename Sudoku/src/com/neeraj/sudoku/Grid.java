package com.neeraj.sudoku;

import java.util.*;

public class Grid {
    private int numFilledCells=0;
    private Cell[][] cells;
    Grid(){
        int i,j;
        cells = new Cell[9][];
        for(i=0;i<9;i++){
            cells[i] = new Cell[9];
            for(j=0;j<9;j++){
                cells[i][j]=new Cell(i,j);
            }
        }
    }
    Cell getCellAtLoc(int row,int column){
        return cells[row][column];
    }
    boolean isLocEmpty(int row,int column){
        return cells[row][column].isEmpty();
    }
    
    int getValueAtLoc(int row,int column) throws InvalidQueryException{
        return cells[row][column].getValue();
    }
    Grid copy(){
        int r,c;
        Grid cpy = new Grid();
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                try{
                    cpy.fastInsert(this.getValueAtLoc(r, c), r, c);
                }
                catch(Exception ex){
                    /*cell at location (r,c) is empty*/
                }
            }
        }
        return cpy;
    }
    ArrayList<Integer> getConstraintsAtLoc(int row,int column){
        /*constraints at a location are the possible values that
        the location can be filled with
        */
        int i,val,c,r;
        ArrayList<Integer> constraints = new ArrayList<Integer>();
        boolean[] isConstraint = new boolean[10];
        for(val=1;val<=9;val++){
            isConstraint[val]=true;
        }
        int startingRowIndex=row/3*3;        //indices of the topleft cell of
        int startingColumnIndex=column/3*3;  //3x3 grid containing (row,column)
        for(r=0;r<9;r++){
            try{
               val=cells[r][column].getValue();
               isConstraint[val]=false;
            }
            catch(Exception ex){
                /*cell at position (r,column) is empty*/
            }
        }
        for(c=0;c<9;c++){
            try{
                val=cells[row][c].getValue();
                isConstraint[val]=false;
            }
            catch(Exception ex){
                /*cell at position (row,c) is empty*/
            }
        }
        for(r=startingRowIndex;r<(startingRowIndex+3);r++){
            for(c=startingColumnIndex;c<(startingColumnIndex+3);c++){
                try{
                    val=cells[r][c].getValue();
                    isConstraint[val]=false;
                }
                catch(Exception ex){
                    /*cell at position (r,c) is empty*/
                }
            }
        }
        for(val=1;val<=9;val++){
            if(isConstraint[val]){
                constraints.add(val);
            }
        }
        return constraints;
    }
    int getNumFilledCells(){
        return numFilledCells;
    }
    int[] getCellLocation(Cell c){
        int[] indices = new int[2];
        indices[0]=c.getRow();
        indices[1]=c.getColumn();
        return indices;
    }
    boolean fillCertainLocations(){
        /*fills all the locations which have single constraints
        returns true if successfull and false if a hole is found
        hole : it is a location that has 0 constraints (thus making
        the grid invalid)
        */
        boolean flag=true;
        int r,c;
        ArrayList<Integer> constraints;
        while(flag==true){
            flag=false;
            for(r=0;r<9;r++){
                for(c=0;c<9;c++){
                    if(cells[r][c].isEmpty()){
                        constraints=this.getConstraintsAtLoc(r,c);
                        if(constraints.size()==0){
                            return false;  /*hole found*/
                        }
                        if(constraints.size()==1){
                            try{
                                this.fastInsert(constraints.get(0), r, c);
                                flag=true;
                            }
                            catch(Exception ex){
                                /*empty*/
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    void findErrorZones(){
        /*marks all locations with invalid values as red*/
        boolean[][] errorZones = new boolean[9][9];
        int r,c;
        boolean[] isSeen;
        boolean[] isNumInErrorZone;
        int val;
        for(r=0;r<9;r++){
            isSeen = new boolean[10];
            isNumInErrorZone = new boolean[10];
            for(c=0;c<9;c++){
                /*scans for repeated values in a row r*/
                try{
                    val=cells[r][c].getValue();
                    if(isSeen[val]){
                        isNumInErrorZone[val]=true;
                    }
                    else{
                        isSeen[val]=true;
                    }
                }
                catch(Exception ex){
                    //empty
                }
            }
            for(c=0;c<9;c++){
                /*marks locations with invalid values in errorZone matrix*/
                try{
                    val=cells[r][c].getValue();
                    if(isNumInErrorZone[val]){
                        errorZones[r][c]=true;
                    }
                }
                catch(Exception ex){
                    val=0;
                    //empty
                }
            }
        }
        for(c=0;c<9;c++){
            isSeen = new boolean[10];
            isNumInErrorZone = new boolean[10];
            for(r=0;r<9;r++){
                /*scans for repeated values in column c*/
                try{
                    val=cells[r][c].getValue();
                    if(isSeen[val]){
                        isNumInErrorZone[val]=true;
                    }
                    else{
                        isSeen[val]=true;
                    }
                }
                catch(Exception ex){
                    //empty
                }
            }
            for(r=0;r<9;r++){
                try{
                    /*marks invalid locations in errorZone matrix*/
                    val=cells[r][c].getValue();
                    if(isNumInErrorZone[val]){
                        errorZones[r][c]=true;
                    }
                }
                catch(Exception ex){
                    //empty
                }
            }
        }
        int init_row;
        int init_col;
        for(init_row=0;init_row<9;init_row+=3){         //scans for repeated values
            for(init_col=0;init_col<9;init_col+=3){     //in 3x3 grids
                isSeen = new boolean[10];
                isNumInErrorZone = new boolean[10];
                for(r=init_row;r<(init_row+3);r++){
                    for(c=init_col;c<(init_col+3);c++){
                        try{
                            val=cells[r][c].getValue();
                            if(isSeen[val]){
                                isNumInErrorZone[val]=true;
                            }
                            else{
                                isSeen[val]=true;
                            }
                        }
                        catch(Exception ex){
                            //empty
                        }
                    }
                }
                for(r=init_row;r<(init_row+3);r++){
                    for(c=init_col;c<(init_col+3);c++){
                        try{
                            val=cells[r][c].getValue();
                            if(isNumInErrorZone[val]){
                                errorZones[r][c]=true;
                            }
                        }
                        catch(Exception ex){
                            //empty
                        }
                    }
                }
            }
        }
        /*all the locations marked in errorZone matrix are 
        coloured red below*/
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                if(errorZones[r][c]){
                    cells[r][c].markAsRed();
                }
                else{
                    cells[r][c].unmarkAsRed();
                }
            }
        }
    }
    void insert(int val,int row,int col){
        /*inserts val at specified row and col
        and also marks invalid locations as red*/
        this.fastInsert(val, row, col);
        this.findErrorZones();
    }
    void fastInsert(int val,int row,int col){
        try{
            if(cells[row][col].isEmpty()){
                this.numFilledCells++;
            }
            cells[row][col].setValue(val);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    void copyValuesOfGrid(Grid g){
        int r,c;
        this.clearGrid();
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                try{
                    this.fastInsert(g.getValueAtLoc(r, c), r, c);
                }
                catch(Exception ex){
                    //empty
                }
            }
        }
    }
    boolean isValidPuzzle(){
        int r,c;
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                if(cells[r][c].isRedZone()){
                    return false;
                }
            }
        }
        return true;
    }
    void clearGrid(){
        int r,c;
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                cells[r][c].clear();
            }
        }
        this.numFilledCells=0;
    }
    void clearCell(Cell c){
        if(this.cells[c.getRow()][c.getColumn()]!=c){
            return ;
        }
        if(!c.isEmpty()){
            c.clear();
            this.numFilledCells--;
        }
    }
    void fillRandomValues(double fillFraction){
        /*fills the grid with random values
        numFilledCells/81 is approx. fillFraction
        */ 
        this.clearGrid();
        int r,c;
        Cell cell;
        int val;
        Grid filledGrid = SudokuSolver.generateRandomSudoku(fillFraction);
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                cell = filledGrid.getCellAtLoc(r, c);
                try{
                    val = cell.getValue();
                    this.fastInsert(val, r, c);
                    cells[r][c].setModifiability(false);
                }
                catch(Exception ex){
                    //cell is empty
                    cells[r][c].setModifiability(true);
                }
            }
        }
    }
    void restoreOriginal(){
        int r,c;
        for(r=0;r<9;r++){
            for(c=0;c<9;c++){
                if(cells[r][c].isModifiable()){
                    this.clearCell(cells[r][c]);
                }
            }
        }
    }
    boolean isFilled(){
        return this.isValidPuzzle()&&(this.numFilledCells==81);
    }
}