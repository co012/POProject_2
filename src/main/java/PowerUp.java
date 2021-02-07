import java.util.Random;

public enum PowerUp {
    INVINCIBILITY,
    PENETRATING_BULLETS,
    FASTER_BULLETS,
    RUBBER_BULLETS,
    EXTRA_MOVE,
    EXTRA_LIFE;

    public static PowerUp getRandomPowerUp(){
        int randomIndex = new Random().nextInt(values().length);
        return values()[randomIndex];
    }



}
