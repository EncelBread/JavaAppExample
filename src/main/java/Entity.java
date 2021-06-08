import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;

public class Entity implements Externalizable
{
    private static int idCounter = 0;

    protected int id; //приравнивается idCounter, после чего idCounter увеличивается на 1
    protected World world;
    protected String title;
    protected double posX;
    protected double posZ;
    protected boolean aggressive;
    protected int maxHealth;
    protected double health;
    protected int attackDamage;
    protected Entity target;

    public Entity(String title, double posX, double posZ, boolean aggressive, int maxHealth, int health, int attackDamage) {
        this.title = title;
        this.posX = posX;
        this.posZ = posZ;
        this.aggressive = aggressive;
        this.maxHealth = maxHealth;
        this.health = health;
        this.attackDamage = attackDamage;
    }

    public Entity(int id, String title, double posX, double posZ, boolean aggressive, int maxHealth, int health, int attackDamage) {
        this.id = id;
        this.title = title;
        this.posX = posX;
        this.posZ = posZ;
        this.aggressive = aggressive;
        this.maxHealth = maxHealth;
        this.health = health;
        this.attackDamage = attackDamage;
    }

    public Entity() {  }

    public static int getIdCounter() {
        return idCounter;
    }

    public static void setIdCounter(int idCounter) {
        Entity.idCounter = idCounter;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public void setAggressive(boolean aggressive) {
        this.aggressive = aggressive;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Entity getTarget() {
        return target;
    }

    public void setTarget(Entity target) {
        this.target = target;
    }

    public boolean update()
    {
        if(aggressive)
        {
            double r;
            if(target!=null)
            {
                r = world.getDistance(posX,posZ,target);
            }else
            {
                searchTarget();
                if(target!=null)
                {
                    r = world.getDistance(posX,posZ,target);

                    System.out.println(r);
                }else
                {
                    r = 0;
                }
            }
            if(r>2) {
                //System.out.println("check");
                if (target.getPosX()-getPosX() < 1) {
                    posX--;
                } else if (target.getPosX()-getPosX() > 1) {
                    posX++;
                }
                if (target.getPosZ()-getPosZ() < 1) {
                    posZ--;
                } else if (target.getPosZ()-getPosZ() > 1) {
                    posZ++;
                }
            }
            else if (target!=null)
            {
                attack(target);
            }
            return true;
        }else
        {
            return false;
        }
    }
    //если aggressive == true, то сущность ищет ближашую к себе не агрссивную Entity в раудиусе 20 ед. и движется к ней
    //за 1 обновление сервера сущность может пройти 1 ед. по x и 1 ед. по z
    //если расстояние до атакуемой сущности <2 ед. то вызывается метод attack(entity)

    public void searchTarget()
    {
        for(Entity entity : world.getEntitiesNearEntity(this,20))
        {
            if(!entity.isAggressive() && target == null)
            {
                System.out.println(entity);
                target = entity;
            }
        }
    }

    public void attack(Entity entity)
    {
        entity.setHealth(entity.getHealth()-this.getAttackDamage() - 0.5 * GameServer.getInstance().getServerConfig().getDifficulty());
        if(entity instanceof EntityPlayer && entity.getHealth()>0)
        {
            this.setHealth(this.getHealth()-entity.getAttackDamage() - 0.5 * GameServer.getInstance().getServerConfig().getDifficulty());
            //System.out.println(getHealth());
            if(getHealth()<=0)
            {
                System.out.println(entity.getTitle() + " killed " + this.getTitle());
                GameServer.getInstance().getServerWorld().entities.remove(this);
                try {
                    DatabaseUtils.updateEntityDeath(entity);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    DatabaseUtils.insertBattleLog(entity,this);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }else if(entity.getHealth()<=0)
        {
            System.out.println(this.getTitle() + " killed " + entity.getTitle());
            System.out.println(entity);
            GameServer.getInstance().getServerWorld().entities.remove(entity);
            target=null;
            try {
                DatabaseUtils.updateEntityDeath(entity);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                DatabaseUtils.insertBattleLog(this,entity);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if(this instanceof EntityPlayer)
            {
                ((EntityPlayer) this).setExp(((EntityPlayer) this).getExp() + entity.getMaxHealth() * GameServer.getInstance().getServerConfig().getDifficulty());
            }
            System.out.println(world);
        }
    }


    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posX=" + posX +
                ", posZ=" + posZ +
                ", aggressive=" + aggressive +
                ", maxHealth=" + maxHealth +
                ", health=" + health +
                ", attackDamage=" + attackDamage +
                ", target=" + target +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(getIdCounter());
        out.writeInt(this.getId());
        out.writeObject(this.getWorld());
        out.writeObject(this.getTitle());
        out.writeObject(this.getPosX());
        out.writeObject(this.getPosZ());
        out.writeObject(this.isAggressive());
        out.writeObject(this.getMaxHealth());
        out.writeObject(this.getHealth());
        out.writeObject(this.getAttackDamage());
        out.writeObject(this.getTarget());
    }


    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException{
        idCounter = (int) in.readInt();
        this.id = (int) in.readInt();
        this.world = (World) in.readObject();
        this.title = (String) in.readObject();
        this.posX = (double) in.readObject();
        this.posZ = (double) in.readObject();
        this.aggressive = (boolean) in.readObject();
        this.maxHealth = (int) in.readObject();
        this.health = (double) in.readObject();
        this.attackDamage = (int) in.readObject();
        this.target = (Entity) in.readObject();
    }
}
//если entity не является игроком, то ей просто наносится урон ()
//если entity это EntityPlayer, то после удада по игроку, если хп >0, он наносит ответный удар
//урон наносится по формуле entity.attackDamage + 0.5 * difficulty (получаем через инстанцирование из объекта GameServer)
//если после нанесения урона сущности ее хп <= 0 она удаляется из мира и в консоль выводится кто и кого убил