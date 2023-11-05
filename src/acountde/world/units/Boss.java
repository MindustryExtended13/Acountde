package acountde.world.units;

public class Boss extends ACUnitType {
    public Boss(String name) {
        super(name);
        collideDamages = true;
        collideImmunity = 10;
    }
}