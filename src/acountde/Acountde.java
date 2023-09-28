package acountde;

import acountde.dimension.AcountdeServer;
import acountde.dimension.Dimension;
import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import me13.core.logger.ILogger;
import me13.core.logger.LoggerFactory;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

public class Acountde extends Mod {
    public static final String MOD_NAME = Acountde.class.getSimpleName();
    public static final ILogger LOGGER = LoggerFactory.build(MOD_NAME);
    public static final String MOD_ID = MOD_NAME.toLowerCase();
    public static AcountdeServer server;

    public Acountde() {
        LOGGER.info("Loaded mod constructor");

        Events.on(EventType.SaveWriteEvent.class, (e) -> {
            server.changeDimension(Dimension.OVERWORLD);
        });
    }

    @Override
    public void init() {
        LOGGER.info("Mod init");
        LOGGER.info("Loading server");
        server = new AcountdeServer();
    }

    @Override
    public void loadContent() {
        LOGGER.info("Loading mod content");
    }
}