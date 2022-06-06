package ir.ac.kntu.orm.processor;

import com.squareup.javapoet.*;
import ir.ac.kntu.orm.mapping.meta.EntityManager;
import ir.ac.kntu.orm.repo.annotations.Join;
import ir.ac.kntu.orm.repo.annotations.Query;
import ir.ac.kntu.orm.repo.annotations.Repository;
import ir.ac.kntu.orm.utils.NameConverter;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Repository processor for generating repository classes.
 *
 * @author Mahdi Lotfi
 */
@SupportedAnnotationTypes({
        "ir.ac.kntu.orm.repo.annotations.Repository",
})
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class RepositoryProcessor extends AbstractProcessor {

    public static final String IMPLEMENTATION_SUFFIX = "Impl";
    public static final String DATA_SOURCE_FIELD = "dataSource";
    public static final String ENTITY_MANAGER_FIELD = "entityManager";

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
                    .addField(EntityManager.class, ENTITY_MANAGER_FIELD, Modifier.PRIVATE, Modifier.FINAL)
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
        Map<String, TypeMirror> parameters = e.getParameters().stream().collect(Collectors.toMap(p -> p.getSimpleName().toString(), VariableElement::asType));

        MethodSpec.Builder builder = MethodSpec.methodBuilder(e.getSimpleName().toString());

        TypeName returnTypeName = ClassName.get(e.getReturnType());

        builder
                .returns(returnTypeName)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .addParameters(e.getParameters().stream().map(ParameterSpec::get).collect(Collectors.toList()))
                .beginControlFlow("try ($T con = $L.getConnection())", Connection.class, DATA_SOURCE_FIELD);

        List<String> params = new ArrayList<>();

        String query = restructureParams(e.getAnnotation(Query.class).value(), params);

        builder.addStatement("$T ps = con.prepareStatement($S)", PreparedStatement.class, query);

        for (int i = 0; i < params.size(); i++) {
            builder.addStatement("ps.setObject($L, $L)", i + 1, params.get(i));
        }

        builder.addStatement("$T rs = ps.executeQuery()", ResultSet.class);

        appendMappingStatements(e, builder, returnTypeName);

        builder
                .nextControlFlow("catch ($T e)", SQLException.class)
                .addStatement("throw new $T(e)", RuntimeException.class)
                .endControlFlow();

        return builder.build();
    }

    private void appendMappingStatements(ExecutableElement e, MethodSpec.Builder builder, TypeName returnTypeName) {
        if (returnTypeName instanceof ParameterizedTypeName) {
            ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) returnTypeName;
            if (parameterizedTypeName.rawType.equals(ClassName.get(List.class))) {
                if (parameterizedTypeName.typeArguments.get(0).equals(ArrayTypeName.get(Object[].class))) {
                    builder.addCode("return $L.prepareMapping()", ENTITY_MANAGER_FIELD);
                    builder.addStatement(".getResultList(rs)");
                } else {
                    builder.addCode("return $L.prepareMapping($T.class, $S)", ENTITY_MANAGER_FIELD, parameterizedTypeName.typeArguments.get(0), "");
                    builder.addStatement(".getResultList(rs)");
                }
            } else if (parameterizedTypeName.rawType.equals(ClassName.get(Optional.class))) {
                if (parameterizedTypeName.typeArguments.get(0).equals(ArrayTypeName.get(Object[].class))) {
                    builder.addCode("return $T.ofNullable($L.prepareMapping()", Optional.class, ENTITY_MANAGER_FIELD);
                    builder.addStatement(".getUniqueResult(rs))");
                } else {
                    builder.addCode("return $T.ofNullable($L.prepareMapping($T.class, $S)", Optional.class, ENTITY_MANAGER_FIELD, parameterizedTypeName.typeArguments.get(0), "");
                    builder.addStatement(".getUniqueResult(rs))");
                }
            }
        } else {
            builder.addCode("return $L.prepareMapping($T.class, $S)", ENTITY_MANAGER_FIELD, returnTypeName, "");
            builder.addStatement(".getUniqueResult(rs)");
        }
    }

    private String restructureParams(String query, List<String> params) {
        Matcher matcher = Pattern.compile(":(([a-zA-Z\\d]+)(\\.([a-zA-z\\d]+))?)").matcher(query);
        while (matcher.find()) {
            if (matcher.group(4) == null) {
                query = query.replaceFirst(matcher.group(), "?");
                params.add(matcher.group(2));
            } else {
                query = query.replaceFirst(matcher.group(), "?");
                params.add(matcher.group(2) + "." + NameConverter.fieldToGetter(matcher.group(4)) + "()");
            }
        }
        return query;
    }

    private void prepareMapping(MethodSpec.Builder builder, TypeName entity, String alias, Join[] joins) {
        builder.addCode("return $L.prepareMapping($T.class, $S)", ENTITY_MANAGER_FIELD, entity, alias);
        for (Join join : joins)
            builder.addCode(".join($S, $S)", join.alias(), join.path());
    }

    /**
     * Returns default constructor for repository families.
     *
     * @return constructor
     */
    private MethodSpec getConstructor() {
        return MethodSpec.constructorBuilder()
                .addParameter(EntityManager.class, ENTITY_MANAGER_FIELD)
                .addStatement("this.$L = $L", ENTITY_MANAGER_FIELD, ENTITY_MANAGER_FIELD)
                .addStatement("this.$L = $L.getDataSource()", DATA_SOURCE_FIELD, ENTITY_MANAGER_FIELD)
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
