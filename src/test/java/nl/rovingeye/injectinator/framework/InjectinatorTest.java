package nl.rovingeye.injectinator.framework;

import nl.rovingeye.injectinator.example.logger.AnotherLogger;
import nl.rovingeye.injectinator.example.logger.Logger;
import nl.rovingeye.injectinator.example.service.ExampleFieldService;
import nl.rovingeye.injectinator.framework.annotation.InjectMe;
import nl.rovingeye.injectinator.framework.module.AbstractConfigModule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class InjectinatorTest {

    private Injectinator injectinator;
    private static List<String> log;
    private static List<String> extra;
    private static List<String> another;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void injectWithTooManyAnnotationsThrowsException() throws Exception {
        this.expectedException.expect(IllegalArgumentException.class);
        this.injectinator.inject(InjectinatorTest.ClassWithTooManyAnnotations.class);
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

    public class ClassWithTooManyAnnotations {

        @InjectMe
        private String foo;

        @InjectMe
        public ClassWithTooManyAnnotations(final String foo) {
            this.foo = foo;
        }
    }
}