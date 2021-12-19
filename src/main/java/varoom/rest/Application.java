package varoom.rest;

import javax.imageio.ImageIO;

import io.micronaut.runtime.Micronaut;

public class Application {

    public static void main(String[] args) {
        ImageIO.setUseCache(false);
        Micronaut.run(Application.class, args);
    }
}
