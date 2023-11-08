package acountde.types;

import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.util.Time;
import mindustry.gen.Puddle;
import mindustry.type.Liquid;

import static arc.Core.atlas;

public class AnimatedLiquid extends Liquid {
    public TextureRegion[] animRegions;
    public TextureRegion noAnimIcon;
    public float animDelay = 20f;
    public int sprites = 2;

    public AnimatedLiquid(String name, Color color) {
        super(name, color);
    }

    @Override
    public void load() {
        super.load();
        animRegions = new TextureRegion[sprites];
        for(int i = 0; i < sprites; i++) {
            animRegions[i] = atlas.find(name + '-' + i, name);
        }
        noAnimIcon = fullIcon;
    }

    //should be called in Trigger.update
    public void update() {
        fullIcon.set(animRegions[(int) (Time.globalTime / animDelay) % sprites]);
        uiIcon.set(fullIcon);
    }
}