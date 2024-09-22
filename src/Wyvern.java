import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Wyvern extends ActiveEntities{
    private PathingStrategy strategy;
    public static final PathingStrategy Wyvern_PATHING=new AStarPathingStrategy();

    public Wyvern(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, PathingStrategy strategy){
        super(id, position, images, animationPeriod, actionPeriod);
        this.strategy=strategy;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> dudeTarget = world.findNearest( this.getPosition(), new ArrayList<>(List.of(Dude_NotFull.class, Dude_Full.class)));
        Point position= dudeTarget.get().getPosition();
        if(dudeTarget.isPresent()&&this.moveToDude( world, dudeTarget.get(), scheduler)){
            world.tryAddEntity(new Grave("grave", position, imageStore.getImageList("grave")));
        }

            scheduler.scheduleEvent( this, new Activity(this, world, imageStore), super.getActionPeriod());

    }
    public Point nextPositionWyvern(WorldModel world, Point destPos) {
        System.out.println(this.getPosition());
        System.out.println(destPos);

        List<Point> path = Wyvern_PATHING.computePath(this.getPosition(), destPos,
                p -> world.withinBounds(p)&& !world.isOccupied(p), //canPassTrough
                Point::adjacent, //withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.size() == 0){//NOTHING GETS ADDED TO THE PATH ONLY FOR THE WYVERN
            return this.getPosition();}
        else
            return path.get(0);
    }
    public boolean moveToDude(WorldModel world, Entity target, EventScheduler scheduler){
        if (this.getPosition().adjacent( target.getPosition())) {
            world.removeEntity( scheduler, target);
            return true;
        } else {
            Point nextPos = nextPositionWyvern( world, target.getPosition()); //this method runs but only returns the current position
            if (!this.getPosition().equals(nextPos)) {//next pos equals the current position which means the next position isn't updating
                System.out.println("f");
                world.moveEntity( scheduler, this, nextPos); //not reaching to here
            }
            return false;
        }
    }
}
