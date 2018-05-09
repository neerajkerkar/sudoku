package com.neeraj.sudoku;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.Border;

public class Cell extends JPanel {
    private int value=-1;
    private JLabel label;
    private int row;
    private int col;
    static Color color = Color.WHITE;
    static Color highlightColor = Color.YELLOW;
    static Color mouseHoverColor = Color.LIGHT_GRAY;
    static Color errorZoneColor = Color.RED;
    static String fontName = "Serif";
    static Color mouseInErrorZoneColor = Color.ORANGE;
    private boolean inErrorZone=false;
    private boolean mouseInCell=false;
    static int fontSize=25;
    private boolean modifiable = true;
    Cell(int row,int col){
        this.row=row;
        this.col=col;
        this.setBackground(Cell.color);
        label=new JLabel(" ");
        label.setFont(new Font(Cell.fontName, Font.PLAIN, Cell.fontSize));
        this.add(label);
        Border lineBorder = BorderFactory.createLineBorder(Color.gray);
        Border emptyBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        Border compound = BorderFactory.createCompoundBorder(
                lineBorder, emptyBorder);
        this.setBorder(compound);
        Dimension panelDimension = new Dimension();
        double maxDimension;
        maxDimension = this.getPreferredSize().getHeight();
        if(this.getPreferredSize().getWidth()>maxDimension){
            maxDimension = this.getPreferredSize().getWidth();
        }
        panelDimension.setSize(maxDimension, maxDimension);
        this.setPreferredSize(panelDimension);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    boolean isEmpty(){
        return value==-1;
    }
    void setValue(int val) throws InvalidDataException {
        /*sets the value of the cell to val,throws
        exception if val is out of valid range
        valid range : integers 1->9
        */
        if(val<1 || val>9){
            throw new InvalidDataException();
        }
        value=val;
        label.setText(String.valueOf(val));
        if(mouseInCell){
            if(this.inErrorZone){
                this.setBackground(Cell.mouseInErrorZoneColor);
            }
            else{
                this.setBackground(Cell.mouseHoverColor);
            }
        }
        else{
            if(this.inErrorZone){
                this.setBackground(Cell.errorZoneColor);
            }
            else{
                this.setBackground(Cell.color);
            }
        }
    }
    int getValue() throws InvalidQueryException {
        /*returns the value stored in the cell,
        throws exception if cell is empty
        */
        if(this.isEmpty()){
            throw new InvalidQueryException();
        }
        return value;
    }
    int getRow(){
        return this.row;
    }
    int getColumn(){
        return this.col;
    }
    boolean isHighlighted(){
        return this.getBackground().equals(Cell.highlightColor);
    }
    void highlight(){
        this.setBackground(Cell.highlightColor);
    }
    void deHighlight(){
        if(this.inErrorZone){
            this.setBackground(Cell.errorZoneColor);
        }
        else{
            this.setBackground(Cell.color);
        }
    }
    
    void mouseEnteredCell(){
        this.mouseInCell=true;
        if(this.isHighlighted()){
            return ;
        }
        if(this.inErrorZone){
            this.setBackground(Cell.mouseInErrorZoneColor);
        }
        else{
            this.setBackground(Cell.mouseHoverColor);
        }
    }
    
    void mouseExitedCell(){
        this.mouseInCell=false;
        if(this.isHighlighted()){
            return ;
        }
        if(this.inErrorZone){
            this.setBackground(Cell.errorZoneColor);
        }
        else{
            this.setBackground(Cell.color);
        }
    }
    void clear(){
        value=-1;
        label.setText(" ");
        this.unmarkAsRed();
        this.setModifiability(true);
    }
    void markAsRed(){
        this.inErrorZone=true;
        if(this.mouseInCell){
            this.setBackground(Cell.mouseInErrorZoneColor);
        }
        else{
            this.setBackground(Cell.errorZoneColor);
        }
    }
    void unmarkAsRed(){
        this.inErrorZone=false;
        if(this.mouseInCell){
            this.setBackground(Cell.mouseHoverColor);
        }
        else{
            this.setBackground(Cell.color);
        }
    }
    boolean isRedZone(){
        return this.inErrorZone;
    }
    boolean isModifiable(){
        return this.modifiable;
    }
    void setModifiability(boolean canModify){
        this.modifiable=canModify;
        if(canModify==true){
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            this.label.setFont(new Font(Cell.fontName, Font.PLAIN, Cell.fontSize));
        }
        else{
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            this.label.setFont(new Font(Cell.fontName, Font.BOLD, Cell.fontSize));
        }
    }
}