import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Game {

    private final GameMap gameMap;
    private final SidebarController sidebarController;
    private final PlayerTank playerTank;
    private GameEndEventHandler gameEndEventHandler;


    public Game(Stage stage, GameProperties gameProperties) throws IOException{
        this.gameEndEventHandler = GameEndEventHandler.voidOnGameOverEventHandler;
        GameMapVisualiser mapVisualiser = new GameMapVisualiser(gameProperties.mapWidth, gameProperties.mapHeight);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sidebar.fxml"));
        final VBox sidebar;
        sidebar =  loader.load();
        this.sidebarController = loader.getController();
        this.gameMap = new GameMap(gameProperties.mapWidth, gameProperties.mapHeight,mapVisualiser,sidebarController,gameProperties);
        this.playerTank = this.gameMap.getPlayerTank();
        final BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: #292929");
        borderPane.setCenter(mapVisualiser.getNode());
        borderPane.setRight(sidebar);

        Scene scene = new Scene(borderPane);
        stage.setScene(scene);
        scene.setOnKeyReleased(this::onKeyReleasedListener);
        stage.show();
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            mapVisualiser.resizeWidth(newValue.doubleValue() - sidebar.getWidth());
            gameMap.draw();
        });
        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            mapVisualiser.resizeHeight(newValue.doubleValue());
            gameMap.draw();
        });


    }

    public void setGameEndEventHandler(GameEndEventHandler gameEndEventHandler) {
        this.gameEndEventHandler = gameEndEventHandler;
    }

    private void onKeyReleasedListener(KeyEvent event){
        KeyCode keyCode = event.getCode();
        if(keyCode.equals(KeyCode.ESCAPE)){
            playerTank.forceDestroy();
            gameEndEventHandler.onGameOver(sidebarController.getScore());
        }

        if (!playerTank.isAlive()){
            return;
        }
        if (keyCode.equals(KeyCode.SHIFT) || keyCode.equals(KeyCode.W)){
            boolean extraMove = playerTank.playerNeedsAnExtraMove();
            playerTank.setIntent(keyCode.equals(KeyCode.SHIFT) ? TankIntent.SHOOT : TankIntent.MOVE);
            if (extraMove) {
                gameMap.extraPlayerMove();
            } else {
                gameMap.act();
            }
        }

        if(keyCode.isDigitKey()){
            PowerUp powerUp = switch (keyCode){
                case DIGIT1 -> PowerUp.INVINCIBILITY;
                case DIGIT2 -> PowerUp.PENETRATING_BULLETS;
                case DIGIT3 -> PowerUp.FASTER_BULLETS;
                case DIGIT4 -> PowerUp.RUBBER_BULLETS;
                case DIGIT5 -> PowerUp.EXTRA_MOVE;
                default -> PowerUp.EXTRA_LIFE;
            };

            playerTank.powerUpManager.usePowerUp(powerUp);
        }

        switch (keyCode){
            case A -> playerTank.rotateTank(false);
            case D -> playerTank.rotateTank(true);
            case LEFT -> playerTank.rotateTurret(false);
            case RIGHT -> playerTank.rotateTurret(true);

        }

        gameMap.draw();
        if(!playerTank.isAlive())handlePlayerDead();

    }



    private void handlePlayerDead(){
        gameEndEventHandler.onGameOver(sidebarController.getScore());

    }

}
