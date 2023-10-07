package acountde;

import acountde.anno.HandleEvent;
import acountde.content.*;
import acountde.data.ACData;
import acountde.dimension.AcountdeServer;
import acountde.gen.EventsSetup;
import acountde.gen.MarksSetup;
import acountde.ui.AcountdeDialogInit;
import acountde.utils.MPS;
import arc.Core;
import arc.graphics.Color;
import me13.core.bundle2.Bundle2;
import me13.core.logger.ILogger;
import me13.core.logger.LoggerFactory;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import mindustry.game.EventType.*;
import mindustry.ui.dialogs.BaseDialog;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Acountde extends Mod {
    public static final String MOD_GITHUB = "https://github.com/MindustryExtended13/Acountde";
    public static final String MOD_NAME = Acountde.class.getSimpleName();
    public static final ILogger LOGGER = LoggerFactory.build(MOD_NAME);
    public static final String MOD_ID = MOD_NAME.toLowerCase();
    public static Mods.LoadedMod instance;
    public static AcountdeServer server;

    public static String get(String name) {
        return Core.bundle.get(MOD_ID + "." + name);
    }

    public static @NotNull String toString(@NotNull Throwable throwable) {
        final StringWriter sw = new StringWriter();
        final PrintWriter pw = new PrintWriter(sw, true);
        throwable.printStackTrace(pw);
        return sw.getBuffer().toString();
    }

    public static void fail(Throwable throwable) {
        String trace = toString(throwable);
        BaseDialog dialog = new BaseDialog("ERROR");
        dialog.add(trace).color(Color.red);
        dialog.show();
        LOGGER.err(trace);
    }

    @HandleEvent(SaveLoadEvent.class)
    public static void saveLoadEvent() {
        if(!server.disableBetaConfiguration) {
            server.restart();
            server.read();
        }
    }

    @HandleEvent(SaveWriteEvent.class)
    public static void saveWriterEvent() {
        server.save();
    }

    @HandleEvent(ClientLoadEvent.class)
    public static void clientLoad() {
        ACData.updateSaveDir();
        MarksSetup.init();
        AcountdeDialogInit.load();

        String tmp;
        for(UnlockableContent content : ACRegistry.content()) {
            tmp = content.name + "-preview";
            if(Core.atlas.has(tmp)) {
                content.uiIcon = Core.atlas.find(tmp);
            }
        }

        /*
        BaseDialog dialog = new BaseDialog("label-test");
        dialog.addCloseButton();
        UIUtil.labelButton(dialog.cont, "example label", () -> {
            LOGGER.info("Perfecto!");
        });
        dialog.show();
         */
    }

    public Acountde() {
        LOGGER.info("Loaded mod constructor");
        EventsSetup.init();
    }

    @Override
    public void init() {
        LOGGER.info("Mod init");
        MPS.init();

        LOGGER.info("Loading server");
        server = new AcountdeServer();
    }

    @Override
    public void loadContent() {
        Bundle2.load(instance = Vars.mods.getMod(Acountde.class));
        LOGGER.info("Loading mod content");

        ACUnits.load();
        ACBlocks.load();
        ACPlanets.load();
        ACDimensions.load();
    }
}