public class Activity extends Action{
    private WorldModel world;
    private ImageStore imageStore;

    public Activity(ActiveEntities entity, WorldModel world, ImageStore imageStore){
        super(entity);
        this.world=world;
        this.imageStore=imageStore;
    }

    public void executeAction(EventScheduler scheduler) {
        ((ActiveEntities)(this.getEntity())).executeActivity(this.world, this.imageStore,scheduler);
        /*
        if(this.getEntity() instanceof Dude_Full) {

            ((Dude_Full) this.getEntity()).executeActivity(this.getWorld(),
                    this.getImageStore(), scheduler);
        }
        if(this.getEntity() instanceof Dude_NotFull) {

            ((Dude_NotFull) this.getEntity()).executeActivity(this.getWorld(),
                    this.getImageStore(), scheduler);
        }
        //depending on the type of enitiy we will cast

        if(this.getEntity() instanceof Fairy) {

            ((Fairy) this.getEntity()).executeActivity(this.getWorld(),
                    this.getImageStore(), scheduler);
        }
        if(this.getEntity() instanceof Tree) {

            ((Tree) this.getEntity()).executeActivity(this.getWorld(),
                    this.getImageStore(), scheduler);
        }


        if(this.getEntity() instanceof Sapling) {

            ((Sapling) this.getEntity()).executeActivity(this.getWorld(),
                    this.getImageStore(), scheduler);
        }
*/



    }}
