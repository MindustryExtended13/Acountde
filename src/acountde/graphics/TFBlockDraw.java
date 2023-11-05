package acountde.graphics;

import arc.Core;
import arc.func.Boolf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.gen.Building;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TFBlockDraw {
    @Contract(value = " -> new", pure = true)
    public static String @NotNull [] getTFMappingId() {
        return new String[] {
                "ffff", "tfff", "ftff", "ttff",
                "fftf", "tftf", "fttf", "tttf",
                "ffft", "tfft", "ftft", "ttft",
                "fftt", "tftt", "fttt", "tttt"
        };
    }

    public static TextureRegion @NotNull[] loadRegions(String prefix) {
        TextureRegion[] regions = new TextureRegion[16];
        int i = 0;
        for(String s : getTFMappingId()) {
            regions[i++] = Core.atlas.find(prefix + s);
        }
        return regions;
    }

    public static TextureRegion getRegion(TextureRegion[] regions,
                                          Building building,
                                          Seq<Building> prox,
                                          Boolf<Point2> provider) {
        byte bits = 0;
        for(int i = 0; i < 4; i++) {
            Point2 point2 = Geometry.d4(i).cpy().add(building.tileX(), building.tileY());
            if(prox.contains(Vars.world.build(point2.x, point2.y)) && provider.get(point2)) {
                bits += 1 << i;
            }
        }
        return regions[bits];
    }

    public static void drawRegions(TextureRegion[] regions,
                                   Color color,
                                   boolean rotate,
                                   @NotNull Building building,
                                   Seq<Building> prox,
                                   Boolf<Point2> provider) {
        float x = building.x;
        float y = building.y;
        float r = rotate ? building.drawrot() : 0;
        if(color != null) {
            Draw.color(color);
        }
        Draw.rect(getRegion(regions, building, prox, provider), x, y, r);
        Draw.reset();
    }
}