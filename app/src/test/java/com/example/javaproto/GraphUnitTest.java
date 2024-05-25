package com.example.javaproto;

import org.junit.Test;

import static org.junit.Assert.*;
import android.content.Context;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
//public class ExampleUnitTest {
//    @Test
//    public void addition_isCorrect() {
//        assertEquals(4, 2 + 2);
//    }
//}

@RunWith(RobolectricTestRunner.class)
public class GraphUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testAddNode() {
        Context context = RuntimeEnvironment.getApplication();
        Graph graph = new Graph(context, null);
        Node newNode = new Node(100, 200, "A", 3);
        graph.addNode(newNode);

        // Assert that the node was added
        assertTrue(graph.getAdjacencyList().containsKey(newNode));
    }

    @Test
    public void testRemoveNode() {
        Context context = RuntimeEnvironment.getApplication();
        Graph graph = new Graph(context, null);
        Node node1 = new Node(200, 300, "B", 4);
        Node node2 = new Node(400, 800, "C", 6);
        Node node3 = new Node(500, 600, "D", 2);
        graph.addNode(node1);
        graph.addNode(node2);
        graph.addNode(node3);
        graph.addEdge(new Edge(node1, node2, 1, 2, 0));
        graph.addEdge(new Edge(node2, node3, 3, 4, 0));
        graph.removeNode(node1);
        graph.removeNode(node1);
        // Assert that node 1 was removed
        assertFalse(graph.getAdjacencyList().containsKey(node1));

        // all the associated edges from and to node 1 should also be removed
        for (List<Edge> edges : graph.getAdjacencyList().values()) {
            for (Edge edge : edges) {
                assertNotEquals(edge.from, node1);
                assertNotEquals(edge.to, node1);
            }
        }
        // node 2 and node 3 should still be there
        assertTrue(graph.getAdjacencyList().containsKey(node2));
        assertTrue(graph.getAdjacencyList().containsKey(node3));
    }

    @Test
    public void testAddEdge() {
        Context context = RuntimeEnvironment.getApplication();
        Graph graph = new Graph(context, null);
        Node nodeA = new Node(100, 100, "A", 1);
        Node nodeB = new Node(200, 200, "B", 2);
        Edge edge = new Edge(nodeA, nodeB, 10, 1, 1);

        // Act
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addEdge(edge);

        // Assert
        assertTrue(graph.getAdjacencyList().containsKey(nodeA));
        assertTrue(graph.getAdjacencyList().get(nodeA).contains(edge));
    }

    @Test
    public void testRemoveEdge() {
        Context context = RuntimeEnvironment.getApplication();
        Graph graph = new Graph(context, null);
        Node nodeA = new Node(100, 100, "A", 1);
        Node nodeB = new Node(200, 200, "B", 2);
        Edge edge = new Edge(nodeA, nodeB, 10, 1, 1);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addEdge(edge);

        // Act
        graph.removeEdge(edge);

        // Assert
        assertFalse(graph.getAdjacencyList().get(nodeA).contains(edge));
    }

    @Test
    public void testGetEdgesFrom() {
        Context context = RuntimeEnvironment.getApplication();
        Graph graph = new Graph(context, null);
        Node nodeA = new Node(100, 100, "A", 1);
        Node nodeB = new Node(200, 200, "B", 2);
        Edge edgeAB = new Edge(nodeA, nodeB, 10, 1, 1);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addEdge(edgeAB);

        // Act
        List<Edge> edgesFromA = graph.getEdgesFrom(nodeA);

        // Assert
        assertTrue(edgesFromA.contains(edgeAB));
    }


    @Test
    public void testGetEdgesTo() {
        Context context = RuntimeEnvironment.getApplication();
        Graph graph = new Graph(context, null);
        Node nodeA = new Node(100, 100, "A", 1);
        Node nodeB = new Node(200, 200, "B", 2);
        Edge edgeAB = new Edge(nodeA, nodeB, 10, 1, 1);
        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addEdge(edgeAB);

        // Act
        List<Edge> edgesToB = graph.getEdgesTo(nodeB);

        // Assert
        assertTrue(edgesToB.contains(edgeAB));
    }

    @Test
    public void testGenerateDefaultName() {
        // Arrange
        Context context = RuntimeEnvironment.getApplication();
        Graph graph = new Graph(context, null);

        String defaultName = graph.generateDefaultName();

        // Assert
        assertEquals("Z", defaultName);
    }





}
