package acountde.content;

import arc.graphics.g2d.Fill;
import arc.math.Interp;
import arc.math.Rand;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static arc.graphics.g2d.Draw.alpha;
import static arc.graphics.g2d.Draw.color;
import static arc.math.Angles.randLenVectors;

import static acountde.content.ACRegistry.*;

public class ACFx {
    public static final Rand rand = new Rand();

    public static final Effect toxicTowerExplosion = register(new Effect(30, 500f, b -> {
        float intensity = 6.8f;
        b.lifetime = 50f + intensity * 65f;

        color(b.color);
        alpha(0.4f);
        for(int i = 0; i < 4; i++){
            rand.setSeed(b.id * 2L + i);
            float lenScl = rand.random(0.4f, 1f);
            int fi = i;
            b.scaled(b.lifetime * lenScl, e -> {
                randLenVectors(e.id + fi - 1, e.fin(Interp.pow10Out), (int)(2f * intensity), 11f * intensity, (x, y, in, out) -> {
                    float fout = e.fout(Interp.pow5Out) * rand.random(0.5f, 1f);
                    float rad = fout * ((2f + intensity) * 2.35f);

                    Fill.circle(e.x + x, e.y + y, rad);
                    Drawf.light(e.x + x, e.y + y, rad * 2.5f, Pal.reactorPurple, 0.5f);
                });
            });
        }
    }));
}
