package com.campee.starship;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;

public class Order {
    private Stage stage;
    private Game game;
    private int orderID;
    private String pickupLocation;
    private String dropoffLocation;
    private double timeEstimate;


    public Order(Stage stage, Game game, int orderID, String pickupLocation, String dropoffLocation, double timeEstimate) {
        this.stage = stage;
        this.game = game;
        this.orderID = orderID;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.timeEstimate = timeEstimate;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public double getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(double timeEstimate) {
        this.timeEstimate = timeEstimate;
    }

    public String queueString() {
        return "Order ID: " + orderID;
    }
    @Override
    public String toString() {
        return
                "Order ID: " + orderID + '\n' +
                        "Pickup Location: " + pickupLocation + '\n' +
                        "Drop-off Location: " + dropoffLocation + '\n' +
                        "Time Estimate: " + timeEstimate ;
    }
}
