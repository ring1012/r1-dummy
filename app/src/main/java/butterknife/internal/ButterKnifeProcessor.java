package butterknife.internal;

import butterknife.Bind;
import butterknife.BindBool;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import butterknife.OnLongClick;
import butterknife.OnPageChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import butterknife.internal.FieldCollectionViewBinding;
import butterknife.internal.ListenerClass;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import org.apache.commons.io.FilenameUtils;

/* loaded from: classes.dex */
public final class ButterKnifeProcessor extends AbstractProcessor {
    static final /* synthetic */ boolean $assertionsDisabled;
    public static final String ANDROID_PREFIX = "android.";
    private static final String COLOR_STATE_LIST_TYPE = "android.content.res.ColorStateList";
    private static final String DRAWABLE_TYPE = "android.graphics.drawable.Drawable";
    private static final String ITERABLE_TYPE = "java.lang.Iterable<?>";
    public static final String JAVA_PREFIX = "java.";
    private static final List<Class<? extends Annotation>> LISTENERS;
    private static final String LIST_TYPE;
    private static final String NULLABLE_ANNOTATION_NAME = "Nullable";
    public static final String SUFFIX = "$$ViewBinder";
    static final String VIEW_TYPE = "android.view.View";
    private Elements elementUtils;
    private Filer filer;
    private Types typeUtils;

