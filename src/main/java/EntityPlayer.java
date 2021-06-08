import java.sql.SQLException;

public class EntityPlayer extends Entity
{
    protected String nickname;
    protected double exp;

    public EntityPlayer(String title, double posX, double posZ, boolean aggressive, int maxHealth, int health, int attackDamage, String nickname, double exp) {
        super(title, posX, posZ, aggressive, maxHealth, health, attackDamage);
        this.nickname = nickname;
        this.exp = exp;
    }

    public EntityPlayer()
    {

    }

    public boolean update()
    {
        super.update();

        if(GameServer.getInstance().getServerTicks()/2==0)
        {
            if(health<maxHealth)
            {
                setHealth(getHealth()+1);
            }
        }
        System.out.println("exp:"+GameServer.getInstance().getServerTicks()/5);
        if(GameServer.getInstance().getServerTicks()/5==0)
        {
            setExp(getExp()+10*GameServer.getInstance().getServerConfig().getDifficulty());
            try {
                DatabaseUtils.updatePlayer(this);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return false;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }
    //сначала вызывает родительскую реализацию
    //и раз в 2 обновлений серверва, если health < maxHealth регенирует себе 1 хп (реализация, счетчик обновлений уже за вами)

}
