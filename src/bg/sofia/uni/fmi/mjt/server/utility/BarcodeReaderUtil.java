package bg.sofia.uni.fmi.mjt.server.utility;

import bg.sofia.uni.fmi.mjt.server.exceptions.BarcodeReaderException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.DECODING_IMAGE_ERROR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NO_BARCODE_FOUND_ERROR_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.TRY_AGAIN_LATER_MSG;

/**
 * Utility class for reading barcodes from image files using the ZXing library.
 */
public final class BarcodeReaderUtil {
    /**
     * Decodes and returns the textual data encoded in a barcode from the specified image file path.
     *
     * @param imageFile the file containing the barcode.
     * @return the text encoded in the barcode, if successfully decoded.
     * @throws BarcodeReaderException if the image does not exist, if no barcode is found,
     *                                or if an error occurs during decoding.
     */
    public static String readBarcodeFromFile(File imageFile) throws BarcodeReaderException {
        try {
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
            Result result = new MultiFormatReader().decode(bitmap, hints);

            return result.getText();

        } catch (NotFoundException e) {
            throw new BarcodeReaderException(NO_BARCODE_FOUND_ERROR_MSG, e);
        } catch (Exception e) {
            throw new BarcodeReaderException(DECODING_IMAGE_ERROR_MSG + TRY_AGAIN_LATER_MSG, e);
        }
    }

    private BarcodeReaderUtil() {
    }
}