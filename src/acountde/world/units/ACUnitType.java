package acountde.world.units;

import me13.core.units.XeonUnitType;
import mindustry.Vars;
import mindustry.content.StatusEffects;

public class ACUnitType extends XeonUnitType {
    public boolean collideDamages = false;
    public int collideImmunity = 30;
    public float collideDamage = 0;

    public ACUnitType(String name) {
        super(name);
    }

    public void allImmunity() {
        immunities.addAll(Vars.content.statusEffects());
    }

    public void bossImmunitySetup() {
        allImmunity();
        immunities.remove(StatusEffects.boss);
    }
}