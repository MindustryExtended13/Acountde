package acountde.shaders;

import arc.graphics.Texture.*;
import arc.graphics.Texture;
import mindustry.Vars;

public class ACSurfaceShader2 extends ACSurfaceShader {
    String[] textureNames;
    Texture[] textures;

    public ACSurfaceShader2(String frag, String... sprites) {
        super(frag);
        if(sprites == null) {
            sprites = new String[0];
        }
        textureNames = sprites;
        textures = new Texture[sprites.length];
        for(int i = 0; i < sprites.length; i++) {
            Texture texture = new Texture(Vars.tree.get("shaders/" + sprites[i] + ".png"));
            texture.setFilter(TextureFilter.linear);
            texture.setWrap(TextureWrap.repeat);
            textures[i] = texture;
        }
    }

    @Override
    public void apply() {
        super.apply();
        for(int i = 0; i < textures.length; i++) {
            textures[i].bind(i + 1);
            setUniformi("u_tex" + i, i + 1);
        }
        Vars.renderer.effectBuffer.getTexture().bind(0);
    }
}