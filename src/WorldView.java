import processing.core.PApplet;
import processing.core.PImage;

import java.util.Optional;

public final class WorldView {
    private PApplet screen;
    private WorldModel world;
    private int tileWidth;
    private int tileHeight;
    private Viewport viewport;

    public WorldView(int numRows, int numCols, PApplet screen, WorldModel world, int tileWidth, int tileHeight) {
        this.screen = screen;
        this.world = world;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.viewport = new Viewport(numRows, numCols);
    }
    public  void drawBackground() {
        for (int row = 0; row < this.viewport.getNumRows(); row++) {
            for (int col = 0; col < this.viewport.getNumCols(); col++) {
                Point worldPoint = this.viewport.viewportToWorld( col, row);
                Optional<PImage> image = this.world.getBackgroundImage( worldPoint);
                if (image.isPresent()) {
                    this.screen.image(image.get(), col * this.tileWidth, row * this.tileHeight);
                }
            }
        }
    }

    public void drawEntities() {
        for (Entity entity : this.world.getEntities()) {
            Point pos = entity.getPosition();

            if (this.contains(pos)) {
                Point viewPoint = this.viewport.worldToViewport( pos.x, pos.y);
                this.screen.image(entity.getCurrentImage(), viewPoint.x * this.tileWidth, viewPoint.y * this.tileHeight);
            }
        }
    }

    public  void drawViewport() {
        this.drawBackground();
        this.drawEntities();
    }

    public void shiftView( int colDelta, int rowDelta) {
        int newCol = this.clamp(this.getViewport().getCol() + colDelta, 0, this.world.getNumCols() - this.getViewport().getNumCols());
        int newRow = this.clamp(this.getViewport().getRow() + rowDelta, 0, this.world.getNumRows() - this.getViewport().getNumRows());

        this.getViewport().shift( newCol, newRow);
    }

    public Viewport getViewport() {
        return viewport;
    }
    public int clamp(int value, int low, int high) {
        return Math.min(high, Math.max(value, low));
    }

    public boolean contains( Point p) {
        return p.y >= this.getViewport().getRow() && p.y < this.getViewport().getRow() + this.getViewport().getNumRows() && p.x >= this.getViewport().getCol() && p.x < this.getViewport().getCol() + this.getViewport().getNumCols();
    }
    public void setBackgroundImage(Point pos, PImage image){
        this.screen.image(image,pos.y*this.tileWidth,pos.x*this.tileWidth);
    }



}
