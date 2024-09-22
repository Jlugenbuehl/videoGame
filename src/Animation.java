public class Animation extends Action{
    private int repeatCount;

    public Animation(AnimatedEntities entity, int repeatCount){
        super(entity);
        this.repeatCount=repeatCount;

    }

    public void executeAction(EventScheduler scheduler) {
        super.getEntity().nextImage();

        if (this.repeatCount != 1) {
            scheduler.scheduleEvent((this.getEntity()),
                    this.getEntity().createAnimationAction(Math.max(this.repeatCount-1,0)), (this.getEntity()).getAnimationPeriod());
        }



}}
