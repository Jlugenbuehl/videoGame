import java.util.List;

import processing.core.PImage;

//import static Functions.*;
//import static Functions.TREE_KEY;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public class Entity {
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private String id;
//    private int resourceLimit;
//    private int resourceCount;
//    private double actionPeriod;
//    private double animationPeriod;
//    private int health;
//    private int healthLimit;
    public Entity(){
        this.id=null;
        this.position=null;
        this.images=null;
        this.imageIndex=0;

    }

    public Entity( String id, Point position,
                   List<PImage> images)
    {
        this.id=id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;

    }

    /**
     * Helper method for testing. Preserve this functionality while refactoring.
     */
    public String log(){
        return this.id.isEmpty() ? null :
                String.format("%s %d %d %d", this.id, this.position.x, this.position.y, this.imageIndex);
    }

//    public double getAnimationPeriod() {
//        switch (this.kind) {
//            case DUDE_FULL:
//            case DUDE_NOT_FULL:
//            case OBSTACLE:
//            case FAIRY:
//            case SAPLING:
//            case TREE:
//                return this.animationPeriod;
//            default:
//                throw new UnsupportedOperationException(String.format("getAnimationPeriod not supported for %s", this.getKind()));
//        }
//    }

    public void nextImage() {
        this.imageIndex=(this.imageIndex + 1);
    }


//    public boolean moveToFairy( WorldModel world, Entity target, EventScheduler scheduler) {
//        if (Point.adjacent(this.position, target.position)) {
//            world.removeEntity( scheduler, target);
//            return true;
//        } else {
//            Point nextPos = nextPositionFairy( world, target.position);
//
//            if (!this.position.equals(nextPos)) {
//                world.moveEntity( scheduler, this, nextPos);
//            }
//            return false;
//        }
//    }

//    public  boolean moveToNotFull( WorldModel world, Entity target, EventScheduler scheduler) {
//        if (Point.adjacent(this.position, target.position)) {
//            this.resourceCount=(this.resourceCount + 1);
//            target.health=(target.health - 1);
//            return true;
//        } else {
//            Point nextPos = this.nextPositionDude( world, target.position);
//
//            if (!this.position.equals(nextPos)) {
//                world.moveEntity( scheduler, this, nextPos);
//            }
//            return false;
//        }
//    }
//
//    public  boolean moveToFull( WorldModel world, Entity target, EventScheduler scheduler) {
//        if (Point.adjacent(this.position, target.position)) {
//            return true;
//        } else {
//            Point nextPos = this.nextPositionDude( world, target.position);
//
//            if (!this.position.equals(nextPos)) {
//                world.moveEntity( scheduler, this, nextPos);
//            }
//            return false;
//        }
//    }
//
//    public  Point nextPositionFairy( WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.position.x);
//        Point newPos = new Point(this.position.x + horiz, this.position.y);
//
//        if (horiz == 0 || world.isOccupied( newPos)) {
//            int vert = Integer.signum(destPos.y - this.position.y);
//            newPos = new Point(this.position.x, this.position.y + vert);
//
//            if (vert == 0 || world.isOccupied( newPos)) {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;
//    }
//
//    public  Point nextPositionDude( WorldModel world, Point destPos) {
//        int horiz = Integer.signum(destPos.x - this.position.x);
//        Point newPos = new Point(this.position.x + horiz, this.position.y);
//
//        if (horiz == 0 || world.isOccupied( newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
//            int vert = Integer.signum(destPos.y - this.position.y);
//            newPos = new Point(this.position.x, this.position.y + vert);
//
//            if (vert == 0 || world.isOccupied( newPos) && world.getOccupancyCell( newPos).getKind() != EntityKind.STUMP) {
//                newPos = this.position;
//            }
//        }
//
//        return newPos;
//    }

    public PImage getCurrentImage() {
        return this.getImages().get(this.imageIndex % this.getImages().size());
    }

public void setId(String id){
        this.id=id;
}
    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }



//    public int getHealth() {
//        return health;
//    }


