package acountde.ui;

import acountde.Acountde;
import arc.graphics.Color;
import arc.scene.event.ClickListener;
import arc.scene.event.HandCursorListener;
import arc.scene.style.Drawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class UIUtil {
    public static final TextButtonStyle DBS = Styles.grayt;
    public static final Drawable DTS = Styles.grayPanel;

    public static @NotNull Cell<Table> category(Table table, String name) {
        return category(table, name, Pal.accent);
    }

    public static @NotNull Cell<Table> category(@NotNull Table table, String name, Color color) {
        Cell<Table> cell = table.table(row -> {
            row.image().color(color).height(4f).growX();
            row.add(Acountde.get(name)).color(color).pad(6);
            row.image().color(color).height(4f).growX();
        }).growX();
        cell.row();
        return cell;
    }

    @Contract("null, _, _ -> null")
    public static Cell<Label> labelButton(Table table, String text, Runnable run) {
        return labelButton(table, text, Color.white, run);
    }

    @Contract("null, _, _, _ -> null")
    public static Cell<Label> labelButton(Table table, String text, Color color, Runnable run) {
        if(table == null) return null;
        var cell = table.add(text).color(color);
        Label mabel = cell.get();
        ClickListener listener = new ClickListener();
        mabel.addListener(listener);
        if(!Vars.mobile) {
            Color a = color.cpy().a(0.5f);
            mabel.addListener(new HandCursorListener());
            mabel.update(() -> mabel.setColor(!listener.isOver() ? color : a));
        }
        mabel.clicked(run);
        return cell;
    }
}