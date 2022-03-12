package nl.thecheerfuldev.injectinator.framework.module;

public interface ConfigModule {

    /**
     * The implementation of this class should initiate all the logic that enables
     * {@link nl.thecheerfuldev.injectinator.framework.Injectinator} to be initialized
     * and do its work.
     */
    void configure();

    <T> Class<? extends T> getInjectable(final Class<T> type);
}
