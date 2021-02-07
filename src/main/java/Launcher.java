import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Launcher extends Application {

    private Stage primaryStage;


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        startNewGame();

    }

    private void onGameEnd(int score) {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("end_game_dialog.fxml"));
        Parent root;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        EndGameController endGameController = loader.getController();
        Stage stage = new Stage();
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(root));
        stage.setOnCloseRequest((e) -> primaryStage.close());
        endGameController.setOnExitGameRunnable(primaryStage::close);
        endGameController.setOnTryAgainRunnable(() -> {
            try {
                startNewGame();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        endGameController.setScore(score);
        stage.showAndWait();

    }

    private void startNewGame() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("configure_game_scene.fxml"));
        Parent root = loader.load();
        ConfigureGameController configureGameController = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        configureGameController.setStage(stage);
        stage.showAndWait();

        Optional<GameProperties> gamePropertiesOptional = configureGameController.getGameProperties();
        if( gamePropertiesOptional.isEmpty()) return;

        Game game = new Game(primaryStage,gamePropertiesOptional.get());
        game.setGameEndEventHandler(this::onGameEnd);
    }


    public static void main(String[] args){
        launch(args);
    }
}
