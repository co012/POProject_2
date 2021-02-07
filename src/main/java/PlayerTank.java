import java.util.concurrent.ExecutionException;

public final class PlayerTank extends Tank{

    private GameInfoChangeListener gameInfoChangeListener;
    private boolean needAnExtraMove;

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        gameInfoChangeListener.onPlayerLifesNumberChange(getLives());
    }

    @Override
    public void forceDestroy() {
        super.forceDestroy();
        gameInfoChangeListener.onPlayerLifesNumberChange(getLives());
    }

    public PlayerTank(Vector2d position, GameMap map) {
        super(position,map);
        this.lifes = 3;
        gameInfoChangeListener = GameInfoChangeListener.voidGameChangeListener;
    }

    @Override
    protected void getExtraLife() {
        super.getExtraLife();
        gameInfoChangeListener.onPlayerLifesNumberChange(lifes);
    }

    public PlayerTank(Vector2d position,GameMap map,GameInfoChangeListener gameInfoChangeListener){
        this(position,map);
        this.gameInfoChangeListener = gameInfoChangeListener;
        gameInfoChangeListener.onPlayerLifesNumberChange(getLives());
        powerUpManager.setGameInfoChangeListener(gameInfoChangeListener);
    }

    @Override
    protected void think() {
        needAnExtraMove = true;
    }

    public void setIntent(TankIntent tankIntent){
        this.tankIntent = tankIntent;
        needAnExtraMove = false;
    }

    public void rotateTurret(boolean clockwise){
        if(clockwise) this.shootDirection = ShootDirection.getClockwiseNext(this.shootDirection);
        else this.shootDirection = ShootDirection.getClockwisePrevious(this.shootDirection);
    }

    public void rotateTank(boolean clockwise){
        //IDK fix
        clockwise = !clockwise;
        if(clockwise) this.moveDirection = MoveDirection.getClockwiseNext(this.moveDirection);
        else this.moveDirection = MoveDirection.getClockwisePrevious(this.moveDirection);
    }

    public boolean playerNeedsAnExtraMove(){
        return needAnExtraMove;
    }

}
