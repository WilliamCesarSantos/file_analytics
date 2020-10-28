package com.fileanalytics.file;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Unit test for {@link FileManagerUnitTest}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class FileManagerUnitTest {

    final String homePath = "./src/test/resources";
    private FileManager manager;

    @Before
    public void before() {
        manager = new FileManager(homePath);
    }

    @Test
    public void filesInOutputDirectoryReturnsJustFilesWithDoneDatExtensions() throws IOException {
        final List<Path> files = manager.filesInOutputDirectory().collect(Collectors.toList());
        Assert.assertEquals(1, files.size());

        for (Path file : files) {
            Assert.assertTrue(file.getFileName().toString().endsWith(".done.dat"));
        }
    }

    @Test
    public void filesInInputDirectoryReturnsJustFilesWithDatExtensions() throws IOException {
        final List<Path> files = manager.filesInInputDirectory().collect(Collectors.toList());
        Assert.assertEquals(2, files.size());

        for (Path file : files) {
            Assert.assertTrue(file.getFileName().toString().endsWith(".dat"));
        }
    }

    @Test
    public void writeFileGenerateNewFile() throws IOException {
        try (final Writer writer = manager.writeFile("unit-test.done.dat")) {
            writer.write("unit-test");
        }

        Path newFile = manager.filesInOutputDirectory()
                .filter(path -> path.getFileName().toString().equals("unit-test.done.dat"))
                .findAny().get();

        Assert.assertTrue(Files.deleteIfExists(newFile));
    }

    @Test
    public void readFileOneDat() throws IOException {
        Path one = manager.filesInInputDirectory()
                .filter(path -> path.getFileName().toString().equals("one.dat"))
                .findFirst().get();

        final String value = manager.readFile(one)
                .readLine();
        Assert.assertEquals("unit-test", value);
    }
}
