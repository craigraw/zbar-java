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

        BufferedImage multiImage = ImageIO.read(new File("multi.png"));
        if(ZBar.isEnabled()) {
            long start = System.currentTimeMillis();
            ZBar.Scan scan = ZBar.scan(multiImage);
            System.out.println(System.currentTimeMillis() - start);
            if(scan != null) {
                System.out.println(scan.stringData());
            }
        }

        BufferedImage scannedImage = ImageIO.read(new File("image28-sharpened.png"));
        if(ZBar.isEnabled()) {
            long start = System.currentTimeMillis();
            ZBar.Scan scan = ZBar.scan(scannedImage);
            System.out.println(System.currentTimeMillis() - start);
            if(scan != null) {
                System.out.println(scan.stringData());
            }
        }

        BufferedImage cveImage = ImageIO.read(new File("cve-test.png"));
        if(ZBar.isEnabled()) {
            long start = System.currentTimeMillis();
            ZBar.Scan scan = ZBar.scan(cveImage);
            System.out.println(System.currentTimeMillis() - start);
            if(scan != null) {
                System.out.println(scan.stringData());
            }
        }

//        BufferedImage invertedImage = ImageIO.read(new File("crash.png"));
//        if(ZBar.isEnabled()) {
//            long start = System.currentTimeMillis();
//            ZBar.Scan scan = ZBar.scan(invertedImage);
//            System.out.println(System.currentTimeMillis() - start);
//            if(scan != null) {
//                System.out.println(scan.stringData());
//            }
//        }
    }
}