package acountde.world.blocks.storage;

import acountde.graphics.ACDraw;
import acountde.world.blocks.ACBlock;
import arc.graphics.g2d.TextureRegion;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.graphics.MultiPacker;
import org.jetbrains.annotations.NotNull;

public class CoinVault extends ACBlock {
    public int capacityIncrease;

    public CoinVault(String name) {
        super(name);
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

    public class CoinVaultBuild extends ACBuild implements CoinBuild {
        public String customName = null;

        @Override
        public float maximumCoinsIncrease() {
            return capacityIncrease;
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
            return 0;
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
