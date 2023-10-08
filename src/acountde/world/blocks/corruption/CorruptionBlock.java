package acountde.world.blocks.corruption;

import acountde.world.blocks.ACBlock;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import mindustry.Vars;

public class CorruptionBlock extends ACBlock {
    public static final float MAX_PULSE = 126f;
    public int pulseInterval = -1;
    public float inpStrength = 2f;

    public CorruptionBlock(String name) {
        super(name);
        squareSprite = false;
        destructible = true;
        update = true;
    }

    public class CorruptionBuild extends ACBuild {
        public int pulseIntervalL = -1;
        public boolean pulsing = false;
        public float pulseStrength;
        public float pulse = 0;

        public void pulse(float strength) {
            pulseStrength = strength;
            pulsing = true;
        }

        public float calculateSize() {
            return size * Vars.tilesize + Mathf.lerp(0, pulseStrength, pulseProgress());
        }

        public float pulseProgress() {
            return pulse / MAX_PULSE;
        }

        public TextureRegion getRegion() {
            return block.region;
        }

        @Override
        public void updateTile() {
            if(pulseInterval != -1 && pulseIntervalL == -1) {
                pulseIntervalL = Mathf.random(pulseInterval);
            }

            if(pulsing) {
                pulse += Mathf.lerp(0, MAX_PULSE, 0.1f);
            } else {
                pulse -= Mathf.lerp(MAX_PULSE, 0, 0.1f);
            }
            pulse = Mathf.clamp(pulse, 0, MAX_PULSE);
            if(pulse >= MAX_PULSE) pulsing = false;

            if(pulseInterval != -1 && !pulsing) {
                if(pulseIntervalL > 0) {
                    pulseIntervalL--;
                } else {
                    pulseIntervalL = pulseInterval;
                    pulse(inpStrength);
                }
            }
        }

        @Override
        public void drawTeam() {
        }

        @Override
        public void draw() {
            float calc = calculateSize();
            Draw.rect(getRegion(), this.x, this.y, calc, calc, this.drawrot());
            drawTeamTop();
        }

        @Override
        public void drawTeamTop() {
            if (this.block.teamRegion.found()) {
                if (this.block.teamRegions[this.team.id] == this.block.teamRegion) {
                    Draw.color(this.team.color);
                }

                float calc = calculateSize();
                Draw.rect(this.block.teamRegions[this.team.id], this.x, this.y, calc, calc, this.drawrot());
                Draw.color();
            }
        }
    }
}