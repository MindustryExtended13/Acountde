package acountde;

import acountde.anno.HandleEvent;
import acountde.content.*;
import acountde.data.ACData;
import acountde.dimension.AcountdeServer;
import acountde.gen.EventsSetup;
import acountde.gen.MarksSetup;
import acountde.invoker.BetaMindyInvoker;
import acountde.types.AnimatedLiquid;
import acountde.ui.AcountdeDialogInit;
import acountde.ui.AcountdeSettings;
import acountde.utils.*;
import acountde.utils.FxUpdater.FxBox;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.TextureRegionDrawable;
import arc.struct.Seq;
import arc.util.Strings;

import me13.core.bundle2.Bundle2;
import me13.core.logger.ILogger;
import me13.core.logger.LoggerFactory;

import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.mod.Mod;
import mindustry.mod.Mods;
import mindustry.game.EventType.*;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;

import static acountde.utils.Shimmer.onMutantChain;
import static mindustry.content.UnitTypes.*;

public class Acountde extends Mod {
    public static final String MOD_GITHUB = "https://github.com/MindustryExtended13/Acountde";
    public static final String MOD_NAME = Acountde.class.getSimpleName();
    public static final ILogger LOGGER = LoggerFactory.build(MOD_NAME);
    public static final String MOD_ID = MOD_NAME.toLowerCase();
    public static Mods.LoadedMod instance;
    public static AcountdeServer server;

    public static boolean isBetaMindyInstalled = false;

    public static boolean doingUpdate() {
        return Vars.world != null && Vars.world.tiles != null && !Vars.state.isPaused();
    }

    public static String get(String name) {
        return Core.bundle.get(MOD_ID + "." + name);
    }

    public static TextureRegion region(String region) {
        return Core.atlas.find(MOD_ID + "-" + region);
    }

    @Contract("_ -> new")
    public static @NotNull TextureRegionDrawable to(TextureRegion region) {
        return new TextureRegionDrawable(region);
    }

    @Contract("_ -> new")
    public static @NotNull TextureRegionDrawable drawable(String region) {
        return to(region(region));
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

    @HandleEvent(EventType.FileTreeInitEvent.class)
    public static void loadShaders() {
        Core.app.post(() -> {
            ACShaders.init();
            ACCacheLayer.init();
        });
    }

    @HandleEvent(SaveWriteEvent.class)
    public static void saveWriterEvent() {
        server.save();
    }

    @HandleEvent(ClientLoadEvent.class)
    public static void clientLoad() {
        MPS.init();
        ACData.updateSaveDir();
        MarksSetup.init();
        AcountdeDialogInit.load();

        FxUpdater.map.put(ACBlocks.shimmer.asFloor(), new FxBox(ACFx.shimmerShimming) {{
            spawnChange = 0.0005f;
        }});

        Seq<AnimatedLiquid> liquidSeq = ACRegistry
                .content()
                .select(c -> c instanceof AnimatedLiquid)
                .map(content -> (AnimatedLiquid) content);

        Events.run(Trigger.update, () -> {
            liquidSeq.each(AnimatedLiquid::update);
        });

        String tmp;
        for(UnlockableContent content : ACRegistry.content()) {
            tmp = content.name + "-preview";
            if(Core.atlas.has(tmp)) {
                content.uiIcon = Core.atlas.find(tmp);
            }
        }

        if(Core.settings.getBool("auto-bundle")) {
            Vars.content.each(c -> {
                if(c instanceof UnlockableContent content) {
                    String x = content.getContentType() + ".";
                    String bundle = content.name + ".name";
                    if(!Core.bundle.has(x + bundle)) {
                        if(!content.isVanilla()) {
                            int i = bundle.indexOf('-');
                            if(i != -1) {
                                content.localizedName = Strings.kebabToCamel(content.name.substring(i));
                            } else {
                                content.localizedName = Strings.kebabToCamel(content.name);
                            }
                        } else {
                            content.localizedName = Strings.kebabToCamel(content.name);
                        }
                        content.localizedName = Strings.insertSpaces(content.localizedName);
                    }
                }
            });
        }
        
        AcountdeSettings.load();
        var binder = LOGGER.atInfo();
        binder.log("units:");
        for(UnitType type : Vars.content.units()) {
            binder.log("\t- {} = {}$", type.localizedName, CostLib.calculateCost(type));
        }
        binder.log("items:");
        for(Item item : Vars.content.items()) {
            binder.log("\t- {} = {}$", item.localizedName, CostLib.calculateCost(item));
        }
        binder.log("liquids:");
        for(Liquid slag : Vars.content.liquids()) {
            binder.log("\t- {} = {}$", slag.localizedName, CostLib.calculateCost(slag));
        }

        ACLiquids.shimmer.canStayOn.addAll(Vars.content.liquids());

        onMutantChain(reign, scepter, fortress, mace, dagger);
        onMutantChain(corvus, vela, quasar, pulsar, nova);
        onMutantChain(toxopid, arkyid, spiroct, atrax, crawler);
        onMutantChain(eclipse, antumbra, zenith, horizon, flare);
        onMutantChain(oct, quad, mega, poly, mono);
        onMutantChain(navanax, aegires, cyerce, oxynoe, retusa);
        onMutantChain(omura, sei, bryde, minke, risso);
        onMutantChain(conquer, vanquish, precept, locus, stell);
        onMutantChain(collaris, tecta, anthicus, cleroi, merui);
        onMutantChain(disrupt, quell, obviate, avert, elude);
        onMutantChain(gamma, beta, alpha);
        onMutantChain(emanate, incite, evoke);
        onMutantChain(latum, renale);

        onMutantChain(dagger, nova, crawler, dagger);
        onMutantChain(stell, merui,  stell);
        onMutantChain(alpha, evoke, alpha);
        onMutantChain(flare, mono, flare);
        onMutantChain(retusa, risso, retusa);

        Shimmer.on(box -> {
            if(box.unitChanged() || box.unit.type != renale) {
                return;
            }

            box.unit.team = Team.all[box.unit.team.id + 1 % Team.all.length];
        });

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

        LOGGER.info("Loading server");
        server = new AcountdeServer();

        LOGGER.info("Loading fx init");
        Core.app.addListener(new FxUpdater());
        Core.app.addListener(new Shimmer());
    }

    @Override
    public void loadContent() {
        Bundle2.load(instance = Vars.mods.getMod(Acountde.class));
        isBetaMindyInstalled = ACUtil.isModEnabled("betamindy");
        LOGGER.info("Loading mod content");

        if(isBetaMindyInstalled) {
            BetaMindyInvoker.load();
        }

        ACStats.load();
        ACLiquids.load();
        ACStatusEffects.load();
        ACUnits.load();
        ACBlocks.load();
        ACPlanets.load();
        ACDimensions.load();
    }
}