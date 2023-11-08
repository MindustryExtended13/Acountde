package acountde.utils;

import acountde.Acountde;
import arc.ApplicationListener;
import arc.math.Mathf;
import arc.struct.ObjectMap;
import mindustry.Vars;
import mindustry.entities.Effect;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import org.jetbrains.annotations.Contract;

public class FxUpdater implements ApplicationListener {
    public static final ObjectMap<Floor, FxBox> map = new ObjectMap<>();

    @Override
    public void update() {
        if(Acountde.doingUpdate()) {
            Vars.world.tiles.eachTile(t -> {
                Floor floor = t.floor();
                if(map.containsKey(floor)) {
                    map.get(floor).spawn(t);
                }
            });
        }
    }

    public static class FxBox {
        public final Effect effect;
        public float spawnChange;

        @Contract(pure = true)
        public FxBox(Effect effect) {
            this.effect = effect;
        }

        public void spawn(Tile tile) {
            if(Mathf.chance(spawnChange)) {
                effect.at(
                        tile.drawx() + Mathf.random(-4f, 4f),
                        tile.drawy() + Mathf.random(-4f, 4f)
                );
            }
        }
    }
}