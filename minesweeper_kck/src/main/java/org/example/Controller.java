package org.example;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;

public class Controller {
    private Board mainBoard;
    private GameView gv;
    private int cursorX = 0;
    private int cursorY = 0;

    public Controller() throws IOException {
        gv = new GameView();
        int selectedOption = gv.generateStartView();
        if (handleMenuSelection(selectedOption)) {
            gameLoop();
        }
        else gv.killScreen();
    }

    private boolean handleMenuSelection(int selectedOption) {
        if (selectedOption == 0) {
            mainBoard = new Board(10, 10, 10);
        } else if (selectedOption == 1) {
            mainBoard = new Board(16, 16, 40);
        } else if (selectedOption == 2) {
            mainBoard = new Board(30, 16, 99);
        } else if (selectedOption == 3){
            return false;
        }
        return true;
    }

    private void gameLoop(){
        try {
            boolean esc = false;
            while (esc == false) {
                gv.generateGameView(mainBoard, cursorX, cursorY);
                KeyStroke keyStroke = gv.readUserInput();

                if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    if (cursorY > 0) cursorY--;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    if (cursorY < mainBoard.getY() - 1) cursorY++;
                } else if (keyStroke.getKeyType() == KeyType.ArrowLeft) {
                    if (cursorX > 0) cursorX--;
                } else if (keyStroke.getKeyType() == KeyType.ArrowRight) {
                    if (cursorX < mainBoard.getX() - 1) cursorX++;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    if (!mainBoard.getField(cursorX, cursorY).isHidden()) {
                        if (!mainBoard.flagCheck(cursorX, cursorY)) {
                            gv.generateGameView(mainBoard, cursorX, cursorY);
                            gv.displayGameOver(mainBoard);
                            esc = true;
                        }
                    } else if (!mainBoard.unfoldField(cursorX, cursorY)) {
                        gv.generateGameView(mainBoard, cursorX, cursorY);
                        gv.displayGameOver(mainBoard);
                        esc = true;
                    }
                } else if (keyStroke.getCharacter() != null && keyStroke.getCharacter() == 'f') {
                    if (mainBoard.getField(cursorX, cursorY).isFlagged()) {
                        mainBoard.unflag(cursorX, cursorY);
                    } else {
                        mainBoard.flag(cursorX, cursorY);
                    }
                } else if (keyStroke.getKeyType() == KeyType.Escape) {
                    gv.killScreen();
                    return;
                }
                if (mainBoard.checkIfWon() == true){
                    gv.generateGameView(mainBoard, cursorX, cursorY);
                    gv.displayGameWon(mainBoard);
                    esc = true;
                }
            }
            esc = false;
            while(esc == false){
                KeyStroke keyStroke = gv.readUserInput();
                if (keyStroke.getKeyType() == KeyType.Enter){
                    int selectedOption = gv.generateStartView();
                    if (handleMenuSelection(selectedOption)) {
                        gameLoop();
                        return;
                    }
                    else {
                        gv.killScreen();
                        return;
                    }
                } else if (keyStroke.getKeyType() == KeyType.Escape){
                    gv.killScreen();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

