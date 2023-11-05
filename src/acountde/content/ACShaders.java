package acountde.content;

import acountde.anno.HandleEvent;
import acountde.shaders.ACBlockShader;
import mindustry.Vars;
import mindustry.game.EventType;

public class ACShaders {
    public static ACBlockShader lava, milkyway;

    public static void init() {
        if(!Vars.headless) {
            lava = new ACBlockShader("lava", "lava_shader");
            milkyway = new ACBlockShader("milkyway", "milkyway_shader");
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
