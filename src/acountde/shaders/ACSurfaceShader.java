package acountde.shaders;

import arc.Core;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;

public class ACSurfaceShader extends Shader {
    String textureName;
    Texture texture;

    public ACSurfaceShader(String frag, String sprite) {
        super(Core.files.internal("shaders/screenspace.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
        textureName = sprite;
    }

    @Override
    public void apply() {
        setUniformf("u_campos",
                Core.camera.position.x - Core.camera.width / 2,
                Core.camera.position.y - Core.camera.height / 2
        );
        setUniformf("u_resolution", Core.camera.width, Core.camera.height);
        setUniformf("u_time", Time.time);
        if(texture == null) {
            texture = new Texture(Vars.tree.get("shaders/" + textureName + ".png"));
            texture.setFilter(Texture.TextureFilter.linear);
            texture.setWrap(Texture.TextureWrap.repeat);
        }
        texture.bind(1);
        Vars.renderer.effectBuffer.getTexture().bind(0);
        setUniformi("u_texture", 1);
    }
}