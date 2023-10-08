package acountde.world.blocks.corruption;

import acountde.content.ACFx;
import acountde.content.ACStatusEffects;
import arc.graphics.Color;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import mindustry.world.meta.StatValues;

public class ToxicTower extends CorruptionBlock {
    public static final float TOXIC_TOWER_RADIUS = 96f;
    public float damage = 540f;

    public ToxicTower(String name) {
        super(name);
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.damage, damage);
        stats.add(Stat.range, StatValues.number(TOXIC_TOWER_RADIUS/8, StatUnit.blocks));
    }

    public class ToxicTowerBuild extends CorruptionBuild {
        int counter;

        @Override
        public void updateTile() {
            super.updateTile();

            Seq<Unit> units = new Seq<>();
            Groups.unit.each(unit -> {
                float x1 = unit.x - x;
                float y1 = unit.y - y;
                float dist = Mathf.sqrt(x1 * x1 + y1 * y1);
                if(dist < TOXIC_TOWER_RADIUS) {
                    units.add(unit);
                }
            });
            if(units.size > 0) {
                if(counter > 0) {
                    counter--;
                } else {
                    counter = 90;
                    pulse(inpStrength * 2);
                    for(int i = 0; i < units.size; i++) {
                        Unit unit = units.get(i);
                        unit.damage(damage);
                        unit.apply(ACStatusEffects.corrupted, 240);
                    }
                    ACFx.toxicTowerExplosion.at(x, y, Color.acid);
                }
            }
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            Drawf.dashCircle(x, y, TOXIC_TOWER_RADIUS, team.color);
        }
    }
}