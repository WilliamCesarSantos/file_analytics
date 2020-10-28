package com.fileanalytics.dataanalytic;

import com.fileanalytics.file.FileManager;
import com.fileanalytics.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Unit test for {@link DataAnalyzerSchedulerUnitTest}
 *
 * @author william.santos
 * @since 0.1.0 - 2020-10-27
 */
public class DataAnalyzerSchedulerUnitTest {

    private DataAnalyzerController controller;
    private FileManager fileManager;

    private DataAnalyzerScheduler scheduler;

    @Before
    public void before() throws IOException {
        controller = Mockito.mock(DataAnalyzerController.class);
        final AnalyzedData analyzedData = new AnalyzedData(Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        Mockito.doReturn(analyzedData).when(controller).analyze(Mockito.any());
        Mockito.doReturn(Optional.of(new Order(0L, "salesManName", Collections.emptyList(), BigDecimal.ZERO))).when(controller).moreExpensiveOrder(Mockito.any());
        Mockito.doReturn(Optional.of("worst-seller")).when(controller).worstSeller(Mockito.any());

        fileManager = Mockito.mock(FileManager.class);
        Mockito.doReturn(Stream.empty()).when(fileManager).filesInInputDirectory();
        Mockito.doReturn(Stream.empty()).when(fileManager).filesInOutputDirectory();
        Mockito.doReturn(Mockito.mock(BufferedReader.class)).when(fileManager).readFile(Mockito.any());
        Mockito.doReturn(Mockito.mock(BufferedWriter.class)).when(fileManager).writeFile(Mockito.any());

        scheduler = new DataAnalyzerScheduler(controller, fileManager);
    }

    @Test
    public void runDoesNotCallFileManagerWriteWhenFileInputIsEmpty() throws IOException {
        scheduler.run();

        Mockito.verify(fileManager, Mockito.never()).writeFile(Mockito.any());
    }

    @Test
    public void runDoesNotCallControllerAnalyzeWhenAllFilesHasAnalyzed() throws IOException {
        final Path one = Mockito.mock(Path.class);
        Mockito.doReturn(one).when(one).getFileName();
        Mockito.doReturn("unit.test.dat").when(one).toString();
        Mockito.doReturn(one).when(one).toAbsolutePath();
        Mockito.doReturn("unit.test.dat").when(one).toString();

        final Path two = Mockito.mock(Path.class);
        Mockito.doReturn(two).when(two).getFileName();
        Mockito.doReturn("unit.test.done.dat").when(two).toString();
        Mockito.doReturn(two).when(two).toAbsolutePath();
        Mockito.doReturn("unit.test.done.dat").when(two).toString();

        Mockito.doReturn(Stream.of(one)).when(fileManager).filesInInputDirectory();
        Mockito.doReturn(Stream.of(two)).when(fileManager).filesInOutputDirectory();

        scheduler.run();

        Mockito.verify(controller, Mockito.never()).analyze(Mockito.any());
    }

    @Test
    public void runCallsControllerAnalyzeForFilesHasntAnalyzed() throws IOException {
        final Path one = Mockito.mock(Path.class);
        Mockito.doReturn(one).when(one).getFileName();
        Mockito.doReturn("unit-test.dat").when(one).toString();
        Mockito.doReturn(one).when(one).toAbsolutePath();
        Mockito.doReturn("unit-test.dat").when(one).toString();

        final Path two = Mockito.mock(Path.class);
        Mockito.doReturn(two).when(two).getFileName();
        Mockito.doReturn("unit.test.dat").when(two).toString();
        Mockito.doReturn(two).when(two).toAbsolutePath();
        Mockito.doReturn("unit.test.dat").when(two).toString();

        final Path three = Mockito.mock(Path.class);
        Mockito.doReturn(three).when(three).getFileName();
        Mockito.doReturn("unit.test.done.dat").when(three).toString();
        Mockito.doReturn(three).when(three).toAbsolutePath();
        Mockito.doReturn("unit.test.done.dat").when(three).toString();

        Mockito.doReturn(Stream.of(one, two)).when(fileManager).filesInInputDirectory();
        Mockito.doReturn(Stream.of(three)).when(fileManager).filesInOutputDirectory();

        scheduler.run();

        Mockito.verify(controller, Mockito.times(1)).analyze(Mockito.any());
    }

    @Test
    public void runCallsFileManagerWriteFileForFilesHasntAnalyzed() throws IOException {
        final Path one = Mockito.mock(Path.class);
        Mockito.doReturn(one).when(one).getFileName();
        Mockito.doReturn("unit-test.dat").when(one).toString();
        Mockito.doReturn(one).when(one).toAbsolutePath();
        Mockito.doReturn("unit-test.dat").when(one).toString();

        final Path two = Mockito.mock(Path.class);
        Mockito.doReturn(two).when(two).getFileName();
        Mockito.doReturn("unit.test.done.dat").when(two).toString();
        Mockito.doReturn(two).when(two).toAbsolutePath();
        Mockito.doReturn("unit.test.done.dat").when(two).toString();

        Mockito.doReturn(Stream.of(one)).when(fileManager).filesInInputDirectory();
        Mockito.doReturn(Stream.of(two)).when(fileManager).filesInOutputDirectory();

        scheduler.run();

        Mockito.verify(fileManager, Mockito.times(1)).writeFile(Mockito.any());
    }

    @Test
    public void controllerAnalyzeCausesExceptionInRunSkipFileAndContinueNext() throws IOException {
        final Path one = Mockito.mock(Path.class);
        Mockito.doReturn(one).when(one).getFileName();
        Mockito.doReturn("unit-test.dat").when(one).toString();
        Mockito.doReturn(one).when(one).toAbsolutePath();
        Mockito.doReturn("unit-test.dat").when(one).toString();

        final Path two = Mockito.mock(Path.class);
        Mockito.doReturn(two).when(two).getFileName();
        Mockito.doReturn("unit.dat").when(two).toString();
        Mockito.doReturn(two).when(two).toAbsolutePath();
        Mockito.doReturn("unit.dat").when(two).toString();

        Mockito.doReturn(Stream.of(one, two)).when(fileManager).filesInInputDirectory();
        Mockito.doReturn(Stream.empty()).when(fileManager).filesInOutputDirectory();

        Mockito.doThrow(IllegalArgumentException.class).when(controller).analyze(Mockito.any());

        scheduler.run();

        Mockito.verify(controller, Mockito.times(2)).analyze(Mockito.any());
    }


}
