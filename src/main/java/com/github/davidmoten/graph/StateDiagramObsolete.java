package com.github.davidmoten.graph;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class StateDiagramObsolete {

    public static void main(String[] args) {
        // Graph<String, Number> g = TestGraphs.getOneComponentGraph();
        Graph<String, String> g = createTestGraph();

        System.out.println(g);
        FRLayout<String, String> layout = new FRLayout<String, String>(g);
        layout.initialize();
        layout.setSize(new Dimension(600, 600));

        // printLayout(layout);
        while (!layout.done()) {
            try {
                Thread.sleep(00);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (layout) {
                layout.step();
            }
        }
        AtomicReference<JPanel> panel = showGui(layout);
        SwingUtilities.invokeLater(() -> panel.get().repaint());
    }

    private static AtomicReference<JPanel> showGui(FRLayout<String, String> layout) {
        AtomicReference<JPanel> ref = new AtomicReference<JPanel>();
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setSize(layout.getSize());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("State Diagram");
            JPanel panel = new MyPanel(layout);
            ref.set(panel);
            frame.getContentPane().add(panel, BorderLayout.CENTER);
            frame.setVisible(true);
        });
        return ref;
    }

    private static class MyPanel extends JPanel {

        private static final long serialVersionUID = -9085027052515908648L;

        private final FRLayout<String, String> layout;

        public MyPanel(FRLayout<String, String> layout) {
            this.layout = layout;
        }

        @Override
        protected void paintComponent(Graphics gOld) {
            super.paintComponent(gOld);
            Graphics2D g = (Graphics2D) gOld;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            for (String edge : layout.getGraph().getEdges().stream().limit(2000)
                    .collect(Collectors.toList())) {
                Pair<String> edges = layout.getGraph().getEndpoints(edge);
                Point d1 = getVertexLocation(layout, edges.getFirst());
                Point d2 = getVertexLocation(layout, edges.getSecond());
                int h = 30;
                if (d1.x == d2.x && d1.y == d2.y) {
                    d2.x += 5;
                    d2.y += 5;
                }
                Point cp = getControlPoint(d1.x, d1.y, d2.x, d2.y, h, true);
                QuadCurve2D.Double q = new QuadCurve2D.Double(d1.x, d1.y, cp.x, cp.y, d2.x, d2.y);
                g.draw(q);
                // g.drawLine(d1.x, d1.y, cp.x, cp.y);
                // g.drawLine(d1.x, d1.y, d2.x, d2.y);
            }
            for (String vertex : layout.getGraph().getVertices()) {
                Point2D d2d = layout.transform(vertex);
                Point d = new Point((int) Math.round(d2d.getX()), (int) Math.round(d2d.getY()));
                int w = g.getFontMetrics().stringWidth(vertex);
                int margin = 20;
                Rectangle box = new Rectangle(d.x - w / 2 - margin,
                        d.y - g.getFontMetrics().getAscent() - margin, w + 2 * margin,
                        g.getFontMetrics().getHeight() + 2 * margin);
                g.clearRect(box.x, box.y, box.width, box.height);
                g.drawRect(box.x, box.y, box.width, box.height);
                g.drawString(vertex, d.x - w / 2, d.y);
            }
        }

    }

    static Point getControlPoint(int x1, int y1, int x2, int y2, int h, boolean positive) {
        int sign = positive ? 1 : -1;
        int x3 = (x1 + x2) / 2;
        int y3 = (y1 + y2) / 2;
        double d = Math.sqrt((y3 - y1) * (y3 - y1) + (x3 - x1) * (x3 - x1));
        int y = (int) Math.round(y3 + sign * h * (x3 - x1) / d);
        int x = (int) Math.round(x3 - sign * h * (y3 - y1) / d);
        return new Point(x, y);
    }

    private static Point getVertexLocation(FRLayout<String, String> layout, String vertex) {
        return new Point((int) Math.round(50 + 0.8 * layout.getX(vertex)),
                (int) Math.round(50 + 0.8 * layout.getY(vertex)));
    }

    public static Graph<String, String> createTestGraph() {
        Graph<String, String> g = new DirectedSparseGraph<String, String>();
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

}
