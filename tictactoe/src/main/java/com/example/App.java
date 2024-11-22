package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class App extends Application {
    private static final int BOARD_SIZE = 5;
    private Button[][] buttons = new Button[BOARD_SIZE][BOARD_SIZE];
    private char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    private boolean isXTurn = true; // Track the current player's turn

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        initializeBoard(gridPane);

        Scene scene = new Scene(gridPane, 400, 400);
        primaryStage.setTitle("TicTacToe 5x5");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void initializeBoard(GridPane gridPane) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                buttons[row][col] = new Button();
                buttons[row][col].setPrefSize(80, 80);
                buttons[row][col].setStyle("-fx-font-size: 24");
                final int r = row;
                final int c = col;

                buttons[row][col].setOnAction(event -> handleMove(r, c));
                gridPane.add(buttons[row][col], col, row);
                board[row][col] = ' '; // Initialize the logical board
            }
        }
    }

    private void handleMove(int row, int col) {
        if (board[row][col] != ' ') {
            return; // Ignore clicks on already occupied cells
        }

        board[row][col] = isXTurn ? 'X' : 'O';
        buttons[row][col].setText(String.valueOf(board[row][col]));
        buttons[row][col].setDisable(true); // Prevent further clicks on this cell

        if (checkWinner(row, col)) {
            endGame(isXTurn ? "Player X" : "Player O");
        } else if (isBoardFull()) {
            endGame("Draw");
        } else {
            isXTurn = !isXTurn; // Switch turns
        }
    }

    private boolean isBoardFull() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWinner(int row, int col) {
        char currentMark = board[row][col];

        // Check horizontally
        if (checkLine(row, 0, 0, 1, currentMark)) return true;

        // Check vertically
        if (checkLine(0, col, 1, 0, currentMark)) return true;

        // Check diagonal (top-left to bottom-right)
        if (checkLine(0, 0, 1, 1, currentMark)) return true;

        // Check diagonal (top-right to bottom-left)
        if (checkLine(0, BOARD_SIZE - 1, 1, -1, currentMark)) return true;

        return false;
    }

    private boolean checkLine(int startRow, int startCol, int rowIncrement, int colIncrement, char mark) {
        int count = 0;
        int row = startRow;
        int col = startCol;

        while (row >= 0 && row < BOARD_SIZE && col >= 0 && col < BOARD_SIZE) {
            if (board[row][col] == mark) {
                count++;
                if (count == 5) return true; // Five marks in a row
            } else {
                count = 0;
            }
            row += rowIncrement;
            col += colIncrement;
        }
        return false;
    }

    private void endGame(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message + " wins!");
        alert.showAndWait();

        resetGame();
    }

    private void resetGame() {
        isXTurn = true;
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                board[row][col] = ' ';
                buttons[row][col].setText("");
                buttons[row][col].setDisable(false);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
