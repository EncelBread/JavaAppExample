import java.sql.*;

public class DatabaseUtils
{
    public static Connection conn()
    {
        String url = "jdbc:postgresql:java";
        String username = "postgres";
        String password = "123";
        System.out.println("Connecting...");
        Connection c = null;

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successful!");
        } catch (Exception e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
        return c;
    }

    public static long insertEntity(Entity entity)
    {
        String sql = "INSERT INTO public.entities(title,create_date) "
                + "VALUES(?,?)";

        long id = 0;

        try(Connection conn = conn();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, entity.getTitle());
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()){
                    if (rs.next()) {
                        id = rs.getLong(1);
                    }
                }catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return id;
    }

    public static int deleteEntity(int id) throws SQLException
    {
        try(Connection conn = conn())
        {
            String sql = "DELETE FROM public.entities WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate();
        }
    }

    public static void updateEntityDeath(Entity entity) throws SQLException {
        try (Connection connection = conn()) {
            String sql = "update public.entities set death_date = ? where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            ps.setLong(2, entity.getId());

            ps.executeUpdate();
        }
    }

    public static void insertPlayer(EntityPlayer player) throws SQLException {
        try (Connection connection = conn()) {
            String sql = "insert into public.players(id, nickname, exp) values(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, player.getId());
            ps.setString(2, player.getNickname());
            ps.setDouble(3, player.getExp());

            ps.executeUpdate();
        }
    }

    public static void updatePlayer(EntityPlayer player) throws SQLException {
        try (Connection connection = conn()) {
            String sql = "update public.players set exp = ? where id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setDouble(1, player.getExp());
            ps.setLong(2, player.getId());

            ps.executeUpdate();
        }
    }

    public static void insertBattleLog(Entity attacker,Entity victim) throws SQLException {
        try (Connection connection = conn()) {
            String sql =
                    "insert into public.battle_logs(attacker, victim, kill_date) values(?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, attacker.getId());
            ps.setLong(2, victim.getId());
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            ps.executeUpdate();
        }
    }
}
