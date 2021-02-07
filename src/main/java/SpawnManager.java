import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class SpawnManager {

    private final LinkedHashMap<MapElement, AtomicInteger> certainElementSpawnIn;
    private final LinkedHashMap<MapElement, Integer> maxSpawnWaitTime;
    private final LinkedHashMap<MapElement, Integer> probabilityOfSuddenSpawn;
    private final Function<MapElement,Boolean> spawnRequestListener;
    private final Random random;

    public SpawnManager (@NotNull GameProperties gameProperties, @NotNull Function<MapElement,Boolean> spawnRequestListener){
        this.spawnRequestListener = spawnRequestListener;
        this.maxSpawnWaitTime = new LinkedHashMap<>();
        this.probabilityOfSuddenSpawn = new LinkedHashMap<>();
        this.certainElementSpawnIn = new LinkedHashMap<>();
        this.random = new Random();

        maxSpawnWaitTime.put(MapElement.TANK,gameProperties.enemyMaxWaitTime);
        probabilityOfSuddenSpawn.put(MapElement.TANK, gameProperties.enemyProb);
        maxSpawnWaitTime.put(MapElement.BOX, gameProperties.obstructionMaxWaitTime);
        probabilityOfSuddenSpawn.put(MapElement.BOX, gameProperties.obstructionProb);
        maxSpawnWaitTime.put(MapElement.POWER_UP,gameProperties.powerUpMaxWaitTime);
        probabilityOfSuddenSpawn.put(MapElement.POWER_UP, gameProperties.powerUpProb);

        maxSpawnWaitTime.forEach( (k,v) -> certainElementSpawnIn.put(k,new AtomicInteger(v)));
    }


    public void act(){
        tryToSpawnElements();
        tryToSpawnWhatNeedsToBeSpawn();
        endTurn();
    }

    public void requestTankSpawn(){
        MapElement mapElement = MapElement.TANK;
        spawnRequestListener.apply(mapElement);
        certainElementSpawnIn.get(mapElement).set(maxSpawnWaitTime.get(mapElement));
    }

    private void tryToSpawnElements(){
        for(Map.Entry<MapElement,Integer> entry : probabilityOfSuddenSpawn.entrySet()){
            MapElement mapElement = entry.getKey();
            int probability = entry.getValue();
            if(random.nextInt(100) >= probability)continue;
            spawnRequestListener.apply(mapElement);
            certainElementSpawnIn.get(mapElement).set(maxSpawnWaitTime.get(mapElement));
        }
    }

    private void tryToSpawnWhatNeedsToBeSpawn(){
        for(Map.Entry<MapElement,AtomicInteger> entry : certainElementSpawnIn.entrySet()){
            AtomicInteger spawnIn = entry.getValue();
            MapElement mapElement = entry.getKey();
            if(spawnIn.get() > 0) continue;

            spawnRequestListener.apply(mapElement);
            certainElementSpawnIn.get(mapElement).set(maxSpawnWaitTime.get(mapElement));

        }
    }

    private void endTurn(){
        certainElementSpawnIn.values().forEach(AtomicInteger::decrementAndGet);
    }

}
