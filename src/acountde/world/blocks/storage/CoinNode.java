package acountde.world.blocks.storage;

import acountde.graphics.ACDraw;
import acountde.ui.dialog.CoinNodeDialog;
import acountde.utils.ACTmp;
import acountde.utils.ACUtil;
import acountde.world.blocks.ACBlock;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import betamindy.world.blocks.storage.AnucoinNode;
import betamindy.world.blocks.storage.AnucoinVault;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.MultiPacker;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import org.jetbrains.annotations.NotNull;

public class CoinNode extends ACBlock {
    public int initialMaximumCoin;
    public int range;

    public CoinNode(String name) {
        super(name);
        update = true;
        configurable = true;
        config(Integer.class, (CoinNodeBuild b, Integer value) -> {
            if(b.links.contains(value)) {
                int i = b.links.indexOf(value);
                b.links.removeIndex(i);
                b.colors.remove(i);
            } else {
                b.links.add(value);
                b.colors.add(Color.rgb(
                        Mathf.random(0, 255),
                        Mathf.random(0, 255),
                        Mathf.random(0, 255)
                ));
            }
        });
        configClear((CoinNodeBuild b) -> {
            b.links.clear();
        });
    }

    public boolean validLink(Building self, Building other) {
        return ACUtil.rectLinkValid(self, other, range / 2);
    }

    @Override
    public void setBars() {
        super.setBars();
        this.addBar("coins", (CoinNodeBuild entity) -> new Bar(
                () -> entity.coins() + "$",
                () -> Color.coral,
                () -> Mathf.clamp(entity.coins / entity.maximumCapacity())
        ));
    }

    @Override
    public void init() {
        super.init();
        clipSize = Math.max(clipSize, range * 16 + 144);
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.linkRange, range, StatUnit.blocks);
        stats.add(Stat.itemCapacity, initialMaximumCoin + "$");
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

    public class CoinNodeBuild extends ACBuild implements CoinBuild {
        public Seq<Color> colors = new Seq<>();
        public IntSeq links = new IntSeq();
        public String customName = null;
        public CoinNodeDialog dialog;
        public float coins = 0;

        {
            dialog = new CoinNodeDialog(this);
        }

        public Color colorOf(Building building) {
            try {
                return colors.get(links.indexOf(building.pos()));
            } catch(Throwable ignored) {
                return Color.white;
            }
        }

        public boolean valid(Building other) {
            return other instanceof CoinBuild && other.team == team && other != this && validLink(this, other);
        }

        public boolean linked(@NotNull Building building) {
            return links.contains(building.pos());
        }

        public Seq<Building> links() {
            return Seq.with(ACUtil.toInt(links)).map(i -> Vars.world.build(i));
        }

        @Override
        public void updateTile() {
            links.each(l -> {
                var b = Vars.world.build(l);
                if(!valid(b)) {
                    configure(l);
                }
            });
        }

        @Override
        public float maximumCapacity() {
            return initialMaximumCoin + links()
                    .select(b -> b instanceof CoinBuild)
                    .map(building -> (CoinBuild) building)
                    .sumf(CoinBuild::maximumCoinsIncrease);
        }

        @Override
        public void draw() {
            super.draw();
            Draw.draw(Layer.blockUnder, () -> {
                for(Building link : links()) {
                    if(link != null) {
                        Drawf.line(team.color, x, y, link.x, link.y);
                    }
                }
            });
        }

        @Override
        public void buildConfiguration(@NotNull Table table) {
            table.button(Icon.tree, () -> {
                dialog.build();
                dialog.show();
            });
        }

        @Override
        public boolean onConfigureBuildTapped(Building other) {
            if(valid(other)) {
                configure(other.pos());
                return false;
            }
            return super.onConfigureBuildTapped(other);
        }

        @Override
        public boolean outputCoin() {
            return true;
        }

        @Override
        public boolean inputCoin() {
            return true;
        }

        @Override
        public void onImportCoin(CoinNode.@NotNull CoinNodeBuild source) {
            float n = coins + source.removeCoin(this, source.coins);
            float max = maximumCapacity();
            setCoins(Math.min(n, max));
            float scalar = max - n;
            if(scalar < 0) {
                source.acceptCoin(this, -scalar);
            }
        }

        @Override
        public void onExtractCoin(CoinNode.@NotNull CoinNodeBuild source) {
            setCoins(coins - source.acceptCoin(this, coins));
        }

        @Override
        public void drawConfigure() {
            super.drawConfigure();
            ACUtil.selectTiles(this::valid);
            for(Building building : ACTmp.seq1b) {
                Color color = linked(building) ? colorOf(building) : Pal.accent;
                Drawf.square(building.x, building.y, building.block.size * 6, 0, color);
            }
        }

        @Override
        public void drawSelect() {
            super.drawSelect();
            Drawf.dashSquare(Pal.accent, x, y, range * 8);
        }

        @Override
        public float acceptCoin(Building source, float amount) {
            float x = maximumCapacity();
            coins += amount;
            if(coins > x) {
                float o = coins;
                coins = x;
                return amount - (o - x);
            }
            return amount;
        }

        @Override
        public float removeCoin(Building source, float amount) {
            coins -= amount;
            if(coins < 0) {
                float o = coins;
                coins = 0;
                return amount + o;
            }
            return amount;
        }

        @Override
        public void setCoins(float amount) {
            coins = amount;
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
        public void write(@NotNull Writes write) {
            write.bool(customName != null);
            if(customName != null) {
                write.str(customName);
            }
            write.f(coins);
            write.i(links.size);
            links.each(write::i);
        }

        @Override
        public void read(@NotNull Reads read, byte revision) {
            if(read.bool()) {
                customName = read.str();
            }
            coins = read.f();
            int len = read.i();
            for(int i = 0; i < len; i++) {
                configure(read.i());
            }
        }
    }
}