package acountde.ui;

import acountde.Acountde;
import arc.func.Boolc;
import arc.func.Cons;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.graphics.Pal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static arc.Core.settings;

@SuppressWarnings("unchecked")
public class AcountdeSettingTable extends Table {
    public Seq<Setting<?>> list = new Seq<>();

    public void resetToDefaults() {
        list.each(Setting::def);
    }

    public Cell<Table> category(String name) {
        return category(name, Pal.accent);
    }

    public Cell<Table> category(String name, Color color) {
        return UIUtil.category(this, "setting-split." + name, color);
    }

    public Cell<CheckBox> checkPref(String name, TextureRegion region, boolean def, @NotNull Boolc changed) {
        Setting<Boolean> bool = new Setting<>(name, def, changed::get);
        settings.defaults(name, def);
        list.add(bool);
        final Cell<CheckBox>[] out = new Cell[1];
        try {
            changed.get(bool.get());
        } catch(Throwable ignored) {
        }
        table(check -> {
            check.table(left -> {
                left.left();
                left.image(region).size(24);
                left.add(Acountde.get("setting." + name + ".name")).pad(6);
            }).growX().left();
            out[0] = check.check("", bool::set).update(box -> {
                box.setChecked(bool.get());
            });
        }).growX().pad(3).tooltip(Acountde.get("setting." + name + ".description")).row();
        return out[0];
    }

    public static class Setting<T> {
        public final String name;
        public Cons<T> changed;
        public T def;

        @Contract(pure = true)
        public Setting(String name, T def, Cons<T> changed) {
            this.changed = changed;
            this.name = name;
            this.def = def;
        }

        public T get() {
            return (T) settings.get(name, def);
        }

        public void set(T obj) {
            try {
                changed.get(obj);
            } catch(Throwable ignored) {
            }
            settings.put(name, obj);
        }

        public void def() {
            set(def);
        }
    }
}