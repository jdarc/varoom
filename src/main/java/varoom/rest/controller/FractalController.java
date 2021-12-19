package varoom.rest.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.http.simple.SimpleHttpResponseFactory;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller("/api/v1")
public class FractalController {

    @Get("/mandelbrot")
    @Produces(MediaType.IMAGE_PNG)
    public byte[] generate(
            @QueryValue(defaultValue = "2.68254503e-1") double real,
            @QueryValue(defaultValue = "3.62449166e-3") double imag,
            @QueryValue(defaultValue = "5.473821e-8") double radius,
            @QueryValue(defaultValue = "500") int max)
            throws IOException {
        var mandelbrot = new Mandelbrot(1920, 1080);
        var aspectRatio = (double) mandelbrot.height / (double) mandelbrot.width;
        var x1 = real - radius;
        var x2 = real + radius;
        var y1 = imag + radius * aspectRatio;
        var y2 = imag - radius * aspectRatio;
        return imageToPng(mandelbrot.generate(x1, y1, x2, y2, max));
    }

    private static byte[] imageToPng(BufferedImage image) throws IOException {
        var bites = new ByteArrayOutputStream();
        var stream = new MemoryCacheImageOutputStream(bites);
        ImageIO.write(image, "png", stream);
        stream.close();
        return bites.toByteArray();
    }
}
