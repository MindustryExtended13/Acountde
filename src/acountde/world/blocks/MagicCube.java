package acountde.world.blocks;

import acountde.Acountde;
import acountde.content.ACRegistry;
import acountde.dimension.Dimension;
import acountde.ui.UIUtil;
import arc.scene.ui.layout.Table;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("all")
public class MagicCube extends ACBlock {
    public MagicCube(String name) {
        super(name);
        destructible = true;
        configurable = true;
        update = true;
    }

    public class MagicCubeBuild extends ACBuild {
        @Override
        public void updateTile() {
            //Acountde.LOGGER.info("{}", this);
        }

        @Override
        public void buildConfiguration(@NotNull Table table) {
            table.table(t -> {
                t.setBackground(UIUtil.DTS);
                t.add(Acountde.get("you_in") + " " + Acountde.server.current.localizedName).pad(3).row();
                for(Dimension dimension : ACRegistry.dimensions()) {
                    t.button(dimension.localizedName, UIUtil.DBS, () -> {
                        Acountde.server.changeDimension(dimension);
                    }).growX().height(35).pad(3).row();
                }
            }).minWidth(300);
        }
    }
}