package acountde.world.blocks;

import mindustry.gen.Building;
import mindustry.world.Block;

public class ACBlock extends Block {
    public ACBlock(String name) {
        super(name);
        destructible = true;
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class ACBuild extends Building {
    }
}