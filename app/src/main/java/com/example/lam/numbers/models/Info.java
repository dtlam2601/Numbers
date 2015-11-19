package com.example.lam.numbers.models;

import java.io.Serializable;

/**
 * Created by Lam on 11/10/2015.
 */
public class Info implements Serializable {
    private int number;
    private int player1;
    private int player2;

    public Info() {
    }

    public Info(int number, int player1, int player2) {
        this.number = number;
        this.player1 = player1;
        this.player2 = player2;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPlayer1() {
        return player1;
    }

    public void setPlayer1(int player1) {
        this.player1 = player1;
    }

    public int getPlayer2() {
        return player2;
    }

    public void setPlayer2(int player2) {
        this.player2 = player2;
    }

}
