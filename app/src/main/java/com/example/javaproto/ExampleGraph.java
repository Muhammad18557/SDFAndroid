//package com.example.javaproto;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Rect;
//import android.util.AttributeSet;
//import android.view.View;
//
//import java.util.ArrayList;
//import java.util.List;
//
//class ExNode {
//    float x, y;
//    String value;
//    String producer;
//    String consumer;
//
//    public ExNode(float x, float y, String value, String producer, String consumer) {
//        this.x = x;
//        this.y = y;
//        this.value = value;
//        this.producer = producer;
//        this.consumer = consumer;
//    }
//}
//
//class ExEdge {
//    ExNode from, to;
//    String payload;
//
//    public ExEdge(ExNode from, ExNode to, String payload) {
//        this.from = from;
//        this.to = to;
//        this.payload = payload;
//    }
//
//}
//
//public class ExampleGraph extends View {
//
//    private List<ExNode> nodes = new ArrayList<>();
//    private List<ExEdge> edges = new ArrayList<>();
//    private Paint nodePaint, edgePaint, textPaint;
//
//    public ExampleGraph(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init();
//    }
//
//    private void init() {
//        nodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        nodePaint.setColor(0xFF0000FF); // Blue for nodes
//        nodePaint.setStyle(Paint.Style.STROKE);
//        nodePaint.setStrokeWidth(4);
//
//
//        edgePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        edgePaint.setColor(0xFF000000); // Black for edges
//        edgePaint.setStrokeWidth(5);
//
//        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        textPaint.setColor(0xFFFF0000); // Red for text
//        textPaint.setTextSize(30);
//
//        Paint textPaint_white = new Paint(Paint.ANTI_ALIAS_FLAG);
//        textPaint_white.setColor(0xFFFFFFFF); // Red for text
//        textPaint_white.setTextSize(25);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//
//        int radius = 65;
//
//        // Draw edges
//        for (ExEdge edge : edges) {
//            // Calculate direction vector (dx, dy) from 'from' node to 'to' node
//            float dx = edge.to.x - edge.from.x;
//            float dy = edge.to.y - edge.from.y;
//            // Calculate the distance between nodes
//            float distance = (float) Math.sqrt(dx * dx + dy * dy);
//            // Normalize the direction vector (make its length equal to 1)
//            dx /= distance;
//            dy /= distance;
//            // Scale the direction vector by the radius to get the offset from the node's center
//            dx *= radius;
//            dy *= radius;
//            // Calculate the start and end points for the edge
//            float startX = edge.from.x + dx;
//            float startY = edge.from.y + dy;
//            float endX = edge.to.x - dx;
//            float endY = edge.to.y - dy;
//
//            canvas.drawLine(startX, startY, endX, endY, edgePaint);
//
//            float textX = (startX + endX) / 2;
//            float textY = (startY + endY) / 2;
//
//            // Measure text size
//            Rect textBounds = new Rect();
//            textPaint.getTextBounds(edge.payload, 0, edge.payload.length(), textBounds);
//
//            // Calculate rectangle size around text, with some padding
//            int padding = 10;
//            Rect rect = new Rect(
//                    (int) textX - (textBounds.width() / 2) - padding,
//                    (int) textY - (textBounds.height() / 2) - padding,
//                    (int) textX + (textBounds.width() / 2) + padding,
//                    (int) textY + (textBounds.height() / 2) + padding
//            );
//
//            Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//            rectPaint.setColor(0xFF000000);
//            rectPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//
//            // Draw the rectangle
//            canvas.drawRect(rect, rectPaint);
//
//            // Adjust text coordinates to center it inside the rectangle
//            canvas.drawText(edge.payload, textX - (textBounds.width() / 2f), textY + (textBounds.height() / 2f) - textPaint.descent(), textPaint);
//        }
//
//        // Draw nodes
//        for (ExNode node : nodes) {
//            canvas.drawCircle(node.x, node.y, radius, nodePaint); // Draw node
//
//            Rect textBounds = new Rect();
//            textPaint.getTextBounds(node.value, 0, node.value.length(), textBounds);
//            float textX = node.x - textBounds.width() / 2f;
//            float textY = node.y + textBounds.height() / 2f;
//            canvas.drawText(node.value, textX, textY, textPaint);
//
//
//            // Draw incoming value to the left of the node
//            textPaint.getTextBounds(node.producer, 0, node.producer.length(), textBounds);
//            textX = node.x - radius - textBounds.width() - 10; // Adjust position to the left of the node
//            textY = node.y + textBounds.height() / 2f;
//            canvas.drawText(node.producer, textX, textY, textPaint);
//
//            // Draw outgoing value to the right of the node
//            textPaint.getTextBounds(node.producer, 0, node.producer.length(), textBounds);
//            textX = node.x + radius + 10; // Adjust position to the right of the node
//            textY = node.y + textBounds.height() / 2f;
//            canvas.drawText(node.producer, textX, textY, textPaint);
//
//        }
//    }
//
//
//    public void addNode(ExNode node) {
//        nodes.add(node);
//        invalidate(); // Redraw
//    }
//
//    public void addEdge(ExEdge edge) {
//        edges.add(edge);
//        invalidate(); // Redraw
//    }
//}
//
