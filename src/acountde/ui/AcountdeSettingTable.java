package acountde.ui;

import acountde.Acountde;
import arc.func.Boolc;
import arc.func.Cons;
import arc.func.Cons2;
import arc.func.Intc;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.CheckBox;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.graphics.Pal;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static arc.Core.bundle;
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

    public Cell<Slider> sliderPrefI(String name,
                                    TextureRegion region,
                                    int min,
                                    int max,
                                    int step,
                                    int def,
                                    @NotNull Intc changed) {
        return sliderPrefI(name, region, min, max, step, def, (info, i) -> {
            info.right();
            info.add(min + "-" + max).right().row();
            info.add("0").update(l -> {
                l.setText(String.valueOf(i.get()));
            }).right().row();
        }, changed);
    }

    public Cell<Slider> sliderPrefI(String name,
                                    TextureRegion region,
                                    int min,
                                    int max,
                                    int step,
                                    int def,
                                    Cons2<Table, Setting<Integer>> infoBuilder,
                                    @NotNull Intc changed) {
        Setting<Integer> i = new Setting<>(name, def, changed::get);
        settings.defaults(name, def);
        list.add(i);
        final Cell<Slider>[] out = new Cell[1];
        try {
            changed.get(i.get());
        } catch(Throwable ignored) {
        }
        table(slid -> {
            slid.table(left -> {
                left.left();
                left.image(region).size(24);
                left.add(bundle.get("setting." + name + ".name")).pad(6);
            }).growX().left();
            slid.table(info -> {
                infoBuilder.get(info, i);
            }).pad(0, 6, 0, 6);
            out[0] = slid.slider(min, max, step, def, (f) -> i.set((int) f)).update(box -> {
                box.setValue(i.get());
            }).width(200f);
        }).growX().pad(3).tooltip(bundle.get("setting." + name + ".description")).row();
        return out[0];
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
                left.add(bundle.get("setting." + name + ".name")).pad(6);
            }).growX().left();
            out[0] = check.check("", bool::set).update(box -> {
                box.setChecked(bool.get());
            });
        }).growX().pad(3).tooltip(bundle.get("setting." + name + ".description")).row();
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