package acountde.content;

import mindustry.type.StatusEffect;

import static acountde.content.ACRegistry.*;

public class ACStatusEffects {
    public static StatusEffect corrupted;

    public static void load() {
        corrupted = register(new StatusEffect("corrupted") {{
            speedMultiplier = 0.5f;
            damage = 5f;
        }});
    }
}
