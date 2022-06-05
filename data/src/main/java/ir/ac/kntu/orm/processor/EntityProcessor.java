package ir.ac.kntu.orm.processor;

import ir.ac.kntu.orm.mapping.annotations.Entity;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({
        "ir.ac.kntu.orm.mapping.annotations.Entity"
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class EntityProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeElement[] elements = roundEnv.getElementsAnnotatedWith(Entity.class)
                .stream()
                .map(e -> (TypeElement) e)
                .toArray(TypeElement[]::new);

        if (elements.length == 0) return false;

        try {
            FileObject resource = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "META-INF/orm-entities.inf", elements);
            PrintWriter writer = new PrintWriter(resource.openWriter());
            writer.print(Arrays.stream(elements)
                    .map(e -> e.getQualifiedName().toString())
                    .collect(Collectors.joining("\n")));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to create meta.inf file", e);
        }

        return false;
    }
}
