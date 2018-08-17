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

package nl.rovingeye.injectinator.example;

import nl.rovingeye.injectinator.example.service.ExampleConstructorSingletonService;
import nl.rovingeye.injectinator.example.service.ExampleFieldService;
import nl.rovingeye.injectinator.example.service.ExampleSetterService;
import nl.rovingeye.injectinator.example.service.ExampleSetterSingletonService;
import nl.rovingeye.injectinator.framework.Injectinator;

public final class ExampleApp {

    private ExampleApp() {
    }

    public static void main(final String[] args) throws Exception {
        final Injectinator injectinator = Injectinator.getInjectinator();
        final ExampleFieldService exampleFieldService = injectinator.inject(ExampleFieldService.class);
        final ExampleFieldService exampleFieldService2 = injectinator.inject(ExampleFieldService.class);
        final ExampleConstructorSingletonService exampleConstructorSingletonService = injectinator.inject(ExampleConstructorSingletonService.class);
        final ExampleSetterService exampleSetterService = injectinator.inject(ExampleSetterService.class);
        final ExampleSetterSingletonService exampleSetterSingletonService = injectinator.inject(ExampleSetterSingletonService.class);

        exampleFieldService.setExtraMessage("This is an alternate extra message");
        exampleFieldService.doStuff();
        exampleFieldService2.doStuff();
        exampleConstructorSingletonService.doStuff();
        exampleSetterService.doStuff();
        exampleSetterSingletonService.doStuff();
    }
}
