package nl.thecheerfuldev.injectinator.framework;

import nl.thecheerfuldev.injectinator.framework.annotation.InjectMe;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectionType;
import nl.thecheerfuldev.injectinator.framework.module.ConfigModule;
import nl.thecheerfuldev.injectinator.framework.module.FileConfigModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Injectinator {

    private final ConfigModule configModule;
    private final Map<Class<?>, Object> singletons = new HashMap<>();

    private Injectinator(final ConfigModule configModule) {
        this.configModule = configModule;
    }

    /**
     * @param configModule Valid {@link ConfigModule} that enables the {@link Injectinator} instance
     *                     to inject annotated dependencies.
     * @return Instance of {@link Injectinator} that can be used to inject annotated dependencies
     * that were configured in {@link ConfigModule}.
     */
    public static Injectinator getInjectinator(final ConfigModule configModule) {
        configModule.configure();
        return new Injectinator(configModule);
    }

    /**
     * @return Instance of {@link Injectinator} that can be used to inject annotated dependencies
     * that were configured via {@link FileConfigModule}.
     */
    public static Injectinator getInjectinator() {
        return getInjectinator(new FileConfigModule());
    }

    /**
     * @param classToInjectInto Class of which you wish to get an instance, including all injected dependencies.
     * @return Instance of {@code Class<T>}, which was given as a parameter.
     * @throws Exception when more than one type is annotated with {@link InjectMe}
     */
    public <T> T inject(final Class<T> classToInjectInto) throws Exception {
        if (classToInjectInto == null) {
            return null;
        }

        if (isMoreThanOneAnnotatedTypePresent(classToInjectInto, InjectMe.class)) {
            throw new IllegalArgumentException("Only constructor, field OR setter injection allowed within a single class.");
        }

        return injectIntoClass(classToInjectInto);
    }

    private <T> T injectIntoClass(final Class<T> classToInjectInto) throws Exception {
        for (final Constructor<?> constructor : classToInjectInto.getConstructors()) {
            if (constructor.isAnnotationPresent(InjectMe.class)) {
                return injectFieldsViaConstructor(classToInjectInto, constructor);
            }
        }

        for (final Method method : classToInjectInto.getMethods()) {
            if (method.isAnnotationPresent(InjectMe.class)) {
                return injectFieldsViaSetter(classToInjectInto);
            }
        }

        return injectFields(classToInjectInto);
    }

    private <T> T injectFieldsViaSetter(final Class<T> classToInjectInto) throws Exception {
        final T newInstance = getInstance(classToInjectInto);
        for (final Method method : classToInjectInto.getMethods()) {
            if (method.isAnnotationPresent(InjectMe.class)) {
                checkSetter(method);
                if (isMethodAnnotatedAsSingleton(method)) {
                    method.invoke(newInstance, getSingleton(method.getParameterTypes()[0]));
                } else {
                    method.invoke(newInstance, (Object) inject(this.configModule.getInjectable(method.getParameterTypes()[0])));
                }
            }
        }
        return newInstance;
    }

    private <T> T injectFieldsViaConstructor(final Class<T> classToInjectInto, final Constructor<?> constructor)
            throws Exception {
        if (isInnerClass(classToInjectInto)) {
            return getInnerClassInstance(classToInjectInto);
        }

        final Class<?>[] dependencyTypes = constructor.getParameterTypes();
        final Object[] dependencies = new Object[dependencyTypes.length];
        int i = 0;
        for (final Class<?> dependency : dependencyTypes) {
            dependencies[i++] = (constructor.getAnnotation(InjectMe.class).injectionType() == InjectionType.SINGLETON) ?
                    getSingleton(dependency) : inject(this.configModule.getInjectable(dependency));
        }
        return classToInjectInto.getConstructor(dependencyTypes).newInstance(dependencies);
    }

    private <T> T injectFields(final Class<T> classToInjectInto) throws Exception {
        final T newInstance = getInstance(classToInjectInto);
        for (final Field field : classToInjectInto.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectMe.class)) {
                field.setAccessible(true);
                if (field.getAnnotation(InjectMe.class).injectionType() == InjectionType.SINGLETON) {
                    field.set(newInstance, getSingleton(field.getType()));
                } else {
                    final Class<?> injectable = this.configModule.getInjectable(field.getType());
                    field.set(newInstance, inject(injectable));
                }
            }
        }
        return newInstance;
    }

    private <T> T getInstance(final Class<T> type) throws Exception {
        return isInnerClass(type) ? getInnerClassInstance(type) : type.getConstructor().newInstance();
    }

    private <T> T getInnerClassInstance(final Class<T> classToInjectInto) throws Exception {
        final Class<?> enclosingClass = classToInjectInto.getEnclosingClass();
        for (final Constructor<?> constructor : classToInjectInto.getConstructors()) {
            if (constructor.isAnnotationPresent(InjectMe.class)) {
                final Constructor<T> declaredConstructor = classToInjectInto.getDeclaredConstructor(constructor.getParameterTypes());
                return declaredConstructor.newInstance(getConstructorParameterInstances(enclosingClass, constructor));
            }
        }

        final Constructor<T> declaredConstructor = classToInjectInto.getDeclaredConstructor(enclosingClass);
        return declaredConstructor.newInstance(enclosingClass.getConstructor().newInstance());
    }

    private Object[] getConstructorParameterInstances(final Class<?> enclosingClass, final Constructor<?> constructor) throws Exception {
        final Object[] constructorParameterInstances = new Object[constructor.getParameterCount()];
        constructorParameterInstances[0] = inject(enclosingClass);
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        for (int i = 1; i < parameterTypes.length; i++) {
            constructorParameterInstances[i] = inject(this.configModule.getInjectable(parameterTypes[i]));
        }
        return constructorParameterInstances;
    }

    private <T> boolean isInnerClass(final Class<T> classToInjectInto) {
        return classToInjectInto.getEnclosingClass() != null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getSingleton(final Class<T> type) throws Exception {
        if (!this.singletons.containsKey(type)) {
            this.singletons.put(type, inject(this.configModule.getInjectable(type)));
        }
        return (T) this.singletons.get(type);
    }

    private boolean isAnnotationPresent(final Class<? extends Annotation> annotation, final AnnotatedElement... elements) {
        return Arrays.stream(elements).anyMatch(element -> element.isAnnotationPresent(annotation));
    }

    private <T> boolean isMoreThanOneAnnotatedTypePresent(final Class<T> clazz, final Class<? extends Annotation> annotation) {
        int annotationCounter = 0;
        if (isAnnotationPresent(annotation, clazz.getConstructors())) {
            annotationCounter++;
        }
        if (isAnnotationPresent(annotation, clazz.getDeclaredFields())) {
            annotationCounter++;
        }
        if (isAnnotationPresent(annotation, clazz.getMethods())) {
            annotationCounter++;
        }

        return annotationCounter > 1;
    }

    private boolean isMethodAnnotatedAsSingleton(final Method method) {
        return method.getAnnotation(InjectMe.class).injectionType() == InjectionType.SINGLETON;
    }

    private void checkSetter(final Method method) {
        if (method.getParameterCount() != 1) {
            throw new IllegalArgumentException("A setter can only have 1 parameter.");
        }
    }
}
