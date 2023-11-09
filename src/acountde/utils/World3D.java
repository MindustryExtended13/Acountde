package acountde.utils;

import acountde.Acountde;
import acountde.content.ACDimensions;
import acountde.content.ACRegistry;
import acountde.dimension.Dimension;
import acountde.dimension.Level;
import arc.math.geom.Point3;
import arc.math.geom.Vec3;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Tile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class World3D {
    public static Dimension getDimAtZ(int z) {
        return ACRegistry.dimensions().get(z);
    }

    public static int z(@NotNull Level level) {
        return z(level.instance);
    }

    public static int z(Dimension dimension) {
        return ACRegistry.dimensions().indexOf(dimension);
    }

    @Contract("_ -> new")
    public static @NotNull Vec3 getVecPos(@NotNull Tile tile) {
        return new Vec3(tile.x, tile.y, getTilePos(tile).z);
    }

    @Contract("_ -> new")
    public static @NotNull Vec3 getVecPos(@NotNull Building building) {
        return new Vec3(building.x, building.y, getPos(building).z);
    }

    @Contract("_ -> new")
    public static @NotNull Point3 getTilePos(@NotNull Tile tile) {
        int[] z = new int[] {z(ACDimensions.overworld)};
        Acountde.server.loaded.each((dim, level) -> {
            level._0.tiles.eachTile(t -> {
                if(t == tile) {
                    z[0] = z(dim);
                }
            });
        });
        Vars.world.tiles.eachTile(t -> {
            if(t == tile) {
                z[0] = z(Acountde.server.current);
            }
        });
        return new Point3(tile.x, tile.y, z[0]);
    }

    @Contract("_ -> new")
    public static @NotNull Point3 getPos(Building building) {
        int[] z = new int[] {z(ACDimensions.overworld)};
        Acountde.server.loaded.each((dim, level) -> {
            ACUtil.selectBuild(level._0);
            if(ACTmp.seq1b.contains(building)) {
                z[0] = z(dim);
            }
        });
        ACUtil.selectBuild();
        if(ACTmp.seq1b.contains(building)) {
            z[0] = z(Acountde.server.current);
        }
        return new Point3(building.tileX(), building.tileY(), z[0]);
    }
}