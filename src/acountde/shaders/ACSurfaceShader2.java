package acountde.shaders;

import arc.graphics.Texture;
import mindustry.Vars;

public class ACSurfaceShader2 extends ACSurfaceShader {
    String textureName;
    Texture texture;

    public ACSurfaceShader2(String frag, String sprite) {
        super(frag);
        textureName = sprite;
        texture = new Texture(Vars.tree.get("shaders/" + textureName + ".png"));
        texture.setFilter(Texture.TextureFilter.linear);
        texture.setWrap(Texture.TextureWrap.repeat);
    }

    @Override
    public void apply() {
        super.apply();
        texture.bind(1);
        Vars.renderer.effectBuffer.getTexture().bind(0);
        setUniformi("u_tex", 1);
    }
}