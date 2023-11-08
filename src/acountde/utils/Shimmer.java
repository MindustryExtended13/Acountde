package acountde.utils;

import acountde.Acountde;
import acountde.content.ACBlocks;
import acountde.content.ACStatusEffects;
import arc.ApplicationListener;
import arc.func.Cons;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.UnitType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Shimmer implements ApplicationListener {
    public static final Seq<TransEvent> events = new Seq<>();

    public static void onMutantChain(UnitType @NotNull... types) {
        if(types.length < 2) return;
        for(int i = 0; i < types.length; i++) {
            if(i < types.length - 1) {
                onMutant(types[i], types[i + 1]);
            }
        }
    }

    public static void onMutant(UnitType from, UnitType to) {
        events.add(new TypeMutation(from, to));
    }

    public static void on(Cons<Box> cons) {
        events.add(new TransEvent() {
            @Override
            public void handle(Box box) {
                cons.get(box);
            }
        });
    }

    public static @NotNull Box transmutation(Unit unit) {
        Box box = new Box(unit);
        events.each(event -> {
            event.handle(box);
        });
        return box;
    }

    @Override
    public void update() {
        if(Acountde.doingUpdate()) {
            Groups.unit.each(u -> {
                if(!u.type.flying && u.floorOn() == ACBlocks.shimmer) {
                    if(!u.hasEffect(ACStatusEffects.shimmerTransmutation) &&
                            !u.hasEffect(ACStatusEffects.shimmered)) {
                        u.apply(ACStatusEffects.shimmerTransmutation, 180);
                    }
                }
            });
        }
    }

    public static class Box {
        public final Unit old;
        public Unit unit;

        @Contract(pure = true)
        public Box(Unit unit) {
            this.old = this.unit = unit;
        }

        public boolean unitChanged() {
            return old != unit;
        }
    }

    public static class TypeMutation extends TransEvent {
        public final UnitType from, to;

        public TypeMutation(UnitType from, UnitType to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public void handle(@NotNull Box box) {
            if(!box.unitChanged() && box.unit.type == from) {
                box.unit = ACUtil.setType(box.unit, to);
            }
        }
    }

    public static class TransEvent {
        public void handle(Box box) {
        }
    }
}