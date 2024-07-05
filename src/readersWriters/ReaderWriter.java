package readersWriters;

import interfaces.IReadableWritable;

/**
 * Abstract class defining operations for reading from and writing to a file for objects of type T.
 * @param <T> The type of objects that this class can read and write, must implement Comparable<T>.
 */
public abstract class ReaderWriter<T extends Comparable<T>> implements IReadableWritable<T> {
}