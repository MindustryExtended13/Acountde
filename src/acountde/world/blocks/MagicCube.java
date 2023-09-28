package acountde.world.blocks;

import acountde.Acountde;
import acountde.content.ACRegistry;
import acountde.dimension.Dimension;
import acountde.world.blocks.mark.DeveloperContent;
import arc.scene.ui.layout.Table;
import mindustry.gen.Building;
import mindustry.ui.Styles;
import mindustry.world.Block;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("InnerClassMayBeStatic")
public class MagicCube extends Block implements DeveloperContent {
    public MagicCube(String name) {
        super(name);
        destructible = true;
        configurable = true;
    }

    public class MagicCubeBuild extends Building {
        @Override
        public void buildConfiguration(@NotNull Table table) {
            table.table(t -> {
                t.setBackground(Styles.grayPanel);
                t.add(Acountde.get("you_in") + " " + Acountde.server.current.localizedName).pad(3).row();
                for(Dimension dimension : ACRegistry.dimensions()) {
                    t.button(dimension.localizedName, Styles.grayt, () -> {
                        Acountde.server.changeDimension(dimension);
                    }).growX().height(35).pad(3).row();
                }
            }).minWidth(300);
        }
    }
}