//    public Action createAnimationAction( int repeatCount) {
//        return new Animation( this, null, null, repeatCount);
//    }
//
//    public Action createActivityAction( WorldModel world, ImageStore imageStore) {
//        return new Activity( this, world, imageStore, 0);
//    }
//
//    public  void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
//
////            case DUDE_FULL:
//                scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent( this, this.createAnimationAction( 0), this.animationPeriod);}
//                break;
//
//            case DUDE_NOT_FULL:
//                scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent( this, this.createAnimationAction( 0), this.animationPeriod);
//                break;
//
//            case OBSTACLE:
//                scheduler.scheduleEvent( this, this.createAnimationAction( 0), this.animationPeriod);
//                break;
//
//            case FAIRY:
//                scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent( this, this.createAnimationAction( 0), this.animationPeriod);
//                break;
//
//            case SAPLING:
//                scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent( this, this.createAnimationAction( 0), this.animationPeriod);
//                break;
//
//            case TREE:
//                scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//                scheduler.scheduleEvent( this, this.createAnimationAction( 0), this.animationPeriod);
//                break;
//



    public List<PImage> getImages() {
        return images;
    }

    public void setImages(List<PImage> images) {
        this.images = images;
    }




//    public boolean transformNotFull( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this.resourceCount >= this.resourceLimit) {
//            Entity dude = world.createDudeFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);
//
//            world.removeEntity( scheduler, this);
//            scheduler.unscheduleAllEvents(this);
//
//            world.addEntity( dude);
//            dude.scheduleActions(scheduler, world, imageStore);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    public void transformFull( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        Entity dude = world.createDudeNotFull(this.id, this.position, this.actionPeriod, this.animationPeriod, this.resourceLimit, this.images);
//
//        world.removeEntity( scheduler, this);
//
//        world.addEntity( dude);
//        dude.scheduleActions(scheduler, world, imageStore);
//    }
//
//
//    public boolean transformPlant( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this.kind == EntityKind.TREE) {
//            return transformTree( world, scheduler, imageStore);
//        } else if (this.kind == EntityKind.SAPLING) {
//            return transformSapling( world, scheduler, imageStore);
//        } else {
//            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", this));
//        }
//    }
//
//    public boolean transformTree( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this.health <= 0) {
//            Entity stump = world.createStump(Functions.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList( Functions.STUMP_KEY));
//
//            world.removeEntity( scheduler, this);
//
//            world.addEntity( stump);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
//        if (this.health <= 0) {
//            Entity stump = world.createStump(Functions.STUMP_KEY + "_" + this.id, this.position, imageStore.getImageList( Functions.STUMP_KEY));
//
//            world.removeEntity( scheduler, this);
//
//            world.addEntity( stump);
//
//            return true;
//        } else if (this.health >= this.healthLimit) {
//            Entity tree = world.createTree(Functions.TREE_KEY + "_" + this.id, this.position, Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN), Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN), Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN), imageStore.getImageList( Functions.TREE_KEY));
//
//            world.removeEntity( scheduler, this);
//
//            world.addEntity(tree);
//            tree.scheduleActions( scheduler, world, imageStore);
//
//            return true;
//        }
//
//        return false;
//    }
//    public void executeSaplingActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        this.health=( this.health + 1);
//        if (!this.transformPlant( world, scheduler, imageStore)) {
//            scheduler.scheduleEvent(this, this.createActivityAction( world, imageStore), this.actionPeriod);
//        }
//    }
//
//    public void executeTreeActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//
//        if (!this.transformPlant( world, scheduler, imageStore)) {
//
//            scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//        }
//    }
//
//    public void executeFairyActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Entity> fairyTarget = world.findNearest( this.position, new ArrayList<>(List.of(EntityKind.STUMP)));
//
//        if (fairyTarget.isPresent()) {
//            Point tgtPos = fairyTarget.get().getPosition();
//
//            if (this.moveToFairy( world, fairyTarget.get(), scheduler)) {
//
//                Entity sapling = world.createSapling(Functions.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList( Functions.SAPLING_KEY), 0);
//
//                world.addEntity( sapling);
//                sapling.scheduleActions(scheduler, world, imageStore);
//            }
//        }
//
//        scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//    }
//
//    public void executeDudeNotFullActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Entity> target = world.findNearest( this.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));
//
//        if (target.isEmpty() || !this.moveToNotFull(world,  target.get(), scheduler) || !this.transformNotFull( world, scheduler, imageStore)) {
//            scheduler.scheduleEvent(this, this.createActivityAction( world, imageStore), this.actionPeriod);
//        }
//    }
//
//    public void executeDudeFullActivity( WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        Optional<Entity> fullTarget = world.findNearest( this.position, new ArrayList<>(List.of(EntityKind.HOUSE)));
//
//        if (fullTarget.isPresent() && this.moveToFull( world, fullTarget.get(), scheduler)) {
//            this.transformFull( world, scheduler, imageStore);
//        } else {
//            scheduler.scheduleEvent( this, this.createActivityAction( world, imageStore), this.actionPeriod);
//        }
//    }
}



