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
import org.junit.jupiter.api.Test;

public class FileConfigModuleTest {

    private static final String THIS_FILE_DOESNT_EXIST_TXT = "this_file_doesnt_exist.txt";
    private FileConfigModule fileConfigModule;

    @Test
    public void configureWithNonExistingFileThrowsIllegalArgumentException() {
        this.fileConfigModule = new FileConfigModule(THIS_FILE_DOESNT_EXIST_TXT);

        Assertions.assertThrows(IllegalArgumentException.class, this.fileConfigModule::configure);
    }
}