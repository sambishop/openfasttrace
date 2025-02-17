package org.itsallcode.openfasttrace.api.importer.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An {@link InputFile} for a file on disk, represented by a {@link Path}.
 */
public class RealFileInput implements InputFile
{
    private final Path path;
    private final Charset charset;

    private RealFileInput(final Path path, final Charset charset)
    {
        this.path = path;
        this.charset = charset;
    }

    /**
     * Create an {@link InputFile} for a real file on disk.
     * {@link StandardCharsets#UTF_8} is used for reading the file.
     * 
     * @param file
     *            a real file on disk.
     * @return an {@link InputFile}.
     */
    public static InputFile forPath(final Path file)
    {
        return forPath(file, StandardCharsets.UTF_8);
    }

    /**
     * Create an {@link InputFile} for a real file on disk.
     * 
     * @param path
     *            path to file on disk.
     * @param charset
     *            the {@link Charset} used when reading this file.
     * @return an {@link InputFile}.
     */
    public static InputFile forPath(final Path path, final Charset charset)
    {
        return new RealFileInput(path, charset);
    }

    @Override
    public BufferedReader createReader() throws IOException
    {
        return Files.newBufferedReader(this.path, this.charset);
    }

    @Override
    public String getPath()
    {
        return this.path.toString();
    }

    @Override
    public String toString()
    {
        return getPath();
    }

    @Override
    public boolean isRealFile()
    {
        return true;
    }

    @Override
    public Path toPath()
    {
        return this.path;
    }
}
