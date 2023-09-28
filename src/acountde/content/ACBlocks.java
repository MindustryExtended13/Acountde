package acountde.content;

import acountde.world.blocks.MagicCube;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

import static acountde.content.ACRegistry.*;

public class ACBlocks {
    public static Block magicCube;

    public static void load() {
        magicCube = register(new MagicCube("magic-cube") {{
            requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.empty);
            size = 2;
        }});
    }
}