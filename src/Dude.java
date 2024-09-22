import processing.core.PImage;

import java.util.List;

public abstract class Dude extends ActiveEntities {
    private int resourceLimit;
    private int resourceCount;
    private PathingStrategy strategy;
    public static final PathingStrategy Dude_PATHING=new AStarPathingStrategy();

    public Dude(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, PathingStrategy strategy, int resourceLimit, int resourceCount) {
        super(id, position, images, animationPeriod, actionPeriod);
        this.resourceLimit=resourceLimit;
        this.resourceCount=resourceCount;
        this.setStrategy(strategy);
    }

    public Point nextPositionDude(WorldModel world, Point destPos) {
        List<Point> path = new AStarPathingStrategy().computePath(getPosition(), destPos,
                p -> world.withinBounds(p)&& !world.isOccupied(p), //canPassTrough
                Point::adjacent, //withinReach
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (path.size() == 0)
            return this.getPosition();
        else
            return path.get(0);
    }


    public int getResourceLimit() {
        return resourceLimit;
    }


    public int getResourceCount() {
        return resourceCount;
    }

    public void setResourceCount(int resourceCount) {
        this.resourceCount = resourceCount;
    }

    public PathingStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PathingStrategy strategy) {
        this.strategy = strategy;
    }
}
