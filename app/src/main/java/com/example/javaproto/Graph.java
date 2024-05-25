package com.example.javaproto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Graph extends View {
    private Map<Node, List<Edge>> adjacencyList;
    private Paint nodePaint, edgePaint, textPaint, bufferPaint;
    int radius = 65;
    private Node selectedNode = null;
    private static final long DOUBLE_CLICK_TIME_DELTA = 300; // milliseconds
    private long lastClickTime = 0;
    private Node lastClickedNode = null;
    private Edge lastClickedEdge = null;

    private Node longPressNode = null;
    private final long LONG_PRESS_THRESHOLD = 500;

    private float initialTouchX, initialTouchY;

    private boolean addingEdge = false;
    private long touchDownTime = 0;


    public Graph(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init() {
        this.adjacencyList = new HashMap<>();
        nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nodePaint.setColor(0xFF0000FF); // Blue for nodes
        nodePaint.setStyle(Paint.Style.STROKE);
        nodePaint.setStrokeWidth(4);

        edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        edgePaint.setColor(0xFF000000); // Black for edges
        edgePaint.setStrokeWidth(5);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(0xFFFF0000); // Red for text
        textPaint.setTextSize(30);

        bufferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bufferPaint.setColor(0xFF000000);
        bufferPaint.setStyle(Paint.Style.FILL);
    }
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw all the nodes
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            Node currNode = entry.getKey();
            drawNode(currNode, canvas);
        }
        // Draw all the edges
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            for (Edge edge : entry.getValue()) {
                drawEdge(edge, canvas);
            }
        }
    }
    private void drawNode(Node node, Canvas canvas) {
        // Draw a Node
        canvas.drawCircle(node.x, node.y, radius, nodePaint);
        // calculate the position of the text i.e. the name/label of the node
        // use Rect object to measure dimensions
        Rect textBounds = new Rect();
        textPaint.getTextBounds(node.name, 0, node.name.length(), textBounds);
        float textX = node.x - textBounds.width() / 2f;
        float textY = node.y + textBounds.width() / 2f;
        canvas.drawText(node.name, textX, textY, textPaint);

    }
    public void drawEdge(Edge edge, Canvas canvas) {
        // Calculate direction vector (dx, dy) from 'from' node to 'to' node
        float dx = edge.to.x - edge.from.x;
        float dy = edge.to.y - edge.from.y;

        // Calculate the distance between nodes
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // Normalize the direction vector (make its length equal to 1)
        dx /= distance;
        dy /= distance;
        // Scale the direction vector by the radius to get the offset from the node's center
        dx *= this.radius;
        dy *= this.radius;

        // Calculate the start and end points for the edge
        float startX = edge.from.x + dx;
        float startY = edge.from.y + dy;
        float endX = edge.to.x - dx;
        float endY = edge.to.y - dy;
        // Draw the edge
        canvas.drawLine(startX, startY, endX, endY, edgePaint);

        // Draw the buffer associated with edge
        float bufferX = (startX + endX) / 2;
        float bufferY = (startY + endY) / 2;
        edge.buffer.x = bufferX;
        edge.buffer.y = bufferY;
        String bufferValue = String.valueOf(edge.buffer.value);

        Rect textBounds = new Rect();
        textPaint.getTextBounds(bufferValue, 0, bufferValue.length(), textBounds);
        // additional padding
        int padding = 20;
        float bufferWidth = textBounds.width() + padding;
        float bufferHeight = textBounds.height() + padding;
        canvas.drawRect(bufferX - bufferWidth / 2,
                        bufferY - bufferHeight / 2,
                       bufferX + bufferWidth / 2,
                     bufferY + bufferHeight / 2,
                            bufferPaint);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(bufferValue, bufferX, bufferY + textBounds.height() / 2 - textPaint.descent(), textPaint);

        // Draw producers and consumers for the edges
        String producer = String.valueOf(edge.producerVal);
        String consumer = String.valueOf(edge.consumerVal);
        // textPaint.getTextBounds(producer , 0, producer.length(), textBounds);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(producer, startX, startY, textPaint);
        canvas.drawText(consumer, endX, endY, textPaint);

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        float edgeBufferRange = radius * 0.8f;

        switch (event.getAction()) {
//
            case MotionEvent.ACTION_DOWN:
                long clickTime = System.currentTimeMillis();
                initialTouchX = touchX;
                initialTouchY = touchY;
                touchDownTime = System.currentTimeMillis();
                for (Node node : adjacencyList.keySet()) {
                    if (Math.hypot(touchX - node.x, touchY - node.y) <= radius) {
                        if (node.equals(lastClickedNode) && clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                            // Double Press : Edit Node
                            showEditNodeDialog(node);
                            lastClickTime = 0;
                            lastClickedNode = null;
                        } else {
                            if (addingEdge && node != longPressNode) {
                                this.addEdge(new Edge(longPressNode, node, 1, 2, 4));
                                addingEdge = false;
                                longPressNode = null;
                                nodePaint.setColor(0xFF0000FF);
                                return true;
                            }
                            // Not a double click
                            lastClickedNode = node;
                            lastClickTime = clickTime;
                            selectedNode = node;
                            longPressNode = node; // potentially a long press so record the node
                        }
                        return true;
                    }
                }
                for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
                    for (Edge edge : entry.getValue()) {
                        if (Math.hypot(touchX - edge.buffer.x, touchY - edge.buffer.y) <= edgeBufferRange) {
                            if (edge.equals(lastClickedEdge) && clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                                showEditEdgeDialog(edge);
                                lastClickTime = 0;
                                lastClickedEdge = null;
                            }
                            else {
                                lastClickedEdge = edge;
                                lastClickTime = clickTime;
                            }
                            return true;
                        }
                    }
                }
                // White space clicked, means add a new node where clicked
                Node newNode = new Node(touchX, touchY, generateDefaultName(), 0);
                this.addNode(newNode);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (selectedNode != null) {
                    // Move the selected node with the touch
                    selectedNode.x = touchX;
                    selectedNode.y = touchY;
                    invalidate(); // Request to redraw the view
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                long currTime = System.currentTimeMillis();
                if (Math.abs(initialTouchX - touchX) <= 0.002f && Math.abs(initialTouchY - touchY) <= 0.002f && currTime - touchDownTime >= LONG_PRESS_THRESHOLD) {
                    // Long Press detected: Activate add edge mode
                    addingEdge = true;
                    nodePaint.setColor(Color.YELLOW);
                    invalidate();
                }
                selectedNode = null; // Deselect the node
                return true;
        }
        return super.onTouchEvent(event); // If we didn't handle the touch event, pass it on
    }
    String generateDefaultName() {
        return "Z";
    }
    private void showEditEdgeDialog(final Edge edge) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Edge Properties");

        // Custom layout for edge properties
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create edit texts for each property
        final TextView producerLabel = new TextView(getContext());
        producerLabel.setText("Producer Value:");
        layout.addView(producerLabel);
        final EditText producerInput = new EditText(getContext());
        producerInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        producerInput.setHint("Enter Producer value here");
        producerInput.setText(String.valueOf(edge.producerVal));
        layout.addView(producerInput);

        final TextView consumerLabel = new TextView(getContext());
        consumerLabel.setText("Consumer Value:");
        layout.addView(consumerLabel);
        final EditText consumerInput = new EditText(getContext());
        consumerInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        consumerInput.setHint("Enter Consumer value here");
        consumerInput.setText(String.valueOf(edge.consumerVal));
        layout.addView(consumerInput);

        final TextView bufferValueLabel = new TextView(getContext());
        bufferValueLabel.setText("Buffer Value");
        layout.addView(bufferValueLabel);
        final EditText bufferInput = new EditText(getContext());
        bufferInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        bufferInput.setHint("Enter Buffer value");
        bufferInput.setText(String.valueOf(edge.buffer.value));
        layout.addView(bufferInput);

        final TextView edgeLabel = new TextView(getContext());
        edgeLabel.setText("Edge Label:");
        layout.addView(edgeLabel);
        final EditText labelInput = new EditText(getContext());
        labelInput.setInputType(InputType.TYPE_CLASS_TEXT);
        labelInput.setHint("Enter a label for the edge (optional)");
        if (edge.label != null) {  labelInput.setText(edge.label); }
        layout.addView(labelInput);

        builder.setView(layout);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            Log.d("Graph", "Updating edge values");
            edge.producerVal = Integer.parseInt(producerInput.getText().toString());
            Log.d("Graph", "New Edge Producer Value: " + edge.producerVal);
            edge.consumerVal = Integer.parseInt(consumerInput.getText().toString());
            Log.d("Graph", "New Edge Consumer Value: " + edge.consumerVal);
            edge.buffer.value = Integer.parseInt(bufferInput.getText().toString());
            Log.d("Graph", "New Edge Buffer Value: " + edge.buffer.value);
            edge.label = labelInput.getText().toString();
            invalidate();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    private void showEditNodeDialog(final Node node) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Edit Node Name");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(node.name); // Set the current name as default
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                node.name = input.getText().toString(); // Set the new name
                invalidate(); // Redraw to show the new name
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeNode(node);
            }
        });
        builder.show();
    }

    public void addNode(Node newNode) {
        adjacencyList.putIfAbsent(newNode, new ArrayList<>());
        invalidate(); // Redraw
    }
    public void removeNode(Node node) {
        // Remove the node
        adjacencyList.remove(node);

        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            List<Edge> edges = entry.getValue();
            Iterator<Edge> iterator = edges.iterator();
            while (iterator.hasNext()) {
                Edge edge = iterator.next();
                if (edge.to.equals(node)) {
                    iterator.remove();
                }
            }
        }
        invalidate();
    }
    public void addEdge(Edge newEdge) {
        this.addNode(newEdge.from);
        this.addNode(newEdge.to);

        adjacencyList.get(newEdge.from).add(newEdge);

        if (newEdge.bidirectional) {
            // for later
        }
        invalidate(); // Redraw
    }

    public void removeEdge(Edge edge) {
        Node from = edge.from;

        if (adjacencyList.containsKey(from)) {
            adjacencyList.get(from).remove(edge);
        }
    }
    public List<Edge> getEdgesFrom(Node node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }
    public List<Edge> getEdgesTo(Node node) {
        List<Edge> incomingEdges = new ArrayList<>();
        for (Map.Entry<Node, List<Edge>> entry : adjacencyList.entrySet()) {
            for (Edge edge : entry.getValue()) {
                if (edge.to.equals(node)) {
                    incomingEdges.add(edge);
                }
            }
        }
        return incomingEdges;
    }

    // acts as a getter for the current state of the graph
    public Map<Node, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }
}