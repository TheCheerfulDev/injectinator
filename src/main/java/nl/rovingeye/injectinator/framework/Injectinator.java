package nl.rovingeye.injectinator.framework;

import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;
import nl.rovingeye.injectinator.framework.module.ConfigModule;

import java.lang.annotation.Annotation;
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

    public static Injectinator getInjectinator(final ConfigModule configModule) {
        configModule.configure();
        return new Injectinator(configModule);
    }

    public <T> T inject(final Class<T> classToInjectInto) throws Exception {
        if (classToInjectInto == null) {
            return null;
        }

        if (isMoreThanOneAnnotatedTypePresent(classToInjectInto, InjectMe.class)) {
            throw new IllegalArgumentException("Only constructor, field OR setter injection allowed in a single class.");
        }

        return injectFieldsIntoClass(classToInjectInto);
    }

    private <T> T injectFieldsIntoClass(final Class<T> classToInjectInto)
            throws Exception {
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
                if (method.getAnnotation(InjectMe.class).injectionType() == InjectType.SINGLETON) {
                    method.invoke(newInstance, getSingleton(method.getParameterTypes()[0]));
                } else {
                    method.invoke(newInstance, (Object) inject(this.configModule.getInjectable(method.getParameterTypes()[0])));
                }
            }
        }
        return newInstance;
    }

    private <T> T injectFieldsViaConstructor(final Class<T> classToInjectInto, final Constructor<?> constructor) throws Exception {
        if (isInnerClass(classToInjectInto)) {
            return getInnerClassInstance(classToInjectInto);
        }

        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Object[] objArr = new Object[parameterTypes.length];
        int i = 0;
        for (final Class<?> dependency : parameterTypes) {
            final Class<?> injectable = this.configModule.getInjectable(dependency);
            if (dependency.isAssignableFrom(injectable)) {
                objArr[i++] = (constructor.getAnnotation(InjectMe.class).injectionType() == InjectType.SINGLETON) ? getSingleton(dependency) : inject(injectable);
            }
        }
        return classToInjectInto.getConstructor(parameterTypes).newInstance(objArr);
    }

    private <T> T injectFields(final Class<T> classToInjectInto) throws Exception {
        final T newInstance = getInstance(classToInjectInto);
        for (final Field field : classToInjectInto.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectMe.class)) {
                field.setAccessible(true);
                if (field.getAnnotation(InjectMe.class).injectionType() == InjectType.SINGLETON) {
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
        return isInnerClass(type) ? getInnerClassInstance(type) : type.newInstance();
    }

    private <T> T getInnerClassInstance(final Class<T> classToInjectInto) throws Exception {
        final Class<?> enclosingClass = classToInjectInto.getEnclosingClass();
        for (final Constructor<?> constructor : classToInjectInto.getConstructors()) {
            final Object[] constructorParameterInstances = new Object[constructor.getParameterCount()];
            if (constructor.isAnnotationPresent(InjectMe.class)) {
                constructorParameterInstances[0] = inject(enclosingClass);
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                for (int i = 1; i < parameterTypes.length; i++) {
                    constructorParameterInstances[i] = inject(this.configModule.getInjectable(parameterTypes[i]));
                }
                final Constructor<T> declaredConstructor = classToInjectInto.getDeclaredConstructor(constructor.getParameterTypes());
                return declaredConstructor.newInstance(constructorParameterInstances);
            }
        }

        final Constructor<T> declaredConstructor = classToInjectInto.getDeclaredConstructor(enclosingClass);
        return declaredConstructor.newInstance(enclosingClass.newInstance());
    }

    private <T> boolean isInnerClass(final Class<T> classToInjectInto) {
        return classToInjectInto.getEnclosingClass() != null;
    }

    @SuppressWarnings("unchecked")
    private <T> T getSingleton(final Class<T> type) throws Exception {
        if (this.singletons.containsKey(type)) {
            return (T) this.singletons.get(type);
        }
        this.singletons.put(type, inject(this.configModule.getInjectable(type)));
        return (T) this.singletons.get(type);
    }

    private boolean isConstructorAnnotationPresent(final Class<? extends Annotation> annotation, final Constructor<?>... constructors) {
        return Arrays.stream(constructors).anyMatch(constructor -> constructor.isAnnotationPresent(annotation));
    }

    private boolean isFieldAnnotationPresent(final Class<? extends Annotation> annotation, final Field... fields) {
        return Arrays.stream(fields).anyMatch(field -> field.isAnnotationPresent(annotation));
    }

    private boolean isSetterAnnotationPresent(final Class<? extends Annotation> annotation, final Method... methods) {
        return Arrays.stream(methods).anyMatch(method -> method.isAnnotationPresent(annotation));
    }

    private <T> boolean isMoreThanOneAnnotatedTypePresent(final Class<T> clazz, final Class<? extends Annotation> annotation) {
        int annotationCounter = 0;
        if (isConstructorAnnotationPresent(annotation, clazz.getConstructors())) {
            annotationCounter++;
        }
        if (isFieldAnnotationPresent(annotation, clazz.getDeclaredFields())) {
            annotationCounter++;
        }
        if (isSetterAnnotationPresent(annotation, clazz.getDeclaredMethods())) {
            annotationCounter++;
        }

        return annotationCounter > 1;
    }

    private void checkSetter(final Method method) {
        if (method.getParameterCount() != 1) {
            throw new IllegalArgumentException("A setter can only have 1 parameter.");
        }
    }
}
