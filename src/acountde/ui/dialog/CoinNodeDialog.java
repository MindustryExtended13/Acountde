package acountde.ui.dialog;

import acountde.Acountde;
import acountde.ui.UIUtil;
import acountde.world.blocks.storage.CoinBuild;
import acountde.world.blocks.storage.CoinNode;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import betamindy.world.blocks.storage.AnucoinNode;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import org.jetbrains.annotations.NotNull;

public class CoinNodeDialog extends BuildableDialog {
    public CoinNode.CoinNodeBuild build;

    public CoinNodeDialog(CoinNode.@NotNull CoinNodeBuild build) {
        super(build.displayName());
        this.build = build;
        build(true);
    }

    @Override
    public void build() {
        if(build == null) return;
        title.setText(build.displayName());
        cont.clearChildren();
        cont.center().top();
        cont.table(main -> {
            main.add(Acountde.get("money") + ": " + (build.coins() < 0 ? "[red]" : "[gray]") + build.coins() +
                    "$[] [gray]/ " + build.maximumCapacity() + "$").left().row();
            UIUtil.category(main, "transaction");
            main.pane(list -> {
                for(Building b : build.links()) {
                    CoinBuild x2 = (CoinBuild) b;
                    list.table(table -> {
                        table.setBackground(Tex.button);
                        table.image().color(build.colorOf(b)).pad(6).size(8, 32);
                        table.image(b.block.uiIcon).pad(6).size(32);
                        table.table(info -> {
                            info.left();
                            info.add(x2.displayName()).left().row();
                            info.add((x2.coins() < 0 ? "[red]" : "[gray]") + x2.coins() +
                                            "$[] [gray]/ " + x2.maximumCapacity() + "$").left().row();
                        }).growX();
                        table.table(buttons -> {
                            buttons.button(Icon.export, Styles.clearNonei, () -> {
                                x2.onExtractCoin(build);
                                build();
                            }).tooltip(Acountde.get("export-coin")).size(32).pad(6).update(btn -> {
                                btn.setDisabled(!x2.outputCoin());
                            });
                            buttons.button(Icon.download, Styles.clearNonei, () -> {
                                x2.onImportCoin(build);
                                build();
                            }).tooltip(Acountde.get("import-coin")).size(32).pad(6).update(btn -> {
                                btn.setDisabled(!x2.inputCoin());
                            });
                            buttons.button(Icon.pencil, Styles.clearNonei, () -> {
                                openRename(x2);
                            }).tooltip(Acountde.get("rename")).size(32).pad(6);
                            buttons.button(Icon.refresh, Styles.clearNonei, () -> {
                                x2.rename(null);
                                build();
                            }).tooltip(Acountde.get("clear-name")).size(32).pad(6);
                        });
                    }).pad(6).growX().row();
                }
            }).growX();
        }).width(Vars.mobile ? 200 : 1000);
        buttons.clearChildren();
        buttons.defaults().size(250, 50).pad(6);
        buttons.button("@back", Icon.left, this::hide);
        if(Vars.mobile) buttons.row();
        buttons.button(Acountde.get("rename"), Icon.pencil, () -> {
            openRename(build);
        });
        if(Vars.mobile) buttons.row();
        buttons.button(Acountde.get("refresh"), Icon.refresh, () -> {
            build(true);
        });
    }

    public void openRename(@NotNull CoinBuild x2) {
        Dialog dialog = new Dialog();
        var x = dialog.cont.field(x2.displayName(), (s) -> {});
        x.growX().row();
        dialog.buttons.defaults().size(250, 50).pad(6);
        dialog.buttons.button(Acountde.get("rename"), Icon.pencil, () -> {
            x2.rename(x.get().getText());
            build();
            dialog.hide();
        });
        if(Vars.mobile) buttons.row();
        dialog.buttons.button("@back", Icon.left, dialog::hide);
        dialog.show();
    }
}
