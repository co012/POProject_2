import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class GameMapVisualiser {

    private final int GAME_UNIT = 16;
    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;

    private double xScale;
    private double yScale;

    private final Canvas canvas;
    private final static String BACKGROUND_COLOR = "#292929";

    private final ImageManager imageManager;

    public GameMapVisualiser(int mapWidthInGameUnits,int mapHeightInGameUnits) {
        MAP_WIDTH = mapWidthInGameUnits;
        MAP_HEIGHT = mapHeightInGameUnits;
        xScale = 1;
        yScale = 1;
        canvas = new Canvas(MAP_WIDTH * GAME_UNIT * xScale,MAP_HEIGHT * GAME_UNIT * yScale);
        imageManager = new ImageManager();
    }

    public double getDisplayedWidth(int width){
        return width * GAME_UNIT * xScale;
    }

    public double getDisplayedHeight(int height){
        return height * GAME_UNIT * yScale;
    }

    public void resizeWidth(double displayedWidth){
        canvas.setWidth(displayedWidth);
        xScale = displayedWidth/(MAP_WIDTH * GAME_UNIT);
    }

    public void resizeHeight(double displayedHeight){
        canvas.setHeight(displayedHeight);
        yScale = displayedHeight/(MAP_HEIGHT * GAME_UNIT);
    }

    public synchronized void clear(){
        canvas.getGraphicsContext2D().setFill(Color.web(BACKGROUND_COLOR));
        canvas.getGraphicsContext2D().fillRect(0,0,getDisplayedWidth(MAP_WIDTH),getDisplayedHeight(MAP_HEIGHT));
    }


    public Node getNode(){
        return canvas;
    }

    public void drawTanks(Collection<Tank> tanks) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(Tank tank: tanks){

            for(PowerUp powerUp: tank.powerUpManager.getActivePowerUps()){

                gc.drawImage(
                        imageManager.powerUp,
                        imageManager.getPowerUpImageNumberInXAxis(powerUp) * GAME_UNIT,
                        0,
                        GAME_UNIT,
                        GAME_UNIT,
                        getDisplayedWidth(tank.position.x),
                        getDisplayedHeight(tank.position.y),
                        getDisplayedWidth(1),
                        getDisplayedHeight(1)
                );

            }

            int x_source_offset_body = switch (tank.moveDirection){
                case UP,DOWN -> 0;
                case LEFT,RIGHT -> 16;
            };

            int y_source_offset = tank instanceof PlayerTank ? 0 : 16;

            gc.drawImage(
                    imageManager.tankBody,
                    x_source_offset_body,
                    y_source_offset,
                    GAME_UNIT,
                    GAME_UNIT,
                    getDisplayedWidth(tank.position.x),
                    getDisplayedHeight(tank.position.y),
                    getDisplayedWidth(1),
                    getDisplayedHeight(1)
            );

            int x_source_offset_turret = shootDirectionToXOffset(tank.shootDirection);

            gc.drawImage(
                    imageManager.tankTurret,
                    x_source_offset_turret,
                    y_source_offset,
                    GAME_UNIT,
                    GAME_UNIT,
                    getDisplayedWidth(tank.position.x),
                    getDisplayedHeight(tank.position.y),
                    getDisplayedWidth(1),
                    getDisplayedHeight(1)
            );

        }
    }


    public void drawBoxes(LinkedHashMap<Vector2d,Box> boxes){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for (Map.Entry<Vector2d,Box> entry : boxes.entrySet()){
            Box box = entry.getValue();
            Vector2d position = entry.getKey();

            gc.drawImage(
                    box.isDamaged() ? imageManager.box_damaged : imageManager.box,
                    getDisplayedWidth(position.x),
                    getDisplayedHeight(position.y),
                    getDisplayedWidth(1),
                    getDisplayedHeight(1)
            );

        }

    }

    public void drawBullets(LinkedList<Bullet> bullets){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(Bullet bullet : bullets){
            int x_source_offset_turret = shootDirectionToXOffset(bullet.getShootDirection());

            gc.drawImage(
                    imageManager.bullet,
                    x_source_offset_turret,
                    0,
                    GAME_UNIT,
                    GAME_UNIT,
                    getDisplayedWidth(bullet.getPosition().x),
                    getDisplayedHeight(bullet.getPosition().y),
                    getDisplayedWidth(1),
                    getDisplayedHeight(1)
            );

        }
    }

    public void drawExplosions(Collection<Kaboom> explosions){
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(Kaboom kaboom : explosions){
            gc.drawImage(
                    imageManager.kaboom,
                    getDisplayedWidth(kaboom.position.x),
                    getDisplayedHeight(kaboom.position.y),
                    getDisplayedWidth(1),
                    getDisplayedHeight(1)
            );
        }
    }

    public void drawPowerUps(Map<Vector2d,PowerUp> powerUpMap){
        GraphicsContext gc = canvas.getGraphicsContext2D();

        for(Map.Entry<Vector2d,PowerUp> entry : powerUpMap.entrySet()){
            PowerUp powerUp = entry.getValue();
            Vector2d position = entry.getKey();

            gc.drawImage(
                    imageManager.powerUp,
                    imageManager.getPowerUpImageNumberInXAxis(powerUp) * GAME_UNIT,
                    0,
                    GAME_UNIT,
                    GAME_UNIT,
                    getDisplayedWidth(position.x),
                    getDisplayedHeight(position.y),
                    getDisplayedWidth(1),
                    getDisplayedHeight(1)
            );
        }
    }

    private int shootDirectionToXOffset(ShootDirection shootDirection){
        return GAME_UNIT * switch (shootDirection){
            case NORTH -> 4;
            case NORTH_EAST -> 3;
            case EAST -> 2;
            case SOUTH_EAST -> 1;
            case SOUTH -> 0;
            case SOUTH_WEST -> 7;
            case WEST -> 6;
            case NORTH_WEST -> 5;
        };
    }
}
