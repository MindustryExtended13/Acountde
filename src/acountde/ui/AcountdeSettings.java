package acountde.ui;

import acountde.invoker.BetaMindyInvoker;

import acountde.utils.CostLib;
import arc.func.Boolc;
import arc.func.Cons;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Cell;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Reflect;

import arc.util.Scaling;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;
import mindustry.ui.fragments.MenuFragment;
import mindustry.world.Block;

import static arc.Core.*;
import static mindustry.Vars.*;
import static acountde.Acountde.*;

public class AcountdeSettings {
    public static final ObjectMap<String, Cons<AcountdeSettingTable>> categories = new ObjectMap<>();
    public static MenuFragment.MenuButton button;
    public static AcountdeSettingTable content;
    public static String current = null;
    public static int index = -1;

    public static void addCategory(String name, Cons<AcountdeSettingTable> cons) {
        if(name != null && cons != null) categories.put(name, cons);
    }

    public static void changeCategory(String name) {
        Cons<AcountdeSettingTable> cons = categories.get(name);
        if(cons != null) {
            content.clearChildren();
            content.list.clear();
            cons.get(content);
            current = name;
        }
    }

    public static void load() {
        button = new MenuFragment.MenuButton(get("settings.category"), Icon.settings, () -> {
            Seq<String> keys = categories.keys().toSeq();
            index = keys.indexOf("main");
            BaseDialog dialog = new BaseDialog("invalid");
            dialog.clearChildren();
            dialog.add(get("settings.category")).color(Pal.accent).row();
            dialog.image().color(Pal.accent).growX().height(4).row();
            dialog.table(table -> {
                table.top();
                table.table(buttons -> {
                    buttons.button(Icon.left, () -> {
                        changeCategory(keys.get(--index));
                    }).size(48).pad(6).update(btn -> {
                        btn.setDisabled(index <= 0);
                    });
                    buttons.table(info -> {
                        info.setBackground(Tex.pane);
                        info.add("").update(l -> l.setText(current));
                    }).size(mobile ? 150 : 600, 48).pad(6);
                    buttons.button(Icon.right, () -> {
                        changeCategory(keys.get(++index));
                    }).size(48).pad(6).update(btn -> {
                        btn.setDisabled(index >= keys.size - 1);
                    });
                }).row();
                content = new AcountdeSettingTable();
                ScrollPane pane = new ScrollPane(content, scene.getStyle(ScrollPane.ScrollPaneStyle.class));
                Cell<ScrollPane> cell = table.add(pane);
                if(!mobile) {
                    cell.minWidth(1000);
                }
            }).grow().top().row();
            dialog.table(buttons -> {
                buttons.defaults().size(250, 50).pad(6);
                buttons.button("@back", Icon.left, () -> {
                    dialog.hide();
                    Reflect.invoke(SettingsMenuDialog.class, ui.settings, "back", new Object[0]);
                });
                if(mobile) {
                    buttons.row();
                }
                String b = bundle.get("settings.reset", "Reset to Defaults");
                buttons.button(b, Icon.refresh, content::resetToDefaults);
            }).growX();
            dialog.show();
            changeCategory(keys.get(index));
        });
    }

    static {
        Boolc ignoredBool = (ignored) -> {};

        addCategory("main", (t) -> {
            t.category("main");
            var _tmp = Icon.save.getRegion();
            t.checkPref("acountde-auto-bundle", Icon.folder.getRegion(), false, ignoredBool);
            t.checkPref("acountde-schizophrenia", Icon.planet.getRegion(), false, ignoredBool);
            t.checkPref("acountde-autosave", _tmp, false, ignoredBool);
            t.sliderPrefI("acountde-save-interval", _tmp, 600, 9600, 60, 4800, (ignored) -> {
                server.autosaveCounter = server.saveInterval();
            });
            t.category("tech");
            t.table(b -> {
                var content = Vars.content;
                b.defaults().size(200, 60).pad(3);
                Runnable r = () -> {if(mobile) {b.row();}};
                b.button(get("unlock.blocks"), () -> {
                    content.blocks().each(Block::quietUnlock);
                });
                r.run();
                b.button(get("unlock.items"), () -> {
                    content.items().each(Item::quietUnlock);
                });
                r.run();
                b.button(get("unlock.liquids"), () -> {
                    content.liquids().each(Liquid::quietUnlock);
                });
                r.run();
                b.button(get("unlock.units"), () -> {
                    content.units().each(UnitType::quietUnlock);
                });
                r.run();
                b.button(get("unlock.all"), () -> {
                    content.each(c -> {
                        if(c instanceof UnlockableContent cont) {
                            cont.quietUnlock();
                        }
                    });
                });
            });
        });

        addCategory("betamindy", (t) -> {
            boolean disabled = !isBetaMindyInstalled;
            t.category("betamindy-main");
            t.checkPref("acountde-betamindy-hack", region("frog"), false, BetaMindyInvoker::setHack)
                    .disabled(disabled);
            t.checkPref("acountde-anucoin-support", region("anucoin-outline"), true, ignoredBool)
                    .disabled(disabled);
            t.sliderPrefI("acountde-anucoin-prize", region("math"), 0, 11, 1, 5, (info, ignored) -> {
                info.image().scaling(Scaling.fit).size(32).update(i -> {
                    i.setDrawable(CostLib.getImage());
                });
            }, (ignored) -> {
            });
            t.category("betamindy-asset");
        });
    }
}