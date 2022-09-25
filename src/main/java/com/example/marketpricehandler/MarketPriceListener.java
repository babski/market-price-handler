package com.example.marketpricehandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Observable;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
class MarketPriceListener extends Observable {

    WatchService watchService;
    String sourceDirectory;

    /*
    * Provided source directory will be monitored for price feed files.
    * Adding new file or appending new lines to the existing file in the directory triggers feed processing.
    * */
    MarketPriceListener(String sourceDirectory) throws IOException {
        this.watchService = FileSystems.getDefault().newWatchService();
        this.sourceDirectory = sourceDirectory;
        Path path = Paths.get(sourceDirectory);
        path.register(watchService, ENTRY_MODIFY, ENTRY_CREATE);
    }

    void listen() throws InterruptedException {
        WatchKey key;
        while ((key = watchService.take()) != null) {
            for (WatchEvent<?> event : key.pollEvents()) {
                log.info("File {} set for processing.", event.context());
                setChanged();
                notifyObservers(sourceDirectory + event.context());
            }
            key.reset();
        }
    }
}
