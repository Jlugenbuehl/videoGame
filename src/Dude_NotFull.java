import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Dude_NotFull extends Dude{
    public Dude_NotFull(String id, Point position,List<PImage> images, double animationPeriod, double actionPeriod,PathingStrategy strategy, int resourceLimit, int resourceCount){
        super(id, position, images, animationPeriod, actionPeriod,strategy, resourceLimit, resourceCount);
    }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = world.findNearest( super.getPosition(), new ArrayList<>(Arrays.asList(Tree.class)));

        if (target.isEmpty() || !this.moveToNotFull(world,  (Plant)target.get(), scheduler) || !this.transformNotFull( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), super.getActionPeriod());
        }
    }
    public  boolean moveToNotFull( WorldModel world, Plant target, EventScheduler scheduler) {
        if (this.getPosition().adjacent( target.getPosition())) {
            super.setResourceCount(super.getResourceCount()+1);
            ((Plant)target).setHealth(((Plant)target).getHealth()-1);
            return true;
        } else {
            Point nextPos = this.nextPositionDude( world, target.getPosition());

            if (!this.getPosition().equals(nextPos)) {
                world.moveEntity( scheduler, this, nextPos);
            }
            return false;
        }
    }
    public boolean transformNotFull( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (super.getResourceCount() >= super.getResourceLimit()) {
            Dude_Full dude = new Dude_Full(super.getId(), super.getPosition(), super.getImages(), super.getAnimationPeriod(),super.getActionPeriod(), super.getStrategy(), super.getResourceLimit(), super.getResourceCount());

            world.removeEntity( scheduler, this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity( dude);
            dude.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

}


