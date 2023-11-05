package acountde.utils;

import arc.Events;
import arc.struct.ObjectSet;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.type.Planet;
import org.jetbrains.annotations.NotNull;

public class MPS {
    public static void init() {
        ObjectSet<Planet> solarSystemsSet = new ObjectSet<>();
        for(Planet planet : Vars.content.planets()) {
            Planet solarSystem = planet.solarSystem;
            if(solarSystem == null) continue;
            solarSystemsSet.add(solarSystem);
        }
        Planet[] solarSystems = solarSystemsSet.toSeq().toArray(Planet.class);
        Events.run(EventType.Trigger.update, () -> {
            for(Planet planet : solarSystems){
                updatePlanet(planet);
            }
            PlanetParams state = Vars.ui.planet.state;
            if(state.solarSystem != state.planet.solarSystem) {
                state.solarSystem = state.planet.solarSystem;
            }
        });
    }

    private static void updatePlanet(@NotNull Planet planet) {
        planet.position.setZero();
        planet.addParentOffset(planet.position);
        if(planet.parent != null) {
            planet.position.add(planet.parent.position);
        }
        for(Planet child : planet.children) {
            updatePlanet(child);
        }
    }
}
