import java.util.function.Function;

public abstract class Tank {
    protected Vector2d position;
    protected ShootDirection shootDirection;
    protected MoveDirection moveDirection;
    protected TankIntent tankIntent;
    protected GameMap map;
    protected int lifes;
    public final PowerUpManager powerUpManager;


    public Tank(Vector2d position,GameMap map){
        this.position = position;
        this.shootDirection = ShootDirection.NORTH;
        this.moveDirection = MoveDirection.UP;
        this.tankIntent = TankIntent.NONE;
        this.map = map;
        this.powerUpManager = new PowerUpManager(this::getExtraLife);
    }

    protected void getExtraLife(){
        lifes++;
    }


    public void act(){
        if(tankIntent.equals(TankIntent.NONE)){
            think();
        }

        if(tankIntent.equals(TankIntent.MOVE) && isAlive()){
            Vector2d potentialNewPosition = position.add(MoveDirection.toUnitVector(moveDirection));
            MapElement mapElement = map.getMapElementOnField(potentialNewPosition);
            if(!(mapElement.equals(MapElement.BOX) || mapElement.equals(MapElement.MAP_BOUNDARY))){
                Vector2d oldPosition = position;
                position = potentialNewPosition;
                map.onTankPositionChange(this,oldPosition,potentialNewPosition);
            }

        }else if(tankIntent.equals(TankIntent.SHOOT)){
            Bullet bullet = new Bullet(position,shootDirection,map,
                    powerUpManager.isPowerUpActive(PowerUp.FASTER_BULLETS),
                    powerUpManager.isPowerUpActive(PowerUp.RUBBER_BULLETS),
                    powerUpManager.isPowerUpActive(PowerUp.PENETRATING_BULLETS));
            map.spawnBullet(bullet);

        }

        tankIntent = TankIntent.NONE;


    }

    protected abstract void think();
    public void takeDamage(int damage){
        if(powerUpManager.isPowerUpActive(PowerUp.INVINCIBILITY))return;
        lifes -= damage;
    };
    public void forceDestroy(){
        lifes = 0;
    }
    public int getLives(){
        return lifes;
    }
    public boolean isAlive(){
        return lifes > 0;
    }

    public void endTurn(){
        powerUpManager.endTurn();
    }


}
