package nl.rovingeye.injectinator.framework;

import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.module.ConfigModule;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Injectinator {

    private ConfigModule configModule;

    private Injectinator() {
    }

    public static Injectinator getInjectinator(final ConfigModule configModule) {
        final Injectinator injectinator = new Injectinator();
        injectinator.configModule = configModule;
        configModule.configure();
        return injectinator;
    }

    public <T> T inject(final Class<T> classToInjectInto) throws Exception {
        if (classToInjectInto == null) {
            return null;
        }
        return injectFieldsIntoClass(classToInjectInto);
    }

    private <T> T injectFieldsIntoClass(final Class<T> classToInjectInto)
            throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        for (final Constructor<?> constructor : classToInjectInto.getConstructors()) {
            if (constructor.isAnnotationPresent(InjectMe.class)) {
                return injectFieldsViaConstructor(classToInjectInto, constructor);
            }
        }
        return injectFields(classToInjectInto);
    }

    private <T> T injectFields(final Class<T> classToInjectInto) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final T newInstance = classToInjectInto.newInstance();
        for (final Field field : classToInjectInto.getDeclaredFields()) {
            if (field.isAnnotationPresent(InjectMe.class)) {
                final Class<?> dependency = this.configModule.getInjectable(field.getType());
                field.setAccessible(true);
                field.set(newInstance, dependency.getConstructor().newInstance());
            }
        }
        return newInstance;
    }

    private <T> T injectFieldsViaConstructor(final Class<T> classToInjectInto, final Constructor<?> constructor) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        final Class<?>[] parameterTypes = constructor.getParameterTypes();
        final Object[] objArr = new Object[parameterTypes.length];
        int i = 0;
        for (final Class<?> clazz : parameterTypes) {
            final Class<?> dependency = this.configModule.getInjectable(clazz);
            if (clazz.isAssignableFrom(dependency)) {
                objArr[i++] = dependency.getConstructor().newInstance();
            }
        }
        return classToInjectInto.getConstructor(parameterTypes).newInstance(objArr);
    }
}
