package acountde.dimension;

import acountde.Acountde;
import acountde.types.entity.ACDataUnitEntity;
import acountde.utils.ACTmp;
import acountde.utils.ACUtil;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.gen.*;
import mindustry.world.Tiles;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Level {
    public Seq<Entityc> subAll = new Seq<>();
    public World _0 = new World();
    public Dimension instance;
    boolean loaded = false;

    public void save(@NotNull World world) {
        subAll.clear();
        Groups.all.each(e -> {
            if(needInclude(e)) {
               subAll.add(e);
            }
        });

        _0.tiles = new Tiles(world.width(), world.height());
        world.tiles.each((x, y) -> {
            _0.tiles.set(x, y, world.tiles.get(x, y));
        });
    }

    public void move(@NotNull World world) {
        Acountde.server.resetTeamData();
        world.loadGenerator(_0.tiles.width, _0.tiles.height, t -> {
            t.each((x, y) -> t.set(x, y, _0.tiles.get(x, y)));
        });
        float t = Vars.tilesize;
        float f = Vars.finalWorldBounds;
        Groups.resize(-f, -f, _0.tiles.width * t + f/2, _0.tiles.height * t + f/2);
        Groups.all.each(e -> {
            if(needInclude(e)) {
                e.remove();
            }
        });
        subAll.each(Entityc::add);
        ACUtil.selectBuild();
        for(Building b : ACTmp.seq1b) {
            if(!subAll.contains(b)) {
                b.add();
            }
        }
        checkGeneration();
    }

    public void checkGeneration() {
        if(instance != null && !loaded) {
            instance.generateLevel(this);
            loaded = true;
        }
    }

    @Contract(value = "null -> false", pure = true)
    public static boolean needInclude(Entityc e) {
        if(e instanceof ACDataUnitEntity) {
            return false;
        }
        return e instanceof PowerGraphUpdaterc || e instanceof WeatherState
                || e instanceof Building || e instanceof Puddle
                || e instanceof Unit || e instanceof Fire;
    }
}