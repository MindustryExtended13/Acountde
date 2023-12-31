package acountde.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.gl.Shader;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.CacheLayer.*;
import org.jetbrains.annotations.NotNull;

import static acountde.Acountde.LOGGER;
import static mindustry.Vars.renderer;

public class ACCacheLayer {
    public static ShaderLayer lava, milkyway, shimmer;

    public static void init() {
        LOGGER.info("[accent]<FTE + POST (CACHELAYER)>[]");
        milkyway = new ModShaderLayer(ACShaders.milkyway);
        shimmer = new ModShaderLayer(ACShaders.shimmer);
        lava = new ModShaderLayer(ACShaders.lava);
        CacheLayer.add(milkyway, shimmer, lava);
    }

    public static class ModShaderLayer extends ShaderLayer {
        public ModShaderLayer(Shader shader) {
            super(shader);
        }

        @Override
        public void begin() {
            renderer.blocks.floor.endc();
            renderer.effectBuffer.begin();
            Core.graphics.clear(Color.clear);
            renderer.blocks.floor.beginc();
        }

        @Override
        public void end() {
            renderer.blocks.floor.endc();
            renderer.effectBuffer.end();
            renderer.effectBuffer.blit(shader);
            renderer.blocks.floor.beginc();
        }
    }
}
