package acountde.shaders;

import arc.Core;
import arc.graphics.Texture;
import arc.graphics.gl.Shader;
import arc.util.Time;
import mindustry.Vars;
import mindustry.graphics.Shaders;

import static mindustry.Vars.renderer;

public class ACSurfaceShader extends Shader {
    Texture noiseTex;

    public ACSurfaceShader(String frag) {
        super(Shaders.getShaderFi("screenspace.vert"), Vars.tree.get("shaders/" + frag + ".frag"));
        loadNoise();
    }

    public String textureName() {
        return "noise";
    }

    public void loadNoise() {
        Core.assets.load("sprites/" + textureName() + ".png", Texture.class).loaded = t -> {
            t.setFilter(Texture.TextureFilter.linear);
            t.setWrap(Texture.TextureWrap.repeat);
        };
    }

    @Override
    public void apply() {
        setUniformf("u_campos",
                Core.camera.position.x - Core.camera.width / 2,
                Core.camera.position.y - Core.camera.height / 2
        );
        setUniformf("u_ccampos", Core.camera.position);
        setUniformf("u_resolution", Core.camera.width, Core.camera.height);
        setUniformf("u_rresolution", Core.graphics.getWidth(), Core.graphics.getHeight());
        setUniformf("u_time", Time.time);

        if(hasUniform("u_noise")) {
            if(noiseTex == null) {
                noiseTex = Core.assets.get("sprites/" + textureName() + ".png", Texture.class);
            }

            noiseTex.bind(1);
            renderer.effectBuffer.getTexture().bind(0);

            setUniformi("u_noise", 1);
        }
    }
}