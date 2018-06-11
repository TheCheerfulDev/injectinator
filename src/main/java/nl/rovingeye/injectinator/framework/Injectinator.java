package nl.rovingeye.injectinator.framework;

import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.annotation.InjectType;
import nl.rovingeye.injectinator.framework.module.ConfigModule;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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

        if (constructorAndFieldAnnotatedPresent(classToInjectInto, InjectMe.class)) {
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

        for (final Method method : classToInjectInto.getMethods()) {
            if (method.isAnnotationPresent(InjectMe.class)) {
                return injectFieldsViaSetter(classToInjectInto);
            }
        }

        return injectFields(classToInjectInto);
    }

    private <T> T injectFieldsViaSetter(final Class<T> classToInjectInto) throws Exception {
        final T newInstance = classToInjectInto.newInstance();
        for (final Method method : classToInjectInto.getMethods()) {
            if (method.isAnnotationPresent(InjectMe.class)) {
                method.invoke(newInstance, (Object) inject(this.configModule.getInjectable(method.getParameterTypes()[0])));
            }
        }
        return newInstance;
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

    @SuppressWarnings("unchecked")
    private <T> T getSingleton(final Class<T> type) throws Exception {

        if (this.singletons.containsKey(type)) {
            return (T) this.singletons.get(type);
        }
        this.singletons.put(type, inject(this.configModule.getInjectable(type)));
        return (T) this.singletons.get(type);

    }

    private boolean isConstructorAnnotationPresent(final Class<? extends Annotation> annotation, final Constructor<?>... constructors) {
        for (final Constructor<?> constructor : constructors) {
            if (constructor.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }

    private boolean isFieldAnnotationPresent(final Class<? extends Annotation> annotation, final Field... fields) {
        for (final Field field : fields) {
            if (field.isAnnotationPresent(annotation)) {
                return true;
            }
        }
        return false;
    }

    private <T> boolean constructorAndFieldAnnotatedPresent(final Class<T> clazz, final Class<? extends Annotation> annotation) {
        return isConstructorAnnotationPresent(
                annotation, clazz.getConstructors()) &&
                isFieldAnnotationPresent(annotation, clazz.getDeclaredFields());
    }
}
