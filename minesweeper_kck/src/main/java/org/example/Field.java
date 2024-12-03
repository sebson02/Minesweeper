package org.example;

public class Field {
    private int value;
    private boolean hidden = true;
    private boolean flagged = false;
    public Field(){
        this.value = 0;
    }
    public void setBomb(){
        this.value = 9;
    }
    public void increse(){
        if (this.value != 9){
            value++;
        }
    }
    public void unfold(){
        this.hidden = false;
    }
    public void flag(){
        flagged = true;
    }
    public void unflag(){
        flagged = false;
    }
    public int getValue(){
        return value;
    }
    public boolean isHidden(){
        return hidden;
    }
    public boolean isFlagged(){
        return flagged;
    }
}
