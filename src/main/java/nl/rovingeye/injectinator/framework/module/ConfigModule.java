package nl.rovingeye.injectinator.framework.module;

public interface ConfigModule {

    void configure();

    <T> Class<? extends T> getInjectable(final Class<T> type);
}
