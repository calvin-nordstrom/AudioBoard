package com.calvinnordstrom.audioboard.model;

import com.calvinnordstrom.audioboard.data.Serializer;

import java.io.File;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PersistenceManager {
    private final Serializer<DataModel> serializer = new Serializer<>(
            DataModel.class,
            new File(System.getenv("APPDATA"), "AudioBoard"),
            "model.ser"
    );
    private final Duration saveDelay;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> pendingSave;

    public PersistenceManager(Duration saveDelay) {
        this.saveDelay = saveDelay;
    }

    public synchronized void notifyChanged(DataModel dataModel) {
        if (pendingSave != null) {
            pendingSave.cancel(false);
        }

        pendingSave = executor.schedule(
                () -> serializer.save(dataModel),
                saveDelay.toMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    public synchronized void stop() {
        if (pendingSave != null) {
            pendingSave.cancel(false);
        }

        executor.shutdown();
    }

    public void save(DataModel dataModel) {
        serializer.save(dataModel);
    }

    public DataModel load() {
        return serializer.load(new DataModel());
    }
}
