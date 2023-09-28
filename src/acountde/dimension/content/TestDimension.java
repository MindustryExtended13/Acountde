package acountde.dimension.content;

import acountde.dimension.Dimension;
import acountde.dimension.Level;
import arc.math.geom.Point2;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.world.blocks.environment.Floor;
import org.jetbrains.annotations.NotNull;

public class TestDimension extends Dimension {
    @Override
    public void generateLevel(@NotNull Level level) {
        level.tiles.eachTile(tile -> {
            tile.setFloor((Floor) Blocks.salt);
        });
        level.tiles.get(17, 17).setNet(Blocks.coreShard, Vars.player.team(), 0);
    }

    @Override
    public Point2 getSize() {
        return new Point2(35, 35);
    }

    public TestDimension() {
        super();
    }
}
