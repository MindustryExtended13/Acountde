package acountde.types.entity;

import acountde.utils.ACTmp;
import acountde.utils.ACUtil;
import acountde.world.units.ACUnitType;
import arc.struct.ObjectMap;
import me13.core.units.XeonUnitEntity;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Teamc;
import org.jetbrains.annotations.NotNull;

public class ACUnitEntity extends XeonUnitEntity {
    public ObjectMap<Teamc, Integer> map = new ObjectMap<>();
    public ACUnitType instance;

    public boolean canDamage(@NotNull Teamc target) {
        return target.team() != team && !map.containsKey(target);
    }

    @Override
    public void update() {
        super.update();
        if(instance == null && type instanceof ACUnitType t) {
            instance = t;
        }

        var keys = map.keys();
        while(keys.hasNext()) {
            Teamc t = keys.next();
            int i = map.get(t);
            map.put(t, --i);
            if(i <= 0) {
                map.remove(t);
            }
        }

        ACUtil.asRect(this);
        ACTmp.switchRect();
        if(instance != null && instance.collideDamages) {
            Groups.unit.each(unit -> {
                if(unit != this && canDamage(unit)) {
                    ACUtil.asRect(unit);
                    if(ACUtil.collides(ACTmp.rect2, ACTmp.rect1)) {
                        unit.damage(instance.collideDamage);
                        map.put(unit, instance.collideImmunity);
                    }
                }
            });

            Vars.world.tiles.eachTile(t -> {
                Building build = t.build;
                if(build != null && canDamage(build)) {
                    ACUtil.asRect(build);
                    if(ACUtil.collides(ACTmp.rect2, ACTmp.rect1)) {
                        build.damage(instance.collideDamage);
                        map.put(build, instance.collideImmunity);
                    }
                }
            });
        }
    }
}