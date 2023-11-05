package acountde.utils;

import arc.math.geom.Rect;
import arc.struct.Seq;
import mindustry.gen.Building;

public class ACTmp {
    public static final Seq<Building> seq1b = new Seq<>();
    public static final Seq<Building> seq2b = new Seq<>();
    public static final Seq<Building> seq3b = new Seq<>();
    public static final Rect rect1 = new Rect();
    public static final Rect rect2 = new Rect();
    public static final Rect rect3 = new Rect();

    public static void switchSeq() {
        seq3b.clear();
        seq3b.add(seq1b);
        seq1b.clear();
        seq1b.add(seq2b);
        seq2b.clear();
        seq2b.add(seq3b);
    }

    public static void switchRect() {
        rect3.set(rect1);
        rect1.set(rect2);
        rect2.set(rect3);
    }
}