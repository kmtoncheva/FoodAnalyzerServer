package bg.sofia.uni.fmi.mjt.server.exceptions;

/**
 * Exception thrown to indicate an error occurred during barcode reading or processing.
 * <p>
 * This exception is used when the barcode reader fails to decode an image,
 * encounters an invalid format, or experiences any other issue while handling barcode data.
 */
public class BarcodeReaderException extends Exception {
    public BarcodeReaderException(String message) {
        super(message);
    }

    public BarcodeReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}