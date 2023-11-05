package acountde.ui;

import acountde.Acountde;
import acountde.invoker.BetaMindyInvoker;
import arc.func.Cons;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Reflect;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.type.UnitType;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;
import mindustry.world.Block;

import static arc.Core.*;
import static arc.Core.bundle;
import static mindustry.Vars.*;
import static acountde.Acountde.*;

public class AcountdeSettings {
    public static final ObjectMap<String, Cons<AcountdeSettingTable>> categories = new ObjectMap<>();
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
        ui.menufrag.addButton(get("settings.category"), drawable("setting-icon"), () -> {
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
                        info.setBackground(Tex.button);
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
        addCategory("main", (t) -> {
            t.category("main");
            t.checkPref("auto-bundle", Icon.folder.getRegion(), false, (ignored) -> {});
            t.category("tech");
            t.table(b -> {
                b.defaults().size(200, 60).pad(3);
                Runnable r = () -> {if(mobile) {b.row();}};
                b.button(get("unlock.blocks"), to(Blocks.copperWall.uiIcon), () -> {
                    Vars.content.blocks().each(Block::quietUnlock);
                });
                r.run();
                b.button(get("unlock.items"), to(Items.copper.uiIcon), () -> {
                    Vars.content.items().each(Item::quietUnlock);
                });
                r.run();
                b.button(get("unlock.liquids"), to(Liquids.ozone.uiIcon), () -> {
                    Vars.content.liquids().each(Liquid::quietUnlock);
                });
                r.run();
                b.button(get("unlock.units"), to(UnitTypes.dagger.uiIcon), () -> {
                    Vars.content.units().each(UnitType::quietUnlock);
                });
                r.run();
                b.button(get("unlock.all"), to(Items.lead.uiIcon), () -> {
                    Vars.content.liquids().each(Liquid::quietUnlock);
                    Vars.content.items().each(Item::quietUnlock);
                });
            });
        });

        addCategory("betamindy", (t) -> {
            t.category("betamindy-main");
            t.checkPref("betamindy-hack", region("frog"), false, BetaMindyInvoker::setHack)
                    .disabled(!isBetaMindyInstalled);
            t.checkPref("anucoin-support", region("anucoin-outline"), true, (ignored) -> {})
                    .disabled(!isBetaMindyInstalled);
            t.category("betamindy-asset");
        });
    }
}