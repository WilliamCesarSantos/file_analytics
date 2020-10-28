package com.fileanalytics.file;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Utility class for handle {@link Path}
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
public final class FileUtil {

    /**
     * Extensions valid for app only .dat
     */
    public static final String VALID_EXTENSION = ".dat";
    /**
     * Mark to indicate the file is processed
     */
    public static final String FILE_PROCESS_SUFFIX = ".done";

    /**
     * Directory where are the files to import and process
     */
    public static final String INPUT_DIRECTORY = "/data/in";

    /**
     * Directory where are write the files processed
     */
    public static final String OUTPUT_DIRECTORY = "/data/out";

    private FileUtil() {
    }

    /**
     * Remove the extension({@link #VALID_EXTENSION}) of file name and returns it
     *
     * @param path
     * @return String
     */
    public static String removeExtensionInputFile(final Path path) {
        Objects.requireNonNull(path, "path cannot be null");
        return path.getFileName().toString().replace(FileUtil.VALID_EXTENSION, "");
    }

    /**
     * Remove the extension({@link #FILE_PROCESS_SUFFIX} + {@link #VALID_EXTENSION} ) of file name and returns it
     *
     * @param path
     * @return String
     */
    public static String removeExtensionOutputFile(final Path path) {
        Objects.requireNonNull(path, "path cannot be null");
        return path.getFileName().toString().replace(FileUtil.FILE_PROCESS_SUFFIX + FileUtil.VALID_EXTENSION, "");
    }

    /**
     * Generate file name applying {@link #FILE_PROCESS_SUFFIX} to mark as processed
     *
     * @param fileIn
     * @return
     */
    public static String outputFileName(final Path fileIn) {
        Objects.requireNonNull(fileIn, "fileIn cannot be null");
        final String originName = removeExtensionInputFile(fileIn);
        return originName + FileUtil.FILE_PROCESS_SUFFIX + FileUtil.VALID_EXTENSION;
    }
}
