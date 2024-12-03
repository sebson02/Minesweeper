package org.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;

import java.io.IOException;

public class GameView {
    private Screen screen;

    public GameView() throws IOException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();
    }

    public void generateGameView(Board board, int cursorX, int cursorY) throws IOException {
        screen.clear();
        TextGraphics graphics = screen.newTextGraphics();
        int pos;
        if (board.getX() == 10){
            pos = 30;
        }
        else if (board.getX() == 16){
            pos = 24;
        }
        else pos = 10;
        for (int i = 0; i < board.getX(); i++) {
            for (int j = 0; j < board.getY(); j++) {
                Field field = board.getField(i, j);

                if (i == cursorX && j == cursorY) {
                    graphics.setBackgroundColor(TextColor.ANSI.YELLOW);
                } else {
                    graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                }

                if (!field.isHidden()) {
                    if (field.getValue() == 9) {
                        graphics.setForegroundColor(TextColor.ANSI.RED);
                        graphics.putString(pos + 2 * i, j, "X", SGR.BOLD);
                    } else {
                        graphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
                        graphics.putString(pos + 2 * i, j, String.valueOf(field.getValue()));
                    }
                } else if (field.isFlagged()) {
                    graphics.setForegroundColor(TextColor.ANSI.MAGENTA);
                    graphics.putString(pos + 2 * i, j, "F");
                } else {
                    graphics.setForegroundColor(TextColor.ANSI.WHITE);
                    graphics.putString(pos + 2 * i, j, ".");
                }
            }
        }

        screen.refresh();
    }

    public KeyStroke readUserInput() throws IOException {
        return screen.readInput();
    }

    public void displayGameOver(Board board) throws IOException {
        board.uncover();
        generateGameView(board, -1, -1);

        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.ANSI.RED);
        graphics.putString(25, board.getY() + 2, "GAME OVER! Press ESC to exit.", SGR.BOLD);
        graphics.putString(25, board.getY() + 3, "Press ENTER to go back to menu.", SGR.BOLD);

        screen.refresh();
    }
    public void displayGameWon(Board board) throws IOException {
        board.uncover();
        generateGameView(board, -1, -1);

        TextGraphics graphics = screen.newTextGraphics();
        graphics.setForegroundColor(TextColor.ANSI.GREEN_BRIGHT);
        graphics.putString(25, board.getY() + 2, "YOU WON! Press ESC to exit.", SGR.BOLD);
        graphics.putString(25, board.getY() + 3, "Press ENTER to go back to menu.", SGR.BOLD);

        screen.refresh();
    }

    public int generateStartView() throws IOException {
        screen.clear();

        String[] options = {
                "Beginner   -  columns: 10    rows: 10    bombs: 10",
                "Advanced   -  columns: 16    rows: 16    bombs: 40",
                "Expert     -  columns: 30    rows: 16    bombs: 99"
        };

        int selectedIndex = 0;

        try {
            boolean continueLoop = true;
            while (continueLoop) {
                drawMenu(screen, options, selectedIndex);
                KeyStroke keyStroke = screen.readInput();

                if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                    selectedIndex = (selectedIndex - 1 + options.length) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                    selectedIndex = (selectedIndex + 1) % options.length;
                } else if (keyStroke.getKeyType() == KeyType.Enter) {
                    continueLoop = false;
                } else if (keyStroke.getKeyType() == KeyType.Escape){
                    selectedIndex = 3;
                    continueLoop = false;
                }
            }
        } finally {
            screen.refresh();
        }
        return selectedIndex;
    }


    private void drawMenu(Screen screen, String[] options, int selectedIndex) throws IOException {
        screen.clear();
        TextGraphics graphics = screen.newTextGraphics();

        drawTitle(graphics);

        for (int i = 0; i < options.length; i++) {
            if (i == selectedIndex) {
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                graphics.setBackgroundColor(TextColor.ANSI.BLUE);
                graphics.putString(10, 10 + i, options[i], SGR.BOLD);
                graphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
            } else {
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                graphics.putString(10, 10 + i, options[i]);
            }
        }

        screen.refresh();
    }

    private void drawTitle(TextGraphics graphics) {
        graphics.setForegroundColor(TextColor.ANSI.CYAN);
        graphics.putString(9, 2, "  __  __ _", SGR.BOLD);
        graphics.putString(9, 3, " |  \\/  (_)_ _  ___ ____ __ _____ ___ _ __  ___ _ _ ", SGR.BOLD);
        graphics.putString(9, 4, " | |\\/| | | ' \\/ -_|_-< V  V / -_) -_) '_ \\/ -_) '_|", SGR.BOLD);
        graphics.putString(9, 5, " |_|  |_|_|_||_\\___/__/\\_/\\_/\\___\\___| .__/\\___|_|  ", SGR.BOLD);
        graphics.putString(9, 6, "                                     |_|            ", SGR.BOLD);
    }
    public void killScreen() throws IOException {
        screen.stopScreen();
    }
}




