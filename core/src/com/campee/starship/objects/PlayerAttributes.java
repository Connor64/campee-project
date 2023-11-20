package com.campee.starship.objects;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;

//please work
public class PlayerAttributes {
    public boolean orderInProgress;
    public int highScore;
    public int score;
    public int ordersCompleted;
    public Queue<String> orderQueue;
    public ArrayList<String> array;


    public PlayerAttributes(boolean orderInProgress, int ordersCompleted, int highScore, int score, Queue<String> orderQueue, ArrayList<String> array) {
        this.orderInProgress = orderInProgress;
        this.highScore = highScore;
        this.score = score;
        this.ordersCompleted = ordersCompleted;
        this.orderQueue = orderQueue;
        this.array = array;
    }



    public PlayerAttributes() {
        this.orderInProgress = false;
        this.highScore = 0;
        this.score = 0;
        this.ordersCompleted = 0;
        this.orderQueue = null;
        this.array = new ArrayList<>();
        //array.add("Queue");
    }

    public ArrayList<String> getArray() {
        return array;
    }

    public void setArray(ArrayList<String> array)  {
        array.add("");
        this.array = array;
    }



    public boolean isOrderInProgress() {
        return orderInProgress;
    }

    public void setOrderInProgress(boolean orderInProgress) {
        this.orderInProgress = orderInProgress;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getOrdersCompleted() {
        return ordersCompleted;
    }

    public Queue<String> getOrderQueue() {
        return orderQueue;
    }

    public void setOrderQueue(Queue<String> orderQueue) {
        this.orderQueue = orderQueue;
    }

    public void acceptOrder(String order) {
        orderQueue.add(order);
    }

    public void completeOrder(String order) {
        orderQueue.remove(order);
    }
}

