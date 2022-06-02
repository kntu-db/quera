package ir.ac.kntu.orm.processor;

import com.squareup.javapoet.*;
import ir.ac.kntu.orm.mapping.RawMapper;
import ir.ac.kntu.orm.mapping.ResultSetMapper;
import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;
import ir.ac.kntu.orm.utils.NamedParameterStatement;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Repository processor for generating repository classes.
 *
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

    public static final String IMPLEMENTATION_SUFFIX = "Impl";
    public static final String DATA_SOURCE_FIELD = "dataSource";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> targets = roundEnv.getElementsAnnotatedWith(Repository.class);
        for (Element target : targets) {
            TypeElement element = (TypeElement) target;

            List<MethodSpec> methodSpecs = target.getEnclosedElements()
                    .stream()
                    .map(e -> ((ExecutableElement) e))
                    .map(this::getMethodSpec)
                    .collect(Collectors.toList());

            methodSpecs.add(getConstructor());

            List<AnnotationSpec> annotationSpecs = getAnnotationMirrors(element)
                    .stream()
                    .map(AnnotationSpec::get)
                    .collect(Collectors.toList());

            TypeSpec repository = TypeSpec.classBuilder(target.getSimpleName().toString() + IMPLEMENTATION_SUFFIX)
                    .addField(DataSource.class, DATA_SOURCE_FIELD, Modifier.PRIVATE, Modifier.FINAL)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addSuperinterface(ClassName.get(element))
                    .addAnnotations(annotationSpecs)
                    .addMethods(methodSpecs)
                    .build();

            JavaFile javaFile = JavaFile.builder(target.getEnclosingElement().toString(), repository).build();

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

    /**
     * Returns method spec for the given method element.
     *
     * @param e method element
     * @return method spec
     */
    private MethodSpec getMethodSpec(ExecutableElement e) {
        MethodSpec.Builder builder = MethodSpec.methodBuilder(e.getSimpleName().toString());

        TypeName returnTypeName = ClassName.get(e.getReturnType());

        builder
                .returns(returnTypeName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameters(e.getParameters().stream().map(ParameterSpec::get).collect(Collectors.toList()))
                .beginControlFlow("try ($T con = $L.getConnection())", Connection.class, DATA_SOURCE_FIELD)
                .addCode("$T rs = new $T(con, $S)", ResultSet.class, NamedParameterStatement.class, e.getAnnotation(Query.class).value());

        for (VariableElement variable : e.getParameters())
            builder.addCode(".setObject($S, $L)", variable.getSimpleName().toString(), variable.getSimpleName().toString());

        builder.addStatement(".executeQuery()");

        if (returnTypeName instanceof ParameterizedTypeName && ((ParameterizedTypeName) returnTypeName).rawType.equals(ClassName.get(List.class))) {
            TypeName parameterTypeName = ((ParameterizedTypeName) returnTypeName).typeArguments.get(0);
            if (parameterTypeName.equals(ArrayTypeName.get(Object[].class)))
                builder.addStatement("return new $T(rs).getMappedResultList()", RawMapper.class);
            else
                builder.addStatement("return new $T<>($T.class, rs).getMappedResultList()", ResultSetMapper.class, parameterTypeName);
        }
        else if (returnTypeName.equals(ArrayTypeName.get(Object[].class)))
            builder.addStatement("return new $T(rs).getMappedUniqueResult()", RawMapper.class);
        else
            builder.addStatement("return new $T<>($T.class, rs).getMappedUniqueResult()", ResultSetMapper.class, returnTypeName);

        builder
                .nextControlFlow("catch ($T e)", SQLException.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow();

        return builder.build();
    }

    /**
     * Returns default constructor for repository families.
     *
     * @return constructor
     */
    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addParameter(DataSource.class, DATA_SOURCE_FIELD)
                .addStatement("this.$L = $L", DATA_SOURCE_FIELD, DATA_SOURCE_FIELD)
                .build();
    }

    /**
     * Returns Annotation mirrors for element.
     * Removes Repository annotation because it is processed by this processor.
     *
     * @param element element
     * @return Annotation mirrors for element excluding Repository annotation
     */
    private List<? extends AnnotationMirror> getAnnotationMirrors(TypeElement element) {
        return element.getAnnotationMirrors()
                .stream()
                .filter(a -> !a.getAnnotationType().toString().equals(Repository.class.getCanonicalName()))
                .collect(Collectors.toList());
    }
}
