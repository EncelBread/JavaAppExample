import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class GameConfig implements Externalizable
{
    protected String ip;
    protected int port;
    protected int difficulty;
    protected long updatePeriod;
    protected int savePeriod;

    @Override
    public String toString() {
        return "GameConfig{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", difficulty=" + difficulty +
                ", updatePeriod=" + updatePeriod +
                ", savePeriod=" + savePeriod +
                '}';
    }

    public GameConfig(String ip, int port, int difficulty, long updatePeriod, int savePeriod) {
        this.ip = ip;
        this.port = port;
        this.difficulty = difficulty;
        this.updatePeriod = updatePeriod;
        this.savePeriod = savePeriod;
    }

    public GameConfig() {
        this.ip = "127.0.0.1";
        this.port = 25655;
        this.difficulty = 2;
        this.updatePeriod = 1000;
        this.savePeriod = 5;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public long getUpdatePeriod() {
        return updatePeriod;
    }

    public void setUpdatePeriod(long updatePeriod) {
        this.updatePeriod = updatePeriod;
    }

    public int getSavePeriod() {
        return savePeriod;
    }

    public void setSavePeriod(int savePeriod) {
        this.savePeriod = savePeriod;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(this.getIp());
        out.writeObject(this.getPort());
        out.writeObject(this.getDifficulty());
        out.writeObject(this.getUpdatePeriod());
        out.writeObject(this.getSavePeriod());
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.ip = (String) in.readObject();
        this.port = (int) in.readObject();
        this.difficulty = (int) in.readObject();
        this.updatePeriod = (long) in.readObject();
        this.savePeriod = (int) in.readObject();
    }
}
