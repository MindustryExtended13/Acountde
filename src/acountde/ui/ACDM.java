package acountde.ui;

import acountde.anno.HandleEvent;
import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import mindustry.game.EventType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ACDM {
    public static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static final Table main = new Table();

    @Contract(pure = true)
    public static @NotNull String getContent() {
        return Core.graphics.getFramesPerSecond() + " FPS | " + dtf.format(LocalDateTime.now());
    }

    @HandleEvent(EventType.ResizeEvent.class)
    public static void rebuild() {
        main.clearChildren();
        main.margin(4f);
        main.table(THIS_TABLE_MADE_TO_TOP_LABEL_AND_NOTHING -> {
            THIS_TABLE_MADE_TO_TOP_LABEL_AND_NOTHING.top().left();
            THIS_TABLE_MADE_TO_TOP_LABEL_AND_NOTHING
                    .labelWrap(getContent())
                    .color(Color.green)
                    .pad(6)
                    .top()
                    .left()
                    .update(label -> {
                        label.setText(getContent());
                    });
        }).size(Core.graphics.getWidth(), Core.graphics.getHeight());
        main.pack();
        main.validate();
    }

    @HandleEvent(EventType.ClientLoadEvent.class)
    public static void load() {
        Events.run(EventType.Trigger.update, () -> {
            Core.scene.root.removeChild(main);
            Core.scene.add(main);
        });
    }
}