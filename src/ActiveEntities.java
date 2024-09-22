import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntities extends AnimatedEntities{

    private double actionPeriod;

    public ActiveEntities(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod) {
        super(id, position, images, animationPeriod);
        this.setActionPeriod(actionPeriod);
    }

    public  void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        super.scheduleActions(scheduler, world, imageStore);
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());

    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);



    public double getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(double actionPeriod) {
        this.actionPeriod = actionPeriod;
    }


//    protected Point nextPosition(WorldModel world, Point destPos){
//        List<Point> path = this.strategy.computePath(getPosition(), destPos,
//                p -> world.withinBounds(p)&& !world.isOccupied(p), //canPassTrough
//                Point::adjacent, //withinReach
//                PathingStrategy.CARDINAL_NEIGHBORS);
//        if (path.size() == 0)
//            return this.getPosition();
//        else
//            return path.get(0);
//
//    }
}
