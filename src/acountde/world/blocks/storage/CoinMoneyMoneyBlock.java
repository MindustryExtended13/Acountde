package acountde.world.blocks.storage;

import acountde.graphics.ACDraw;
import acountde.utils.CostLib;
import acountde.world.blocks.ACBlock;
import arc.graphics.g2d.TextureRegion;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.graphics.MultiPacker;
import mindustry.type.Item;
import org.jetbrains.annotations.NotNull;

public class CoinMoneyMoneyBlock extends ACBlock {
    public static final int TRADING_POST = 0;
    public boolean outputItems = false;
    public int blockType;

    public CoinMoneyMoneyBlock(String name) {
        super(name);
        update = true;
    }

    @Override
    public boolean outputsItems() {
        return outputItems;
    }

    @Override
    protected TextureRegion[] icons() {
        return new TextureRegion[] {region, teamRegions[Team.sharded.id]};
    }

    @Override
    public void createIcons(MultiPacker packer) {
        ACDraw.generateTeamRegions(packer, this);
        super.createIcons(packer);
    }

    public class CoinMoneyMoneyBlockBuild extends ACBuild implements CoinBuild {
        public String customName = null;
        public float coins = 0;

        public void handleItem22(Teamc source, Item item, int amount) {
            noSleep();
            if(blockType == TRADING_POST) {
                items.add(item, amount);
            }
        }

        @Override
        public void updateTile() {
            super.updateTile();
            if(blockType == TRADING_POST) {
                coins = 0;
                items.each((i, c) -> {
                    coins += CostLib.calculateCost(i) * c;
                });
            }
        }

        @Override
        public void handleItem(Building source, Item item) {
            handleItem22(source, item, 1);
        }

        @Override
        public void handleStack(Item item, int amount, Teamc source) {
            handleItem22(source, item, amount);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return switch(blockType) {
                case TRADING_POST -> true;
                default -> super.acceptItem(source, item);
            };
        }

        @Override
        public void onExtractCoin(CoinNode.CoinNodeBuild source) {
            if(blockType == TRADING_POST) {
                source.acceptCoin(this, coins);
                items.clear();
            }
        }

        @Override
        public boolean outputCoin() {
            return blockType == TRADING_POST;
        }

        @Override
        public float acceptCoin(Building source, float amount) {
            return 0;
        }

        @Override
        public float removeCoin(Building source, float amount) {
            return 0;
        }

        @Override
        public void setCoins(float amount) {
            this.coins = amount;
        }

        @Override
        public void rename(String name) {
            customName = name;
        }

        @Override
        public String displayName() {
            return customName == null ? localizedName : customName;
        }

        @Override
        public float coins() {
            return coins;
        }

        @Override
        public float maximumCapacity() {
            return switch(blockType) {
                case TRADING_POST -> Float.POSITIVE_INFINITY;
                default -> 0;
            };
        }

        @Override
        public void write(@NotNull Writes write) {
            write.bool(customName != null);
            if(customName != null) {
                write.str(customName);
            }
        }

        @Override
        public void read(@NotNull Reads read, byte revision) {
            if(read.bool()) {
                customName = read.str();
            }
        }
    }
}
