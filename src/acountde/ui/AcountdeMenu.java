package acountde.ui;

import acountde.anno.HandleEvent;
import arc.Core;
import arc.scene.Element;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Table;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.fragments.MenuFragment;
import mindustry.ui.fragments.MenuFragment.MenuButton;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static mindustry.Vars.*;
import static acountde.Acountde.*;

public class AcountdeMenu {
    public static void load() {
        if(mobile) {
            for(MenuButton button : buttons()) {
                ui.menufrag.addButton(button);
            }
        } else {
            ui.menufrag.addButton(new MenuButton(name(), drawable("setting-icon"), () -> {}, buttons()));
            rebuild();
        }
    }

    @Contract(pure = true)
    public static String name() {
        return MOD_NAME;
    }

    @HandleEvent(EventType.ResizeEvent.class)
    public static void rebuild() {
        if(ui.menufrag.getClass() != MenuFragment.class) {
            return;
        }
        Core.app.post(() -> {
            var buttons = (Table) ui.menuGroup.find("buttons");
            TextButton btn = buttons.find(e -> {
                if(!(e instanceof TextButton)) return false;
                return ((TextButton) e).find(x -> {
                    return x instanceof Label l && name().contentEquals(l.getText());
                }) != null;
            });
            var old = buttons.getChildren().copy();
            buttons.clearChildren();
            buttons.add(old.get(0)).row();
            buttons.add(old.get(1)).row();
            buttons.add(btn).row();
            for(int i = 2; i < old.size; i++) {
                Element x = old.get(i);
                if(x != btn) {
                    buttons.add(x).row();
                }
            }
        });
    }

    @Contract(value = " -> new", pure = true)
    public static MenuButton @NotNull[] buttons() {
        return new MenuButton[] {
                AcountdeSettings.button,
                new MenuButton("Documentation", Icon.book, () -> {}),
                new MenuButton("M-WT Docs", drawable("time-control-small"), () -> {})
        };
    }
}