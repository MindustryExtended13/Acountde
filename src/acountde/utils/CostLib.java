package acountde.utils;

import arc.math.Mathf;
import arc.struct.ObjectMap;
import mindustry.content.Items;
import mindustry.gen.Unit;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Contract;

public class CostLib {
    public static ObjectMap<Item, Float> customItems = new ObjectMap<>();

    static {
        customItems.put(Items.sand, 1f);
        customItems.put(Items.scrap, 0.5f);
        customItems.put(Items.sporePod, 10f);
    }

    @Contract(pure = true)
    public static float calculateMoney(int anucoin) {
        return anucoin * 2;
    }

    @Contract(pure = true)
    public static int calculateAnucoin(float money) {
        return Mathf.floor(money / 2f);
    }

    @Contract(pure = true)
    public static float calculateCost(@NotNull Liquid liquid) {
        return ACUtil.rou(ACUtil.det(
                1 + liquid.temperature,
                -(1 + liquid.flammability),
                1 + liquid.viscosity,
                1 + liquid.explosiveness
        ));
    }

    @Contract(pure = true)
    public static float calculateCost(@NotNull Item item) {
        if(customItems.containsKey(item)) return customItems.get(item);
        return ACUtil.rou((1 + item.cost + item.charge + item.flammability + item.radioactivity
                + item.explosiveness) * (item.hardness == 0 ? 7 : item.hardness));
    }

    @Contract(pure = true)
    public static float calculateCost(Unit unit) {
        return unit == null ? 0 : calculateCost(unit.type);
    }

    @Contract(pure = true)
    public static float calculateCost(@NotNull UnitType type) {
        return ACUtil.rou(ACUtil.det(type.health, -type.hitSize, 1 + type.armor, type.hitSize));
    }
}