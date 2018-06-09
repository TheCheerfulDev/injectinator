package nl.rovingeye.injectinator.framework;

import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.module.ConfigModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Injectinator {

    private ConfigModule configModule;

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

        if (constructorAndFieldAnnotatedWith(classToInjectInto, InjectMe.class)) {
            throw new IllegalArgumentException("Only constructor OR field injection allowed in a single class.");
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
        return injectFields(classToInjectInto);
    }

    private <T> T injectFieldsViaConstructor(final Class<T> classToInjectInto, final Constructor<?> constructor) throws Exception {
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Object[] objArr = new Object[parameterTypes.length];
        int i = 0;
        for (final Class<?> dependency : parameterTypes) {
            final Class<?> injectable = this.configModule.getInjectable(dependency);
            if (dependency.isAssignableFrom(injectable)) {
                objArr[i++] = inject(injectable);
            }
        }
        return classToInjectInto.getConstructor(parameterTypes).newInstance(objArr);
    }

    private <T> T injectFields(final Class<T> classToInjectInto) throws Exception {
        final T newInstance = classToInjectInto.newInstance();
        for (final Field field : classToInjectInto.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectMe.class)) {
                final Class<?> injectable = this.configModule.getInjectable(field.getType());
                field.setAccessible(true);
                field.set(newInstance, inject(injectable));
            }
        }
        return newInstance;
    }

    private <T> boolean constructorAndFieldAnnotatedWith(final Class<T> clazz, final Class<? extends Annotation> annotation) {
        boolean constructorAnnotated = false;
        boolean anyFieldAnnotated = false;

        for (final Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.isAnnotationPresent(annotation)) {
                constructorAnnotated = true;
                break;
            }
        }

        for (final Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(annotation)) {
                anyFieldAnnotated = true;
                break;
            }
        }

        return constructorAnnotated && anyFieldAnnotated;
    }
}
