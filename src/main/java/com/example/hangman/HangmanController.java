package com.example.hangman;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashSet;

public class HangmanController {

    private final int NUM_OF_COLUMNS_OF_LETTERS = 3;
    private final int NUM_OF_LETTERS = 26;
    private final int FONT_SIZE = 24;
    private final int FONT_SIZE_BUTTONS = 18;
    private final int ROW_0 = 0;
    private final int ROW_1 = 1;
    private final int COL_0 = 0;
    private final int CORNER_RADIUS_BUTTON_LETTERS = 30;
    private final int CORNER_RADIUS_LABEL_LETTERS = 150;

    @FXML
    private Button buttonStartNewGame;

    @FXML
    private Label labelWinLose;

    @FXML
    private GridPane gridLetters;

    @FXML
    private GridPane gridWord;

    @FXML
    private Canvas canvas;

    private GraphicsContext gc;

    private Button[] buttonLetters;
    private Label[] labelLetters;


    private HangmanLogic hangmanLogic;

    @FXML
    private void initialize() {
        this.gc = this.canvas.getGraphicsContext2D();
        this.setButtonLetters();
        this.hangmanLogic = new HangmanLogic();

    }

    @FXML
    void buttonStartNewGameClicked(ActionEvent event) {

        this.labelWinLose.setVisible(false);

        if (labelLetters != null) {
            for (Label labelLetter : labelLetters) {
                this.gridWord.getChildren().remove(labelLetter);
            }
        }

        this.gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Button button : buttonLetters) {
            button.setDisable(false);
            button.setVisible(true);
        }

        int numOfLetters = hangmanLogic.pickNewWord();
        labelLetters = new Label[numOfLetters];
        for (int i = 0; i < numOfLetters; i++) {
            labelLetters[i] = new Label("-");
            labelLetters[i].setPrefSize(gridWord.getPrefWidth() / numOfLetters, gridWord.getPrefHeight());
            labelLetters[i].setFont(new Font("cooper black", FONT_SIZE));
            labelLetters[i].setTextFill(Color.WHITE);
            labelLetters[i].setBackground(
                    new Background(new BackgroundFill(Color.RED, new CornerRadii(CORNER_RADIUS_LABEL_LETTERS), null)));
            labelLetters[i].setAlignment(Pos.CENTER);
            gridWord.add(labelLetters[i], i, i / numOfLetters);
        }
    }

    private void endGame() {

        for (Button button : buttonLetters) {
            button.setDisable(true);
        }

        if (hangmanLogic.hasWon()) {
            labelWinLose.setText("Congratulations! You Won!!!");
        } else {
            labelWinLose.setText("Game Over!");
        }
        labelWinLose.setVisible(true);
    }

    @FXML
    void buttonQuitClicked(ActionEvent event) {
        System.exit(0);
    }

    private void setButtonLetters() {

        buttonLetters = new Button[NUM_OF_LETTERS];
        for (int i = 0; i < buttonLetters.length; i++) {
            char letter = (char) (i + 'A');
            buttonLetters[i] = new Button(letter + "");
            buttonLetters[i].setTextFill(Color.WHITE);
            buttonLetters[i].setPrefWidth(gridLetters.getPrefWidth() / NUM_OF_COLUMNS_OF_LETTERS);
            buttonLetters[i].setBackground(new Background(
                    new BackgroundFill(Color.BLUE, new CornerRadii(CORNER_RADIUS_BUTTON_LETTERS), null)));

            buttonLetters[i].setFont(new Font("cooper black", FONT_SIZE_BUTTONS));
            buttonLetters[i].setDisable(true);
            gridLetters.add(buttonLetters[i], i % NUM_OF_COLUMNS_OF_LETTERS, i / NUM_OF_COLUMNS_OF_LETTERS);
            buttonLetters[i].setOnAction(actionEvent -> handleButtonLettersEvent(actionEvent, letter));
        }
    }

    private void handleButtonLettersEvent(ActionEvent actionEvent, char letter) {

        final double SPINE_START_X = this.canvas.getWidth() / 2;
        final double SPINE_START_Y = this.canvas.getHeight() / 2 - 30;
        final double SPINE_END_X = this.canvas.getWidth() / 2;
        final double SPINE_END_Y = this.canvas.getHeight() / 2 + 30;
        Button button = (Button) (actionEvent.getSource());

        button.setVisible(false);
        if (hangmanLogic.letterExists(letter)) {
            HashSet<Integer> indices = hangmanLogic.examineLetterInWord(letter);
            for (Integer index : indices) {
                labelLetters[index].setText(letter + "");
            }

            if (hangmanLogic.hasWon()) {
                endGame();
            }

        } else {
            int failures = hangmanLogic.getFailures();
            gc.setStroke(Color.WHITE);
            gc.setFill(Color.WHITE);
            switch (failures) {
                case 1:
                    gc.strokeLine(SPINE_START_X, SPINE_START_Y, SPINE_END_X, SPINE_END_Y);
                    break;
                case 2:
                    gc.strokeLine(SPINE_START_X, SPINE_START_Y, SPINE_END_X - 20, SPINE_END_Y - 20);
                    break;
                case 3:
                    gc.strokeLine(SPINE_START_X, SPINE_START_Y, SPINE_END_X + 20, SPINE_END_Y - 20);
                    break;
                case 4:
                    gc.strokeLine(SPINE_END_X, SPINE_END_Y, SPINE_END_X - 20, SPINE_END_Y + 20);
                    break;
                case 5:
                    gc.strokeLine(SPINE_END_X, SPINE_END_Y, SPINE_END_X + 20, SPINE_END_Y + 20);
                    break;
                case 6:
                    gc.fillOval(SPINE_START_X - 15, SPINE_START_Y - 25, 30, 30);
                    break;
                case 7:
                    gc.strokeLine(SPINE_START_X, SPINE_START_Y, SPINE_START_X, SPINE_START_Y - 80);
                    break;
                case 8:
                    gc.strokeLine(SPINE_START_X, SPINE_START_Y - 80, SPINE_START_X - 80,
                            SPINE_START_Y - 80);
                    break;
                case 9:
                    gc.strokeLine(SPINE_START_X - 80, SPINE_START_Y + 80, SPINE_START_X - 80,
                            SPINE_START_Y - 80);
                    endGame();
                    break;
                case 10:
                    break;
            }
        }
    }
}
