package org.example;

import java.util.Random;
public class Board {
    private int x;
    private int y;
    private int n;
    private Field board[][];
    public Board(int x, int y, int n){
        this.x = x;
        this.y = y;
        this.n = n;
        board = new Field[x][y];
        for (int i=0; i<x; i++){
            for (int j=0; j<y; j++){
                board[i][j] = new Field();
            }
        }
        setBoard();
    }
    private void setBoard(){
        drawBombs();
        countFields();
    }
    private void drawBombs(){
        Random r = new Random();
        int bomb_x;
        int bomb_y;
        for (int i=0; i<n; i++){
            bomb_x = r.nextInt(0, x-1);
            bomb_y = r.nextInt(0, y-1);
            if (board[bomb_x][bomb_y].getValue() != 9){
                board[bomb_x][bomb_y].setBomb();
            }
            else i--;
        }
    }
    private void countFields(){
        for (int i=0; i<x; i++){
            for (int j=0; j<y; j++){
                if (board[i][j].getValue() == 9){
                    if (i>0 && j>0)     board[i-1][j-1].increse();
                    if (j>0)            board[i][j-1].increse();
                    if (i<x-1 && j>0)   board[i+1][j-1].increse();
                    if (i>0)            board[i-1][j].increse();
                    if (i<x-1)          board[i+1][j].increse();
                    if (i>0 && j<y-1)   board[i-1][j+1].increse();
                    if (j<y-1)          board[i][j+1].increse();
                    if (i<x-1 && j<y-1) board[i+1][j+1].increse();
                }
            }
        }
    }
    public boolean flagCheck(int x, int y) {
        if (!isWithinBounds(x, y) || board[x][y].isHidden()) {
            return true;
        }

        int flags = 0;
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},         {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (isWithinBounds(nx, ny) && board[nx][ny].isFlagged()) {
                flags++;
            }
        }
        if (flags == board[x][y].getValue()) {
            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
                if (isWithinBounds(nx, ny) && board[nx][ny].isHidden() && !board[nx][ny].isFlagged()) {
                    if (!unfoldField(nx, ny)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < this.x && y >= 0 && y < this.y;
    }

    public boolean unfoldField(int x, int y){
        if (x < this.x && x >= 0 && y < this.y && y >= 0){
            if (board[x][y].isHidden() && board[x][y].isFlagged() == false) {
                if (board[x][y].getValue() == 9){
                    uncover();
                    return false;
                }
                if (board[x][y].getValue() == 0){
                    board[x][y].unfold();
                    unfoldField(x-1, y-1);
                    unfoldField(x-1, y);
                    unfoldField(x-1, y+1);
                    unfoldField(x, y-1);
                    unfoldField(x, y+1);
                    unfoldField(x+1, y-1);
                    unfoldField(x+1, y);
                    unfoldField(x+1, y+1);
                }
                else {
                    board[x][y].unfold();
                }
            }
        }
        return true;
    }
    public void flag(int x, int y){
        if (x < this.x && x >= 0 && y < this.y && y >= 0){
            if (board[x][y].isHidden() && board[x][y].isFlagged() == false){
                board[x][y].flag();
            }
        }
    }
    public void unflag(int x, int y){
        if (x < this.x && x >= 0 && y < this.y && y >= 0){
            if (board[x][y].isFlagged() == true){
                board[x][y].unflag();
            }
        }
    }
    public void uncover(){
        for (int i=0; i<x; i++){
            for (int j=0; j<y; j++){
                board[i][j].unfold();
            }
        }
    }
    public boolean checkIfWon(){
        int a = 0;
        for (int i = 0; i<x-1; i++){
            for (int j=0; j<y-1; j++){
                if (board[i][j].getValue() == 9 && board[i][j].isFlagged() == true){
                    a++;
                }
                if (board[i][j].getValue() != 9 && board[i][j].isFlagged() == true){
                    a--;
                }
            }
        }
        if(a == this.n){
            return true;
        }
        return false;
    }
    public Field getField(int x, int y) {
        if (x >= 0 && x < this.x && y >= 0 && y < this.y) {
            return board[x][y];
        }
        throw new IndexOutOfBoundsException("Invalid field coordinates.");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
