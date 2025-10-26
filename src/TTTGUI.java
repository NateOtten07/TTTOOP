import javax.swing.*;
import java.awt.*;

public class TTTGUI extends JFrame {
    private static final int SIZE = 3;
    private TicTacToeGame game;
    private TTTTileButton[][] tiles;
    private JLabel statusLabel;
    private JButton quitButton;
    private JButton playAgainButton;

    public TTTGUI() {
        game = new TicTacToeGame();
        tiles = new TTTTileButton[SIZE][SIZE];

        setTitle("Tic Tac Toe");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel heading = new JLabel("Tic Tac Toe", JLabel.CENTER);
        heading.setFont(new Font("Serif", Font.BOLD, 28));
        mainPanel.add(heading, BorderLayout.NORTH);

        JPanel gamePanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                TTTTileButton tile = new TTTTileButton(r, c);
                tile.setFont(new Font("Arial", Font.BOLD, 48));
                tile.addActionListener(e -> handleMove(tile));
                tiles[r][c] = tile;
                gamePanel.add(tile);
            }
        }
        mainPanel.add(gamePanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Player X's turn", JLabel.CENTER);

        quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        playAgainButton = new JButton("Play Again");
        playAgainButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to start a new game?",
                    "Play Again",
                    JOptionPane.YES_NO_OPTION
            );
            if (choice == JOptionPane.YES_OPTION) {
                resetGame();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(playAgainButton);
        buttonPanel.add(quitButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(statusLabel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(southPanel, BorderLayout.SOUTH);

        add(mainPanel);
        setVisible(true);
    }

    private void handleMove(TTTTileButton tile) {
        int r = tile.getRow();
        int c = tile.getCol();

        if (!game.makeMove(r, c)) {
            JOptionPane.showMessageDialog(this, "Invalid move! Tile already taken.");
            return;
        }

        tile.setText(String.valueOf(game.getCurrentPlayer().getSymbol()));

        if (game.checkWin()) {
            JOptionPane.showMessageDialog(this, game.getCurrentPlayer().getName() + " wins!");
            disableBoard();
            return;
        }

        if (game.checkDraw()) {
            JOptionPane.showMessageDialog(this, "It's a tie!");
            disableBoard();
            return;
        }

        game.switchTurn();
        statusLabel.setText(game.getCurrentPlayer().getName() + "'s turn");
    }

    private void resetGame() {
        game.resetGame();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                tiles[r][c].setText(" ");
                tiles[r][c].setEnabled(true);
            }
        }
        statusLabel.setText("Player X's turn");
    }

    private void disableBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                tiles[r][c].setEnabled(false);
            }
        }
    }

    private class TicTacToeGame {
        private TTTBoard board;
        private TTTPlayer playerX;
        private TTTPlayer playerO;
        private TTTPlayer currentPlayer;

        public TicTacToeGame() {
            board = new TTTBoard();
            playerX = new TTTPlayer("Player X", 'X');
            playerO = new TTTPlayer("Player O", 'O');
            currentPlayer = playerX;
        }

        public boolean makeMove(int row, int col) {
            return board.placeMark(row, col, currentPlayer.getSymbol());
        }

        public boolean checkWin() {
            return board.checkWin(currentPlayer.getSymbol());
        }

        public boolean checkDraw() {
            return board.isFull();
        }

        public void switchTurn() {
            currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
        }

        public TTTPlayer getCurrentPlayer() {
            return currentPlayer;
        }

        public void resetGame() {
            board.clearBoard();
            currentPlayer = playerX;
        }
    }

    private class TTTBoard {
        private char[][] grid;
        private static final int SIZE = 3;

        public TTTBoard() {
            grid = new char[SIZE][SIZE];
            clearBoard();
        }

        public boolean placeMark(int row, int col, char symbol) {
            if (grid[row][col] == ' ') {
                grid[row][col] = symbol;
                return true;
            }
            return false;
        }

        public boolean isFull() {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (grid[r][c] == ' ') return false;
                }
            }
            return true;
        }

        public boolean checkWin(char symbol) {
            return checkRows(symbol) || checkCols(symbol) || checkDiagonals(symbol);
        }

        private boolean checkRows(char symbol) {
            for (int r = 0; r < SIZE; r++) {
                if (grid[r][0] == symbol && grid[r][1] == symbol && grid[r][2] == symbol) {
                    return true;
                }
            }
            return false;
        }

        private boolean checkCols(char symbol) {
            for (int c = 0; c < SIZE; c++) {
                if (grid[0][c] == symbol && grid[1][c] == symbol && grid[2][c] == symbol) {
                    return true;
                }
            }
            return false;
        }

        private boolean checkDiagonals(char symbol) {
            return (grid[0][0] == symbol && grid[1][1] == symbol && grid[2][2] == symbol) ||
                    (grid[0][2] == symbol && grid[1][1] == symbol && grid[2][0] == symbol);
        }

        public void clearBoard() {
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    grid[r][c] = ' ';
                }
            }
        }
    }

    private class TTTPlayer {
        private String name;
        private char symbol;

        public TTTPlayer(String name, char symbol) {
            this.name = name;
            this.symbol = symbol;
        }

        public String getName() {
            return name;
        }

        public char getSymbol() {
            return symbol;
        }
    }
}