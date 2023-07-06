package org.example;

import net.sourceforge.zbar.ZBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");

        BufferedImage bufferedImage = ImageIO.read(new File("screenshot.png"));

        if(ZBar.isEnabled()) {
            long start = System.currentTimeMillis();
            ZBar.Scan scan = ZBar.scan(bufferedImage);
            System.out.println(System.currentTimeMillis() - start);
            if(scan != null) {
                System.out.println(scan.stringData());
            }
        }

        if(ZBar.isEnabled()) {
            long start = System.currentTimeMillis();
            ZBar.Scan scan = ZBar.scan(bufferedImage);
            System.out.println(System.currentTimeMillis() - start);
            if(scan != null) {
                System.out.println(scan.stringData());
            }
        }
    }
}