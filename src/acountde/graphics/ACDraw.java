package acountde.graphics;

import acountde.Acountde;
import arc.Core;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import betamindy.graphics.Drawm;
import mindustry.game.Team;
import mindustry.gen.Puddle;
import mindustry.graphics.Layer;
import mindustry.type.Liquid;
import mindustry.world.Block;
import arc.graphics.g2d.PixmapRegion;
import mindustry.graphics.MultiPacker;
import org.jetbrains.annotations.NotNull;

public class ACDraw {
    public static TextureRegion particle() {
        return Acountde.region("particle");
    }

    public static TextureRegion arrow() {
        return Acountde.region("arrow");
    }

    public static TextureRegion moon() {
        return Acountde.region("moon");
    }

    public static void generateTeamRegions(MultiPacker packer, @NotNull Block b) {
        PixmapRegion teamr = Core.atlas.getPixmap(b.name + "-team");

        for(int i = 0; i < Team.all.length; ++i) {
            Team team = Team.all[i];
            if (team.hasPalette) {
                Pixmap out = new Pixmap(teamr.width, teamr.height);

                for(int x = 0; x < teamr.width; ++x) {
                    for(int y = 0; y < teamr.height; ++y) {
                        int color = teamr.getRaw(x, y);
                        int index = color == -1 ? 0 :
                                (color == -590952705 ? 1 : (color == -1652588545 ? 2 : -1));
                        out.setRaw(x, y, index == -1 ? teamr.getRaw(x, y) : team.palettei[index]);
                    }
                }

                packer.add(MultiPacker.PageType.main, b.name + "-team-" + team.name, out);
            }
        }

        b.load();
    }
}
