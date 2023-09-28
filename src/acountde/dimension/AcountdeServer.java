package acountde.dimension;

import acountde.content.ACDimensions;
import acountde.content.ACRegistry;
import arc.files.Fi;
import arc.math.geom.Point2;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.Teams;
import mindustry.gen.Call;
import mindustry.io.SaveIO;
import mindustry.world.Tiles;

import java.io.IOException;

import static mindustry.Vars.*;
import static mindustry.Vars.ui;

public class AcountdeServer {
    public ObjectMap<Dimension, Level> loaded = new ObjectMap<>();
    public Seq<Dimension> saveIODimensions = new Seq<>();
    public Dimension current = ACDimensions.overworld;
    public boolean disableBetaConfiguration = false;

    public void save() {
        Fi stateFile = LevelSaves.getStateFile();
        assert stateFile != null;
        if(!stateFile.exists()) {
            try {
                if(!stateFile.file().createNewFile()) {
                    throw new RuntimeException("Can`t create state file");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try(Writes writes = stateFile.writes(false)) {
            Seq<Dimension> dims = loaded.keys().toSeq();
            writes.i(dims.size);
            dims.each(d -> {
                writes.str(d.name);
            });
        }
    }

    public void read() {
        Fi stateFile = LevelSaves.getStateFile();
        assert stateFile != null;
        if(stateFile.exists()) {
            try(Reads reads = stateFile.reads()) {
                int size = reads.i();
                for(int i = 0; i < size; i++) {
                    saveIODimensions.add(ACRegistry.dimension(reads.str()));
                }
            }
        }
    }

    public void restart() {
        current = ACDimensions.overworld;
        saveIODimensions.clear();
        loaded.clear();
    }

    public void resetTeamData() {
        Vars.state.teams = new Teams();
    }

    public void loadData(Fi fi, Runnable loaded) {
        ui.loadAnd(() -> {
            ui.paused.hide();
            try {
                net.reset();
                disableBetaConfiguration = true;
                SaveIO.load(fi);
                disableBetaConfiguration = false;
                state.rules.editor = false;
                state.rules.sector = null;
                state.set(GameState.State.playing);
                loaded.run();
            } catch (SaveIO.SaveException e){
                Log.err(e);
                logic.reset();
                ui.showErrorMessage("@save.corrupted");
            }
        });
    }

    public void changeDimension(Dimension to) {
        if(to == current) return;
        getLevel(current).save(Vars.world);
        Fi fi = LevelSaves.getLevelFile(current);
        if(fi != null) {
            if(!fi.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    fi.file().createNewFile();
                } catch(Throwable ignored) {
                }
            }

            SaveIO.write(fi);
        }
        boolean move = false;
        if(saveIODimensions.contains(to)) {
            Fi fi2 = LevelSaves.getLevelFile(to);
            if(fi2 != null && fi2.exists()) {
                loadData(fi2, () -> {
                    getLevel(to).save(Vars.world);
                });
            } else {
                move = true;
            }
        } else {
            move = true;
        }
        if(move) {
            getLevel(to).move(Vars.world);
        }
        saveIODimensions.remove(to);
        Call.unitDespawn(Vars.player.unit());
        if(!Vars.headless) {
            Vars.control.input.config.hideConfig();
        }
        if(move) {
            getLevel(to).save(Vars.world);
        }
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