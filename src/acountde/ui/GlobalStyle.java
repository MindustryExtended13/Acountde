package acountde.ui;

import arc.scene.ui.Button.ButtonStyle;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

import org.jetbrains.annotations.NotNull;

public class GlobalStyle {
    private static void enable(@NotNull ButtonStyle style) {
        style.up = Tex.pane;
        style.over = Tex.flatDownBase;
        style.down = Tex.whitePane;
        style.disabled = Tex.paneSolid;
    }

    public static void enable() {
        enable(Styles.defaulti);
        enable(Styles.defaultb);
        enable(Styles.defaultt);
    }
}