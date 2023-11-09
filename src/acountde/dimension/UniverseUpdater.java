package acountde.dimension;

import acountde.Acountde;
import acountde.utils.ACTmp;
import acountde.utils.ACUtil;
import arc.ApplicationListener;
import mindustry.Vars;
import mindustry.gen.Building;

import static acountde.Acountde.*;

public class UniverseUpdater implements ApplicationListener {
    public static boolean invalid() {
        boolean[] invalid = new boolean[1];
        server.loaded.each((dim, level) -> {
            level._0.tiles.eachTile(t -> {
                if(t == null) {
                    invalid[0] = true;
                }
            });
        });
        Vars.world.tiles.eachTile(t -> {
            if(t == null) {
                invalid[0] = true;
            }
        });
        return invalid[0];
    }

    @Override
    public void update() {
        if(!Acountde.doingUpdate() || invalid()) {
            return; //don`t update if server not ready
        }

        server.loaded.each((dim, level) -> {
            if(server.current != dim) {
                var old = Vars.world;
                Vars.world = level._0;
                ACUtil.selectBuild(level._0);
                ACTmp.seq1b.each(Building::update);
                level.subAll.each(entity -> {
                    if(!(entity instanceof Building)) {
                        entity.update();
                    }
                });
                Vars.world = old;
            }
        });
    }
}