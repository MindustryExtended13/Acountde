package acountde.content;

import acountde.types.ShimmerEffect;
import mindustry.type.StatusEffect;

import static acountde.content.ACRegistry.*;

public class ACStatusEffects {
    public static StatusEffect corrupted, shimmered, shimmerBuff, shimmerTransmutation;

    public static void load() {
        corrupted = register(new StatusEffect("corrupted") {{
            speedMultiplier = 0.5f;
            damage = 5f;
        }});

        shimmered = register(new ShimmerEffect("shimmered") {{
            type = Type.AFTER_MUTATION;
            speedMultiplier = 2;
        }});

        shimmerTransmutation = register(new ShimmerEffect("shimmer-transmutation") {{
            type = Type.TRANSMUTATION;
            damage = 0.1f;
        }});

        shimmerBuff = register(new ShimmerEffect("shimmer-buff") {{
            type = Type.BUFF;
            buildSpeedMultiplier = 4;
            healthMultiplier = 1.5f;
            damageMultiplier = 3;
            reloadMultiplier = 2;
            speedMultiplier = 2;
            damage = -1;
        }});
    }
}
