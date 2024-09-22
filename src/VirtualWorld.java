import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import processing.core.*;

public final class VirtualWorld extends PApplet {
    private static String[] ARGS;

    private static final int VIEW_WIDTH = 640;
    private static final int VIEW_HEIGHT = 480;
    private static final int TILE_WIDTH = 32;
    private static final int TILE_HEIGHT = 32;

    private static final int VIEW_COLS = getViewWidth() / getTileWidth();
    private static final int VIEW_ROWS = getViewHeight() / getTileHeight();

    private static final String IMAGE_LIST_FILE_NAME = "imagelist";
    private static final String DEFAULT_IMAGE_NAME = "background_default";
    private static final int DEFAULT_IMAGE_COLOR = 0x808080;

    private static final String FAST_FLAG = "-fast";
    private static final String FASTER_FLAG = "-faster";
    private static final String FASTEST_FLAG = "-fastest";
    private static final double FAST_SCALE = 0.5;
    private static final double FASTER_SCALE = 0.25;
    private static final double FASTEST_SCALE = 0.10;

    private String loadFile = "world.sav";
    private long startTimeMillis = 0;
    private double timeScale = 1.0;

    private ImageStore imageStore;
    private WorldModel world;
    private WorldView view;
    private EventScheduler scheduler;

    public static int getViewWidth() {
        return VIEW_WIDTH;
    }

    public static int getViewHeight() {
        return VIEW_HEIGHT;
    }

    public static int getTileWidth() {
        return TILE_WIDTH;
    }

    public static int getTileHeight() {
        return TILE_HEIGHT;
    }

    public  int getViewCols() {
        return VIEW_COLS;
    }

    public  int getViewRows() {
        return VIEW_ROWS;
    }

    public  String getImageListFileName() {
        return IMAGE_LIST_FILE_NAME;
    }

    public  String getDefaultImageName() {
        return DEFAULT_IMAGE_NAME;
    }

    public  int getDefaultImageColor() {
        return DEFAULT_IMAGE_COLOR;
    }

    public  double getFastScale() {
        return FAST_SCALE;
    }

    public  double getFasterScale() {
        return FASTER_SCALE;
    }

    public  double getFastestScale() {
        return FASTEST_SCALE;
    }

    public void settings() {
        size(getViewWidth(), getViewHeight());
    }

    /*
       Processing entry point for "sketch" setup.
    */
    public void setup() {
        this.parseCommandLine(ARGS);
        this.loadImages(getImageListFileName());
        this.loadWorld(this.loadFile, this.imageStore);

        this.view=(new WorldView(getViewRows(), getViewCols(), this, this.world, getTileWidth(), getTileHeight()));
        this.scheduler=(new EventScheduler());
        this.startTimeMillis=(System.currentTimeMillis());
        this.scheduleActions( this.world,this.scheduler,this.imageStore);
    }


    public void draw() {
        double appTime = (System.currentTimeMillis() - this.startTimeMillis) * 0.001;
        double frameTime = (appTime - this.scheduler.getCurrentTime())/ this.timeScale;
        this.update(frameTime);
        this.view.drawViewport();
    }

    public void update(double frameTime){
        this.scheduler.updateOnTime( frameTime);
    }

