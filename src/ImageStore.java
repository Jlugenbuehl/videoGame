import java.util.*;

import processing.core.PImage;
import processing.core.PApplet;

//import static Functions.*;

public final class ImageStore {
    public Map<String, List<PImage>> images;
    public List<PImage> defaultImages;

    public ImageStore(PImage defaultImage) {
        this.images = new HashMap<>();
        defaultImages = new LinkedList<>();
        defaultImages.add(defaultImage);
    }
    public  List<PImage> getImageList( String key) {
        return this.images.getOrDefault(key, this.defaultImages);
    }
    public  void loadImages(Scanner in,  PApplet screen) {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                processImageLine(this.images, in.nextLine(), screen);
            } catch (NumberFormatException e) {
                System.out.printf("Image format error on line %d\n", lineNumber);
            }
            lineNumber++;
        }
    }

    public  void processImageLine(Map<String, List<PImage>> images, String line, PApplet screen) {
        String[] attrs = line.split("\\s");
        if (attrs.length >= 2) {
            String key = attrs[0];
            PImage img = screen.loadImage(attrs[1]);
            if (img != null && img.width != -1) {
                List<PImage> imgs = getImages(images, key);
                imgs.add(img);
                imgs.stream().forEach(x->System.out.println(x));
                if (attrs.length >= Functions.KEYED_IMAGE_MIN) {
                    int r = Integer.parseInt(attrs[Functions.KEYED_RED_IDX]);
                    int g = Integer.parseInt(attrs[Functions.KEYED_GREEN_IDX]);
                    int b = Integer.parseInt(attrs[Functions.KEYED_BLUE_IDX]);
                    Functions.setAlpha(img, screen.color(r, g, b), 0);
                }
            }
        }
    }
    public List<PImage> getImages(Map<String, List<PImage>> images, String key) {
        return images.computeIfAbsent(key, k -> new LinkedList<>());
    }

}
