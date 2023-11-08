package acountde.content;

import acountde.graphics.ACPal;
import acountde.types.AnimatedLiquid;
import mindustry.type.Liquid;

import static acountde.content.ACRegistry.register;

public class ACLiquids {
    public static Liquid shimmer;

    public static void load() {
        shimmer = register(new AnimatedLiquid("shimmer-liquid", ACPal.shimmer) {{
            lightColor = ACPal.shimmer.cpy().a(0.5f);
            particleEffect = ACFx.shimmerShimming;
            effect = ACStatusEffects.shimmerBuff;
            sprites = 2;
        }});
    }
}