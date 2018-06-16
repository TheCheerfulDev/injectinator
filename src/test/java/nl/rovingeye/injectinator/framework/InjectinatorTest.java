package nl.rovingeye.injectinator.framework;

import nl.rovingeye.injectinator.example.AnotherLogger;
import nl.rovingeye.injectinator.example.IAnotherLogger;
import nl.rovingeye.injectinator.example.ILogger;
import nl.rovingeye.injectinator.example.service.ExampleFieldService;
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

    @Before
    public void setUp() {
        log = new ArrayList<>();
        extra = new ArrayList<>();

        this.injectinator = Injectinator.getInjectinator(new AbstractConfigModule() {
            @Override
            public void configure() {
                enableInjectable(ILogger.class, InjectinatorTest.MyStubLogger.class); //Nope not happening it seems...
                enableInjectable(IAnotherLogger.class, AnotherLogger.class);
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
    }


    class MyStubLogger implements ILogger {

        @Override
        public void log(final String message) {
            log.add(message);
        }

        @Override
        public void setExtraMessage(final String extraMessage) {
            extra.add(extraMessage);
        }
    }

}