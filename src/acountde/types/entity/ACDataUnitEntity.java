package acountde.types.entity;

import arc.util.io.Reads;
import arc.util.io.Writes;
import me13.core.units.XeonUnitEntity;
import mindustry.game.Team;

public class ACDataUnitEntity extends XeonUnitEntity {
    public static final float SPAWN_CORDS = -666;
    public long SAVE_ID;

    @Override
    public void update() {
        set(SPAWN_CORDS, SPAWN_CORDS);
    }

    @Override
    public void kill() {
    }

    @Override
    public void read(Reads read) {
        super.read(read);
        SAVE_ID = read.l();
    }

    @Override
    public void write(Writes write) {
        super.write(write);
        write.l(SAVE_ID);
    }

    @Override
    public void draw() {
    }

    @Override
    public boolean hasWeapons() {
        return false;
    }

    @Override
    public boolean targetable(Team targeter) {
        return false;
    }

    @Override
    public boolean hittable() {
        return false;
    }
}