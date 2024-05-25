package com.example.javaproto;
public class Buffer {
    int value;
    Edge edge;

    float x, y;

    // Overload constructor because Java doesn't have default parameter values like Python, C++
    public Buffer(int value) {
        this.value = value;
    }
    public Buffer(int value, Edge edge) {
        this.value = value;
        this.edge = edge;
    }
    public Buffer(int value, float posX, float posY) {
        this(value);
        this.x = posX;
        this.y = posY;
    }

}
