package acountde.annotation;

import arc.func.Prov;
import arc.struct.Seq;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import me13.core.annotations.BaseProcessor;
import me13.core.annotations.util.MethodBox;
import me13.core.annotations.util.VarBox;
import org.jetbrains.annotations.NotNull;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

public abstract class AcountdeProcessor extends BaseProcessor {
    public static final String packageName = "acountde.gen";

    public static @NotNull Seq<VarBox> getParameters(@NotNull MethodBox box) {
        Seq<VarBox> boxes = new Seq<>();
        for(VariableElement element : box.e.getParameters()) {
            boxes.add(new VarBox(element));
        }
        return boxes;
    }

    public static TypeMirror mirror(@NotNull Prov<Class<?>> prov) {
        TypeMirror mirror = null;
        try {
            prov.get();
        } catch(MirroredTypeException mte) {
            mirror = mte.getTypeMirror();
        }
        return mirror;
    }

    public static TypeName tname(@NotNull TypeMirror mirror) {
        return tname(mirror.toString());
    }

    public static TypeName tname(@NotNull String name) {
        if(!name.contains(".")) return ClassName.get(packageName, name).box();
        String pack = name.substring(0, name.lastIndexOf("."));
        String simple = name.substring(name.lastIndexOf(".") + 1);
        return ClassName.get(pack, simple).box();
    }

    {
        childs = 1;
    }
}
