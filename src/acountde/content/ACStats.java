package acountde.content;

import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class ACStats {
    public static Stat shimmerTransformation;

    public static void load() {
        shimmerTransformation = new Stat("shimmer-transformation", StatCat.function);
    }
}