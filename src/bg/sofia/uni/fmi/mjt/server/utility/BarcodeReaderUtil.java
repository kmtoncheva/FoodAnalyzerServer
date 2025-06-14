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
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.IMAGE_NOT_FOUND_MSG;
import static bg.sofia.uni.fmi.mjt.server.constants.ServerMessagesConstants.NO_BARCODE_FOUND_ERROR_MSG;

public final class BarcodeReaderUtil {
    public static String readImage(String imagePath) throws BarcodeReaderException {
        try {
            File file = new File(imagePath);
            if (!file.exists()) {
                throw new BarcodeReaderException(IMAGE_NOT_FOUND_MSG + imagePath);
            }

            BufferedImage bufferedImage = ImageIO.read(file);
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
            throw new BarcodeReaderException(DECODING_IMAGE_ERROR_MSG + imagePath, e);
        }
    }

    private BarcodeReaderUtil() {
    }
}
