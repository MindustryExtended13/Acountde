package acountde.world.blocks.storage;

import mindustry.gen.Building;

public interface CoinBuild {
    float acceptCoin(Building source, float amount);
    float removeCoin(Building source, float amount);
    void setCoins(float amount);
    void rename(String name);
    String displayName();
    float coins();

    default void onImportCoin(CoinNode.CoinNodeBuild source) {
    }

    default void onExtractCoin(CoinNode.CoinNodeBuild source) {
    }

    default float maximumCapacity() {
        return 0;
    }

    default float maximumCoinsIncrease() {
        return 0;
    }

    default boolean inputCoin() {
        return false;
    }

    default boolean outputCoin() {
        return false;
    }

    default float requiredCoin(Building source) {
        return coins() < 0 ? -coins() : 0;
    }
}