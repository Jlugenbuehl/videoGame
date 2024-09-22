import processing.core.PImage;

import java.util.List;

public class Tree extends Plant{

        public Tree(String id, Point position,List<PImage> images, double animationPeriod, double actionPeriod, int health){
            super(id, position, images, animationPeriod, actionPeriod, health);
        }
    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        if (!this.transformTree( world, scheduler, imageStore)) {

            scheduler.scheduleEvent( this, new Activity( this, world, imageStore), this.getActionPeriod());
        }
    }

    public boolean transformTree( WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Stump stump = new Stump(Functions.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList( Functions.STUMP_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity( stump);

            return true;
        }

        return false;
    }


    }

