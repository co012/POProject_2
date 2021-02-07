import java.io.IOException;

public interface GameEndEventHandler {
    void onGameOver(int score);
    GameEndEventHandler voidOnGameOverEventHandler = score -> { };
}
