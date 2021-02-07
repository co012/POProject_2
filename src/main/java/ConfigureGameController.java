import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

import java.util.Optional;

public class ConfigureGameController {

    @FXML
    private Slider powerUpProbSlider;
    @FXML
    private Slider powerUpMaxWaitTimeSlider;
    @FXML
    private Slider enemyProbSlider;
    @FXML
    private Slider obstructionProbSlider;
    @FXML
    private Slider enemyMaxWaitTimeSlider;
    @FXML
    private Slider obstructionMaxWaitTimeSlider;
    @FXML
    private Slider mapWidthSlider, mapHeightSlider;
    @FXML
    private Label mapWidthLabel, mapHeightLabel;

    private GameProperties gameProperties;
    private Stage stage;


    public ConfigureGameController() {
        Platform.runLater(this::init);
    }

    private void init() {
        mapWidthSlider.valueProperty().addListener((o, oldVal, newVal) -> mapWidthLabel.setText(String.valueOf(newVal.intValue())));
        mapHeightSlider.valueProperty().addListener((o, oldVal, newVal) -> mapHeightLabel.setText(String.valueOf(newVal.intValue())));
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void onOkButtonAction(){
        int mapWidth = mapWidthSlider.valueProperty().intValue();
        int mapHeight = mapHeightSlider.valueProperty().intValue();
        int powerUpProb = powerUpProbSlider.valueProperty().intValue();
        int powerUpMaxWaitTime = powerUpMaxWaitTimeSlider.valueProperty().intValue();
        int enemyProb = enemyProbSlider.valueProperty().intValue();
        int obstructionProb = obstructionProbSlider.valueProperty().intValue();
        int enemyMaxWaitTime = enemyMaxWaitTimeSlider.valueProperty().intValue();
        int obstructionMaxWaitTime = obstructionMaxWaitTimeSlider.valueProperty().intValue();

        gameProperties = new GameProperties(mapWidth, mapHeight, powerUpProb, powerUpMaxWaitTime, enemyProb, enemyMaxWaitTime, obstructionProb, obstructionMaxWaitTime);

        stage.close();
    }

    public Optional<GameProperties> getGameProperties() {
        return Optional.ofNullable(gameProperties);
    }
}
