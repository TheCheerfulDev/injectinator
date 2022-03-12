package nl.thecheerfuldev.injectinator.framework.module;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class FileConfigModuleTest {

    private static final String THIS_FILE_DOESNT_EXIST_TXT = "this_file_doesnt_exist.txt";
    private FileConfigModule fileConfigModule;

    @Test
    void configureWithNonExistingFileThrowsIllegalArgumentException() {
        this.fileConfigModule = new FileConfigModule(THIS_FILE_DOESNT_EXIST_TXT);

        Assertions.assertThrows(IllegalArgumentException.class, this.fileConfigModule::configure);
    }
}