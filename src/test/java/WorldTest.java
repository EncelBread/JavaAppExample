import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

    int x,z;
    private World testWorld;
    private Entity entity1,entity2,entity3;
    private List<Entity> entitiesInRegion = new ArrayList<>();
    private List<Entity> entitiesNearEntity = new ArrayList<>();

    @BeforeEach
    public void setUp()
    {
        x = 3;
        z = 5;
        testWorld = new World(1,"Earth");
        testWorld.addEntity(entity1 = new Entity("Moscow",1,1,false,10,5,0));
        testWorld.addEntity(entity2 = new Entity("Kiev",30,20,true,10,5,1));
        testWorld.addEntity(entity3 = new Entity("Chernigov",10,1,false,10,5,0));
    }

    @Test
    public void testGetDistance()
    {
        double value = testWorld.getDistance(x,z,entity1);
        BigDecimal result = new BigDecimal(value);
        result = result.setScale(3, RoundingMode.DOWN);
        BigDecimal expRes = BigDecimal.valueOf(4.472);

        assertEquals(expRes,result);
    }

    @Test
    public void testGetEntitiesInRegion()
    {
        entitiesInRegion.add(entity1);
        entitiesInRegion.add(entity3);

        assertEquals(entitiesInRegion, testWorld.getEntitiesInRegion(1,1,20));
    }

    @Test
    public void testGetEntitiesNearEntity()
    {
        entitiesNearEntity.add(entity1);
        entitiesNearEntity.add(entity3);

        assertEquals(entitiesNearEntity, testWorld.getEntitiesNearEntity(entity1,20));
    }
}