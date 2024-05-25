package com.example.javaproto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Graph sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdf = findViewById(R.id.customGraphView);

        // Initialize some nodes
        Node node1 = new Node(100, 200, "A", 3);
        Node node2 = new Node(500, 200, "B", 5);
        Node node3 = new Node(500, 600, "C", 3);
        Node node4 = new Node(700, 800, "D", 4);
        Node node5 = new Node(900, 600, "E", 5);

        // add them to graph
        sdf.addNode(node1);
        sdf.addNode(node2);
        sdf.addNode(node3);
        sdf.addNode(node4);
        sdf.addNode(node5);

        Edge edge1 = new Edge(node1, node2, 1, 2, 12);
        Edge edge2 = new Edge(node3, node4, 15, 14, 23);
        Edge edge3 = new Edge(node4, node5, 3, 9, 5);
        Edge edge4 = new Edge(node4, node2, 1, 11, 3);

        sdf.addEdge(edge1);
        sdf.addEdge(edge2);
        sdf.addEdge(edge3);
        sdf.addEdge(edge4);

    }
}
