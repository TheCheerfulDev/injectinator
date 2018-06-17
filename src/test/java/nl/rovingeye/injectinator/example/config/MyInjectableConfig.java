package nl.rovingeye.injectinator.example.config;

import nl.rovingeye.injectinator.example.logger.AnotherLogger;
import nl.rovingeye.injectinator.example.logger.DefaultLogger;
import nl.rovingeye.injectinator.example.logger.ExtraLogger;
import nl.rovingeye.injectinator.example.logger.Logger;
import nl.rovingeye.injectinator.framework.module.AbstractConfigModule;

public class MyInjectableConfig extends AbstractConfigModule {

    @Override
    public void configure() {
        enableInjectable(Logger.class, DefaultLogger.class);
        enableInjectable(AnotherLogger.class, ExtraLogger.class);
    }
}
