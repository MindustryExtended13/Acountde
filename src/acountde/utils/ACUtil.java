package acountde.utils;

import arc.func.Boolf;
import arc.math.geom.Rect;
import arc.struct.IntSeq;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Hitboxc;
import mindustry.mod.Mods.LoadedMod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ACUtil {
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
        ACTmp.seq1b.clear();
        Vars.world.tiles.eachTile(t -> {
            Building building = t.build;
            if(building != null && !ACTmp.seq1b.contains(building)) {
                ACTmp.seq1b.add(building);
            }
        });
    }

    public static void selectTiles(Boolf<Building> b) {
        selectBuild();
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