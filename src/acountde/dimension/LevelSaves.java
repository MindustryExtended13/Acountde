package acountde.dimension;

import acountde.Acountde;
import acountde.data.ACData;
import arc.files.Fi;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.gen.Building;
import mindustry.gen.EntityMapping;
import mindustry.gen.Entityc;
import mindustry.world.Tile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LevelSaves {
    public static final String LEVEL_MAP_EXTENSION = ".artificial_map_data";

    public static @Nullable Fi getStateFile() {
        Fi dir = ACData.getSaveDir();
        if(dir == null) return null;
        return dir.child("levels.data");
    }

    public static @Nullable Fi getLevelFile(Dimension dimension) {
        Fi levelDir = ACData.getSaveDir();
        if(levelDir == null) return null;
        Fi child = levelDir.child("levels");
        if(!child.exists()) child.mkdirs();
        return child.child(dimension.name + LEVEL_MAP_EXTENSION);
    }

    public static void read(@NotNull Reads reads, @NotNull Level level) {
        int height = reads.i();
        int width = reads.i();
        level._0.resize(width, height);
        level._0.tiles.fill();
        World old = Vars.world;
        Vars.world = level._0;
        int s;
        try {
            s = reads.i();
            for(int i = 0; i < s; i++) {
                int floorID = reads.i();
                int overlayID = reads.i();
                int blockID = reads.i();
                int posScalar = reads.i();
                Tile tile = level._0.tile(posScalar);
                if(floorID != -1) {
                    tile.setFloor(Vars.content.block(floorID).asFloor());
                }
                if(overlayID != -1) {
                    tile.setOverlay(Vars.content.block(overlayID));
                }
                if(blockID != -1) {
                    tile.setBlock(Vars.content.block(blockID));
                }
                if(reads.bool()) {
                    int posScalar2 = reads.i();
                    if(posScalar2 == posScalar) {
                        tile.build.readAll(reads, tile.build.version()); //#revision_does_nothing
                    }
                }
            }
        } catch(Throwable throwable) {
            Acountde.LOGGER.err("Failed to load buildings region of level [{}]", level.instance);
            Acountde.fail(throwable);
        }
        try {
        } catch(Throwable throwable) {
            Acountde.LOGGER.err("Failed to load buildings region of level [{}]", level.instance);
            Acountde.fail(throwable);
        }
        try {
            s = reads.i();
            for(int i = 0; i < s; i++) {
                int classID = reads.i();
                Entityc entity = (Entityc) EntityMapping.map(classID).get();
                entity.read(reads);
                entity.afterRead();
            }
        } catch(Throwable throwable) {
            Acountde.LOGGER.err("Failed to load entities region of level [{}]", level.instance);
            Acountde.fail(throwable);
        }
        Vars.world = old;
    }

    public static void save(@NotNull Writes writes, @NotNull Level level) {
        try {
            writes.i(level._0.tiles.height);
            writes.i(level._0.tiles.width);
            Seq<TileData> dataSeq = new Seq<>();
            level._0.tiles.eachTile(t -> {
                TileData data = new TileData();
                if(t.floor() != null) {
                    data.floorID = t.floorID();
                }
                if(t.overlay() != null) {
                    data.overlayID = t.overlayID();
                }
                if(t.build == null || t.build.tile == t) {
                    if(t.block() != null) {
                        data.blockID = t.blockID();
                    } else if(t.build != null) {
                        data.blockID = t.build.block.id;
                    }
                }
                data.tileInstance = t;
                dataSeq.add(data);
            });
            writes.i(dataSeq.size);
            for(TileData dat : dataSeq) {
                writes.i(dat.floorID);
                writes.i(dat.overlayID);
                writes.i(dat.blockID);
                Tile tile = dat.tileInstance;
                writes.i(tile.pos());
                writes.bool(tile.build != null);
                if (tile.build != null) {
                    Tile t = tile.build.tile;
                    writes.i(t.pos());
                    if (t == tile) {
                        tile.build.writeAll(writes);
                    }
                }
            }
            Seq<Entityc> x = level.subAll.select(e -> {
                return !(e instanceof Building);
            });
            writes.i(x.size);
            for(Entityc entityc : x) {
                writes.i(entityc.classId());
                entityc.write(writes);
            }
        } catch(Throwable throwable) {
            Acountde.LOGGER.err("Failed to save data of level [{}]", level.instance);
            Acountde.fail(throwable);
        }
    }

    public static class TileData {
        public int floorID = -1;
        public int overlayID = -1;
        public int blockID = -1;
        public Tile tileInstance;
    }
}