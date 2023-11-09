package acountde.data;

import acountde.Acountde;
import acountde.content.ACUnits;
import acountde.types.entity.ACDataUnitEntity;
import arc.Core;
import arc.files.Fi;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.gen.Groups;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ACData {
    public static final Seq<Long> all_ids = new Seq<>();
    public static Fi save_dir;

    public static void updateSaveDir() {
        save_dir = Core.settings.getDataDirectory().child("game_data");
        if(!save_dir.exists()) {
            save_dir.mkdirs();
        }
    }

    public static void updateData() {
        all_ids.clear();
        for(Fi fi : save_dir.list()) {
            if(fi.isDirectory()) {
                try {
                    all_ids.add(Long.parseLong(fi.name()));
                } catch(Throwable ignored) {
                }
            }
        }
    }

    public static void handle(@NotNull ACDataUnitEntity entity) {
        entity.SAVE_ID = Mathf.random(0, Long.MAX_VALUE);
        if(all_ids.contains(entity.SAVE_ID)) {
            handle(entity);
        }
    }

    public static boolean HAVE_SAVE_ID() {
        return GET_SAVE_ID() != -1;
    }

    public static ACDataUnitEntity getEntity() {
        return (ACDataUnitEntity) Groups.unit.find(u -> u instanceof ACDataUnitEntity);
    }

    public static long GET_SAVE_ID() {
        long[] out = new long[] {-1};
        Groups.unit.each(unit -> {
            if(unit instanceof ACDataUnitEntity entity) {
                out[0] = entity.SAVE_ID;
            }
        });
        if(out[0] == -1) {
            updateData();
            var entity = ACUnits.dataUnit.spawn(-32, -32);
            if(entity instanceof ACDataUnitEntity entity1) {
                handle(entity1);
                return entity1.SAVE_ID;
            }
        }
        return out[0];
    }

    public static @Nullable Fi getSaveDir() {
        if(HAVE_SAVE_ID()) {
            Fi fi = save_dir.child(String.valueOf(GET_SAVE_ID()));
            if(!fi.exists()) fi.mkdirs();
            return fi;
        } else {
            return null;
        }
    }
}