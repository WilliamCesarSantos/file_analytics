package com.fileanalytics.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Scanning files to process
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
@Component
public class FileManager {

    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private final Path pathIn;
    private final Path pathOut;

    public FileManager(@Value("${HOMEPATH}") final String homePath) {
        pathIn = Paths.get(homePath + FileUtil.INPUT_DIRECTORY);
        pathOut = Paths.get(homePath + FileUtil.OUTPUT_DIRECTORY);
    }

    /**
     * Scan input directory and returns files found
     *
     * @return Stream
     * @throws IOException
     */
    public Stream<Path> filesInOutputDirectory() throws IOException {
        return Files.find(pathOut, 1, (path, filter) -> path.getFileName().toString().endsWith(FileUtil.FILE_PROCESS_SUFFIX + FileUtil.VALID_EXTENSION))
                .peek(path -> logger.debug("Found processed file {}", path.toAbsolutePath().toString()));
    }

    /**
     * Scan output directory and returns files found
     *
     * @return Stream
     * @throws IOException
     */
    public Stream<Path> filesInInputDirectory() throws IOException {
        return Files.find(pathIn, 1, (path, filter) -> path.getFileName().toString().endsWith(FileUtil.VALID_EXTENSION))
                .peek(path -> logger.debug("Found file {}", path.toAbsolutePath().toString()));
    }

    /**
     * Create a new file and open writer
     *
     * @param fileName that be create in output directory
     * @return BufferedWriter
     * @throws IOException
     */
    public Writer writeFile(final String fileName) throws IOException {
        final Path fileOut = Files.createFile(Paths.get(pathOut.toString(), fileName));
        logger.debug("Creating file: {}", fileOut.toAbsolutePath().toString());
        return Files.newBufferedWriter(fileOut, UTF_8);
    }

    /**
     * Create new Reader to file
     *
     * @param path
     * @return
     * @throws IOException
     */
    public BufferedReader readFile(final Path path) throws IOException {
        logger.debug("Creating reader for file: {}", path.toAbsolutePath().toString());
        return Files.newBufferedReader(path, UTF_8);
    }

    /**
     * Check if exist the direct input and output
     */
    @PostConstruct
    public void checkRootPath() {
        if (!Files.isDirectory(pathIn)) {
            throw new IllegalStateException("Path " + pathIn.toAbsolutePath() + " is not a directory, please fix and try again it");
        }
        logger.info("Input directory located at: {}", pathIn.toAbsolutePath().toString());
        if (!Files.isDirectory(pathOut)) {
            throw new IllegalStateException("Path " + pathOut.toAbsolutePath() + " is not a directory, please fix and try again it");
        }
        logger.info("Output directory located at: {}", pathOut.toAbsolutePath().toString());
    }
}
