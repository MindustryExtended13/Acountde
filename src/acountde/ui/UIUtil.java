package acountde.ui;

import arc.graphics.Color;
import arc.scene.event.ClickListener;
import arc.scene.event.HandCursorListener;
import arc.scene.style.Drawable;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton.TextButtonStyle;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.Styles;
import org.jetbrains.annotations.Contract;

public class UIUtil {
    public static final TextButtonStyle DBS = Styles.grayt;
    public static final Drawable DTS = Styles.grayPanel;

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