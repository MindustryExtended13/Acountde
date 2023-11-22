package acountde.world.blocks;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.world.Block;
import mindustry.world.Tile;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ACBlock extends Block {
    public boolean debugBinary = false;
    public boolean theorem2 = false;

    public ACBlock(String name) {
        super(name);
        destructible = true;
    }

    public static class PositionAngleResult {
        public int angle;

        public boolean isDiagonal() {
            return angle < 0;
        }
    }

    public static class BlockAngle {
        public int[] values;

        @Contract(pure = true)
        public BlockAngle() {
            values = new int[0];
        }

        public BlockAngle(int rotation) {
            values = new int[1];
            values[0] = rotation;
        }

        public BlockAngle(int r1, int r2) {
            values = new int[2];
            values[0] = r1;
            values[1] = r2;
        }

        public boolean isBinary() {
            return values.length == 2;
        }

        public boolean isBroken() {
            return values.length > 2 || values.length == 0;
        }
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    public class ACBuild extends Building {
        public final Seq<Tile> tileProximity = new Seq<>();
        public final Seq<Tile> selfProximity = new Seq<>();

        private void updateTP(@NotNull Tile s, Seq<Tile> output, Seq<Tile> cache) {
            if(s.build == this) {
                cache.add(s);
                for(Point2 point2 : Geometry.d4) {
                    Tile sub = Vars.world.tile(point2.x + s.x, point2.y + s.y);
                    if(sub != null) {
                        if(cache.contains(sub, true)) {
                            continue;
                        }
                        updateTP(sub, output, cache);
                    }
                }
            } else {
                output.add(s);
            }
        }

        public void updateTileProximity() {
            tileProximity.clear();
            selfProximity.clear();
            updateTP(tile, tileProximity, selfProximity);
        }

        public PositionAngleResult getPositionRotationFor(@NotNull Tile tile) {
            PositionAngleResult result = new PositionAngleResult();
            float x = tile.drawx() - this.x;
            float y = tile.drawy() - this.y;
            float abs_x = Math.abs(x);
            float abs_y = Math.abs(y);

            if(abs_x == abs_y) {
                result.angle = -1;
            } else if(abs_x > abs_y) {
                result.angle = x > 0 ? 0 : 2;
            } else {
                result.angle = y > 0 ? 1 : 3;
            }

            return result;
        }

        public BlockAngle getRotationFor(@NotNull Tile tile) {
            float x = tile.drawx() - this.x;
            float y = tile.drawy() - this.y;

            int rx = x > 0 ? 0 : 2;
            int ry = y > 0 ? 1 : 3;
            if(x == 0 && y == 0) {
                return new BlockAngle();
            } else if(x == 0) {
                return new BlockAngle(ry);
            } else if(y == 0) {
                return new BlockAngle(rx);
            } else {
                return new BlockAngle(rx, ry);
            }
        }

        @Override
        public void draw() {
            super.draw();

            if(debugBinary) {
                Lines.stroke(1);
                Draw.color(Color.yellow);
                Draw.alpha(0.3f);
                for(Tile tile : tileProximity) {
                    Lines.rect(tile.drawx() - 4, tile.drawy() - 4, 8, 8);
                }
                if(theorem2) {
                    for(Tile tile1 : tileProximity) {
                        BlockAngle angle = getRotationFor(tile1);
                        int[] rots = angle.values;
                        float tx = tile1.drawx();
                        float ty = tile1.drawy();
                        if(angle.isBinary()) {
                            Draw.color(Color.blue);
                            Draw.alpha(0.7f);
                            Lines.line(tx, ty, tx, ty + 16 * Geometry.d4(rots[1]).y);

                            Draw.color(Color.red);
                            Draw.alpha(0.7f);
                            Lines.line(tx, ty, tx + 16 * Geometry.d4(rots[0]).x, ty);
                        } else {
                            Draw.color(Color.purple);
                            Draw.alpha(0.7f);
                            var point = Geometry.d4(rots[0]);
                            Vec2 p = new Vec2(point.x, point.y)
                                    .times(new Vec2(16, 16))
                                    .add(tx, ty);
                            Lines.line(tx, ty, p.x, p.y);
                        }
                    }
                } else {
                    for(Tile tile1 : tileProximity) {
                        PositionAngleResult result = getPositionRotationFor(tile1);
                        if(!result.isDiagonal()) {
                            float tx = tile1.drawx();
                            float ty = tile1.drawy();

                            Draw.color(Color.purple);
                            Draw.alpha(0.7f);
                            var point = Geometry.d4(result.angle);
                            Vec2 p = new Vec2(point.x, point.y)
                                    .times(new Vec2(16, 16))
                                    .add(tx, ty);
                            Lines.line(tx, ty, p.x, p.y);
                        }
                    }
                }
                Draw.reset();
            }
        }

        @Override
        public void updateProximity() {
            super.updateProximity();
            updateTileProximity();
        }
    }
}