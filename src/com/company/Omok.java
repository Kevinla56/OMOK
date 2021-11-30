package com.company;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class Omok extends JPanel {

    public static void main(String[] args) {
        JFrame window = new JFrame("Omok");
        Omok content = new Omok();
        window.setContentPane(content);
        window.pack();

        window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        window.setVisible(true);
    }

    private JButton Start;
    private JButton Reset;
    private JLabel message;

    public Omok() {
        setLayout(null);
        setPreferredSize( new Dimension(450,330) );
        setBackground(new Color(255,255,255));
        Board board = new Board();

        add(board);
        add(Start);
        add(Reset);
        add(message);

        board.setBounds(20,20,225,225);
        Start.setBounds(250, 80, 120, 30);
        Reset.setBounds(250, 140, 120, 30);
        message.setBounds(-45, 250, 350, 30);
    }

    class Board extends JPanel implements ActionListener, MouseListener {
        boolean inProgress;

        int[][] board;
        int currentPlayer;
        int win_r1;
        int win_r2;
        int win_c1;
        int win_c2;

        static final int EMPTY = 0;
        static final int player1 = 2;
        static final int player2 = 1;

        public Board() {
            setBackground(new Color(205,133,63));
            addMouseListener(this);

            Start = new JButton("Start");
            Start.addActionListener(this);

            Reset = new JButton("Reset");
            Reset.addActionListener(this);

            message = new JLabel("",JLabel.CENTER);
            board = new int[15][15];

            startGame();
        }

        public void actionPerformed(ActionEvent evt) {
            Object src = evt.getSource();
            if (src == Start)
                startGame();
            else if (src == Reset)
                resetGame();
        }

        void startGame() {
            for (int row = 0; row < 15; row++)
                for (int col = 0; col < 15; col++)
                    board[row][col] = EMPTY;
            currentPlayer = player1;
            message.setText("player1:  Make your move.");
            inProgress = true;
            Start.setEnabled(false);
            win_r1 = -1;
            repaint();
        }

        void resetGame(){
            for (int row = 0; row < 15; row++)
                for (int col = 0; col < 15; col++)
                    board[row][col] = EMPTY;
            currentPlayer = player1;
            message.setText("It's player 1 turn.");
            inProgress = true;
            win_r1 = -1;
            repaint();
        }

        void gameOver(String str) {
            message.setText(str);
            Start.setEnabled(true);
            inProgress = false;
        }

        void userInput(int row, int col) {
            if (!inProgress)
            {
                return;
            }
            
            if ( board[row][col] != EMPTY ) {
                if (currentPlayer == player1)
                    message.setText("It's player 1 turn.");
                else
                    message.setText("It's player 2 turn.");
                return;
            }

            board[row][col] = currentPlayer;
            repaint();

            if (determineWinner(row,col)) {
                if (currentPlayer == player2)
                    gameOver("player 2 wins!");
                else
                    gameOver("player 1 wins!");
                return;
            }

            boolean emptySpace = false;
            for (int i = 0; i < 15; i++)
                for (int j = 0; j < 15; j++)
                    if (board[i][j] == EMPTY)
                        emptySpace = true;
            if (emptySpace == false) {
                gameOver("Game Over");
                return;
            }

            if (currentPlayer == player1) {
                currentPlayer = player2;
                message.setText("It's player 2 turn.");
            } else {
                currentPlayer = player1;
                message.setText("It's player 1 turn.");
            }
        }

        private boolean determineWinner(int row, int col) {

            if (count( board[row][col], row, col, 1, 0 ) >= 5)
                return true;
            if (count( board[row][col], row, col, 0, 1 ) >= 5)
                return true;
            if (count( board[row][col], row, col, 1, -1 ) >= 5)
                return true;
            if (count( board[row][col], row, col, 1, 1 ) >= 5)
                return true;

            win_r1 = -1;
            return false;

        }

        private int count(int player, int row, int col, int dirX, int dirY) {

            int ct = 1;
            int r, c;

            r = row + dirX;
            c = col + dirY;
            while ( r >= 0 && r < 15 && c >= 0 && c < 15 && board[r][c] == player ) {
                ct++;
                r += dirX;
                c += dirY;
            }

            win_r1 = r - dirX;
            win_c1 = c - dirY;

            r = row - dirX;
            c = col - dirY;
            while ( r >= 0 && r < 15 && c >= 0 && c < 15 && board[r][c] == player ) {
                ct++;
                r -= dirX;
                c -= dirY;
            }

            win_r2 = r + dirX;
            win_c2 = c + dirY;

            return ct;
        }

        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            g.setColor(Color.DARK_GRAY);
            for (int i = 1; i < 15; i++) {
                g.drawLine(1 + 15*i, 0, 1 + 15*i, getSize().height);
                g.drawLine(0, 1 + 15*i, getSize().width, 1 + 15*i);
            }
            g.setColor(Color.BLACK);
            g.drawRect(0,0,getSize().width-1,getSize().height-1);
            g.drawRect(1,1,getSize().width-1,getSize().height-1);

            for (int row = 0; row < 15; row++)
                for (int col = 0; col < 15; col++)
                    if (board[row][col] != EMPTY)
                        createPiece(g, board[row][col], row, col);

        }

        private void createPiece(Graphics g, int piece, int row, int col) {
            if (piece == player2)
                g.setColor(Color.WHITE);
            else
                g.setColor(Color.BLACK);
            g.fillOval(3 + 15*col, 3 + 15*row, 10, 10);
        }

        public void mousePressed(MouseEvent evt) {
                int col = (evt.getX() - 2) / 15;
                int row = (evt.getY() - 2) / 15;
                if (col >= 0 && col < 15 && row >= 0 && row < 15)
                    userInput(row,col);
        }

        public void mouseReleased(MouseEvent evt) { }
        public void mouseClicked(MouseEvent evt) { }
        public void mouseEntered(MouseEvent evt) { }
        public void mouseExited(MouseEvent evt) { }

    }
}