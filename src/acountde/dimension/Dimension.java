package acountde.dimension;

import acountde.dimension.content.TestDimension;
import arc.math.geom.Point2;
import arc.struct.Seq;

public class Dimension {
    public static final Seq<Dimension> all = new Seq<>();

    public static final Dimension OVERWORLD = new Dimension();
    public static final TestDimension TEST = new TestDimension();

    public void generateLevel(Level level) {
    }

    public Point2 getSize() {
        return null;
    }

    public Dimension() {
        all.add(this);
    }
}