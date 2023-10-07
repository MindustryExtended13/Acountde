package acountde.annotation.processor;

import acountde.anno.HandleEvent;
import acountde.annotation.AcountdeProcessor;
import arc.Events;
import arc.struct.Seq;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import me13.core.annotations.util.MethodBox;
import me13.core.annotations.util.VarBox;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Modifier;

@SupportedAnnotationTypes("acountde.anno.HandleEvent")
public class EventAnnotationProcessor extends AcountdeProcessor {
    @Override
    public void process() {
        MethodSpec.Builder method = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.STATIC, Modifier.PUBLIC);

        for(MethodBox box : methods(HandleEvent.class)) {
            if(box.is(Modifier.PUBLIC) && box.is(Modifier.STATIC)) {
                Seq<VarBox> boxes = AcountdeProcessor.getParameters(box);
                boolean isNotSingle = boxes.size > 0;
                String cl;
                if(isNotSingle) {
                    if(boxes.size > 1) {
                        throw new ArrayIndexOutOfBoundsException("method must have 0-1 parameters");
                    } else {
                        cl = boxes.get(0).e.asType().toString();
                    }
                } else {
                    cl = mirror(box.annotation(HandleEvent.class)::value).toString();
                }
                TypeName clType = tname(cl);
                method.beginControlFlow("$T.on($T.class, (e) ->", tname(Events.class), clType);
                TypeName t = tname(box.e.getEnclosingElement().toString());
                String s = box.e.getSimpleName().toString();
                if(isNotSingle) {
                    method.addStatement("$T.$L(e)", t, s);
                } else {
                    method.addStatement("$T.$L()", t, s);
                }
                method.endControlFlow(")");
            } else {
                err("method must be public and static");
            }
        }

        try {
            TypeSpec.Builder builder = TypeSpec.classBuilder("EventsSetup");
            write(builder.addModifiers(Modifier.PUBLIC).addMethod(method.build()), packageName);
        } catch(Throwable t) {
            err("Can`t create class: " + t.toString());
        }
    }
}