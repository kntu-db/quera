package ir.ac.kntu.orm.processor;

import com.sun.source.tree.Tree;
import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;
import javassist.ClassPool;
import javassist.CtClass;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.FileOutputStream;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes({
        "ir.ac.kntu.orm.repo.annotations.Entity",
        "ir.ac.kntu.orm.repo.annotations.Join",
        "ir.ac.kntu.orm.repo.annotations.Query",
        "ir.ac.kntu.orm.repo.annotations.Repository",
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ClassPool pool = ClassPool.getDefault();
        Set<? extends Element> targets = roundEnv.getElementsAnnotatedWith(Repository.class);
        for (Element target : targets) {
            CtClass ctClass = pool.makeClass(target.getSimpleName().toString() + "Impl");
            // write ctClass to a file
//            try {
//                ctClass.writeFile();
//            } catch (Exception e) {
//                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
//            }
        }
        return false;
    }
}
