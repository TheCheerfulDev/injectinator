package nl.rovingeye.injectinator.framework.module;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfigModule implements ConfigModule {

    private final Map<Class<?>, Class<?>> injectables = new HashMap<>();

    protected <T> void enableInjectable(final Class<T> baseClass, final Class<? extends T> subClass) {
        this.injectables.put(baseClass, subClass.asSubclass(baseClass));
    }

    @Override
    public <T> Class<? extends T> getInjectable(final Class<T> type) {
        final Class<?> implementation = this.injectables.get(type);
        if (implementation == null) {
            throw new IllegalArgumentException("There is no injectable for type: " + type);
        }
        return implementation.asSubclass(type);
    }

}
