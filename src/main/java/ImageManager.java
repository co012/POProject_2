import javafx.scene.image.Image;

public class ImageManager {

    private final static String PATH_TO_IMAGE_FOLDER = "textures/";
    private final static String TANK_BODY_FILENAME = "tank_body.png";
    private final static String TANK_TURRET_FILENAME = "tank_turret.png";
    private final static String BOX_FILENAME = "box.png";
    private final static String BOX_DAMAGED_FILENAME = "box_damaged.png";
    private final static String BULLET_FILENAME = "bullet.png";
    private final static String KABOOM_FILENAME = "kaboom.png";
    private final static String POWER_UP_FILENAME = "power_ups.png";
    public final Image tankBody, tankTurret;
    public final Image box, box_damaged;
    public final Image bullet;
    public final Image kaboom;
    public final Image powerUp;

    public ImageManager() {
        this.tankBody = new Image(PATH_TO_IMAGE_FOLDER + TANK_BODY_FILENAME);
        this.tankTurret = new Image(PATH_TO_IMAGE_FOLDER + TANK_TURRET_FILENAME);
        this.box = new Image(PATH_TO_IMAGE_FOLDER + BOX_FILENAME);
        this.box_damaged = new Image(PATH_TO_IMAGE_FOLDER + BOX_DAMAGED_FILENAME);
        this.bullet = new Image(PATH_TO_IMAGE_FOLDER + BULLET_FILENAME);
        this.kaboom = new Image(PATH_TO_IMAGE_FOLDER + KABOOM_FILENAME);
        this.powerUp = new Image(PATH_TO_IMAGE_FOLDER + POWER_UP_FILENAME);
    }

    public int getPowerUpImageNumberInXAxis(PowerUp powerUp) {
        return switch (powerUp) {
            case EXTRA_MOVE -> 0;
            case FASTER_BULLETS -> 1;
            case PENETRATING_BULLETS -> 2;
            case RUBBER_BULLETS -> 3;
            case INVINCIBILITY -> 4;
            case EXTRA_LIFE -> 5;

        };
    }


}
