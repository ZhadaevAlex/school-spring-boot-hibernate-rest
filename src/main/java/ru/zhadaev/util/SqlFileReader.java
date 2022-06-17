package ru.zhadaev.util;

import ru.zhadaev.exception.FileReadException;
import ru.zhadaev.exception.IsNotFileException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class SqlFileReader {
    public String read(String fileName) throws NoSuchFileException, IsNotFileException {
        requiredNonNull(fileName);
        requiredExistenceFile(fileName);
        requiredIsFile(fileName);

        String str;
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            str = stream.reduce((s1, s2) -> s1 + s2).orElse("");
        } catch(IOException e) {
            throw new FileReadException(String.format("Error opening the file %s", fileName));
        }

        return str;
    }

    private void requiredNonNull(String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("Non null input fileName required!");
        }
    }

    private void requiredExistenceFile (String fileName) throws NoSuchFileException {
        if (!new File(fileName).exists()) {
            throw new NoSuchFileException(String.format("File %s not found", fileName));
        }
    }

    private void requiredIsFile (String fileName) throws IsNotFileException {
        if (!new File(fileName).isFile()) {
            throw new IsNotFileException(String.format("%s is not file", fileName));
        }
    }
}
