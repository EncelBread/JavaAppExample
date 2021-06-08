import javax.xml.crypto.Data;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class World implements Externalizable
{
    protected int id = 1;
    protected String worldName;
    protected List<Entity> entities = new ArrayList<>();

    public World(int id, String worldName) {
        this.id = id;
        this.worldName = worldName;
    }

    public World() {  }

    public void update()
    {
        for (int i = 0; i < entities.size(); i++) {
            entities.get(i).update();
            //System.out.println("Entity " + entities.get(i).getTitle() + " is updated");
        }
        /*
        for(Entity entity : entities)
        {
            entity.update();
        }
        */
    }
    //вызывает update() у всех entity в листе

    public void addEntity(Entity entity)
    {
        entity.setWorld(this);
        entity.setId((int)(DatabaseUtils.insertEntity(entity)));
        entities.add(entity);
        if(entity instanceof EntityPlayer)
        {
            try {
                DatabaseUtils.insertPlayer((EntityPlayer) entity);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    public double getDistance(double x, double z, Entity entity)
    {
        return Math.sqrt(Math.pow(x - entity.posX, 2) + Math.pow(z - entity.posZ, 2));
    }

    public List<Entity> getEntitiesInRegion(double x, double z, double range)
    {
        List<Entity> entitiesInRegion = new ArrayList<>();
        for(Entity entity : entities)
        {
            if(getDistance(x,z,entity)<20)
            {
                entitiesInRegion.add(entity);
            }
        }
        
        entitiesInRegion.sort(Comparator.comparingDouble(o -> getDistance(x,z,o)));

        return entitiesInRegion;
    }
    //возращает отсортированный в порядке близости точке x/z список сущностей

    public List<Entity> getEntitiesNearEntity(Entity entity, double range)
    {
        return getEntitiesInRegion(entity.getPosX(),entity.getPosZ(),range);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "World{" +
                "id=" + id +
                ", worldName='" + worldName + '\'' +
                ", entities=" + entities +
                '}';
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(this.getId());
        out.writeUTF(this.getWorldName());
        out.writeInt(entities.size());
        for(Externalizable ext : entities)
        {
            ext.writeExternal(out);
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.id = (int) in.readInt();
        this.worldName = in.readUTF();
        int count = in.readInt();
        for(int i=0; i<count; i++){
            Entity ext = new Entity();
            ext.readExternal(in);
            entities.add(ext);
        }
    }
    //возращает отсортированный в порядке близости точке entity.posX/entity.posZ список сущностей
}
