package acountde.ui;

import arc.scene.style.Drawable;
import arc.scene.ui.Button.ButtonStyle;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class GlobalStyle {
    private static boolean enabled;

    @Contract(pure = true)
    public static boolean isEnabled() {
        return enabled;
    }

    private static void enable(@NotNull ButtonStyle style) {
        style.up = Tex.pane;
        style.over = Tex.flatDownBase;
        style.down = Tex.whitePane;
        style.disabled = Tex.paneSolid;
    }

    public static void enable() {
        if(enabled) return;
        enabled = true;

        enable(Styles.defaulti);
        enable(Styles.defaultb);
        enable(Styles.defaultt);
    }
}