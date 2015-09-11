package com.github.davidmoten.graph;

import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer;

public class StateDiagram {
    public static void main(String[] args) {
        Graph<String, String> g = createTestGraph();
        FRLayout<String, String> layout = new FRLayout<String, String>(g);
        while (!layout.done())
            layout.step();

        // layout.setSize(new Dimension(500, 500));
        VisualizationViewer<String, String> vv = new VisualizationViewer<String, String>(layout,
                new Dimension(600, 600));
        PickedState<String> pickedState = vv.getPickedVertexState();
        {
            Transformer<String, String> transformer = new Transformer<String, String>() {
                @Override
                public String transform(String vertex) {
                    return vertex;
                }
            };
            vv.getRenderContext().setVertexLabelTransformer(transformer);
        }
        Transformer<String, Paint> seedFillVertex = new SeedFillColor<>(pickedState);
        Transformer<String, Paint> seedDrawVertex = new SeedDrawColor<>(pickedState);
        vv.getRenderContext().setVertexFillPaintTransformer(seedFillVertex);
        vv.getRenderContext().setVertexDrawPaintTransformer(seedDrawVertex);
        {
            Transformer<String, String> transformer = new Transformer<String, String>() {
                @Override
                public String transform(String arg0) {
                    return arg0;
                }
            };
            vv.getRenderContext().setEdgeLabelTransformer(transformer);
        }
        vv.getRenderContext().setVertexShapeTransformer(createVertexShapeTransformer(layout));

        vv.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        // The following code adds capability for mouse picking of
        // vertices/edges. Vertices can even be moved!
        final DefaultModalGraphMouse<String, Number> graphMouse = new DefaultModalGraphMouse<String, Number>();
        vv.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);

        JFrame frame = new JFrame();
        JPanel surround = new JPanel();
        surround.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        surround.add(vv);
        frame.getContentPane().add(surround);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }

    private static Transformer<String, Shape> createVertexShapeTransformer(
            Layout<String, String> layout) {
        return new Transformer<String, Shape>() {

            @Override
            public Shape transform(String vertex) {
                int w = 100;
                int margin = 5;
                int ascent = 12;
                int descent = 5;
                Shape shape = new Rectangle(-w / 2 - margin, -ascent - margin, w + 2 * margin,
                        (ascent + descent) + 2 * margin);
                return shape;
            }
        };
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