    static {
        $assertionsDisabled = !ButterKnifeProcessor.class.desiredAssertionStatus();
        LIST_TYPE = List.class.getCanonicalName();
        LISTENERS = Arrays.asList(OnCheckedChanged.class, OnClick.class, OnEditorAction.class, OnFocusChange.class, OnItemClick.class, OnItemLongClick.class, OnItemSelected.class, OnLongClick.class, OnPageChange.class, OnTextChanged.class, OnTouch.class);
    }

    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        this.elementUtils = env.getElementUtils();
        this.typeUtils = env.getTypeUtils();
        this.filer = env.getFiler();
    }

    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Bind.class.getCanonicalName());
        for (Class<? extends Annotation> listener : LISTENERS) {
            types.add(listener.getCanonicalName());
        }
        types.add(BindBool.class.getCanonicalName());
        types.add(BindColor.class.getCanonicalName());
        types.add(BindDimen.class.getCanonicalName());
        types.add(BindDrawable.class.getCanonicalName());
        types.add(BindInt.class.getCanonicalName());
        types.add(BindString.class.getCanonicalName());
        return types;
    }

    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment env) throws IOException {
        Map<TypeElement, BindingClass> targetClassMap = findAndParseTargets(env);
        for (Map.Entry<TypeElement, BindingClass> entry : targetClassMap.entrySet()) {
            Element element = (TypeElement) entry.getKey();
            BindingClass bindingClass = entry.getValue();
            try {
                JavaFileObject jfo = this.filer.createSourceFile(bindingClass.getFqcn(), new Element[]{element});
                Writer writer = jfo.openWriter();
                writer.write(bindingClass.brewJava());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                error(element, "Unable to write view binder for type %s: %s", element, e.getMessage());
            }
        }
        return true;
    }

    private Map<TypeElement, BindingClass> findAndParseTargets(RoundEnvironment env) {
        Map<TypeElement, BindingClass> targetClassMap = new LinkedHashMap<>();
        Set<String> erasedTargetNames = new LinkedHashSet<>();
        for (Element element : env.getElementsAnnotatedWith(Bind.class)) {
            try {
                parseBind(element, targetClassMap, erasedTargetNames);
            } catch (Exception e) {
                logParsingError(element, Bind.class, e);
            }
        }
        for (Class<? extends Annotation> listener : LISTENERS) {
            findAndParseListener(env, listener, targetClassMap, erasedTargetNames);
        }
        for (Element element2 : env.getElementsAnnotatedWith(BindBool.class)) {
            try {
                parseResourceBool(element2, targetClassMap, erasedTargetNames);
            } catch (Exception e2) {
                logParsingError(element2, BindBool.class, e2);
            }
        }
        for (Element element3 : env.getElementsAnnotatedWith(BindColor.class)) {
            try {
                parseResourceColor(element3, targetClassMap, erasedTargetNames);
            } catch (Exception e3) {
                logParsingError(element3, BindColor.class, e3);
            }
        }
        for (Element element4 : env.getElementsAnnotatedWith(BindDimen.class)) {
            try {
                parseResourceDimen(element4, targetClassMap, erasedTargetNames);
            } catch (Exception e4) {
                logParsingError(element4, BindDimen.class, e4);
            }
        }
        for (Element element5 : env.getElementsAnnotatedWith(BindDrawable.class)) {
            try {
                parseResourceDrawable(element5, targetClassMap, erasedTargetNames);
            } catch (Exception e5) {
                logParsingError(element5, BindDrawable.class, e5);
            }
        }
        for (Element element6 : env.getElementsAnnotatedWith(BindInt.class)) {
            try {
                parseResourceInt(element6, targetClassMap, erasedTargetNames);
            } catch (Exception e6) {
                logParsingError(element6, BindInt.class, e6);
            }
        }
        for (Element element7 : env.getElementsAnnotatedWith(BindString.class)) {
            try {
                parseResourceString(element7, targetClassMap, erasedTargetNames);
            } catch (Exception e7) {
                logParsingError(element7, BindString.class, e7);
            }
        }
        for (Map.Entry<TypeElement, BindingClass> entry : targetClassMap.entrySet()) {
            String parentClassFqcn = findParentFqcn(entry.getKey(), erasedTargetNames);
            if (parentClassFqcn != null) {
                entry.getValue().setParentViewBinder(parentClassFqcn + SUFFIX);
            }
        }
        return targetClassMap;
    }

    private void logParsingError(Element element, Class<? extends Annotation> annotation, Exception e) {
        StringWriter stackTrace = new StringWriter();
        e.printStackTrace(new PrintWriter(stackTrace));
        error(element, "Unable to parse @%s binding.\n\n%s", annotation.getSimpleName(), stackTrace);
    }

    private boolean isInaccessibleViaGeneratedCode(Class<? extends Annotation> annotationClass, String targetThing, Element element) {
        boolean hasError = false;
        TypeElement enclosingElement = element.getEnclosingElement();
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE) || modifiers.contains(Modifier.STATIC)) {
            error(element, "@%s %s must not be private or static. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (enclosingElement.getKind() != ElementKind.CLASS) {
            error(enclosingElement, "@%s %s may only be contained in classes. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (enclosingElement.getModifiers().contains(Modifier.PRIVATE)) {
            error(enclosingElement, "@%s %s may not be contained in private classes. (%s.%s)", annotationClass.getSimpleName(), targetThing, enclosingElement.getQualifiedName(), element.getSimpleName());
            return true;
        }
        return hasError;
    }

    private boolean isBindingInWrongPackage(Class<? extends Annotation> annotationClass, Element element) {
        TypeElement enclosingElement = element.getEnclosingElement();
        String qualifiedName = enclosingElement.getQualifiedName().toString();
        if (qualifiedName.startsWith(ANDROID_PREFIX)) {
            error(element, "@%s-annotated class incorrectly in Android framework package. (%s)", annotationClass.getSimpleName(), qualifiedName);
            return true;
        }
        if (!qualifiedName.startsWith(JAVA_PREFIX)) {
            return false;
        }
        error(element, "@%s-annotated class incorrectly in Java framework package. (%s)", annotationClass.getSimpleName(), qualifiedName);
        return true;
    }

    private void parseBind(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        if (!isInaccessibleViaGeneratedCode(Bind.class, "fields", element) && !isBindingInWrongPackage(Bind.class, element)) {
            TypeMirror elementType = element.asType();
            if (elementType.getKind() == TypeKind.ARRAY) {
                parseBindMany(element, targetClassMap, erasedTargetNames);
                return;
            }
            if (LIST_TYPE.equals(doubleErasure(elementType))) {
                parseBindMany(element, targetClassMap, erasedTargetNames);
            } else if (isSubtypeOfType(elementType, ITERABLE_TYPE)) {
                error(element, "@%s must be a List or array. (%s.%s)", Bind.class.getSimpleName(), element.getEnclosingElement().getQualifiedName(), element.getSimpleName());
            } else {
                parseBindOne(element, targetClassMap, erasedTargetNames);
            }
        }
    }

    private void parseBindOne(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        TypeMirror elementType = element.asType();
        if (elementType.getKind() == TypeKind.TYPEVAR) {
            TypeVariable typeVariable = (TypeVariable) elementType;
            elementType = typeVariable.getUpperBound();
        }
        if (!isSubtypeOfType(elementType, VIEW_TYPE) && !isInterface(elementType)) {
            error(element, "@%s fields must extend from View or be an interface. (%s.%s)", Bind.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        int[] ids = ((Bind) element.getAnnotation(Bind.class)).value();
        if (ids.length != 1) {
            error(element, "@%s for a view must only specify one ID. Found: %s. (%s.%s)", Bind.class.getSimpleName(), Arrays.toString(ids), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!hasError) {
            int id = ids[0];
            BindingClass bindingClass = targetClassMap.get(enclosingElement);
            if (bindingClass != null) {
                ViewBindings viewBindings = bindingClass.getViewBinding(id);
                if (viewBindings != null) {
                    Iterator<FieldViewBinding> iterator = viewBindings.getFieldBindings().iterator();
                    if (iterator.hasNext()) {
                        FieldViewBinding existingBinding = iterator.next();
                        error(element, "Attempt to use @%s for an already bound ID %d on '%s'. (%s.%s)", Bind.class.getSimpleName(), Integer.valueOf(id), existingBinding.getName(), enclosingElement.getQualifiedName(), element.getSimpleName());
                        return;
                    }
                }
            } else {
                bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            }
            String name = element.getSimpleName().toString();
            String type = elementType.toString();
            boolean required = isRequiredBinding(element);
            FieldViewBinding binding = new FieldViewBinding(name, type, required);
            bindingClass.addField(id, binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseBindMany(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        FieldCollectionViewBinding.Kind kind;
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        ArrayType arrayTypeAsType = element.asType();
        String erasedType = doubleErasure(arrayTypeAsType);
        TypeMirror viewType = null;
        if (arrayTypeAsType.getKind() == TypeKind.ARRAY) {
            ArrayType arrayType = arrayTypeAsType;
            viewType = arrayType.getComponentType();
            kind = FieldCollectionViewBinding.Kind.ARRAY;
        } else if (LIST_TYPE.equals(erasedType)) {
            DeclaredType declaredType = (DeclaredType) arrayTypeAsType;
            List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
            if (typeArguments.size() != 1) {
                error(element, "@%s List must have a generic component. (%s.%s)", Bind.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
                hasError = true;
            } else {
                TypeMirror viewType2 = typeArguments.get(0);
                viewType = viewType2;
            }
            kind = FieldCollectionViewBinding.Kind.LIST;
        } else {
            throw new AssertionError();
        }
        if (viewType != null && viewType.getKind() == TypeKind.TYPEVAR) {
            TypeVariable typeVariable = (TypeVariable) viewType;
            viewType = typeVariable.getUpperBound();
        }
        if (viewType != null && !isSubtypeOfType(viewType, VIEW_TYPE) && !isInterface(viewType)) {
            error(element, "@%s List or array type must extend from View or be an interface. (%s.%s)", Bind.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!hasError) {
            String name = element.getSimpleName().toString();
            int[] ids = ((Bind) element.getAnnotation(Bind.class)).value();
            if (ids.length == 0) {
                error(element, "@%s must specify at least one ID. (%s.%s)", Bind.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
                return;
            }
            Integer duplicateId = findDuplicate(ids);
            if (duplicateId != null) {
                error(element, "@%s annotation contains duplicate ID %d. (%s.%s)", Bind.class.getSimpleName(), duplicateId, enclosingElement.getQualifiedName(), element.getSimpleName());
            }
            if (!$assertionsDisabled && viewType == null) {
                throw new AssertionError();
            }
            String type = viewType.toString();
            boolean required = isRequiredBinding(element);
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            FieldCollectionViewBinding binding = new FieldCollectionViewBinding(name, type, kind, required);
            bindingClass.addFieldCollection(ids, binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseResourceBool(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        if (element.asType().getKind() != TypeKind.BOOLEAN) {
            error(element, "@%s field type must be 'boolean'. (%s.%s)", BindBool.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!(hasError | isInaccessibleViaGeneratedCode(BindBool.class, "fields", element) | isBindingInWrongPackage(BindBool.class, element))) {
            String name = element.getSimpleName().toString();
            int id = ((BindBool) element.getAnnotation(BindBool.class)).value();
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            FieldResourceBinding binding = new FieldResourceBinding(id, name, "getBoolean");
            bindingClass.addResource(binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseResourceColor(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        boolean isColorStateList = false;
        TypeMirror elementType = element.asType();
        if (COLOR_STATE_LIST_TYPE.equals(elementType.toString())) {
            isColorStateList = true;
        } else if (elementType.getKind() != TypeKind.INT) {
            error(element, "@%s field type must be 'int' or 'ColorStateList'. (%s.%s)", BindColor.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!(hasError | isInaccessibleViaGeneratedCode(BindColor.class, "fields", element) | isBindingInWrongPackage(BindColor.class, element))) {
            String name = element.getSimpleName().toString();
            int id = ((BindColor) element.getAnnotation(BindColor.class)).value();
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            FieldResourceBinding binding = new FieldResourceBinding(id, name, isColorStateList ? "getColorStateList" : "getColor");
            bindingClass.addResource(binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseResourceDimen(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        boolean isInt = false;
        TypeMirror elementType = element.asType();
        if (elementType.getKind() == TypeKind.INT) {
            isInt = true;
        } else if (elementType.getKind() != TypeKind.FLOAT) {
            error(element, "@%s field type must be 'int' or 'float'. (%s.%s)", BindDimen.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!(hasError | isInaccessibleViaGeneratedCode(BindDimen.class, "fields", element) | isBindingInWrongPackage(BindDimen.class, element))) {
            String name = element.getSimpleName().toString();
            int id = ((BindDimen) element.getAnnotation(BindDimen.class)).value();
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            FieldResourceBinding binding = new FieldResourceBinding(id, name, isInt ? "getDimensionPixelSize" : "getDimension");
            bindingClass.addResource(binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseResourceDrawable(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        if (!DRAWABLE_TYPE.equals(element.asType().toString())) {
            error(element, "@%s field type must be 'Drawable'. (%s.%s)", BindDrawable.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!(hasError | isInaccessibleViaGeneratedCode(BindDrawable.class, "fields", element) | isBindingInWrongPackage(BindDrawable.class, element))) {
            String name = element.getSimpleName().toString();
            int id = ((BindDrawable) element.getAnnotation(BindDrawable.class)).value();
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            FieldResourceBinding binding = new FieldResourceBinding(id, name, "getDrawable");
            bindingClass.addResource(binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseResourceInt(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        if (element.asType().getKind() != TypeKind.INT) {
            error(element, "@%s field type must be 'int'. (%s.%s)", BindInt.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!(hasError | isInaccessibleViaGeneratedCode(BindInt.class, "fields", element) | isBindingInWrongPackage(BindInt.class, element))) {
            String name = element.getSimpleName().toString();
            int id = ((BindInt) element.getAnnotation(BindInt.class)).value();
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            FieldResourceBinding binding = new FieldResourceBinding(id, name, "getInteger");
            bindingClass.addResource(binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private void parseResourceString(Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        boolean hasError = false;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        if (!"java.lang.String".equals(element.asType().toString())) {
            error(element, "@%s field type must be 'String'. (%s.%s)", BindString.class.getSimpleName(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError = true;
        }
        if (!(hasError | isInaccessibleViaGeneratedCode(BindString.class, "fields", element) | isBindingInWrongPackage(BindString.class, element))) {
            String name = element.getSimpleName().toString();
            int id = ((BindString) element.getAnnotation(BindString.class)).value();
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            FieldResourceBinding binding = new FieldResourceBinding(id, name, "getString");
            bindingClass.addResource(binding);
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private static Integer findDuplicate(int[] array) {
        Set<Integer> seenElements = new LinkedHashSet<>();
        for (int element : array) {
            if (!seenElements.add(Integer.valueOf(element))) {
                return Integer.valueOf(element);
            }
        }
        return null;
    }

    private String doubleErasure(TypeMirror elementType) {
        String name = this.typeUtils.erasure(elementType).toString();
        int typeParamStart = name.indexOf(60);
        if (typeParamStart != -1) {
            return name.substring(0, typeParamStart);
        }
        return name;
    }

    private void findAndParseListener(RoundEnvironment env, Class<? extends Annotation> annotationClass, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) {
        for (Element element : env.getElementsAnnotatedWith(annotationClass)) {
            try {
                parseListenerAnnotation(annotationClass, element, targetClassMap, erasedTargetNames);
            } catch (Exception e) {
                StringWriter stackTrace = new StringWriter();
                e.printStackTrace(new PrintWriter(stackTrace));
                error(element, "Unable to generate view binder for @%s.\n\n%s", annotationClass.getSimpleName(), stackTrace.toString());
            }
        }
    }

    private void parseListenerAnnotation(Class<? extends Annotation> annotationClass, Element element, Map<TypeElement, BindingClass> targetClassMap, Set<String> erasedTargetNames) throws Exception {
        ListenerMethod method;
        if (!(element instanceof ExecutableElement) || element.getKind() != ElementKind.METHOD) {
            throw new IllegalStateException(String.format("@%s annotation must be on a method.", annotationClass.getSimpleName()));
        }
        ExecutableElement executableElement = (ExecutableElement) element;
        TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
        Annotation annotation = element.getAnnotation(annotationClass);
        Method annotationValue = annotationClass.getDeclaredMethod("value", new Class[0]);
        if (annotationValue.getReturnType() != int[].class) {
            throw new IllegalStateException(String.format("@%s annotation value() type not int[].", annotationClass));
        }
        int[] ids = (int[]) annotationValue.invoke(annotation, new Object[0]);
        String name = executableElement.getSimpleName().toString();
        boolean required = isRequiredBinding(element);
        boolean hasError = isInaccessibleViaGeneratedCode(annotationClass, "methods", element);
        boolean hasError2 = hasError | isBindingInWrongPackage(annotationClass, element);
        Integer duplicateId = findDuplicate(ids);
        if (duplicateId != null) {
            error(element, "@%s annotation for method contains duplicate ID %d. (%s.%s)", annotationClass.getSimpleName(), duplicateId, enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError2 = true;
        }
        ListenerClass listener = (ListenerClass) annotationClass.getAnnotation(ListenerClass.class);
        if (listener == null) {
            throw new IllegalStateException(String.format("No @%s defined on @%s.", ListenerClass.class.getSimpleName(), annotationClass.getSimpleName()));
        }
        for (int id : ids) {
            if (id == -1) {
                if (ids.length == 1) {
                    if (!required) {
                        error(element, "ID-free binding must not be annotated with @Nullable. (%s.%s)", enclosingElement.getQualifiedName(), element.getSimpleName());
                        hasError2 = true;
                    }
                    String targetType = listener.targetType();
                    if (!isSubtypeOfType(enclosingElement.asType(), targetType) && !isInterface(enclosingElement.asType())) {
                        error(element, "@%s annotation without an ID may only be used with an object of type \"%s\" or an interface. (%s.%s)", annotationClass.getSimpleName(), targetType, enclosingElement.getQualifiedName(), element.getSimpleName());
                        hasError2 = true;
                    }
                } else {
                    error(element, "@%s annotation contains invalid ID %d. (%s.%s)", annotationClass.getSimpleName(), Integer.valueOf(id), enclosingElement.getQualifiedName(), element.getSimpleName());
                    hasError2 = true;
                }
            }
        }
        ListenerMethod[] methods = listener.method();
        if (methods.length > 1) {
            throw new IllegalStateException(String.format("Multiple listener methods specified on @%s.", annotationClass.getSimpleName()));
        }
        if (methods.length == 1) {
            if (listener.callbacks() != ListenerClass.NONE.class) {
                throw new IllegalStateException(String.format("Both method() and callback() defined on @%s.", annotationClass.getSimpleName()));
            }
            method = methods[0];
        } else {
            Method annotationCallback = annotationClass.getDeclaredMethod("callback", new Class[0]);
            Enum<?> callback = (Enum) annotationCallback.invoke(annotation, new Object[0]);
            Field callbackField = callback.getDeclaringClass().getField(callback.name());
            method = (ListenerMethod) callbackField.getAnnotation(ListenerMethod.class);
            if (method == null) {
                throw new IllegalStateException(String.format("No @%s defined on @%s's %s.%s.", ListenerMethod.class.getSimpleName(), annotationClass.getSimpleName(), callback.getDeclaringClass().getSimpleName(), callback.name()));
            }
        }
        List<? extends VariableElement> methodParameters = executableElement.getParameters();
        if (methodParameters.size() > method.parameters().length) {
            error(element, "@%s methods can have at most %s parameter(s). (%s.%s)", annotationClass.getSimpleName(), Integer.valueOf(method.parameters().length), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError2 = true;
        }
        TypeMirror returnType = executableElement.getReturnType();
        if (returnType instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) returnType;
            returnType = typeVariable.getUpperBound();
        }
        if (!returnType.toString().equals(method.returnType())) {
            error(element, "@%s methods must have a '%s' return type. (%s.%s)", annotationClass.getSimpleName(), method.returnType(), enclosingElement.getQualifiedName(), element.getSimpleName());
            hasError2 = true;
        }
        if (!hasError2) {
            Parameter[] parameters = Parameter.NONE;
            if (!methodParameters.isEmpty()) {
                parameters = new Parameter[methodParameters.size()];
                BitSet methodParameterUsed = new BitSet(methodParameters.size());
                String[] parameterTypes = method.parameters();
                for (int i = 0; i < methodParameters.size(); i++) {
                    VariableElement methodParameter = (VariableElement) methodParameters.get(i);
                    TypeMirror methodParameterType = methodParameter.asType();
                    if (methodParameterType instanceof TypeVariable) {
                        TypeVariable typeVariable2 = (TypeVariable) methodParameterType;
                        methodParameterType = typeVariable2.getUpperBound();
                    }
                    for (int j = 0; j < parameterTypes.length; j++) {
                        if (!methodParameterUsed.get(j) && (isSubtypeOfType(methodParameterType, parameterTypes[j]) || isInterface(methodParameterType))) {
                            parameters[i] = new Parameter(j, methodParameterType.toString());
                            methodParameterUsed.set(j);
                            break;
                        }
                    }
                    if (parameters[i] == null) {
                        StringBuilder builder = new StringBuilder();
                        builder.append("Unable to match @").append(annotationClass.getSimpleName()).append(" method arguments. (").append((CharSequence) enclosingElement.getQualifiedName()).append(FilenameUtils.EXTENSION_SEPARATOR).append((CharSequence) element.getSimpleName()).append(')');
                        for (int j2 = 0; j2 < parameters.length; j2++) {
                            Parameter parameter = parameters[j2];
                            builder.append("\n\n  Parameter #").append(j2 + 1).append(": ").append(((VariableElement) methodParameters.get(j2)).asType().toString()).append("\n    ");
                            if (parameter == null) {
                                builder.append("did not match any listener parameters");
                            } else {
                                builder.append("matched listener parameter #").append(parameter.getListenerPosition() + 1).append(": ").append(parameter.getType());
                            }
                        }
                        builder.append("\n\nMethods may have up to ").append(method.parameters().length).append(" parameter(s):\n");
                        for (String parameterType : method.parameters()) {
                            builder.append("\n  ").append(parameterType);
                        }
                        builder.append("\n\nThese may be listed in any order but will be searched for from top to bottom.");
                        error(executableElement, builder.toString(), new Object[0]);
                        return;
                    }
                }
            }
            MethodViewBinding binding = new MethodViewBinding(name, Arrays.asList(parameters), required);
            BindingClass bindingClass = getOrCreateTargetClass(targetClassMap, enclosingElement);
            for (int id2 : ids) {
                if (!bindingClass.addMethod(id2, listener, method, binding)) {
                    error(element, "Multiple listener methods with return value specified for ID %d. (%s.%s)", Integer.valueOf(id2), enclosingElement.getQualifiedName(), element.getSimpleName());
                    return;
                }
            }
            erasedTargetNames.add(enclosingElement.toString());
        }
    }

    private boolean isInterface(TypeMirror typeMirror) {
        return (typeMirror instanceof DeclaredType) && ((DeclaredType) typeMirror).asElement().getKind() == ElementKind.INTERFACE;
    }

    private boolean isSubtypeOfType(TypeMirror typeMirror, String otherType) {
        if (otherType.equals(typeMirror.toString())) {
            return true;
        }
        if (typeMirror.getKind() != TypeKind.DECLARED) {
            return false;
        }
        DeclaredType declaredType = (DeclaredType) typeMirror;
        List<? extends TypeMirror> typeArguments = declaredType.getTypeArguments();
        if (typeArguments.size() > 0) {
            StringBuilder typeString = new StringBuilder(declaredType.asElement().toString());
            typeString.append('<');
            for (int i = 0; i < typeArguments.size(); i++) {
                if (i > 0) {
                    typeString.append(',');
                }
                typeString.append('?');
            }
            typeString.append('>');
            if (typeString.toString().equals(otherType)) {
                return true;
            }
        }
        TypeElement typeElementAsElement = declaredType.asElement();
        if (!(typeElementAsElement instanceof TypeElement)) {
            return false;
        }
        TypeElement typeElement = typeElementAsElement;
        TypeMirror superType = typeElement.getSuperclass();
        if (isSubtypeOfType(superType, otherType)) {
            return true;
        }
        for (TypeMirror interfaceType : typeElement.getInterfaces()) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true;
            }
        }
        return false;
    }

    private BindingClass getOrCreateTargetClass(Map<TypeElement, BindingClass> targetClassMap, TypeElement enclosingElement) {
        BindingClass bindingClass = targetClassMap.get(enclosingElement);
        if (bindingClass == null) {
            String targetType = enclosingElement.getQualifiedName().toString();
            String classPackage = getPackageName(enclosingElement);
            String className = getClassName(enclosingElement, classPackage) + SUFFIX;
            BindingClass bindingClass2 = new BindingClass(classPackage, className, targetType);
            targetClassMap.put(enclosingElement, bindingClass2);
            return bindingClass2;
        }
        return bindingClass;
    }

    private static String getClassName(TypeElement type, String packageName) {
        int packageLen = packageName.length() + 1;
        return type.getQualifiedName().toString().substring(packageLen).replace(FilenameUtils.EXTENSION_SEPARATOR, '$');
    }

    private String findParentFqcn(TypeElement typeElement, Set<String> parents) {
        do {
            DeclaredType superclass = typeElement.getSuperclass();
            if (superclass.getKind() == TypeKind.NONE) {
                return null;
            }
            typeElement = (TypeElement) superclass.asElement();
        } while (!parents.contains(typeElement.toString()));
        String packageName = getPackageName(typeElement);
        return packageName + "." + getClassName(typeElement, packageName);
    }

    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void error(Element element, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, message, element);
    }

    private String getPackageName(TypeElement type) {
        return this.elementUtils.getPackageOf(type).getQualifiedName().toString();
    }

    private static boolean hasAnnotationWithName(Element element, String simpleName) {
        for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
            String annotationName = mirror.getAnnotationType().asElement().getSimpleName().toString();
            if (simpleName.equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isRequiredBinding(Element element) {
        return !hasAnnotationWithName(element, NULLABLE_ANNOTATION_NAME);
    }
}
