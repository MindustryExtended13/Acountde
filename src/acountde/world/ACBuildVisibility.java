package acountde.world;

import acountde.Acountde;
import mindustry.world.meta.BuildVisibility;

import static mindustry.world.meta.BuildVisibility.*;

public class ACBuildVisibility {
    public static final BuildVisibility schizophrenia = new BuildVisibility(() -> {
        return editorOnly.visible() || (sandboxOnly.visible() && Acountde.schizophrenia());
    });
}