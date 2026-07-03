package com.calvinnordstrom.audioboard.data;

import java.io.*;

/**
 * A generic utility for serializing and deserializing Java objects to disk.
 * <p>
 * Each instance of {@code Serializer} is bound to a specific file and type,
 * simplifying repeated save/load operations for the same object type.
 * </p>
 *
 * @param <T> the type of object to serialize; must implement {@link Serializable}
 */
public class Serializer<T extends Serializable> {
    private final File directory;
    private final File file;
    private final Class<T> type;

    /**
     * Creates a Serializer for a given class type and storage location.
     *
     * @param type the class type to serialize/deserialize
     * @param directory the directory to store the serialized file in
     * @param fileName  the name of the serialized file
     */
    public Serializer(Class<T> type, File directory, String fileName) {
        this.type = type;
        this.directory = directory;
        this.file = new File(directory, fileName);

        if (!directory.exists() && !directory.mkdirs()) {
            System.err.println("Failed to create directory: " + directory.getAbsolutePath());
        }

        try {
            if (!file.exists() && !file.createNewFile()) {
                System.err.println("Failed to create file: " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.err.println("Error creating file: " + e.getMessage());
        }
    }

    /**
     * Saves an object instance to disk.
     * <p>
     * If serialization fails, the file will remain unchanged.
     * </p>
     *
     * @param object the object instance to serialize; must not be {@code null}
     */
    public void save(T object) {
        try {
            serialize(object, file);
        } catch (IOException e) {
            System.err.println("Failed to serialize object: " + e.getMessage());
        }
    }

    /**
     * Loads an object from disk.
     * <p>
     * If the file is missing, empty, or deserialization fails, the provided
     * {@code defaultValue} is returned instead.
     * </p>
     *
     * @param defaultValue the fallback value to use if deserialization fails
     * @return the deserialized object, or {@code defaultValue} if unavailable
     */
    public T load(T defaultValue) {
        try {
            T obj = deserialize(file, type);
            return obj != null ? obj : defaultValue;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to deserialize object: " + e.getMessage());
            return defaultValue;
        }
    }

    /**
     * Serializes an object to a file.
     * <p>
     * This method uses a temporary file and atomic replace to prevent partial
     * writes in case of unexpected termination.
     * </p>
     *
     * @param obj the object to serialize; must not be {@code null}
     * @param file the destination file
     * @param <T> the type of the object
     * @throws IOException if writing to disk fails
     */
    public static <T extends Serializable> void serialize(T obj, File file) throws IOException {
        if (obj == null) {
            throw new IllegalArgumentException("Cannot serialize a null object.");
        }
        if (file == null) {
            throw new IllegalArgumentException("File cannot be null.");
        }

        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            throw new IOException("Failed to create directory: " + parentDir.getAbsolutePath());
        }

        File tempFile = new File(file.getAbsolutePath() + ".tmp");
        try (ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile)))) {
            oos.writeObject(obj);
        }

        if (!tempFile.renameTo(file)) {
            if (file.delete() && !tempFile.renameTo(file)) {
                throw new IOException("Failed to replace old file: " + file.getAbsolutePath());
            }
        }
    }

    /**
     * Deserializes an object from a file.
     * <p>
     * Returns {@code null} if the file does not exist, is empty, or cannot
     * be deserialized.
     * </p>
     *
     * @param file the file to deserialize from
     * @param type the expected object type
     * @param <T> the type of the object
     * @return the deserialized object, or {@code null} if deserialization fails
     * @throws IOException if reading from the file fails
     * @throws ClassNotFoundException if the class of the serialized object cannot be found
     */
    public static <T extends Serializable> T deserialize(File file, Class<T> type) throws IOException, ClassNotFoundException {
        if (file == null || !file.exists() || file.length() == 0) {
            return null;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            Object obj = ois.readObject();
            if (!type.isInstance(obj)) {
                throw new IOException("Deserialized object is not of expected type: " + type.getName());
            }
            return type.cast(obj);
        }
    }
}
