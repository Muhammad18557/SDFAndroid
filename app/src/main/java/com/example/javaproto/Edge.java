package com.example.javaproto;
public class Edge {
    Buffer buffer;
    int producerVal, consumerVal;
    String label;
    Node from, to;
    boolean bidirectional = false;
    public Edge(Node from, Node to, int producer, int consumer, int bufferVal) {
        this.buffer = new Buffer(bufferVal);
        this.from = from;
        this.to = to;
        this.producerVal = producer;
        this.consumerVal = consumer;
    }
    public Edge(Node from, Node to, int producer, int consumer, int bufferVal, boolean bidirectional) {
        this(from, to, producer, consumer, bufferVal);
        this.bidirectional = true;
    }

}

