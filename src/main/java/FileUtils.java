import java.io.*;

public class FileUtils
{
    /*если файла не существует функции load вернут null
    все функции прорасывают ошибки наверх, те они не обрабывает их тут
    ошибки должны обрабатываться там, откуда вызывается загрузка или сохранение*/

    public static GameConfig loadConfig() throws IOException, ClassNotFoundException
    {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream("config.txt"));
        GameConfig config = (GameConfig) objectInputStream.readObject();
        objectInputStream.close();
        System.out.println(config);
        return config;
    }

    public static void saveConfig(GameConfig config) throws IOException
    {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream("config.txt"));
        objectOutputStream.writeObject(config);
        objectOutputStream.close();
    }

    public static void saveWorld(World world) throws IOException
    {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream("world.txt"));
        objectOutputStream.writeObject(world);
        objectOutputStream.close();
    }

    public static World loadWorld() throws IOException, ClassNotFoundException
    {
        ObjectInputStream objectInputStream = new ObjectInputStream(
                new FileInputStream("world.txt"));
        World world = (World) objectInputStream.readObject();
        objectInputStream.close();
        System.out.println(world);
        return world;
    }
}
