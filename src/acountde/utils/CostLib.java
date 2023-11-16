package acountde.utils;

import acountde.Acountde;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
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

    public static TextureRegion getImage() {
        return getImageByID(getAnucoinPrizeID());
    }

    public static float getAnucoinPrize() {
        return getPriceByID(getAnucoinPrizeID());
    }

    public static int getAnucoinPrizeID() {
        return Core.settings.getInt("acountde-anucoin-prize");
    }

    public static TextureRegion getImageByID(int id) {
        return Acountde.region("setting-anucoin-prize-" + id);
    }

    public static float getPriceByID(int id) {
        return switch(id) {
            case 0 -> 1 / 3f;
            case 1 -> 1 / 2f;
            case 2 -> 1f;
            case 3 -> Mathf.sqrt(2);
            case 4 -> 1.71828182846f; //e ^ -1
            case 5 -> 2f;
            case 6 -> ACUtil.E;
            case 7 -> 3f;
            case 8 -> Mathf.PI;
            case 9 -> 3.5f; //7 / 2
            case 10 -> 4f;
            case 11 -> 5f;
            default -> throw new IllegalStateException("Unexpected value: " + id);
        };
    }

    static {
        customItems.put(Items.sand, 1f);
        customItems.put(Items.scrap, 0.5f);
        customItems.put(Items.sporePod, Mathf.sqrt(2));
    }

    @Contract(pure = true)
    public static float calculateMoney(int anucoin) {
        return anucoin * getAnucoinPrize();
    }

    @Contract(pure = true)
    public static int calculateAnucoin(float money) {
        return Mathf.floor(money / getAnucoinPrize());
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