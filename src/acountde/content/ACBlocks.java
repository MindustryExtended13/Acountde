package acountde.content;

import acountde.anno.AddMark;
import acountde.anno.MarkType;
import acountde.world.blocks.MagicCube;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.graphics.CacheLayer;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.TreeBlock;
import mindustry.world.meta.BuildVisibility;

import static acountde.content.ACRegistry.*;

public class ACBlocks {
    @AddMark({MarkType.DEVELOPER, MarkType.SANDBOX})
    public static Block magicCube;
    @AddMark(MarkType.DEVELOPER)
    public static Block testFloor, lightTestFloor, darkTestFloor,
            solidFloor, solidFloor1, solidFloor2, solidFloor3, solidFloor4;
    public static Block deepEnzor, enzor, palmTree;

    public static void load() {
        magicCube = register(new MagicCube("magic-cube") {{
            requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.empty);
            size = 2;
        }});

        testFloor      = register(new Floor("test-floor",       0));
        lightTestFloor = register(new Floor("light-test-floor", 0));
        darkTestFloor  = register(new Floor("dark-test-floor",  0));
        solidFloor     = register(new Floor("solid-floor",      4));
        solidFloor1    = register(new Floor("solid-floor1",     0));
        solidFloor2    = register(new Floor("solid-floor2",     0));
        solidFloor3    = register(new Floor("solid-floor3",     0));
        solidFloor4    = register(new Floor("solid-floor4",     0));

        deepEnzor = register(new Floor("deep-enzor", 0) {{
            speedMultiplier = 0.2f;
            liquidDrop = Liquids.water;
            liquidMultiplier = 1.5f;
            isLiquid = true;
            status = StatusEffects.wet;
            statusDuration = 120f;
            drownTime = 200f;
            cacheLayer = CacheLayer.water;
            albedo = 0.9f;
            supportsOverlay = true;
        }});

        enzor = register(new Floor("shallow-enzor", 0) {{
            speedMultiplier = 0.5f;
            status = StatusEffects.wet;
            statusDuration = 90f;
            liquidDrop = Liquids.water;
            isLiquid = true;
            cacheLayer = CacheLayer.water;
            albedo = 0.9f;
            supportsOverlay = true;
        }});

        palmTree = register(new TreeBlock("palm-tree"));
    }
}