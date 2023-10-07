package acountde.ui.dialog;

import mindustry.ui.dialogs.BaseDialog;

public abstract class BuildableDialog extends BaseDialog {
    public BuildableDialog(String title, DialogStyle style) {
        super(title, style);
        build(true);
    }

    public BuildableDialog(String title) {
        super(title);
        build(true);
    }

    public void build(boolean rebuild) {
        build();
        if(rebuild) {
            build(false);
        }
    }

    public abstract void build();
}