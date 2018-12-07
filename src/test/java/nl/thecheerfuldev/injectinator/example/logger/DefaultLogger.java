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

package nl.thecheerfuldev.injectinator.example.logger;

import nl.thecheerfuldev.injectinator.framework.annotation.InjectMe;
import nl.thecheerfuldev.injectinator.framework.annotation.InjectionType;

public class DefaultLogger implements Logger {

    @InjectMe(injectionType = InjectionType.SINGLETON)
    private AnotherLogger anotherLogger;

    private String extraMessage = "This is the default extra message";

    @Override
    public void log(final String message) {
        this.anotherLogger.info("I must print this: " + message + " " + this.extraMessage);
    }

    @Override
    public void setExtraMessage(final String extraMessage) {
        this.extraMessage = extraMessage;
    }
}