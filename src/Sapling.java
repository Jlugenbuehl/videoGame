import processing.core.PImage;

import java.util.List;

public class Sapling extends Plant{
private int healthLimit;
public Sapling(String id, Point position, List<PImage> images, double animationPeriod, double actionPeriod, int health){
    super(id, position, images, animationPeriod, actionPeriod,health);
    this.healthLimit=Functions.SAPLING_HEALTH_LIMIT;

}

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        super.setHealth(super.getHealth()+1);
        if (!this.transformSapling( world, scheduler, imageStore)) {
            scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
        }
    }
    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (this.getHealth() <= 0) {
            Stump stump = new Stump(Functions.STUMP_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList( Functions.STUMP_KEY));

            world.removeEntity( scheduler, this);

            world.addEntity( stump);

            return true;
        } else if (this.getHealth() >= this.healthLimit) {
            Tree tree = new Tree(Functions.TREE_KEY + "_" + this.getId(), this.getPosition(), imageStore.getImageList( Functions.TREE_KEY), Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN),Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN), Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN));

            world.removeEntity( scheduler, this);

            world.addEntity(tree);
            tree.scheduleActions( scheduler, world, imageStore);

            return true;
        }

        return false;
    }



}
