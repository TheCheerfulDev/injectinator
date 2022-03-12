package nl.thecheerfuldev.injectinator.example.config;

import nl.thecheerfuldev.injectinator.example.logger.AnotherLogger;
import nl.thecheerfuldev.injectinator.example.logger.DefaultLogger;
import nl.thecheerfuldev.injectinator.example.logger.ExtraLogger;
import nl.thecheerfuldev.injectinator.example.logger.Logger;
import nl.thecheerfuldev.injectinator.framework.module.AbstractConfigModule;

public class MyInjectableConfig extends AbstractConfigModule {

    @Override
    public void configure() {
        enableInjectable(Logger.class, DefaultLogger.class);
        enableInjectable(AnotherLogger.class, ExtraLogger.class);
    }
}