    // Just for debugging and for P5
    // Be sure to refactor this method as appropriate
    public void mousePressed() {
        Point pressed = mouseToPoint();
        System.out.println("CLICK! " + pressed.x + ", " + pressed.y);
        List<PImage> images= imageStore.getImageList("flowers");
        List<PImage> wImages = imageStore.getImageList("wyvern");
//        Scanner saveFile = new Scanner(this.loadFile);
//        String line = saveFile.nextLine().strip();
//        String[] properties = line.split(" ", Functions.ENTITY_NUM_PROPERTIES + 1);
        Optional<Entity> entityOptional = this.world.getOccupant( pressed);
        if (entityOptional.isPresent()) {
            Entity entity = entityOptional.get();
            if(entity.getClass().equals(Dude_Full.class)||entity.getClass().equals(Dude_NotFull.class)){
            world.removeEntityAt(pressed);
            world.tryAddEntity(new Grave("grave", pressed, imageStore.getImageList("grave")));}
            else if(entity.getClass().equals(Grave.class)){
                world.removeEntityAt(pressed);
                Dude_NotFull dude = new Dude_NotFull("dude", pressed, imageStore.getImageList( Functions.DUDE_KEY), 0.18,
                        0.78,Dude.Dude_PATHING, 5,0);
                world.tryAddEntity(dude);
                dude.scheduleActions(scheduler,world,imageStore);
            }
        }
        else {
            /*when mouse is pressed, spawns in a wyvern in the middle of a plus shape of flowers. Then, the
             * wyvern seeks out dudes and turns them into gravestones. If you press on a dude, the dude will
             * also turn into a graavestone. But if you press on a gravestone then it will turn into a dude_notFull again */
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x, pressed.y), images));
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x + 1, pressed.y), images));
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x - 1, pressed.y), images));
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x, pressed.y - 1), images));
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x-1, pressed.y + 1), images));
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x+1, pressed.y + 1), images));
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x-1, pressed.y - 1), images));
            world.tryAddEntity(new Flower("flowers", new Point(pressed.x+1, pressed.y - 1), images));



            Wyvern wyvern = new Wyvern("wyvern", pressed, wImages, 0.18, 0.75, Wyvern.Wyvern_PATHING);
        world.tryAddEntity(wyvern);
        wyvern.scheduleActions(scheduler, world, imageStore);
        }
    }


    private Point mouseToPoint() {
        return this.view.getViewport().viewportToWorld( mouseX / getTileWidth(), mouseY / getTileHeight());
    }

    public void keyPressed() {
        if (key == CODED) {
            int dx = 0;
            int dy = 0;

            switch (keyCode) {
                case UP -> dy -= 1;
                case DOWN -> dy += 1;
                case LEFT -> dx -= 1;
                case RIGHT -> dx += 1;
            }
            this.view.shiftView( dx, dy);
        }
    }

    public  Background createDefaultBackground(ImageStore imageStore) {
        return new Background(getDefaultImageName(), imageStore.getImageList(getDefaultImageName()));
    }

    public  PImage createImageColored(int width, int height, int color) {
        PImage img = new PImage(width, height, RGB);
        img.loadPixels();
        Arrays.fill(img.pixels, color);
        img.updatePixels();
        return img;
    }

    public void loadImages(String filename) {
        this.imageStore=(new ImageStore(createImageColored(getTileWidth(), getTileHeight(), getDefaultImageColor())));
        try {
            Scanner in = new Scanner(new File(filename));
            this.imageStore.loadImages(in,this);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }

    public void loadWorld(String file, ImageStore imageStore) {
        this.world=(new WorldModel());
        try {
            Scanner in = new Scanner(new File(file));
            this.world.load( in, imageStore, createDefaultBackground(imageStore));
        } catch (FileNotFoundException e) {
            Scanner in = new Scanner(file);
            this.world.load( in, imageStore, createDefaultBackground(imageStore));
        }
    }

    public void parseCommandLine(String[] args) {
        for (String arg : args) {
            switch (arg) {
                case FAST_FLAG -> this.timeScale=(Math.min(getFastScale(), this.timeScale));
                case FASTER_FLAG -> this.timeScale=(Math.min(getFasterScale(), this.timeScale));
                case FASTEST_FLAG -> this.timeScale=(Math.min(getFastestScale(), this.timeScale));
                default -> this.loadFile=arg;
            }
        }
    }

    public static void main(String[] args) {
        VirtualWorld.ARGS = args;
        PApplet.main(VirtualWorld.class);
    }

    public static List<String> headlessMain(String[] args, double lifetime){
        VirtualWorld.ARGS = args;

        VirtualWorld virtualWorld = new VirtualWorld();
        virtualWorld.setup();
        virtualWorld.update(lifetime);

        return virtualWorld.world.log();
    }


    public  void scheduleActions(WorldModel world, EventScheduler scheduler, ImageStore image){
        for(Entity entity: world.getEntities()){
            if (entity instanceof AnimatedEntities animatedEntities){
                animatedEntities.scheduleActions(scheduler,world,image);
            }
    }
}}
