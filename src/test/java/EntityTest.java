import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private static World world;
    private static Entity entity, entityTarget;

    @BeforeEach
    void setUp()
    {
        world = new World(1,"Earth");
        world.addEntity(entityTarget = new Entity("Moscow",1,1,false,10,5,0));
        world.addEntity(entity = new Entity("Kiev",1,2,true,10,5,1));
        world.addEntity(new Entity("Chernigov",3,3,false,10,5,0));
        world.addEntity(new EntityPlayer("Player",5,5,false,10,5,2,"Player_name",0));
    }

    @Test
    void testSearchTarget()
    {
        entity.searchTarget();
        assertEquals(entityTarget, entity.getTarget());
    }

    @Test
    void testUpdate()
    {
        assertFalse(entityTarget.update());
    }
}