package org.arrinna.nobugdemo.exception;

public class GameOverException extends Exception{
    public GameOverException() {
        super("游戏结束！");
    }

    public GameOverException(String message) {
        super(message);
    }
}
