package acountde.dimension;

import arc.Core;
import arc.math.geom.Point2;
import arc.struct.Seq;
import mindustry.Vars;

public class Dimension {
    public static final Seq<Dimension> all = new Seq<>();
    public String localizedName;

    public void generateLevel(Level level) {
    }

    public Point2 getSize() {
        return null;
    }

    public Dimension(String name) {
        localizedName = Core.bundle.get("dimension." + Vars.content.transformName(name));
        all.add(this);
    }
}