package ir.ac.kntu.orm.processor;

import com.squareup.javapoet.*;
import ir.ac.kntu.orm.mapping.ResultSetMapper;
import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;
import ir.ac.kntu.orm.utils.NamedParameterStatement;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.sql.DataSource;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository processor for generating repository classes.
 * @author Mahdi Lotfi
 */
@SupportedAnnotationTypes({
        "ir.ac.kntu.orm.repo.annotations.Entity",
        "ir.ac.kntu.orm.repo.annotations.Join",
        "ir.ac.kntu.orm.repo.annotations.Query",
        "ir.ac.kntu.orm.repo.annotations.Repository",
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryProcessor extends AbstractProcessor {

    private static final String DATA_SOURCE = "dataSource";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> targets = roundEnv.getElementsAnnotatedWith(Repository.class);
        for (Element target : targets) {
            TypeElement element = (TypeElement) target;

            List<MethodSpec> methods = target.getEnclosedElements()
                    .stream()
                    .map(e -> ((ExecutableElement) e))
                    .map(e -> {
                        MethodSpec.Builder builder = MethodSpec.methodBuilder(e.getSimpleName().toString())
                                .addAnnotation(Override.class)
                                .addModifiers(Modifier.PUBLIC)
                                .returns(ClassName.get(e.getReturnType()))
                                .beginControlFlow("try ($T con = dataSource.getConnection())", Connection.class)
                                .addCode("$T rs = new $T(con, $S)", ResultSet.class, NamedParameterStatement.class, e.getAnnotation(Query.class).value());
                        for (VariableElement variable : e.getParameters())
                            builder.addCode(".setObject($S, $L)", variable.getSimpleName().toString(), variable.getSimpleName().toString());
                        builder.addStatement(".executeQuery()");
                        if (ClassName.get(e.getReturnType()) instanceof ParameterizedTypeName && ((ParameterizedTypeName)ClassName.get(e.getReturnType())).rawType.equals(ClassName.get(List.class)))
                            builder.addStatement("return new $T<>($T.class, rs).getMappedResultList()", ResultSetMapper.class, ((ParameterizedTypeName)ClassName.get(e.getReturnType())).typeArguments.get(0));
                        else
                            builder.addStatement("return new $T<>($T.class, rs).getMappedUniqueResult()", ResultSetMapper.class, ClassName.get(e.getReturnType()));
                        return (
                                builder
                                        .nextControlFlow("catch ($T e)", SQLException.class)
                                        .addStatement("throw new $T(e)", RuntimeException.class)
                                        .endControlFlow()
                                        .addParameters(e.getParameters().stream().map(ParameterSpec::get).collect(Collectors.toList()))
                                        .build()
                        );
                    }).collect(Collectors.toList());

            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addParameter(DataSource.class, "dataSource")
                    .addStatement("this.dataSource = dataSource")
                    .build();

            List<? extends AnnotationMirror> annotationMirrors = element.getAnnotationMirrors()
                    .stream().filter(a -> !a.getAnnotationType().toString().equals(Repository.class.getCanonicalName()))
                    .collect(Collectors.toList());

            TypeSpec repository = TypeSpec.classBuilder(target.getSimpleName().toString() + "Impl")
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(element))
                    .addMethods(methods)
                    .addMethod(constructor)
                    .addAnnotations(annotationMirrors.stream().map(AnnotationSpec::get).collect(Collectors.toList()))
                    .addField(DataSource.class, "dataSource", Modifier.PRIVATE)
                    .build();

            JavaFile javaFile = JavaFile.builder(target.getEnclosingElement().toString(), repository)
                    .build();

            try {
                javaFile.writeTo(processingEnv.getFiler());
            } catch (Exception e) {
                processingEnv.getMessager().printMessage(
                        javax.tools.Diagnostic.Kind.ERROR,
                        "Could not write file: " + e.getMessage());
            }
        }
        return false;
    }
}
