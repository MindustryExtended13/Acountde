package acountde.world.blocks.corruption;

import acountde.graphics.TFBlockDraw;
import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.world.Tile;

public class ShadowVein extends CorruptionBlock {
    public TextureRegion[] connectors;
    public TextureRegion[] regions;

    public ShadowVein(String name) {
        super(name);
    }

    @Override
    public void load() {
        super.load();
        regions = TFBlockDraw.loadRegions(name + "-");
        int i = 0;
        connectors = new TextureRegion[4];
        for(String str : new String[] {"fftf", "ffft", "tfff", "ftff"}) {
            connectors[i++] = Core.atlas.find(name + "-" + str);
        }
    }

    public class ShadowVeinBuild extends CorruptionBuild {
        public Seq<Building> tmp_prox = new Seq<>();

        @Override
        public TextureRegion getRegion() {
            return TFBlockDraw.getRegion(regions, this, tmp_prox, (ignored) -> true);
        }

        @Override
        public void draw() {
            super.draw();
            Draw.draw(Layer.blockUnder, () -> {
                for(int i = 0; i < 4; i++) {
                    Point2 p = Geometry.d4(i).cpy().add(tileX(), tileY());
                    Building b = Vars.world.build(p.x, p.y);
                    Tile tile1 = Vars.world.tile(p.x, p.y);
                    if(b instanceof CorruptionBuild &&
                            !(b instanceof ShadowVeinBuild) && !b.block.squareSprite) {
                        Draw.rect(connectors[i], tile1.drawx() - 4, tile1.drawy() - 4);
                    }
                }
            });
        }

        @Override
        public void onProximityUpdate() {
            super.onProximityUpdate();
            tmp_prox = proximity.select(b -> b instanceof CorruptionBuild);
        }
    }
}