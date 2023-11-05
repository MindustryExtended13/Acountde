package acountde.shaders;

import arc.Core;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.scene.ui.layout.Scl;
import arc.util.Time;
import mindustry.Vars;

@Deprecated
public class ACBlockShader extends Shader {
    String textureName;
    Texture texture;

    public ACBlockShader(String frag, String sprite) {
        super(Core.files.internal("shaders/default.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
        textureName = sprite;
    }

    @Override
    public void apply() {
        setUniformf("u_campos",
                Core.camera.position.x - Core.camera.width / 2,
                Core.camera.position.y - Core.camera.height / 2
        );
        setUniformf("u_resolution", Core.camera.width, Core.camera.height);
        setUniformf("u_time", Time.time / Scl.scl(1f));
        setUniformf("u_offset", Core.camera.position.x, Core.camera.position.y);
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