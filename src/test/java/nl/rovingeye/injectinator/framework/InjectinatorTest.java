package nl.rovingeye.injectinator.framework;

import nl.rovingeye.injectinator.example.logger.AnotherLogger;
import nl.rovingeye.injectinator.example.logger.Logger;
import nl.rovingeye.injectinator.example.service.ExampleFieldService;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.module.AbstractConfigModule;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class InjectinatorTest {

    private Injectinator injectinator;
    private static List<String> log;
    private static List<String> extra;
    private static List<String> another;

    @Before
    public void setUp() {
        log = new ArrayList<>();
        extra = new ArrayList<>();
        another = new ArrayList<>();

        this.injectinator = Injectinator.getInjectinator(new AbstractConfigModule() {
            @Override
            public void configure() {
                enableInjectable(Logger.class, InjectinatorTest.MyStubLogger.class);
                enableInjectable(AnotherLogger.class, InjectinatorTest.MyStubExtraLogger.class);
            }
        });
    }

    @Test
    public void inject() throws Exception {
        final ExampleFieldService exampleFieldService = this.injectinator.inject(ExampleFieldService.class);
        assertNotNull(exampleFieldService);
        exampleFieldService.doStuff();
        exampleFieldService.setExtraMessage("test");

        assertFalse(log.isEmpty());
        assertFalse(extra.isEmpty());
        assertFalse(another.isEmpty());
    }

    public class MyStubLogger implements Logger {

        private final AnotherLogger anotherLogger;

        @InjectMe
        public MyStubLogger(final AnotherLogger anotherLogger) {
            this.anotherLogger = anotherLogger;
        }

        @Override
        public void log(final String message) {
            log.add(message);
            this.anotherLogger.info(message);
        }

        @Override
        public void setExtraMessage(final String extraMessage) {
            extra.add(extraMessage);
        }
    }

    public class MyStubExtraLogger implements AnotherLogger {
        @Override
        public void info(final String message) {
            another.add(message);
        }
    }
}