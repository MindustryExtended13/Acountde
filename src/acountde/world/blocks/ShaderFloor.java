package acountde.world.blocks;

import arc.graphics.g2d.Draw;
import arc.graphics.gl.Shader;
import mindustry.graphics.Layer;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

public class ShaderFloor extends Floor {
    public Shader shader;

    public ShaderFloor(String name) {
        super(name);
        cacheLayer = null;
    }

    @Override
    public void drawBase(Tile tile) {
        Draw.draw(Layer.floor + 1, () -> {
            Draw.shader(shader);
            super.drawBase(tile);
            Draw.shader();
            Draw.reset();
        });
    }
}