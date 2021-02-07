
public class EnemyTank extends Tank {


    public EnemyTank(Vector2d position,GameMap map) {
        super(position,map);
        this.lifes = 1;

    }

    @Override
    protected void think() {
        tankIntent = TankIntent.getRandomActiveIntent();
        if(map.random.nextInt(100) >= 50) powerUpManager.activateRandomPowerUp();


        Vector2d distanceToPlayer = map.getPlayerTank().position.subtract(position);
        if(Vector2d.ZERO.equals(distanceToPlayer))return;

        if(0 == distanceToPlayer.toAreaOfRectangle()) moveDirection = MoveDirection.fromUnitVector(distanceToPlayer.toBinaryVector());
        else {
            boolean useX = map.random.nextBoolean();
            Vector2d moveVector = useX ? new Vector2d(distanceToPlayer.x,0).toBinaryVector() : new Vector2d(0, distanceToPlayer.y).toBinaryVector();
            moveDirection = MoveDirection.fromUnitVector(moveVector);
        }

        shootDirection = ShootDirection.fromVector2d(distanceToPlayer.toBinaryVector());



    }


}
