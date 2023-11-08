package acountde.types;

import acountde.Acountde;
import acountde.content.ACFx;
import acountde.content.ACStatusEffects;
import acountde.graphics.ACDraw;
import acountde.graphics.ACPal;
import acountde.utils.ACUtil;
import acountde.utils.Shimmer;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.util.Time;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.type.StatusEffect;

public class ShimmerEffect extends StatusEffect {
    public Type type;

    public ShimmerEffect(String name) {
        super(name);
        effect = ACFx.shimmerShimming;
    }

    @Override
    public void update(Unit unit, float time) {
        super.update(unit, time);
        switch(type) {
            case BUFF -> {}
            case AFTER_MUTATION -> {
                unit.unapply(ACStatusEffects.shimmerTransmutation);
                unit.drownTime(0);
            }
            case TRANSMUTATION -> {
                //3 second maximum
                if(unit.getDuration(this) > 180) {
                    unit.unapply(this);
                    unit.apply(this, 180);
                }

                if(!unit.hasEffect(ACStatusEffects.shimmered) && time < 7f) {
                    Shimmer.Box box = Shimmer.transmutation(unit);
                    Unit out = box.unit;
                    ACFx.shimmerShimmingLarge.at(out);
                    out.apply(ACStatusEffects.shimmered, 180);
                    out.unapply(this);
                    if(!box.unitChanged()) {
                        unit.drownTime(0);
                    }
                }
            }
        }
    }

    @Override
    public void draw(Unit unit, float time) {
        Draw.z(Layer.shields - 9);
        Draw.color(ACPal.shimmer);
        switch(type) {
            case BUFF, AFTER_MUTATION -> {
            }
            case TRANSMUTATION -> {
                if(unit.getDuration(this) > 180) return;
                time = time / 180;

                boolean b = (int) (Time.globalTime % 10) > 4;
                float rot = unit.rotation - 90;
                float hit = unit.hitSize * 2;
                float x = time * hit * 3f;
                float s = Math.max(time * 24, 8);
                Draw.alpha(time / 2);
                Draw.rect(ACDraw.particle(), unit.x, unit.y, x * 3, x * 3, time * 720);
                Drawf.circles(unit.x, unit.y, x, ACPal.shimmer);
                Draw.color(b ? Color.red : Color.green);
                Draw.alpha(0.6f);
                Draw.rect(unit.type.shadowRegion, ACUtil.relative(unit, x, 0), rot);
                Draw.rect(ACDraw.moon(), ACUtil.relative(unit, x + hit, 0), s, s, rot + time * 1440);
                Draw.color(!b ? Color.red : Color.green);
                Draw.alpha(0.6f);
                Draw.rect(unit.type.shadowRegion, ACUtil.relative(unit, -x, 0), rot);
                Draw.rect(ACDraw.moon(), ACUtil.relative(unit, -x - hit, 0), s, s, rot + time * 1440);
            }
        }
    }

    public enum Type {
        TRANSMUTATION,
        AFTER_MUTATION,
        BUFF
    }
}