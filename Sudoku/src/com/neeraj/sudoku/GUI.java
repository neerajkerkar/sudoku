package com.neeraj.sudoku;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.Border;
import java.io.*;

public class GUI {
    Grid grid = new Grid();
    JFrame frame = new JFrame("Sudoku");
    JPanel backgroundPanel;
    Cell cellSelected=null;
    MyMouseClickListener mouseListener = new MyMouseClickListener();
    MyKeyListener keyListener = new MyKeyListener();
    JLabel message = new JLabel("Select a square");
    private boolean finished = false;
    private double fillFraction = 0.3;
    private long elapsedTimeInSeconds=0;
    JLabel time = new JLabel("Timer: 0:0");
    JTextField nameField;
    private javax.swing.Timer timer;
    File scores;
    GUI(){
        /*sets up the frame and empty background panel*/
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addKeyListener(keyListener);
        backgroundPanel = new JPanel();
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        frame.getContentPane().add(backgroundPanel);
        backgroundPanel.setLayout(new BoxLayout(backgroundPanel,BoxLayout.Y_AXIS));
        timer=new javax.swing.Timer(1000,new TimerListener());
        time.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameField = new JTextField(25);
        nameField.setFont(new Font("serif",Font.PLAIN,25));
        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE,nameField.getPreferredSize().height+2));
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        scores = new File("scores.ser");
    }
    void buildStartGUI(){
        /*creates the main menu*/
        backgroundPanel.removeAll();
        message.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel title = new JLabel("SUDOKU");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Serif", Font.PLAIN, 100));
        backgroundPanel.add(title);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,50)));
        JButton playButton = new JButton("Play");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton solutionButton = new JButton("Solution finder");
        solutionButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton aboutButton = new JButton("About");
        aboutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton scoreButton = new JButton("Best Scores");
        scoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(playButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        playButton.addActionListener(new PlayButtonListener());
        backgroundPanel.add(solutionButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        solutionButton.addActionListener(new SolutionFinderButtonListener());
        backgroundPanel.add(aboutButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        aboutButton.addActionListener(new AboutButtonListener());
        backgroundPanel.add(scoreButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        scoreButton.addActionListener(new ScoreButtonListener());
        frame.setBounds(50, 50, 300, 300);
        frame.pack();
        frame.revalidate();
        frame.setVisible(true);
        frame.requestFocusInWindow();
    }
    void buildGridGUI(){
        /*creates gui for 9x9 grid*/
        int row,col,r,c;
        backgroundPanel.removeAll();
        JButton mainMenuButton = new JButton("main menu");
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuButton.addActionListener(new MainMenuButtonListener());
        backgroundPanel.add(mainMenuButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JPanel mainGridPanel = new JPanel(new GridLayout(3,3));
        backgroundPanel.add(mainGridPanel);
        JPanel grid3x3Panel;
        for(r=0;r<9;r+=3){
            for(c=0;c<9;c+=3){
                grid3x3Panel = new JPanel(new GridLayout(3,3));
                grid3x3Panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                for(row=r;row<(r+3);row++){
                    for(col=c;col<(c+3);col++){
                        grid.getCellAtLoc(row, col).addMouseListener(mouseListener);
                        grid3x3Panel.add(grid.getCellAtLoc(row, col));
                    }
                }
                mainGridPanel.add(grid3x3Panel);
            }
        }
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        message.setText("Select a square");
        cellSelected=null;
        backgroundPanel.add(this.message);
    }
    void startSolver(){
        /*creates screen for solution finder*/
        grid.clearGrid();
        this.buildGridGUI();
        JButton solveButton = new JButton("solve");
        solveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton clearButton = new JButton("clear");
        clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        backgroundPanel.add(solveButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        backgroundPanel.add(clearButton);
        backgroundPanel.revalidate();
        solveButton.addActionListener(new SolveButtonListener());
        clearButton.addActionListener(new ClearButtonListener());
        frame.pack();
        frame.repaint();
        frame.requestFocusInWindow();
    }
    void startGame(){
        /*creates screen for game player*/
        finished = false;
        grid.fillRandomValues(fillFraction);
        this.buildGridGUI();
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        backgroundPanel.add(time);
        JButton reset = new JButton("reset");
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);
        reset.addActionListener(new ResetButtonListener());
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        backgroundPanel.add(reset);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JButton nextButton = new JButton("next puzzle");
        nextButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        nextButton.addActionListener(new NextButtonListener());
        backgroundPanel.add(nextButton);
        backgroundPanel.revalidate();
        frame.pack();
        frame.repaint();
        frame.requestFocusInWindow();
        time.setText("Timer: 0:0");
        elapsedTimeInSeconds=0;
        timer.start();
    }
    JTextArea buildTextField(){
        backgroundPanel.removeAll();
        JButton mainMenuButton = new JButton("main menu");
        mainMenuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainMenuButton.addActionListener(new MainMenuButtonListener());
        backgroundPanel.add(mainMenuButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,10)));
        JTextArea text = new JTextArea(15,35);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setFont(new Font("serif",Font.PLAIN,22));
        text.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Border emptyBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        text.setBorder(emptyBorder);
        backgroundPanel.add(scrollPane);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        backgroundPanel.revalidate();
        frame.pack();
        frame.requestFocusInWindow();
        return text;
    }
    void buildAboutScreen(){
        JTextArea text=buildTextField();
        text.setText("");
        text.append("About the game:\nSudoku , originally called Number Place ,is ");
        text.append("a logic based combinatorial number-placement puzzle. ");
        text.append("The objective is to fill a 9x9 grid with digits so that ");
        text.append("each column, each row, and each of the nine 3x3 sub-grids ");
        text.append("that compose the grid contains all of the digits from 1 to 9.");
        text.append("\n\n\nCredits : Neeraj Kerkar");
        frame.repaint();
    }
    void buildScoreScreen(){
        JTextArea text=buildTextField();
        createScoreTable(text);
        frame.pack();
        frame.repaint();
    }
    void createScoreTable(JTextArea text){
        try{
            HashMap<String,TimeRecord> map = new HashMap<String,TimeRecord>();
            long min;
            long sec;
            ArrayList<TimeRecord> scoreRec;
            text.setText("");
            text.append("#RANK\tNAME\tTIME\n");
            scoreRec=readScores();
            Collections.sort(scoreRec);
            int i=1;
            for(TimeRecord record:scoreRec){
                min=record.getTime()/60;
                sec=record.getTime()%60;
                text.append("  "+i+"\t"+record.getName()+"\t"+min+":"+sec+"\n");
                i++;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    void buildNameScreen(){
        JLabel solveTimeLabel = new JLabel();
        long min = elapsedTimeInSeconds/60;
        long sec = elapsedTimeInSeconds%60;
        solveTimeLabel.setText("Your time: "+min+":"+sec);
        solveTimeLabel.setFont(new Font("serif",Font.BOLD,30));
        solveTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel enterName = new JLabel();
        enterName.setText("Enter your name:");
        enterName.setFont(new Font("serif",Font.PLAIN,25));
        enterName.setAlignmentX(Component.CENTER_ALIGNMENT);
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.addActionListener(new OKButtonListener());
        nameField.setText("");
        backgroundPanel.removeAll();
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        backgroundPanel.add(solveTimeLabel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        backgroundPanel.add(enterName);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        backgroundPanel.add(nameField);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        backgroundPanel.add(okButton);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0,25)));
        backgroundPanel.revalidate();
        frame.pack();
        frame.repaint();
    }
    ArrayList<TimeRecord> readScores(){
        ArrayList<TimeRecord> scoreList = new ArrayList<TimeRecord>();
        ObjectInputStream obIn;
        try{
            obIn = new ObjectInputStream(new FileInputStream(scores));
        }
        catch(Exception ex){
            return scoreList;
        }
        TimeRecord rec;
        while(true){
            try{
               rec = (TimeRecord) obIn.readObject();
               scoreList.add(rec);
            }
            catch(Exception ex){
                break;
            }
        }
        try{
            obIn.close();
        }
        catch(Exception ex){
            //do nothing
        }
        return scoreList;
    }
    void writeScores(ArrayList<TimeRecord> scoreList){
        ObjectOutputStream obOut;
        try{
            obOut = new ObjectOutputStream(new FileOutputStream(scores));
            for(TimeRecord rec:scoreList){
               obOut.writeObject(rec);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    class SolveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(!grid.isValidPuzzle()){
                message.setText("Not a valid puzzle,recheck red squares");
                frame.repaint();
                frame.requestFocusInWindow();
                return ;
            }
            Grid solution = SudokuSolver.solve(grid);
            if(solution!=null){
                grid.copyValuesOfGrid(solution);
                message.setText("press clear to reset all squares");
            }
            else{
                message.setText("No solution exists , press clear to reset all squares");
            }
            frame.repaint();
            frame.requestFocusInWindow();
        }
    }
    class ClearButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            grid.clearGrid();
            message.setText("Select a square");
            frame.repaint();
            frame.requestFocusInWindow();
        }
    }
    class SolutionFinderButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            startSolver();
            frame.requestFocusInWindow();
        }
    }
    class MainMenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            buildStartGUI();
            timer.stop();
            frame.requestFocusInWindow();
        }
    }
    class ResetButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            finished=false;
            message.setText("Select a square");
            grid.restoreOriginal();
            grid.findErrorZones();
            frame.repaint();
            frame.requestFocusInWindow();
            elapsedTimeInSeconds=0;
            time.setText("Timer: 0:0");
            timer.start();
        }
    }
    class PlayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            startGame();
            frame.requestFocusInWindow();
        }
    }
    class NextButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            finished=false;
            grid.fillRandomValues(fillFraction);
            grid.findErrorZones();
            message.setText("Select a square");
            frame.repaint();
            frame.requestFocusInWindow();
            elapsedTimeInSeconds=0;
            time.setText("Timer: 0:0");
            timer.start();
        }
    }
    class AboutButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            buildAboutScreen();
        }
    }
    class ScoreButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            buildScoreScreen();
        }
    }
    class OKButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            if(!nameField.getText().trim().equals("")){
                boolean flag=false;
                String name = nameField.getText().trim();
                TimeRecord rec = new TimeRecord(name,elapsedTimeInSeconds);
                nameField.setText("");
                time.setText("Timer: 0:0");
                elapsedTimeInSeconds=0;
                ArrayList<TimeRecord> scoreList = readScores();
                for(int i=0;i<scoreList.size();i++){
                    if(scoreList.get(i).getName().equals(rec.getName())){
                        if(rec.getTime()<scoreList.get(i).getTime()){
                            scoreList.set(i, rec);
                            flag=true;
                            break;
                        }
                    }
                }
                if(flag==false){
                    scoreList.add(rec);
                }
                writeScores(scoreList);
                buildStartGUI();
            }
        }
    }
    class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            elapsedTimeInSeconds+=1;
            long minutes=elapsedTimeInSeconds/60;
            long seconds=elapsedTimeInSeconds%60;
            time.setText("Timer: "+minutes+":"+seconds);
        }
    }
    class MyMouseClickListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e){
            if(finished){
                return ;
            }
            int r,c;
            Cell cell2;
            Object source=e.getSource();
            if(source instanceof Cell){
                Cell cell = (Cell) source;
                if(cell.isModifiable()){
                    cell.highlight();
                    cellSelected=cell;
                    message.setText("Type a number between 1 and 9/BACKSPACE to clear square");
                }
                else{
                    cellSelected=null;
                    message.setText("Select a square");
                }
                for(r=0;r<9;r++){
                    for(c=0;c<9;c++){
                        cell2=grid.getCellAtLoc(r, c);
                        if(cell2!=cell){
                            cell2.deHighlight();
                        }
                    }
                }
            }
            frame.repaint();
        }
        @Override
        public void mouseEntered(MouseEvent e){
            Object source=e.getSource();
            if(source instanceof Cell){
                Cell cell = (Cell) source;
                cell.mouseEnteredCell();
            }
            frame.repaint();
        }
        @Override
        public void mouseExited(MouseEvent e){
            Object source=e.getSource();
            if(source instanceof Cell){
                Cell cell = (Cell) source;
                cell.mouseExitedCell();
            }
            frame.repaint();
        }
        @Override
        public void mousePressed(MouseEvent e){
            //empty
        }
        @Override
        public void mouseReleased(MouseEvent e){
            //empty
        }
    }
    class MyKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e){
            char ch = e.getKeyChar();
            switch(ch){
                case '1':case '2':case '3':case '4':case '5':case '6':case '7':case '8':case '9':{
                    if(cellSelected!=null){
                        try{
                            int[] indices=grid.getCellLocation(cellSelected);
                            grid.insert(Character.getNumericValue(ch), indices[0], indices[1]);
                            cellSelected=null;
                            if(grid.isFilled()&&(finished==false)){
                                finished=true;
                                message.setText("Good job!");
                                timer.stop();
                                buildNameScreen();
                            }
                            else{
                                message.setText("Select a square");
                            }
                        }
                        catch(Exception ex){
                            //empty
                        }
                    }
                    break;
                }
                case '\b':{
                    if(cellSelected!=null){
                        grid.clearCell(cellSelected);
                        grid.findErrorZones();
                        message.setText("Select a square");
                        frame.repaint();
                    }
                    break;
                }
            }
            frame.repaint();
        }
        @Override
        public void keyPressed(KeyEvent e){
            //empty
        }
        @Override
        public void keyReleased(KeyEvent e){
            //empty
        }
    }
}