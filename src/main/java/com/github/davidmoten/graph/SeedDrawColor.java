package com.github.davidmoten.graph;

import java.awt.Color;
import java.awt.Paint;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.picking.PickedInfo;

final class SeedDrawColor<V> implements Transformer<V, Paint> {
    protected PickedInfo<V> pi;
    protected final static float dark_value = 0.8f;
    protected final static float light_value = 0.2f;
    protected boolean seed_coloring;

    public SeedDrawColor(PickedInfo<V> pi) {
        this.pi = pi;
        seed_coloring = false;
    }

    public void setSeedColoring(boolean b) {
        this.seed_coloring = b;
    }

    @Override
    public Paint transform(V v) {
        return Color.BLACK;
    }

    // public Paint getFillPaint(V v)
    // {
    // float alpha = transparency.get(v).floatValue();
    // if (pi.isPicked(v))
    // {
    // return new Color(1f, 1f, 0, alpha);
    // }
    // else
    // {
    // if (seed_coloring && seedVertices.contains(v))
    // {
    // Color dark = new Color(0, 0, dark_value, alpha);
    // Color light = new Color(0, 0, light_value, alpha);
    // return new GradientPaint( 0, 0, dark, 10, 0, light, true);
    // }
    // else
    // return new Color(1f, 0, 0, alpha);
    // }
    //
    // }
}
