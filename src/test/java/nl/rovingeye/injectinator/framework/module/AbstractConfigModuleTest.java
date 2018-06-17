package nl.rovingeye.injectinator.framework.module;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AbstractConfigModuleTest {

    private static final Class<List> BASE_CLASS = List.class;
    private static final Class<ArrayList> SUB_CLASS = ArrayList.class;
    private AbstractConfigModule configModule;

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Before
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
    public void getInjectableReturnsSubclass() {
        assertEquals(SUB_CLASS, this.configModule.getInjectable(BASE_CLASS));
    }

    @Test
    public void getInjectableNoInjectableThrowsException() {
        this.expectedException.expect(IllegalArgumentException.class);
        this.expectedException.expectMessage("There is no injectable for type: class java.lang.String");
        this.configModule.getInjectable(String.class);
    }
}