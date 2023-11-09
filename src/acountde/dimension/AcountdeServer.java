package acountde.dimension;

import acountde.Acountde;
import acountde.content.ACDimensions;
import acountde.content.ACRegistry;
import arc.ApplicationListener;
import arc.Core;
import arc.files.Fi;
import arc.math.geom.Point2;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import mindustry.core.GameState;
import mindustry.game.Teams;
import mindustry.io.SaveIO;
import mindustry.world.Tiles;

import static arc.Core.settings;
import static mindustry.Vars.*;

public class AcountdeServer implements ApplicationListener {
    public ObjectMap<Dimension, Level> loaded = new ObjectMap<>();
    public Dimension current = ACDimensions.overworld;
    public boolean disableAlphaConfiguration = false;
    public boolean disableBetaConfiguration = false;
    public int autosaveCounter = saveInterval();
    public Dimension overrideOld;

    public Dimension getOld() {
        return overrideOld == null ? ACDimensions.overworld : overrideOld;
    }

    public void restart() {
        current = ACDimensions.overworld;
        overrideOld = null;
        loaded.clear();
    }

    public boolean isSaved(Dimension dimension) {
        Fi fi = LevelSaves.getLevelFile(dimension);
        return fi != null && fi.exists();
    }

    public void trySave() {
        trySave(loaded.keys().toSeq());
    }

    public void trySave(Seq<Dimension> list) {
        trySave(0, list);
    }

    public void trySave(int i, Seq<Dimension> list) {
        trySave(i, list, current);
    }

    public void trySave(int i, Seq<Dimension> list, Dimension back) {
        if(list == null || list.isEmpty() || i > list.size - 1) return;
        trySave(list.get(i), false, () -> {
            if(i < list.size - 1) {
                trySave(i + 1, list, back);
            } else {
                changeDimension(back);
            }
        });
    }

    public void trySave(Dimension dimension, Runnable loaded) {
        trySave(dimension, true, loaded);
    }

    public void trySave(Dimension dimension, boolean back, Runnable loaded) {
        Dimension old = current;
        Fi fi = LevelSaves.getLevelFile(dimension);
        if(fi != null) {
            if(!fi.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    fi.file().createNewFile();
                } catch(Throwable ignored) {
                }
            }

            changeDimension(dimension, () -> {
                writeData(fi);
                loaded.run();
                if(back) {
                    changeDimension(overrideOld == null ? old : overrideOld);
                }
            });
        }
    }

    public void tryLoad() {
        tryLoad(ACRegistry.dimensions().select(this::isSaved));
    }

    public void tryLoad(Seq<Dimension> list) {
        overrideOld = current;
        tryLoad(0, list);
        overrideOld = null;
    }

    public void tryLoad(int i, Seq<Dimension> list) {
        if(list == null || list.isEmpty() || i > list.size - 1) return;
        tryLoad(list.get(i), i >= list.size - 1, () -> {
            if(i < list.size - 1) tryLoad(i + 1, list);
        });
    }

    public void tryLoad(Dimension dimension, boolean back, Runnable loaded) {
        changeDimension(dimension, () -> {
            loaded.run();
            if(back) {
                changeDimension(getOld());
            }
        });
    }

    public void resetTeamData() {
        state.teams = new Teams();
    }

    public void changeDimension(Dimension to) {
        changeDimension(to, () -> {});
    }

    public void changeDimension(Dimension to, Runnable loaded) {
        Acountde.LOGGER.info("Saving dimension {}", current);
        getLevel(current).save(world);
        Fi fi = LevelSaves.getLevelFile(current);
        if(fi != null) {
            if(!fi.exists()) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    fi.file().createNewFile();
                } catch(Throwable ignored) {
                }
            }
            writeData(fi);
        }
        Acountde.LOGGER.info("Uploading dimension {}", to);
        Fi fi2 = LevelSaves.getLevelFile(to);
        if(fi2 != null && fi2.exists()) {
            loadData(fi2, loaded);
        } else {
            getLevel(to).move(world);
            control.input.config.hideConfig();
            getLevel(to).save(world);
            if(fi2 != null) {
                try {
                    //noinspection ResultOfMethodCallIgnored
                    fi2.file().createNewFile();
                    writeData(fi2);
                    loadData(fi2, () -> {
                        getLevel(to).save(world);
                        loaded.run();
                    });
                } catch(Throwable ignored) {
                }
            }
        }
        getLevel(to).save(world);
        current = to;
    }

    public Level getLevel(Dimension to) {
        if(loaded.containsKey(to)) {
            return loaded.get(to);
        } else {
            Level level = new Level();
            Point2 p = to.getSize();
            if(p != null) {
                level._0.tiles = new Tiles(p.x, p.y);
                level._0.tiles.fill();
            }
            level.instance = to;
            loaded.put(to, level);
            return level;
        }
    }

    public int saveInterval() {
        return Math.max(600, settings.getInt("acountde-save-interval"));
    }

    public void writeData(Fi fi) {
        try {
            disableAlphaConfiguration = true;
            SaveIO.write(fi);
            disableAlphaConfiguration = false;
        } catch(Throwable e) {
            Acountde.LOGGER.err("Failed to save: {}", e);
        }
    }

    public void loadData(Fi fi, Runnable runnable) {
        ui.loadAnd(() -> {
            ui.paused.hide();
            try {
                net.reset();
                disableBetaConfiguration = true;
                SaveIO.load(fi);
                disableBetaConfiguration = false;
                state.rules.editor = false;
                //state.rules.sector = null;
                state.set(GameState.State.playing);
                runnable.run();
            } catch (SaveIO.SaveException e){
                Acountde.LOGGER.err("Error while loading save: {}", e);
                logic.reset();
                ui.showErrorMessage("@save.corrupted");
            }
        });
    }

    @Override
    public void update() {
        if(settings.getBool("acountde-autosave") && Acountde.doingUpdate() && state.isPlaying()) {
            if(autosaveCounter > 0) {
                autosaveCounter--;
            } else {
                autosaveCounter = saveInterval();
                ui.showInfoToast("[ACOUNTDE] Saved", 2);
                trySave();
            }
        }
    }
}