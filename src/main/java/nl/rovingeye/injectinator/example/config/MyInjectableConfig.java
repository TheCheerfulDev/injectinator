package nl.rovingeye.injectinator.example.config;

import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.example.Logger;
import nl.rovingeye.injectinator.framework.module.AbstractConfigModule;

public class MyInjectableConfig extends AbstractConfigModule {

    @Override
    public void configure() {
        enableInjectable(ILogger.class, Logger.class);
    }
}
