package acountde.content;

import acountde.anno.HandleEvent;
import acountde.shaders.ACSurfaceShader;
import mindustry.Vars;
import mindustry.game.EventType;

public class ACShaders {
    public static ACSurfaceShader lava, milkyway;

    public static void init() {
        if(!Vars.headless) {
            lava = new ACSurfaceShader("lava", "lava_shader");
            milkyway = new ACSurfaceShader("milkyway", "milkyway_shader");
        }
    }

    @HandleEvent(EventType.DisposeEvent.class)
    public static void disposeShaders() {
        if(!Vars.headless) {
            lava.dispose();
            milkyway.dispose();
        }
    }
}
