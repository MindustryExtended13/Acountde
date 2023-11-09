package acountde.dimension;

import arc.Core;
import arc.math.geom.Point2;
import mindustry.Vars;

public class Dimension {
    public String localizedName, name;

    public void generateLevel(Level level) {
    }

    public Point2 getSize() {
        return null;
    }

    public Dimension(String name) {
        localizedName = Core.bundle.get("dimension." + Vars.content.transformName(name));
        this.name = name;
    }

    @Override
    public String toString() {
        return localizedName;
    }
}