import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;

public class GameServer //класс нужно оформит с использованием инстанцирования
{
    protected GameConfig serverConfig;
    protected World serverWorld;
    protected int serverTicks;

    public void updateServer() //вызывает update() у всех entity
    {
        this.serverWorld.update();
        serverTicks++;
        if(serverTicks % GameServer.getInstance().getServerConfig().savePeriod == 0)
        {
            try{
                FileUtils.saveWorld(serverWorld);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public GameServer() {
        this.serverConfig = null;
        this.serverWorld = null;
        this.serverTicks = 0;
        instance = this;
    }

    private static GameServer instance;

    public static void main(String[] args)
    {
        GameConfig config = new GameConfig();

        try {
            FileUtils.saveConfig(config);
        } catch (IOException e) {
            e.printStackTrace();
        }

        World newWorld = new World(1,"Earth");
        newWorld.addEntity(new Entity("Moscow",1,1,false,10,5,0));
        newWorld.addEntity(new Entity("Kiev",1,2,true,10,5,1));
        newWorld.addEntity(new Entity("Chernigov",3,3,false,10,5,0));
        newWorld.addEntity(new EntityPlayer("Player",5,5,false,10,5,2,"Player_name",0));

        System.out.println(newWorld);

        try{
            FileUtils.saveWorld(newWorld);
        } catch (IOException e) {
        e.printStackTrace();
        }

        new GameServer();

        try {
            GameServer.getInstance().loadWorld();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            GameServer.getInstance().loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println(GameServer.getInstance().toString());
        //GameServer.getInstance().toString();

        for(int i = 0;i<30;i++)
        {
            GameServer.getInstance().updateServer();
            //System.out.println("Iteration: "+i);
        }
        try {
            Thread.sleep(GameServer.getInstance().getServerConfig().updatePeriod);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() throws IOException
    {
        try {
            serverConfig = FileUtils.loadConfig();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*вызывает FileUtils.loadConfig, если вернулся конфиг, то устанавливает его в поле config
        если вернулся null (файла нет) создает дефолтный конфиг и сразу сохранет его*/
    }

    private void loadWorld()  throws IOException
    {
        try {
            serverWorld = FileUtils.loadWorld();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        /*вызывает FileUtils.loadWorld, если вернулся мир, то устанавливает его в поле serverWorld
    если мира нет, создает новый и сразу сохраняет его*/
    }

    public static GameServer getInstance() {
        return instance;
    }

    public World getServerWorld() {
        return serverWorld;
    }

    public void setServerWorld(World serverWorld) {
        this.serverWorld = serverWorld;
    }

    public int getServerTicks() {
        return serverTicks;
    }

    public void setServerTicks(int serverTicks) {
        this.serverTicks = serverTicks;
    }

    public GameConfig getServerConfig() {
        return serverConfig;
    }

    @Override
    public String toString() {
        return "GameServer{" +
                "serverConfig=" + serverConfig +
                ", serverWorld=" + serverWorld +
                ", serverTicks=" + serverTicks +
                '}';
    }
}

//создает новый объект класса GameServer используя шаблон инстанцирования и выводии в его в консоль
//после чего в цикле на 30 итераций раз в секунду вызывает обновление сервера
//следующим кодом можно сделать задерку сервера на 1000 миллисекунд
	/*try {
            Thread.sleep(1000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
//после завершения цикла выводит объект GameServer в консоль*/