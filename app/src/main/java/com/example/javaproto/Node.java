package com.example.javaproto;

class Node {
    float x, y;
    String name;
    int payload = 0;
    public Node(float xPos, float yPos, String nodeLabel) {
        this.x = xPos;
        this.y = yPos;
        this.name = nodeLabel;
    }
    public Node(float xPos, float yPos, String nodeLabel, int executionValue) {
        this.x = xPos;
        this.y = yPos;
        this.name = nodeLabel;
        this.payload = executionValue;
    }
}
