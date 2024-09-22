import processing.core.PImage;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Fairy extends ActiveEntities{
    public static final PathingStrategy Fairy_PATHING = new
            AStarPathingStrategy();
    private PathingStrategy strategy;
    public Fairy(String id, Point position,List<PImage> images, double animationPeriod, double actionPeriod, PathingStrategy strategy){
        super(id, position, images, animationPeriod,actionPeriod);
        this.strategy=strategy;

    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = world.findNearest( super.getPosition(), new ArrayList<>(List.of(Stump.class)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (this.moveToFairy( world, fairyTarget.get(), scheduler)) {

                Sapling sapling = new Sapling(Functions.SAPLING_KEY + "_" + fairyTarget.get().getId(), tgtPos, imageStore.getImageList( Functions.SAPLING_KEY),Functions.SAPLING_ACTION_ANIMATION_PERIOD, Functions.SAPLING_ACTION_ANIMATION_PERIOD, 0);

                world.addEntity( sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent( this, new Activity(this, world, imageStore), super.getActionPeriod());

    }

    public boolean moveToFairy( WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent( target.getPosition())) {
            world.removeEntity( scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionFairy( world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }
    public  Point nextPositionFairy( WorldModel world, Point destPos) {
        List<Point> path = new AStarPathingStrategy().computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p)&& !world.isOccupied(p), //canPassTrough
                Point::adjacent, //withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.size() == 0){
            return this.getPosition();}
        else{
            return path.get(0);}
//        int horiz = Integer.signum(destPos.x - this.getPosition().x);
//        Point newPos = new Point(this.getPosition().x + horiz, this.getPosition().y);
//
//        if (horiz == 0 || world.isOccupied( newPos)) {
//            int vert = Integer.signum(destPos.y - this.getPosition().y);
//            newPos = new Point(this.getPosition().x, this.getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied( newPos)) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
    }

}
