import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntities extends Entity{
    private double animationPeriod;

    public AnimatedEntities(String id, Point position, List<PImage> images, double animationPeriod) {
        super(id, position, images);
        this.animationPeriod=animationPeriod;
    }
    public  void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore) {
        scheduler.scheduleEvent(this,
                new Animation(this, 0),
                this.getAnimationPeriod());
    }
    public Animation createAnimationAction(int repeatCount){
        return new Animation(this,  repeatCount);
    }




    public double getAnimationPeriod(){
        return this.animationPeriod;
    }

    public void setAnimationPeriod(double animationPeriod) {
        this.animationPeriod = animationPeriod;
    }


}
