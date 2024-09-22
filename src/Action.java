/**
 * An action that can be taken by an entity
 */
public abstract class Action {
    private AnimatedEntities entity;

    public Action( AnimatedEntities entity) {
        this.entity=entity;



    }
    public abstract void executeAction(EventScheduler scheduler);

//    public void executeAnimationAction(EventScheduler scheduler) {
//        this.getEntity().nextImage();
//
//        if (this.getRepeatCount() != 1) {
//            scheduler.scheduleEvent(this.getEntity(), this.getEntity().createAnimationAction( Math.max(this.getRepeatCount() - 1, 0)), this.getEntity().getAnimationPeriod());
//        }
//    }

//    public void executeAction( EventScheduler scheduler) {
//        switch (this.kind) {
//            case ACTIVITY:
//                this.executeActivityAction(scheduler);
//                break;
//
//            case ANIMATION:
//                this.executeAnimationAction(scheduler);
//                break;
//        }
//    }

//    public void executeActivityAction(EventScheduler scheduler) {
//        switch (this.entity.getKind()) {
//            case SAPLING:
//                this.entity.executeSaplingActivity( this.world, this.imageStore, scheduler);
//                break;
//            case TREE:
//                this.entity.executeTreeActivity( this.world, this.imageStore, scheduler);
//                break;
//            case FAIRY:
//                this.entity.executeFairyActivity( this.world, this.imageStore, scheduler);
//                break;
//            case DUDE_NOT_FULL:
//                this.entity.executeDudeNotFullActivity( this.world, this.imageStore, scheduler);
//                break;
//            case DUDE_FULL:
//                this.entity.executeDudeFullActivity( this.world, this.imageStore, scheduler);
//                break;
//            default:
//                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.entity.getKind()));
//        }
//        this.getEntity().executeActivity(this.getWorld(), this.getImageStore(),scheduler);
//    }

    public AnimatedEntities getEntity() {
        return entity;
    }

    public void setEntity(ActiveEntities entity) {
        this.entity = entity;
    }



}
