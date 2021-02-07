import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SidebarController implements GameInfoChangeListener{
    @FXML private Label extraMoveLabel;
    @FXML private Label fasterBulletsLabel;
    @FXML private Label penetratingBulletsLabel;
    @FXML private Label rubberBulletsLabel;
    @FXML private Label invincibilityLabel;
    private int score = 0;

    @FXML
    private Label scoreLabel;
    @FXML
    private Label lifesLabel;

    @Override
    public void onTankDestroyed() {
        score++;
        scoreLabel.setText(String.valueOf(score));
    }

    @Override
    public void onPowerUpsNumberChange(PowerUp powerUp, int newNumber) {
        switch (powerUp){
            case EXTRA_MOVE -> extraMoveLabel.setText(String.valueOf(newNumber));
            case FASTER_BULLETS -> fasterBulletsLabel.setText(String.valueOf(newNumber));
            case PENETRATING_BULLETS -> penetratingBulletsLabel.setText(String.valueOf(newNumber));
            case RUBBER_BULLETS -> rubberBulletsLabel.setText(String.valueOf(newNumber));
            case INVINCIBILITY -> invincibilityLabel.setText(String.valueOf(newNumber));
        }

    }

    @Override
    public void onPlayerLifesNumberChange(int newNumber) {
        lifesLabel.setText(String.valueOf(newNumber));
    }

    public int getScore() {
        return Integer.parseInt(scoreLabel.getText());
    }
}
