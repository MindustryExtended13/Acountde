package acountde.ui;

import acountde.ui.dialog.AcountdeDialog;
import arc.Events;
import arc.scene.Element;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Table;
import mindustry.game.EventType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.ModsDialog;

import static arc.Core.*;
import static mindustry.Vars.*;
import static acountde.Acountde.*;

public class AcountdeDialogInit {
    public static Dialog currentDialog = null;

    public static void load() {
        Events.run(EventType.Trigger.update, () -> {
            String description = instance.meta.description;
            if (scene != null && !headless) {
                if (currentDialog != null && !currentDialog.isShown()) {
                    currentDialog = null;
                }
                Dialog dialog = scene.getDialog();
                if (dialog instanceof BaseDialog &&
                        instance.meta.displayName().contentEquals(dialog.title.getText())) {
                    if (dialog == currentDialog) {
                        return;
                    }
                    if (currentDialog != null && scene.root.getChildren().contains(currentDialog)) {
                        return;
                    }
                    dialog.cont.getChildren().each(e -> {
                        if (e instanceof ScrollPane scrollPane) {
                            Element widget = scrollPane.getWidget();
                            if (widget instanceof Table table) {
                                for (Element child : table.getChildren()) {
                                    if (child instanceof Label label &&
                                            label.getText().toString().equals(description)) {
                                        if (!(dialog instanceof ModsDialog)) {
                                            dialog.hide(null);
                                            new AcountdeDialog().show();
                                        }

                                        currentDialog = dialog;
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}