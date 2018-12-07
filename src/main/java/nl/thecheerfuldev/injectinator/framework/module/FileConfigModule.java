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

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

public class FileConfigModule extends AbstractConfigModule {

    private final String configResource;

    public FileConfigModule() {
        this.configResource = "injectables.txt";
    }

    public FileConfigModule(final String configResource) {
        this.configResource = configResource;
    }

    @Override
    public void configure() {
        try {
            getInjectablesFromResource();
        } catch (final URISyntaxException | IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private void getInjectablesFromResource() throws URISyntaxException, IOException {
        final URL resource = ClassLoader.getSystemClassLoader().getResource(this.configResource);

        if (Objects.isNull(resource)) {
            throw new IllegalArgumentException(String.format("Config resource [%s] doesn't exist.", this.configResource));
        }

        try (final Stream<String> lines = Files.lines(Paths.get(resource.toURI()))) {
            lines.forEach(line -> {
                try {
                    setInjectable(line);
                } catch (final ClassNotFoundException e) {
                    throw new IllegalArgumentException("The specified class could not be found. Check your config.", e);
                }
            });
        }
    }

    private <T> void setInjectable(final String line) throws ClassNotFoundException {
        final Class<T> baseClass = getBaseClass(line);
        final Class<? extends T> subclass = getSubClass(line, baseClass);
        enableInjectable(baseClass, subclass);
    }

    @SuppressWarnings("unchecked")
    private <T> Class<? extends T> getSubClass(final String line, final Class<T> baseClass) throws ClassNotFoundException {
        final int commaIndex = line.indexOf(',');
        final Class<?> aClass = Class.forName(line.substring(commaIndex + 1).trim());
        if (!baseClass.isAssignableFrom(aClass)) {
            throw new IllegalArgumentException(
                    String.format("Configured subclass [%s] is not a subclass of baseclass [%s]", aClass.getTypeName(), baseClass.getTypeName()));
        }
        return (Class<? extends T>) aClass;
    }

    @SuppressWarnings("unchecked")
    private <T> Class<T> getBaseClass(final String line) throws ClassNotFoundException {
        final int commaIndex = line.indexOf(',');
        return (Class<T>) Class.forName(line.substring(0, commaIndex).trim());
    }
}
