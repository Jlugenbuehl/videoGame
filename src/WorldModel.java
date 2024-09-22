import processing.core.PImage;

import java.util.*;

//import static Functions.*;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    private int numRows;
    private int numCols;
    private Background[][] background;
    private Entity[][] occupancy;
    private Set<Entity> entities;

    public WorldModel() {

    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : this.entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }

//    public  Entity createHouse(String id, Point position, List<PImage> images) {
//        return new Entity(EntityKind.HOUSE, id, position, images, 0, 0, 0, 0, 0, 0);
//    }

//    public  Entity createObstacle(String id, Point position, double animationPeriod, List<PImage> images) {
//        return new Entity(EntityKind.OBSTACLE, id, position, images, 0, 0, 0, animationPeriod, 0, 0);
//    }

//    public  Entity createTree(String id, Point position, double actionPeriod, double animationPeriod, int health, List<PImage> images) {
//        return new Entity(EntityKind.TREE, id, position, images, 0, 0, actionPeriod, animationPeriod, health, 0);
//    }

//    public  Entity createStump(String id, Point position, List<PImage> images) {
//        return new Entity(EntityKind.STUMP, id, position, images, 0, 0, 0, 0, 0, 0);
//    }

    // health starts at 0 and builds up until ready to convert to Tree
//    public  Entity createSapling(String id, Point position, List<PImage> images, int health) {
//        return new Entity(EntityKind.SAPLING, id, position, images, 0, 0, Functions.SAPLING_ACTION_ANIMATION_PERIOD, Functions.SAPLING_ACTION_ANIMATION_PERIOD, health, Functions.SAPLING_HEALTH_LIMIT);
//    }

//    public  Entity createFairy(String id, Point position, double actionPeriod, double animationPeriod, List<PImage> images) {
//        return new Entity(EntityKind.FAIRY, id, position, images, 0, 0, actionPeriod, animationPeriod, 0, 0);
//    }

    // need resource count, though it always starts at 0
//    public  Entity createDudeNotFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
//        return new Entity(EntityKind.DUDE_NOT_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
//    }

    // don't technically need resource count ... full
