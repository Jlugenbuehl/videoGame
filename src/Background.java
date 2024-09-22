import java.util.List;

import processing.core.PImage;

/**
 * Represents a background for the 2D world.
 */
public class Background {
    private String id;
    private List<PImage> images;
    private int imageIndex;

    public Background(String id, List<PImage> images) {
        this.setId(id);
        this.setImages(images);
    }


    public PImage getCurrentImage() {
            return this.getImages().get(this.imageIndex);
    }

    public List<PImage> getImages() {
        return images;
    }

    public void setImages(List<PImage> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
