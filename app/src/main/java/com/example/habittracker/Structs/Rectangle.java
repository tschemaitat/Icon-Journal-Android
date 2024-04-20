package com.example.habittracker.Structs;

public class Rectangle {
    private int x;
    private int y;
    private int width;
    private int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean inside(int x, int y){
        if(x > this.x && x < this.x + width && y > this.y && y < this.y + height)
            return true;
        return false;
    }

    public String toString(){
        return "<loc: (" + x + ", " +y+ ") size: (" + width + ", " + height + ")>";
    }
}
