public class GameProperties {

    public final int mapWidth;
    public final int mapHeight;
    public final int powerUpProb;
    public final int powerUpMaxWaitTime;
    public final int enemyProb;
    public final int enemyMaxWaitTime;
    public final int obstructionProb;
    public final int obstructionMaxWaitTime;

    public GameProperties(int mapWidth, int mapHeight, int powerUpProb, int powerUpMaxWaitTime, int enemyProb, int enemyMaxWaitTime, int obstructionProb, int obstructionMaxWaitTime) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.powerUpProb = powerUpProb;
        this.powerUpMaxWaitTime = powerUpMaxWaitTime;
        this.enemyProb = enemyProb;
        this.obstructionProb = obstructionProb;
        this.enemyMaxWaitTime = enemyMaxWaitTime;
        this.obstructionMaxWaitTime = obstructionMaxWaitTime;
    }
    
}
