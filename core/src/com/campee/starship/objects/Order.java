package com.campee.starship;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Scanner;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Collections;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Order {
    private Stage stage;
    private Game game;
    public String orderID;
    private String pickupLocation;
    private String dropoffLocation;
    private int timeEstimate;
    public ArrayList<String> array;
    public int i;
    private Rectangle pickupBounds;
    private Rectangle dropoffBounds;
    private boolean pickedUp;
    private boolean droppedOff;
    String[] words;


    public Order(Stage stage, Game game, String orderID, String pickupLocation, String dropoffLocation, int timeEstimate, ArrayList<String> array) {
        this.stage = stage;
        this.game = game;
        this.orderID = orderID;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.timeEstimate = timeEstimate;
        this.array = array;
        this.words = words;
        i = 0;
        pickedUp = false;
        droppedOff = false;
        pickupBounds = new Rectangle(0, 0, 0, 0);
        dropoffBounds = new Rectangle(0, 0, 0, 0);

    }
    public Order() {
        stage = null;
        game = null;
        orderID = null;
        pickupLocation = null;
        dropoffLocation = null;
        timeEstimate = 0;
        array = new ArrayList<String>();
        i = 0;

    }

    public ArrayList<String> getArray() {
        return array;
    }

//CHANGE FOR DEMO SO WE DONT WAIT FOR ALL SIX :)

    public void setArray(ArrayList<String> array) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("Level1Orders.txt"));
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            array.add(line);
            //String[] words = line.split("\\|");
        }




        this.array = array;
    }

    public String[] arrayToArray() {
        String[] words;
        String line = array.get(i);
        words = line.split("\\|");
        i++;
        return words;
    }

    public String[] stringToArray(String line) {
        return line.split(" ");
    }


    public String arrayToString() {
        words = new String[0];
        String line = array.get(i);
        words = line.split("\\|");


        String s = "ID: " + words[0] + "\nP: " + words[1]
                + "\nD: " + words[2] + "\nTime: " + words[3] + " s";
        return s;

    }

    public Stage getStage() {
        return stage;
    }

    public void seti(int i ) {
        this.i = i;
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

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public void setPickupBounds(float x, float y, float width, float height) {
        pickupBounds.set(x, y, width, height);
    }

    public void setDropoffBounds(float x, float y, float width, float height) {
        dropoffBounds.set(x, y, width, height);
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

    public int getTimeEstimate() {
        return timeEstimate;
    }

    public void setTimeEstimate(int timeEstimate) {
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

    public String getOrderString(){
        return words[0];
    }

    public Rectangle getPickupBounds() {
        return this.pickupBounds;
    }

    public Rectangle getDropoffBounds() {
        return this.dropoffBounds;
    }

    public void setPickedUp(boolean pick) {
        pickedUp = pick;
    }

    public void setDroppedOff(boolean drop) {
        droppedOff = drop;
    }

    public boolean isPickedUp() {
        return this.pickedUp;
    }

    public boolean isDroppedOff() {
        return this.droppedOff;
    }
}