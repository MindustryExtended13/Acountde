package acountde.content;

import acountde.dimension.Dimension;
import arc.struct.Seq;
import mindustry.ctype.UnlockableContent;
import org.jetbrains.annotations.Contract;

public class ACRegistry {
    public static final Seq<Object> all = new Seq<>();

    public static Seq<Dimension> dimensions() {
        return all.select(o -> o instanceof Dimension).map(o -> (Dimension) o);
    }

    public static Seq<UnlockableContent> content() {
        return all.select(o -> o instanceof UnlockableContent).map(o -> (UnlockableContent) o);
    }

    @Contract("_ -> param1")
    public static<T> T register(T object) {
        all.add(object);
        return object;
    }
}