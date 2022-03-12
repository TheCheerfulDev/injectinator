package nl.thecheerfuldev.injectinator.framework.module;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AbstractConfigModuleTest {

    private static final Class<Shape> BASE_CLASS = Shape.class;
    private static final Class<Circle> SUB_CLASS = Circle.class;
    private AbstractConfigModule configModule;

    @BeforeEach
    public void setUp() {
        this.configModule = new AbstractConfigModule() {
            @Override
            public void configure() {
                enableInjectable(BASE_CLASS, SUB_CLASS);
            }
        };
        this.configModule.configure();
    }

    @Test
    void getInjectableReturnsSubclass() {
        assertEquals(SUB_CLASS, this.configModule.getInjectable(BASE_CLASS));
    }

    @Test
    void getInjectableNoInjectableThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> this.configModule.getInjectable(String.class));
    }

    static class Shape {
    }

    static class Circle extends Shape {
    }

}