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

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConfigModule implements ConfigModule {

    private final Map<Class<?>, Class<?>> injectables = new HashMap<>();

    protected <T> void enableInjectable(final Class<T> baseClass, final Class<? extends T> subClass) {
        this.injectables.put(baseClass, subClass.asSubclass(baseClass));
    }

    @Override
    public <T> Class<? extends T> getInjectable(final Class<T> type) {
        final Class<?> implementation = this.injectables.get(type);
        if (implementation == null) {
            throw new IllegalArgumentException("There is no injectable for type: " + type);
        }
        return implementation.asSubclass(type);
    }
}
