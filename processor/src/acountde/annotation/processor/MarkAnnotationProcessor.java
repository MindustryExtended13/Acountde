package acountde.annotation.processor;

import acountde.anno.AddMark;
import acountde.anno.MarkType;
import acountde.annotation.AcountdeProcessor;
import arc.Core;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import me13.core.annotations.util.VarBox;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;

@SupportedAnnotationTypes("acountde.anno.AddMark")
public class MarkAnnotationProcessor extends AcountdeProcessor {
    @Override
    public void process() {
        MethodSpec.Builder method = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC);

        method.addStatement("String tmp");
        for(VarBox box : fields(AddMark.class)) {
            String fieldName = box.enclosingType() + "." + box.name();
            if(box.is(Modifier.STATIC) && box.is(Modifier.PUBLIC)) {
                AddMark x = box.annotation(AddMark.class);
                MarkType[] types = x.value();
                String var = x.var();
                if(var == null || types == null || types.length == 0) {
                    err(fieldName + " have invalid mark settings");
                } else {
                    for(MarkType type : types) {
                        String b = "\"prefixes." + type.name().toLowerCase() + '"';
                        method.addStatement("tmp = $L.$L", fieldName, var);
                        method.addStatement("$L.$L = $T.get($L) + (tmp == null ? \"\" : '\\n' + tmp)",
                                fieldName, var, tname("acountde.Acountde"), b);
                    }
                }
            } else {
                err(fieldName + " must be static and public!");
            }
        }

        try {
            TypeSpec.Builder builder = TypeSpec.classBuilder("MarksSetup");
            write(builder.addModifiers(Modifier.PUBLIC).addMethod(method.build()), packageName);
        } catch(Throwable t) {
            err("Can`t create class: " + t.toString());
        }
    }
}