//    public  Entity createDudeFull(String id, Point position, double actionPeriod, double animationPeriod, int resourceLimit, List<PImage> images) {
//        return new Entity(EntityKind.DUDE_FULL, id, position, images, resourceLimit, 0, actionPeriod, animationPeriod, 0, 0);
//    }
    public  Optional<PImage> getBackgroundImage( Point pos) {
        if (this.withinBounds( pos)) {
            return Optional.of(this.getBackgroundCell(pos).getCurrentImage());
        } else {
            return Optional.empty();
        }
    }

    public Background getBackgroundCell( Point pos) {
        return this.background[pos.y][pos.x];
    }


    public void load( Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        parseSaveFile( saveFile, imageStore, defaultBackground);
        if(this.background == null){
            this.background = new Background[this.numRows][this.numCols];
            for (Background[] row : this.background)
                Arrays.fill(row, defaultBackground);
        }
        if(this.occupancy == null){
            this.occupancy = new Entity[this.numRows][this.numCols];
            this.entities = new HashSet<>();
        }
    }

    public  Optional<Entity> findNearest( Point pos, List<Class<? extends Entity>> kinds) {
        List<Entity> ofType = new LinkedList<>();
        for (Class<? extends Entity> kind : kinds) {
            for (Entity entity : this.entities) {
                if (entity.getClass() == kind) {
                    ofType.add(entity);
                }
            }
        }

        return nearestEntity(ofType, pos);
    }


    public  void addEntity( Entity entity) {
        if (withinBounds( entity.getPosition())) {
            setOccupancyCell( entity.getPosition(), entity);
            this.entities.add(entity);
        }
    }

    public  void moveEntity( EventScheduler scheduler, ActiveEntities entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds( pos) && !pos.equals(oldPos)) {
            this.setOccupancyCell( oldPos, null);
            Optional<Entity> occupant = this.getOccupant( pos);
            occupant.ifPresent(target -> this.removeEntity(scheduler, target));
            this.setOccupancyCell( pos, entity);
            entity.setPosition(pos);
        }
    }

    public  void removeEntity( EventScheduler scheduler, Entity entity) {
        scheduler.unscheduleAllEvents( entity);
        this.removeEntityAt(entity.getPosition());
    }

    public  void removeEntityAt( Point pos) {
        if (this.withinBounds( pos) && this.getOccupancyCell( pos) != null) {
            Entity entity = this.getOccupancyCell( pos);


            entity.setPosition(new Point(-1, -1));
            this.entities.remove(entity);
            this.setOccupancyCell( pos, null);
        }
    }


    public  Optional<Entity> getOccupant( Point pos) {
        if (isOccupied( pos)) {
            return Optional.of(getOccupancyCell( pos));
        } else {
            return Optional.empty();
        }
    }

    public  Entity getOccupancyCell( Point pos) {
        return this.occupancy[pos.y][pos.x];
    }

    public  void setOccupancyCell( Point pos, Entity entity) {
        this.occupancy[pos.y][pos.x] = entity;
    }

    public  void tryAddEntity( Entity entity) {
        if (this.isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            this.removeEntityAt(entity.getPosition());
        }

        this.addEntity( entity);
    }

    public  boolean withinBounds( Point pos) {
        return pos.y >= 0 && pos.y < this.numRows && pos.x >= 0 && pos.x < this.numCols;
    }

    public  boolean isOccupied( Point pos) {
         return this.withinBounds( pos) && this.getOccupancyCell( pos) != null;
    }

    public  Optional<Entity> nearestEntity(List<Entity> entities, Point pos) {
        if (entities.isEmpty()) {
            return Optional.empty();
        } else {
            Entity nearest = entities.get(0);
            int nearestDistance = Point.distanceSquared(nearest.getPosition(), pos);

            for (Entity other : entities) {
                int otherDistance = Point.distanceSquared(other.getPosition(), pos);

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }
    public  void parseSaveFile( Scanner saveFile, ImageStore imageStore, Background defaultBackground){
        String lastHeader = "";
        int headerLine = 0;
        int lineCounter = 0;
        while(saveFile.hasNextLine()){
            lineCounter++;
            String line = saveFile.nextLine().strip();
            if(line.endsWith(":")){
                headerLine = lineCounter;
                lastHeader = line;
                switch (line){
                    case "Backgrounds:" -> this.background = new Background[this.numRows][this.numCols];
                    case "Entities:" -> {
                        this.occupancy = new Entity[this.numRows][this.numCols];
                        this.entities = new HashSet<>();
                    }
                }
            }else{
                switch (lastHeader){
                    case "Rows:" -> this.numRows = Integer.parseInt(line);
                    case "Cols:" -> this.numCols = Integer.parseInt(line);
                    case "Backgrounds:" -> this.parseBackgroundRow( line, lineCounter-headerLine-1, imageStore);
                    case "Entities:" -> this.parseEntity( line, imageStore);
                }
            }
        }
    }
    public  void parseBackgroundRow( String line, int row, ImageStore imageStore) {
        String[] cells = line.split(" ");
        if(row < this.numRows){
            int rows = Math.min(cells.length, this.numCols);
            for (int col = 0; col < rows; col++){
                this.background[row][col] = new Background(cells[col], imageStore.getImageList( cells[col]));
            }
        }
    }

    public  void parseEntity( String line, ImageStore imageStore) {
        String[] properties = line.split(" ", Functions.ENTITY_NUM_PROPERTIES + 1);
        if (properties.length >= Functions.ENTITY_NUM_PROPERTIES) {
            String key = properties[Functions.PROPERTY_KEY];
            String id = properties[Functions.PROPERTY_ID];
            Point pt = new Point(Integer.parseInt(properties[Functions.PROPERTY_COL]), Integer.parseInt(properties[Functions.PROPERTY_ROW]));

            properties = properties.length == Functions.ENTITY_NUM_PROPERTIES ?
                    new String[0] : properties[Functions.ENTITY_NUM_PROPERTIES].split(" ");

            switch (key) {
                case Functions.OBSTACLE_KEY -> this.parseObstacle( properties, pt, id, imageStore);
                case Functions.DUDE_KEY -> this.parseDude( properties, pt, id, imageStore);
                case Functions.FAIRY_KEY -> this.parseFairy( properties, pt, id, imageStore);
                case Functions.HOUSE_KEY -> this.parseHouse( properties, pt, id, imageStore);
                case Functions.TREE_KEY -> this.parseTree( properties, pt, id, imageStore);
                case Functions.SAPLING_KEY -> this.parseSapling( properties, pt, id, imageStore);
                case Functions.STUMP_KEY -> this.parseStump( properties, pt, id, imageStore);
                default -> throw new IllegalArgumentException("Entity key is unknown");
            }
        }else{
            throw new IllegalArgumentException("Entity must be formatted as [key] [id] [x] [y] ...");
        }
    }
    public  void parseSapling( String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[Functions.SAPLING_HEALTH]);
            Sapling entity = new Sapling(id, pt, imageStore.getImageList( Functions.SAPLING_KEY),Functions.SAPLING_ACTION_ANIMATION_PERIOD, Functions.SAPLING_ACTION_ANIMATION_PERIOD, health);
            this.tryAddEntity( entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.SAPLING_KEY, Functions.SAPLING_NUM_PROPERTIES));
        }
    }

    public  void parseDude( String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.DUDE_NUM_PROPERTIES) {
            Dude entity = new Dude_NotFull(id, pt,imageStore.getImageList( Functions.DUDE_KEY), Double.parseDouble(properties[Functions.DUDE_ANIMATION_PERIOD]),Double.parseDouble(properties[Functions.DUDE_ACTION_PERIOD]),  Dude.Dude_PATHING, Integer.parseInt(properties[Functions.DUDE_LIMIT]),0);
            this.tryAddEntity( entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.DUDE_KEY, Functions.DUDE_NUM_PROPERTIES));
        }
    }

    public  void parseFairy( String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.FAIRY_NUM_PROPERTIES) {
            Fairy entity = new Fairy(id, pt, imageStore.getImageList( Functions.FAIRY_KEY),Double.parseDouble(properties[Functions.FAIRY_ANIMATION_PERIOD]),Double.parseDouble(properties[Functions.FAIRY_ACTION_PERIOD]),Fairy.Fairy_PATHING);
            this.tryAddEntity( entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.FAIRY_KEY, Functions.FAIRY_NUM_PROPERTIES));
        }
    }

    public  void parseTree( String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.TREE_NUM_PROPERTIES) {
            Tree entity = new Tree(id, pt, imageStore.getImageList( Functions.TREE_KEY), Double.parseDouble(properties[Functions.TREE_ANIMATION_PERIOD]), Double.parseDouble(properties[Functions.TREE_ACTION_PERIOD]), Integer.parseInt(properties[Functions.TREE_HEALTH]));
            this.tryAddEntity( entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.TREE_KEY, Functions.TREE_NUM_PROPERTIES));
        }
    }

    public  void parseObstacle( String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES) {
            Obstacle entity = new Obstacle(id, pt, imageStore.getImageList( Functions.OBSTACLE_KEY), Double.parseDouble(properties[Functions.OBSTACLE_ANIMATION_PERIOD]));
            this.tryAddEntity(entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.OBSTACLE_KEY, Functions.OBSTACLE_NUM_PROPERTIES));
        }
    }

    public  void parseHouse( String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.HOUSE_NUM_PROPERTIES) {
            House entity = new House(id, pt, imageStore.getImageList( Functions.HOUSE_KEY));
            this.tryAddEntity( entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.HOUSE_KEY, Functions.HOUSE_NUM_PROPERTIES));
        }
    }
    public  void parseStump( String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.STUMP_NUM_PROPERTIES) {
            Stump entity = new Stump(id, pt, imageStore.getImageList( Functions.STUMP_KEY));
            this.tryAddEntity( entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.STUMP_KEY, Functions.STUMP_NUM_PROPERTIES));
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }
}
