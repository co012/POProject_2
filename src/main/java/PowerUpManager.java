import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class PowerUpManager {

    private final LinkedHashMap<PowerUp, AtomicInteger> powerUps;
    private final LinkedHashMap<PowerUp,AtomicInteger> activePowerUpsDurability;
    private GameInfoChangeListener gameInfoChangeListener;
    private final Runnable getExtraLiveRunnable;
    private boolean canMoveASecondTime;

    public PowerUpManager(@NotNull Runnable getExtraLiveRunnable) {
        this.getExtraLiveRunnable = getExtraLiveRunnable;
        activePowerUpsDurability = new LinkedHashMap<>();
        powerUps = new LinkedHashMap<>();
        gameInfoChangeListener = GameInfoChangeListener.voidGameChangeListener;

    }

    public void setGameInfoChangeListener(GameInfoChangeListener gameInfoChangeListener) {
        this.gameInfoChangeListener = gameInfoChangeListener;
    }

    public boolean isPowerUpActive(PowerUp powerUp){
        return activePowerUpsDurability.containsKey(powerUp);
    }

    public void usePowerUp(PowerUp powerUp){
        if(!powerUps.containsKey(powerUp) || activePowerUpsDurability.containsKey(powerUp))return;
        int newPowerUpNumber = powerUps.get(powerUp).decrementAndGet();
        activePowerUpsDurability.put(powerUp,new AtomicInteger(getBaseDurability(powerUp)));
        if(newPowerUpNumber <= 0) powerUps.remove(powerUp);
        gameInfoChangeListener.onPowerUpsNumberChange(powerUp,newPowerUpNumber);
    }

    public void powerUpCollected(@NotNull PowerUp powerUp){
        if(powerUp.equals(PowerUp.EXTRA_LIFE)){
            getExtraLiveRunnable.run();
            return;
        }
        if (!powerUps.containsKey(powerUp)) {
            powerUps.put(powerUp,new AtomicInteger(0));
        }
        int newPowerUpNumber = powerUps.get(powerUp).incrementAndGet();
        gameInfoChangeListener.onPowerUpsNumberChange(powerUp,newPowerUpNumber);
    }

    private int getBaseDurability (PowerUp powerUp){
        return switch (powerUp){
            case EXTRA_LIFE -> 0;
            case EXTRA_MOVE -> 2;
            case INVINCIBILITY -> 4;
            case FASTER_BULLETS -> 3;
            case RUBBER_BULLETS -> 6;
            case PENETRATING_BULLETS -> 9;
        };
    }

    public void endTurn(){
        for(PowerUp powerUp : PowerUp.values()){
            if(!activePowerUpsDurability.containsKey(powerUp))continue;
            int durability = activePowerUpsDurability.get(powerUp).decrementAndGet();
            if(durability < 1) activePowerUpsDurability.remove(powerUp);
        }
    }

    public Collection<PowerUp> getActivePowerUps(){
        return Set.copyOf(activePowerUpsDurability.keySet());
    }


}
