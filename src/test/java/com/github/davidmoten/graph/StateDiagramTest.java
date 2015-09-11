package com.github.davidmoten.graph;

import static org.junit.Assert.assertEquals;

import java.awt.Point;

import org.junit.Test;

public class StateDiagramTest {

    @Test
    public void testHorizontalLine() {
        Point point = StateDiagramObsolete.getControlPoint(0, 0, 100, 0, 10, true);
        assertEquals(50, point.x);
        assertEquals(10, point.y);
    }

    @Test
    public void testHorizontalLineReversed() {
        Point point = StateDiagramObsolete.getControlPoint(100, 0, 0, 0, 10, true);
        assertEquals(50, point.x);
        assertEquals(-10, point.y);
    }

    @Test
    public void testVerticalLine() {
        Point point = StateDiagramObsolete.getControlPoint(0, 0, 0, 100, 10, true);
        assertEquals(-10, point.x);
        assertEquals(50, point.y);
    }

    @Test
    public void testVerticalLineReversed() {
        Point point = StateDiagramObsolete.getControlPoint(0, 100, 0, 0, 10, true);
        assertEquals(10, point.x);
        assertEquals(50, point.y);
    }

}
