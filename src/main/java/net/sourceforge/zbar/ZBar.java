package net.sourceforge.zbar;

import org.example.NativeUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.util.Iterator;

public class ZBar {
    private final static boolean enabled;

    static { // static initializer
        enabled = loadLibrary();
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static Scan scan(BufferedImage bufferedImage) {
        BufferedImage grayscale = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2d = (Graphics2D)grayscale.getGraphics();
        g2d.drawImage(bufferedImage, 0, 0, null);
        g2d.dispose();

        byte[] data = convertToY800(grayscale);

        try(Image image = new Image()) {
            image.setSize(grayscale.getWidth(), grayscale.getHeight());
            image.setFormat("Y800");
            image.setData(data);

            try(ImageScanner scanner = new ImageScanner()) {
                scanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
                scanner.setConfig(Symbol.QRCODE, Config.ENABLE, 1);
                int result = scanner.scanImage(image);
                if(result != 0) {
                    try(SymbolSet results = scanner.getResults()) {
                        Scan scan = null;
                        for(Iterator<Symbol> iter = results.iterator(); iter.hasNext(); ) {
                            try(Symbol symbol = iter.next()) {
                                scan = new Scan(symbol.getDataBytes(), symbol.getData());
                            }
                        }
                        return scan;
                    }
                }
            }
        }

        return null;
    }

    private static byte[] convertToY800(BufferedImage image) {
        // Ensure the image is grayscale
        if (image.getType() != BufferedImage.TYPE_BYTE_GRAY) {
            throw new IllegalArgumentException("Input image must be grayscale");
        }

        // Get the underlying byte array of the image data
        byte[] imageData = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();

        // Check if the image size is even
        int width = image.getWidth();
        int height = image.getHeight();
        if (width % 2 != 0 || height % 2 != 0) {
            throw new IllegalArgumentException("Image dimensions must be even");
        }

        // Prepare the output byte array in Y800 format
        byte[] outputData = new byte[width * height];
        int outputIndex = 0;

        // Convert the grayscale image data to Y800 format
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = imageData[y * width + x] & 0xFF; // Extract the grayscale value

                // Write the grayscale value to the output byte array
                outputData[outputIndex++] = (byte) pixel;
            }
        }

        return outputData;
    }

    private static boolean loadLibrary() {
        try {
            String osName = System.getProperty("os.name");
            String osArch = System.getProperty("os.arch");
            if(osName.startsWith("Mac") && osArch.equals("aarch64")) {
                NativeUtils.loadLibraryFromJar("/native/osx/aarch64/libzbar.dylib");
            } else if(osName.startsWith("Mac")) {
                NativeUtils.loadLibraryFromJar("/native/osx/x64/libzbar.dylib");
            } else if(osName.startsWith("Windows")) {
                NativeUtils.loadLibraryFromJar("/native/windows/x64/libzbar-0.dll");
                NativeUtils.loadLibraryFromJar("/native/windows/x64/libzbarjni-0.dll");
            } else if(osArch.equals("aarch64")) {
                NativeUtils.loadLibraryFromJar("/native/linux/aarch64/libzbar.so");
                NativeUtils.loadLibraryFromJar("/native/linux/aarch64/libzbarjni.so");
            } else {
                NativeUtils.loadLibraryFromJar("/native/linux/x64/libzbar.so");
                NativeUtils.loadLibraryFromJar("/native/linux/x64/libzbarjni.so");
            }

            return true;
        } catch(IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public record Scan(byte[] rawData, String stringData) {}
}
