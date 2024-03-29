package nl.thecheerfuldev.injectinator.framework;

import nl.thecheerfuldev.injectinator.example.logger.AnotherLogger;
import nl.thecheerfuldev.injectinator.example.logger.DefaultLogger;
import nl.thecheerfuldev.injectinator.example.logger.ExtraLogger;
import nl.thecheerfuldev.injectinator.example.logger.Logger;
import nl.thecheerfuldev.injectinator.example.service.ExampleConstructorService;
import nl.thecheerfuldev.injectinator.example.service.ExampleConstructorSingletonService;
import nl.thecheerfuldev.injectinator.example.service.ExampleFieldService;
import nl.thecheerfuldev.injectinator.example.service.ExampleFieldSingletonService;
import nl.thecheerfuldev.injectinator.example.service.ExampleSetterService;
import nl.thecheerfuldev.injectinator.example.service.ExampleSetterSingletonService;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectMe;
import nl.thecheerfuldev.injectinator.framework.module.AbstractConfigModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class InjectinatorTest {

    private Injectinator injectinator;
    private static final List<String> log = new ArrayList<>();
    private static final List<String> extra = new ArrayList<>();
    private static final List<String> another = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        log.clear();
        extra.clear();
        another.clear();

        this.injectinator = Injectinator.getInjectinator(new AbstractConfigModule() {
            @Override
            public void configure() {
            }
        });
    }

    @Test
    void injectInnerClassWithConstructorInjection() throws Exception {
        this.injectinator = Injectinator.getInjectinator(new AbstractConfigModule() {
            @Override
            public void configure() {
                enableInjectable(Logger.class, InjectinatorTest.InnerClassWithConstructorInjection.class);
                enableInjectable(AnotherLogger.class, InjectinatorTest.MyStubExtraLogger.class);
            }
        });
        final ExampleFieldService exampleFieldService = this.injectinator.inject(ExampleFieldService.class);
        assertInjectionWorks(exampleFieldService);
    }

    @Test
    void injectInnerClassWithFieldInjection() throws Exception {
        this.injectinator = Injectinator.getInjectinator(new AbstractConfigModule() {
            @Override
            public void configure() {
                enableInjectable(Logger.class, InjectinatorTest.InnerClassWithFieldInjection.class);
                enableInjectable(AnotherLogger.class, InjectinatorTest.MyStubExtraLogger.class);
            }
        });
        final ExampleFieldService exampleFieldService = this.injectinator.inject(ExampleFieldService.class);
        assertInjectionWorks(exampleFieldService);
    }

    @Test
    void injectInnerClassWithSetterInjection() throws Exception {
        this.injectinator = Injectinator.getInjectinator(new AbstractConfigModule() {
            @Override
            public void configure() {
                enableInjectable(Logger.class, InjectinatorTest.InnerClassWithSetterInjection.class);
                enableInjectable(AnotherLogger.class, InjectinatorTest.MyStubExtraLogger.class);
            }
        });
        final ExampleFieldService exampleFieldService = this.injectinator.inject(ExampleFieldService.class);
        assertInjectionWorks(exampleFieldService);
    }

    @Test
    void injectWithTooManyAnnotationsThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.injectinator.inject(ClassWithTooManyAnnotations.class));
    }

    @Test
    void injectNullReturnsNull() throws Exception {
        assertNull(this.injectinator.inject(null));
    }

    @Test
    void injectClassWithConstructorInjection() throws Exception {
        this.injectinator = getInjectinator();
        final ExampleConstructorService exampleConstructorService1 = this.injectinator.inject(ExampleConstructorService.class);
        final ExampleConstructorService exampleConstructorService2 = this.injectinator.inject(ExampleConstructorService.class);

        assertNotSame(exampleConstructorService1.getLogger(), exampleConstructorService2.getLogger());
    }

    @Test
    void injectClassWithSingletonConstructorInjection() throws Exception {
        this.injectinator = getInjectinator();
        final ExampleConstructorSingletonService exampleConstructorSingletonService1 = this.injectinator.inject(ExampleConstructorSingletonService.class);
        final ExampleConstructorSingletonService exampleConstructorSingletonService2 = this.injectinator.inject(ExampleConstructorSingletonService.class);

        assertSame(exampleConstructorSingletonService1.getLogger(), exampleConstructorSingletonService2.getLogger());
    }

    @Test
    void injectClassWithFieldInjection() throws Exception {
        this.injectinator = getInjectinator();
        final ExampleFieldService exampleFieldService1 = this.injectinator.inject(ExampleFieldService.class);
        final ExampleFieldService exampleFieldService2 = this.injectinator.inject(ExampleFieldService.class);

        assertNotSame(exampleFieldService1.getLogger(), exampleFieldService2.getLogger());
    }

    @Test
    void injectClassWithSingletonFieldInjection() throws Exception {
        this.injectinator = getInjectinator();
        final ExampleFieldSingletonService exampleFieldSingletonService1 = this.injectinator.inject(ExampleFieldSingletonService.class);
        final ExampleFieldSingletonService exampleFieldSingletonService2 = this.injectinator.inject(ExampleFieldSingletonService.class);

        assertSame(exampleFieldSingletonService1.getLogger(), exampleFieldSingletonService2.getLogger());
    }

    @Test
    void injectClassWithSetterInjection() throws Exception {
        this.injectinator = getInjectinator();
        final ExampleSetterService exampleSetterService1 = this.injectinator.inject(ExampleSetterService.class);
        final ExampleSetterService exampleSetterService2 = this.injectinator.inject(ExampleSetterService.class);

        assertNotSame(exampleSetterService1.getLogger(), exampleSetterService2.getLogger());
    }

    @Test
    void injectClassWithSingletonSetterInjection() throws Exception {
        this.injectinator = getInjectinator();
        final ExampleSetterSingletonService exampleSetterSingletonService1 = this.injectinator.inject(ExampleSetterSingletonService.class);
        final ExampleSetterSingletonService exampleSetterSingletonService2 = this.injectinator.inject(ExampleSetterSingletonService.class);

        assertSame(exampleSetterSingletonService1.getLogger(), exampleSetterSingletonService2.getLogger());
    }

    @Test
    void injectClassWithTooManySetterParametersThrowsException() {
        this.injectinator = getInjectinator();
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.injectinator.inject(ClassWithTooManySetterParameters.class));
    }

    private Injectinator getInjectinator() {
        return Injectinator.getInjectinator(new AbstractConfigModule() {
            @Override
            public void configure() {
                enableInjectable(Logger.class, DefaultLogger.class);
                enableInjectable(AnotherLogger.class, ExtraLogger.class);
            }
        });
    }

    private void assertInjectionWorks(final ExampleFieldService exampleFieldService) {
        assertNotNull(exampleFieldService);
        exampleFieldService.doStuff();
        exampleFieldService.setExtraMessage("test");

        assertFalse(log.isEmpty());
        assertFalse(extra.isEmpty());
        assertFalse(another.isEmpty());
    }

    public static class InnerClassWithConstructorInjection implements Logger {

        private final AnotherLogger anotherLogger;

        @InjectMe
        public InnerClassWithConstructorInjection(final AnotherLogger anotherLogger) {
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

    public class InnerClassWithFieldInjection implements Logger {


        @InjectMe
        private AnotherLogger anotherLogger;

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

    public static class InnerClassWithSetterInjection implements Logger {


        private AnotherLogger anotherLogger;

        @Override
        public void log(final String message) {
            log.add(message);
            this.anotherLogger.info(message);
        }

        @Override
        public void setExtraMessage(final String extraMessage) {
            extra.add(extraMessage);
        }

        @InjectMe
        public void setAnotherLogger(final AnotherLogger anotherLogger) {
            this.anotherLogger = anotherLogger;
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
        private final String foo;

        @InjectMe
        public ClassWithTooManyAnnotations(final String foo) {
            this.foo = foo;
        }

    }

    public class ClassWithTooManySetterParameters {
        @InjectMe
        public void setFoo(final String foo, final String bar) {
        }
    }
}