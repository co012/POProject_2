import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EndGameController {

    @FXML
    private Label scoreLabel;

    private Runnable onExitGameRunnable,onTryAgainRunnable;

    public EndGameController(){
        this.onExitGameRunnable = () -> {};
        this.onTryAgainRunnable = () -> {};
    }

    public void setScore(int score){
        Platform.runLater(() -> scoreLabel.setText(String.valueOf(score)));
    }


    public void setOnTryAgainRunnable(Runnable onTryAgainRunnable) {
        this.onTryAgainRunnable = onTryAgainRunnable;
    }

    public void setOnExitGameRunnable(Runnable onExitGameRunnable) {
        this.onExitGameRunnable = onExitGameRunnable;
    }

    @FXML
    private void onExitGameButtonClicked(){
        onExitGameRunnable.run();
    }

    @FXML
    private void onTryAgainButtonClicked(){
        onTryAgainRunnable.run();
    }
}
