package acountde.content;

import acountde.dimension.Dimension;
import acountde.world.dimensions.TestDimension;

import static acountde.content.ACRegistry.*;

public class ACDimensions {
    public static Dimension overworld, test;

    public static void load() {
        overworld = register(new Dimension("overworld"));
        test = register(new TestDimension());
    }
}