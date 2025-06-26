package bg.sofia.uni.fmi.mjt.server.dto.model;

/**
 * A Data Transfer Object (DTO) representing the parameters required for processing
 * a barcode-related request, for identifying a food item by scanning its barcode.
 *
 * @param code      the barcode value (usually a UPC or EAN code) extracted or provided for the food item
 * @param imagePath the file system path to the image containing the barcode
 */
public record BarcodeDto(String code, String imagePath) {
}