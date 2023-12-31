package acountde.content;

import acountde.anno.HandleEvent;
import acountde.shaders.ACSurfaceShader2;
import arc.graphics.gl.Shader;
import mindustry.game.EventType;

public class ACShaders {
    public static Shader lava, milkyway, shimmer;

    public static void init() {
        shimmer = new ACSurfaceShader2("shimmer", "shimmer_shader", "nebula", "alpha");
        milkyway = new ACSurfaceShader2("milkyway", "milkyway_shader");
        lava = new ACSurfaceShader2("lava", "lava_shader");
    }

    @HandleEvent(EventType.DisposeEvent.class)
    public static void disposeShaders() {
        milkyway.dispose();
        lava.dispose();
    }
}