package com.github.davidmoten.graph;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.QuadCurve2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class Main {

    public static void main(String[] args) {
        // Graph<String, Number> g = TestGraphs.getOneComponentGraph();
        Graph<String, String> g = createTestGraph();

        System.out.println(g);
        FRLayout<String, String> layout = new FRLayout<String, String>(g);
        layout.initialize();
        layout.setSize(new Dimension(600, 600));

        displayLayout(layout);
        while (!layout.done()) {
            layout.step();
        }
        displayLayout(layout);
        System.out.println(g);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(layout.getSize());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("State Diagram");
            frame.setVisible(true);
            JPanel panel = new MyPanel(layout);
            frame.getContentPane().add(panel);

        });

    }

    private static class MyPanel extends JPanel {

        private final FRLayout<String, String> layout;

        public MyPanel(FRLayout<String, String> layout) {
            this.layout = layout;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (String edge : layout.getGraph().getEdges()) {
                Pair<String> edges = layout.getGraph().getEndpoints(edge);
                Point d1 = getVertexLocation(layout, edges.getFirst());
                Point d2 = getVertexLocation(layout, edges.getSecond());
                int diffX = d2.x - d1.x;
                int diffY = d2.y - d1.y;
                double propX;
                double propY;
                if (diffX != 0 && diffY != 0) {
                    propX = (double) diffX / diffY;
                    propY = (double) diffY / diffX;
                } else if (diffX == 0 && diffY == 0) {
                    propX = 0;
                    propY = 0;
                } else if (diffX == 0) {
                    propX = 1;
                    propY = 0;
                } else {
                    propX = 0;
                    propY = 1;
                }
                double h = 30;
                double dx = Math.max(h, h * propX);
                double dy = Math.max(h, h * propY);

                double sign = Math.random() > 0.5 ? 1 : -1;

                double controlX = (d1.x + d2.x) / 2 + sign * dx;
                double controlY = (d1.y + d2.y) / 2 + sign * dy;
                QuadCurve2D.Double q = new QuadCurve2D.Double(d1.x, d1.y, controlX, controlY, d2.x,
                        d2.y);
                g2.draw(q);
                // g2.drawLine(d1.x, d1.y, d2.x, d2.y);
            }
            for (String vertex : layout.getGraph().getVertices()) {
                Point d = getVertexLocation(layout, vertex);
                g2.drawString(vertex, d.x - g2.getFontMetrics().stringWidth(vertex) / 2, d.y);
            }
        }

    }

    private static Point getVertexLocation(FRLayout<String, String> layout, String vertex) {
        return new Point((int) Math.round(50 + 0.8 * layout.getX(vertex)),
                (int) Math.round(50 + 0.8 * layout.getY(vertex)));
    }

    public static Graph<String, String> createTestGraph() {
        Graph<String, String> g = new SparseMultigraph<String, String>();
        g.addVertex("Created");
        g.addVertex("Inside");
        g.addVertex("Entered");
        g.addVertex("Never Outside");
        g.addVertex("Outside");
        g.addEdge("In1", "Created", "Never Outside", EdgeType.DIRECTED);
        g.addEdge("In2", "Entered", "Inside", EdgeType.DIRECTED);
        g.addEdge("In3", "Inside", "Inside", EdgeType.DIRECTED);
        g.addEdge("In4", "Never Outside", "Never Outside", EdgeType.DIRECTED);
        g.addEdge("In5", "Outside", "Entered", EdgeType.DIRECTED);
        g.addEdge("Out1", "Created", "Outside", EdgeType.DIRECTED);
        g.addEdge("Out2", "Entered", "Outside", EdgeType.DIRECTED);
        g.addEdge("Out3", "Inside", "Outside", EdgeType.DIRECTED);
        g.addEdge("Out4", "Never Outside", "Outside", EdgeType.DIRECTED);
        g.addEdge("Out5", "Outside", "Outside", EdgeType.DIRECTED);
        return g;
    }

    private static void displayLayout(FRLayout<String, String> layout) {
        System.out.println("------------ Layout ---------------");
        for (String vertex : layout.getGraph().getVertices()) {
            System.out.println(vertex + ": " + layout.transform(vertex));
        }
    }
}
