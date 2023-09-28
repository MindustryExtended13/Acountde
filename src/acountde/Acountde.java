package acountde;

import acountde.content.ACBlocks;
import acountde.content.ACDimensions;
import acountde.content.ACRegistry;
import acountde.content.ACUnits;
import acountde.data.ACData;
import acountde.dimension.AcountdeServer;
import acountde.world.blocks.mark.CampaignContent;
import acountde.world.blocks.mark.DeveloperContent;
import acountde.world.blocks.mark.SandboxContent;
import arc.Core;
import arc.Events;
import me13.core.bundle2.Bundle2;
import me13.core.logger.ILogger;
import me13.core.logger.LoggerFactory;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.mod.Mod;
import mindustry.mod.Mods;

public class Acountde extends Mod {
    public static final String MOD_NAME = Acountde.class.getSimpleName();
    public static final ILogger LOGGER = LoggerFactory.build(MOD_NAME);
    public static final String MOD_ID = MOD_NAME.toLowerCase();
    public static Mods.LoadedMod instance;
    public static AcountdeServer server;

    public static String getPrefix(String name) {
        return Core.bundle.get("prefixes." + MOD_ID + "-" + name);
    }

    public static String get(String name) {
        return Core.bundle.get(MOD_ID + "." + name);
    }

    public Acountde() {
        LOGGER.info("Loaded mod constructor");

        Events.on(EventType.SaveLoadEvent.class, (e) -> {
            if(!server.disableBetaConfiguration) {
                server.restart();
                server.read();
            }
        });

        Events.on(EventType.SaveWriteEvent.class, (e) -> {
            server.save();
        });

        Events.on(EventType.ClientLoadEvent.class, (e) -> {
            ACData.updateSaveDir();
            for(UnlockableContent c : ACRegistry.content()) {
                if(c instanceof CampaignContent) {
                    c.description = getPrefix("campaign")  + c.description;
                }
                if(c instanceof DeveloperContent) {
                    c.description = getPrefix("developer") + c.description;
                }
                if(c instanceof SandboxContent) {
                    c.description = getPrefix("sandbox")   + c.description;
                }
            }
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
        Bundle2.load(instance = Vars.mods.getMod(Acountde.class));
        LOGGER.info("Loading mod content");

        ACUnits.load();
        ACBlocks.load();
        ACDimensions.load();
    }
}