import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {

    private static Entity entity;
    private static long entityID;

    @BeforeEach
    void setUp()
    {
        entity = new Entity("Kiev",1,2,true,10,5,1);
    }

    @Test
    void testInsertEntity() throws SQLException
    {
        entityID = DatabaseUtils.insertEntity(entity);
    }

    @Test
    void testDeleteEntity() throws SQLException
    {
        DatabaseUtils.deleteEntity((int)entityID);
    }

}