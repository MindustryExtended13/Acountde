package acountde.content;

import acountde.types.entity.ACDataUnitEntity;
import me13.core.units.XeonUnitType;
import me13.core.units.XeonUnits;
import mindustry.type.UnitType;

import static acountde.content.ACRegistry.*;

public class ACUnits {
    public static UnitType dataUnit;

    public static void load() {
        XeonUnits.add(ACDataUnitEntity.class, ACDataUnitEntity::new);
        XeonUnits.setupID();

        dataUnit = register(new XeonUnitType("data-unit") {{
            constructor = ACDataUnitEntity::new;
            health = Float.POSITIVE_INFINITY;
            hidden = true;
            hitSize = 0;
        }});
    }
}