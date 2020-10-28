package com.fileanalytics.file;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Path;

/**
 * Unit test for {@link FileUtilUnitTest}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class FileUtilUnitTest {

    @Test
    public void removeExtensionInputFileReturnsFileNameWithoutDatExtension() {
        final Path path = Mockito.mock(Path.class);
        Mockito.doReturn(path).when(path).getFileName();
        Mockito.doReturn("unit.test.dat").when(path).toString();

        final String name = FileUtil.removeExtensionInputFile(path);
        Assert.assertEquals("unit.test", name);
    }

    @Test
    public void removeExtensionInputFileReturnsFileNameWithTxtExtension() {
        final Path path = Mockito.mock(Path.class);
        Mockito.doReturn(path).when(path).getFileName();
        Mockito.doReturn("unit.test.txt").when(path).toString();

        final String name = FileUtil.removeExtensionInputFile(path);
        Assert.assertEquals("unit.test.txt", name);
    }

    @Test(expected = NullPointerException.class)
    public void removeExtensionInputFileDoesNotAcceptNull() {
        FileUtil.removeExtensionInputFile(null);
    }

    @Test
    public void removeExtensionOutputFileReturnsFileNameWithoutDoneDatExtension() {
        final Path path = Mockito.mock(Path.class);
        Mockito.doReturn(path).when(path).getFileName();
        Mockito.doReturn("unit.test.done.dat").when(path).toString();

        final String name = FileUtil.removeExtensionOutputFile(path);
        Assert.assertEquals("unit.test", name);
    }

    @Test
    public void removeExtensionOutputFileReturnsFileNameWithTxtExtension() {
        final Path path = Mockito.mock(Path.class);
        Mockito.doReturn(path).when(path).getFileName();
        Mockito.doReturn("unit.test.done.txt").when(path).toString();

        final String name = FileUtil.removeExtensionOutputFile(path);
        Assert.assertEquals("unit.test.done.txt", name);
    }

    @Test(expected = NullPointerException.class)
    public void removeExtensionOutputFileDoesNotAcceptNull() {
        FileUtil.removeExtensionOutputFile(null);
    }

    @Test
    public void outputFileNameReturnsFileWithDoneDatExtension() {
        final Path path = Mockito.mock(Path.class);
        Mockito.doReturn(path).when(path).getFileName();
        Mockito.doReturn("unit.test.dat").when(path).toString();

        final String name = FileUtil.outputFileName(path);
        Assert.assertEquals("unit.test.done.dat", name);
    }

    @Test(expected = NullPointerException.class)
    public void outputFileNameDoesNotAcceptNull() {
        FileUtil.outputFileName(null);
    }
}
