package com.github.davidmoten.graph;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;

final class SeedFillColor<V> implements Transformer<V, Paint> {
    protected PickedInfo<V> pi;
    protected final static float dark_value = 0.8f;
    protected final static float light_value = 0.2f;
    protected boolean seed_coloring;

    public SeedFillColor(PickedInfo<V> pi) {
        this.pi = pi;
        seed_coloring = false;
    }

    public void setSeedColoring(boolean b) {
        this.seed_coloring = b;
    }

    // public Paint getDrawPaint(V v)
    // {
    // return Color.BLACK;
    // }

    @Override
    public Paint transform(V v) {
        return Color.YELLOW;
    }
}