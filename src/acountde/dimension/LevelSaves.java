package acountde.dimension;

import acountde.data.ACData;
import arc.files.Fi;
import org.jetbrains.annotations.Nullable;

public class LevelSaves {
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
        return child.child(dimension.name + ".msav");
    }

    /*
    public static void save(Level level, Fi fi) {
        if(level == null || fi == null) return;
        if(!fi.exists()) {
            try {
                if(!fi.file().createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try(Writes writes = fi.writes(false)) {
            save(level, writes);
        }
    }

    public static void save(@NotNull Level level, @NotNull Writes writes) {
        var tiles = level.tiles;
        writes.i(level.instance.id());
        writes.i(tiles.width);
        writes.i(tiles.height);
        tiles.eachTile(t -> {
            writes.i(t.floorID());
            if(t.overlay() != null) {
                writes.i(t.overlayID());
            } else {
                writes.i(-1);
            }
        });
        Seq<Building> buildings = level.buildings();
        writes.i(buildings.size);
        buildings.each(building -> {
            writes.i(building.block.id);
            writes.i(building.tileX());
            writes.i(building.tileY());
            building.writeAll(writes);
        });
        Seq<Unit> units = level.units();
        writes.i(units.size);
        units.each(unit -> {
            writes.i(unit.type.id);
            unit.write(writes);
        });
    }

    @Contract("null -> null")
    public static Level read(Fi fi) {
        if(fi == null || !fi.exists()) return null;
        try(Reads reads = fi.reads()) {
            return read(reads);
        }
    }

    public static @NotNull Level read(@NotNull Reads reads) {
        Level level = new Level();
        level.loaded = true;
        level.instance = ACRegistry.dimension(reads.i());
        level.tiles = new Tiles(reads.i(), reads.i());
        level.tiles.fill();
        level.tiles.eachTile(t -> {
            t.setFloor(Vars.content.block(reads.i()).asFloor());
            int overlay = reads.i();
            if(overlay != -1) {
                t.setOverlay(Vars.content.block(overlay));
            } else {
                t.clearOverlay();
            }
        });
        int len = reads.i();
        for(int i = 0; i < len; i++) {
            Block block = Vars.content.block(reads.i());
            Tile out = level.tiles.get(reads.i(), reads.i());
            out.setNet(block);
            Building building = out.build;
            building.readAll(reads, (byte) 0);
            building.updateProximity();
            level.subAll.add(building);
        }
        len = reads.i();
        for(int i = 0; i < len; i++) {
            UnitType type = Vars.content.unit(reads.i());
            Unit unit = type.constructor.get();
            unit.read(reads);
            level.subAll.add(unit);
        }
        level.tiles.eachTile(tile -> {
            if(tile.build != null) {
                tile.build.updateProximity();
            }
        });
        return level;
    }
    */
}