package acountde.utils;

import arc.func.Boolf;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.gen.Building;
import mindustry.gen.Hitboxc;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.mod.Mods.LoadedMod;
import mindustry.type.UnitType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ACUtil {
    @Contract(pure = true)
    public static float layerOf(@NotNull Unit unit) {
        return unit.elevation > 0.5f ? (unit.type.lowAltitude ? Layer.flyingUnitLow : Layer.flyingUnit) :
                unit.type.groundLayer + Mathf.clamp(unit.hitSize / 4000f, 0, 0.01f);
    }

    public static @NotNull Unit setType(@NotNull Unit unit, UnitType type) {
        if(unit.type != type) {
            boolean setPlayerUnit = Vars.player.unit() == unit;
            float rotation = unit.rotation;
            unit.remove();
            Unit out = type.spawn(unit);
            out.rotation = rotation;
            if(setPlayerUnit) {
                Vars.player.unit(out);
            }
            return out;
        } else {
            return unit;
        }
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Vec2 relative(@NotNull Unit unit, float x, float y) {
        return new Vec2(
                unit.x + Angles.trnsx(unit.rotation - 90, x, y),
                unit.y + Angles.trnsy(unit.rotation - 90, x, y)
        );
    }

    @Contract(pure = true)
    public static float det(float a, float b, float c, float d) {
        return a * d - b * c;
    }

    public static float rou(float a) {
        String str = String.valueOf(a);
        int i = str.indexOf('.');
        if(i == -1) return a;
        return Float.parseFloat(str.substring(0, Math.min(i + 3, str.length())));
    }

    public static boolean isModEnabled(String name) {
        return isModEnabled(Vars.mods.getMod(name));
    }

    @Contract("null -> false")
    public static boolean isModEnabled(LoadedMod mod) {
        return mod != null && mod.enabled();
    }

    public static void asRect(@NotNull Building building) {
        float s = building.block.size * 8f;
        ACTmp.rect1.set(building.x - s / 2, building.y - s / 2, s, s);
    }

    public static void asRect(@NotNull Hitboxc hitboxc) {
        float s = hitboxc.hitSize();
        ACTmp.rect1.set(hitboxc.x() - s / 2, hitboxc.y() - s / 2, s, s);
    }

    public static boolean rectLinkValid(@NotNull Building self, @NotNull Building other, int radius) {
        int scalar = Math.max(Math.abs(self.tileX() - other.tileX()), Math.abs(self.tileY() - other.tileY()));
        //Acountde.LOGGER.info("Scalar value of {} is {}", other, scalar);
        return scalar < radius;
    }

    public static void selectBuild() {
        selectBuild(Vars.world);
    }

    public static void selectBuild(@NotNull World world) {
        ACTmp.seq1b.clear();
        world.tiles.eachTile(t -> {
            if(t == null) {
                return;
            }

            Building building = t.build;
            if(building != null && !ACTmp.seq1b.contains(building)) {
                ACTmp.seq1b.add(building);
            }
        });
    }

    public static void selectTiles(Boolf<Building> b) {
        selectTiles(Vars.world, b);
    }

    public static void selectTiles(World world, Boolf<Building> b) {
        selectBuild(world);
        ACTmp.switchSeq();
        ACTmp.seq1b.clear();
        ACTmp.seq2b.each(bu -> {
            if(b.get(bu)) {
                ACTmp.seq1b.add(bu);
            }
        });
    }

    public static Integer[] toInt(@NotNull IntSeq seq) {
        return toInt(seq.toArray());
    }

    @Contract(value = "null -> null", pure = true)
    public static Integer[] toInt(int[] array) {
        if(array == null) return null;
        Integer[] out = new Integer[array.length];
        for(int i = 0; i < array.length; i++) {
            out[i] = array[i];
        }
        return out;
    }

    @Contract(pure = true)
    public static boolean collides(@NotNull Rect a, @NotNull Rect b) {
        return a.x <= b.x + b.width && a.x + a.width >= b.x
                && a.y <= b.y + b.height && a.y + a.height >= b.y;
    }
}