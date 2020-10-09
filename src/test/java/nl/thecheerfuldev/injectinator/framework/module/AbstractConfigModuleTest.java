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

package nl.thecheerfuldev.injectinator.framework.module;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class AbstractConfigModuleTest {

    private static final Class<List> BASE_CLASS = List.class;
    private static final Class<ArrayList> SUB_CLASS = ArrayList.class;
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
}