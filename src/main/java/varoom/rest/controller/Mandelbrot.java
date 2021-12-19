package varoom.rest.controller;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.stream.IntStream;

public final class Mandelbrot {

    private static final int[] colors = colormap();

    public final int width;
    public final int height;

    private final BufferedImage image;

    public Mandelbrot(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public BufferedImage generate(double x1, double y1, double x2, double y2, int max) {
        var width = image.getWidth();
        var height = image.getHeight();
        var pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        var h = (x2 - x1) / width;
        var v = (y2 - y1) / height;
        IntStream.range(0, height).parallel().forEach(row -> {
            var offset = row * width;
            var a = y1 + row * v;
            for (var col = 0; col < width; col++) {
                var b = x1 + col * h;
                var x = 0.0;
                var y = 0.0;
                var iteration = 0;
                while (true) {
                    var z = x * x;
                    var w = y * y;
                    if (++iteration >= max || z + w >= 4.0) {
                        break;
                    }
                    x = 2.0 * x * y + a;
                    y = w - z + b;
                }
                pixels[offset + col] = iteration < max ? colors[iteration & 511] : 0;
            }
        });

        return image;
    }

    private static int[] colormap() {
        var colors = new int[512];
        for (var i = 0; i < colors.length; i++) {
            colors[i] = Color.HSBtoRGB(i / 256.0f, 1.0f, i / (i + 8.0f));
        }
        return colors;
    }
}
