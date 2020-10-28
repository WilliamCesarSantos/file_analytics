package com.fileanalytics.dataanalytic;

import com.fileanalytics.file.FileManager;
import com.fileanalytics.file.FileUtil;
import com.fileanalytics.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Schedule to monitoring folder and process new files available
 *
 * @author william.santos
 * @since 0.1.0, 2020-10-26
 */
@Component
public class DataAnalyzerScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DataAnalyzerScheduler.class);

    private final DataAnalyzerController controller;
    private final FileManager fileManager;

    /**
     * @param controller
     */
    public DataAnalyzerScheduler(@Autowired DataAnalyzerController controller,
                                 final FileManager fileManager) {
        this.controller = controller;
        this.fileManager = fileManager;
    }

    /**
     * Execute schedule by fixed rate at ${south.schedule.fixedRate} ms
     */
    @Scheduled(fixedRateString = "${south.schedule.fixedRate}")
    public void run() {
        Thread.currentThread().setName("DataAnalyzerScheduler");
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> logger.error("Error on execute scheduler in thread {}", thread, throwable));
        try {
            logger.info("Starting scheduler: {}", DataAnalyzerScheduler.class.getName());
            processFiles();
        } catch (Exception exception) {
            logger.error("Error on execute scheduler", exception);
        }
    }

    private void processFiles() throws IOException {
        logger.info("Find new files");

        final Set<String> filesProcessed = fileManager.filesInOutputDirectory()
                .map(FileUtil::removeExtensionOutputFile)
                .collect(Collectors.toCollection(TreeSet::new));

        fileManager.filesInInputDirectory()
                .parallel()
                .filter(path -> {
                    final String name = FileUtil.removeExtensionInputFile(path);
                    boolean processed = filesProcessed.contains(name);
                    if (processed) {
                        logger.debug("Ignoring file {} because it has processed", path.toAbsolutePath().toString());
                    }
                    return !processed;
                })
                .forEach(path -> {
                    logger.info("Process file: {}", path.toAbsolutePath().toString());
                    try (final BufferedReader reader = fileManager.readFile(path)) {
                        final AnalyzedData analyzed = controller.analyze(reader);
                        writeFile(path, analyzed);
                    } catch (final Exception exception) {
                        logger.error("Error on process file: {}", path.toAbsolutePath().toString(), exception);
                    }
                });
    }

    private void writeFile(final Path fileIn, final AnalyzedData analyzed) throws IOException {
        final String fileName = FileUtil.outputFileName(fileIn);
        try (final Writer writer = fileManager.writeFile(fileName)) {
            logger.info("Writing the result of data analyzed in: {}", fileName);
            writer.write("Total customer found is: " + analyzed.getCustomers().size());
            writer.write("\r\n");
            writer.write("Total sales man found is: " + analyzed.getSalesMan().size());
            writer.write("\r\n");
            final Long moreExpensiveOrder = controller.moreExpensiveOrder(analyzed.getOrders()).map(Order::getId).orElse(null);
            writer.write("The more expensive order is: " + moreExpensiveOrder);
            writer.write("\r\n");
            final String worstSeller = controller.worstSeller(analyzed.getOrders()).orElse(null);
            writer.write("The worse seller is: " + worstSeller);
        }
        logger.debug("Written the result in: {}", fileName);
    }

}
