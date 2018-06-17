/*
 * Copyright 2018 Mark Hendriks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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