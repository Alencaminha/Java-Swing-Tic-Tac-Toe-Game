package tictactoegame;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GameFrame extends JFrame {
    private final JButton[][] gameButton = new JButton[3][3];
    private int currentRound = 1;
    private final String gameMode;
    private String markText;
    private boolean gameEnded = false, botCanPlay = false;

    GameFrame(){
        this.setTitle("Tic Tac Toe");
        this.setSize(600, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(3, 3, 10, 10));
        this.getContentPane().setBackground(Color.black);

        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) { // This loop sets the standard configurations for all 9 buttons
                gameButton[i][j] = new JButton();
                gameButton[i][j].setFocusable(false);
                gameButton[i][j].setBorderPainted(false);
                gameButton[i][j].setBackground(Color.white);
                gameButton[i][j].setFont(new Font("Times New Roman", Font.BOLD, 160));
                int finalI = i;
                int finalJ = j;
                gameButton[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent event) {
                        if(event.getSource().equals(gameButton[finalI][finalJ])) {
                            gameButton[finalI][finalJ].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        }
                    }
                    @Override
                    public void mouseExited(MouseEvent event) {
                        if(event.getSource().equals(gameButton[finalI][finalJ])) {
                            gameButton[finalI][finalJ].setCursor(Cursor.getDefaultCursor());
                        }
                    }
                });
                gameButton[i][j].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        playerTurn(gameButton[finalI][finalJ]);
                        if(gameMode.equals("easy") && !gameEnded) { // Checks if the bot is supposed to play based on game mode
                            if(botCanPlay) {
                                try {
                                    Thread.sleep(75);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                botTurn();
                            }
                        }
                    }
                });
                this.add(gameButton[i][j]);
            }
        }

        this.setVisible(true);
        this.gameMode = getGameMode();
    }

    private String getGameMode() {
        String[] options = {"PvP", "Easy"};
        int option = JOptionPane.showOptionDialog(null, "What game mode do you want to play?", "Game mode",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
        if(option == 1) {
            this.setTitle("Tic Tac Toe - Easy");
            return "easy";
        }
        this.setTitle("Tic Tac Toe - PvP");
        return "pvp";
    }

    private void playerTurn(JButton button) {
        markText = getRoundText();
        if(button.getText().equals("")) {
            button.setText(markText);
            if(isWin()) {
                endGame(markText + " is the winner!");
            }
            else if(currentRound == 9) { // The game can only officially draw at round 9
                endGame("It's a draw!");
            }
            currentRound++;
            botCanPlay = true;
        }
    }

    private void botTurn() {
        markText = getRoundText();
        boolean botDone = false;
        do {
            Random random = new Random();
            JButton button = gameButton[random.nextInt(3)][random.nextInt(3)];
            if(button.getText().equals("")) {
                button.setText(markText);
                if(isWin()) {
                    endGame(markText + " is the winner!");
                }
                else if(currentRound == 9) { // The game can only officially draw at round 9
                    endGame("It's a draw!");
                }
                currentRound++;
                botDone = true;
                botCanPlay = false;
            }
        } while(!botDone);
    }

    private String getRoundText() {
        return (currentRound % 2 == 0) ? "O" : "X"; // If current round is even return "O", else return "X"
    }

    private boolean isWin() {
        for(int i = 0; i < 3; i++) {
            if(gameButton[0][i].getText().equals(gameButton[1][i].getText()) && gameButton[1][i].getText().equals(gameButton[2][i].getText())
                    && !(gameButton[2][i].getText().equals(""))){
                return true; // Checking each row for 3 equal characters
            }
            if(gameButton[i][0].getText().equals(gameButton[i][1].getText()) && gameButton[i][1].getText().equals(gameButton[i][2].getText())
                    && !(gameButton[i][2].getText().equals(""))){
                return true; // Checking each column for 3 equal characters
            }
        }
        return gameButton[0][0].getText().equals(gameButton[1][1].getText()) && gameButton[1][1].getText().equals(gameButton[2][2].getText())
                && !(gameButton[2][2].getText().equals("")) || gameButton[2][0].getText().equals(gameButton[1][1].getText())
                && gameButton[1][1].getText().equals(gameButton[0][2].getText()) && !(gameButton[0][2].getText().equals("")); // Checking each diagonal for 3 equal characters
    }

    private void endGame(String resultStatement) {
        gameEnded = true; // For ending the bot moves
        JOptionPane.showMessageDialog(null, resultStatement);
        this.dispose();
        new GameFrame();
    }
}
