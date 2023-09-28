package acountde.dimension;

import arc.math.geom.Point2;
import arc.struct.ObjectMap;
import mindustry.Vars;
import mindustry.game.Teams;
import mindustry.gen.Call;
import mindustry.world.Tiles;

public class AcountdeServer {
    public ObjectMap<Dimension, Level> loaded = new ObjectMap<>();
    public Dimension current = Dimension.OVERWORLD;

    public void resetTeamData() {
        Vars.state.teams = new Teams();
    }

    public void changeDimension(Dimension to) {
        getLevel(current).save(Vars.world);
        getLevel(to).move(Vars.world);
        Call.unitDespawn(Vars.player.unit());
        current = to;
    }

    public Level getLevel(Dimension to) {
        if(loaded.containsKey(to)) {
            return loaded.get(to);
        } else {
            Level level = new Level();
            Point2 p = to.getSize();
            if(p != null) {
                level.tiles = new Tiles(p.x, p.y);
                level.tiles.fill();
            }
            level.instance = to;
            loaded.put(to, level);
            return level;
        }
    }
}