package bg.sofia.uni.fmi.mjt.server.exceptions;

public class BarcodeReaderException extends Exception {
    public BarcodeReaderException(String message) {
        super(message);
    }

    public BarcodeReaderException(String message, Throwable cause) {
        super(message, cause);
    }
}