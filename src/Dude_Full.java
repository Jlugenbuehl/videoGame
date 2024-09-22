import processing.core.PImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dude_Full extends Dude{

    public Dude_Full(String id, Point position,List<PImage> images, double animationPeriod, double actionPeriod, PathingStrategy strategy, int resourceLimit, int resourceCount){
        super(id, position, images, animationPeriod, actionPeriod,strategy, resourceLimit, resourceCount);
    }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest( super.getPosition(), new ArrayList<>(List.of(House.class)));

        if (fullTarget.isPresent() && this.moveToFull( world, fullTarget.get(), scheduler)) {
            this.transformFull( world, scheduler, imageStore);
        } else {
            scheduler.scheduleEvent( this, new Activity(this, world, imageStore), super.getActionPeriod());
        }
    }
    public void transformFull( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Dude_NotFull dude = new Dude_NotFull(super.getId(), super.getPosition(), super.getImages() , super.getAnimationPeriod(),super.getActionPeriod(), super.getStrategy(), super.getResourceLimit(),0);

        world.removeEntity( scheduler, this);

        world.addEntity( dude);
        dude.scheduleActions(scheduler, world, imageStore);
    }
    public  boolean moveToFull( WorldModel world, Entity target, EventScheduler scheduler) {
        if (this.getPosition().adjacent( target.getPosition())) {
            return true;
        } else {
            Point nextPos = this.nextPositionDude( world, target.getPosition());

            if (!super.getPosition().equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }


}

