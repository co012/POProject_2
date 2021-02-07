public class Bullet {
    private static final int BASE_DAMAGE = 1;

    private Vector2d position;
    private ShootDirection shootDirection;
    private final boolean extraMove;
    private final boolean rubberBullet;
    private final boolean penetrating;
    private final GameMap map;
    private boolean stillGoing;


    public Bullet(Vector2d position, ShootDirection shootDirection, GameMap map, boolean extraMove, boolean rubberBullet, boolean penetrating) {
        this.position = position;
        this.shootDirection = shootDirection;
        this.extraMove = extraMove;
        this.rubberBullet = rubberBullet;
        this.penetrating = penetrating;
        this.stillGoing = true;
        this.map = map;


    }

    public Vector2d getPosition() {
        return position;
    }


    public boolean hasExtraMove() {
        return extraMove;
    }

    public boolean isStillGoing() {
        return stillGoing;
    }

    public int getAttackDamage() {
        return penetrating ? BASE_DAMAGE * 2 : BASE_DAMAGE;
    }

    public void reflect(boolean reflectXAxis, boolean reflectYAxis) {
        Vector2d shootDirectionVector = ShootDirection.toVector2d(shootDirection);
        Vector2d newShootDirectionVector = shootDirectionVector.getReflection(reflectXAxis, reflectYAxis);
        this.shootDirection = ShootDirection.fromVector2d(newShootDirectionVector);
    }


    public void handleCollision() {
        if (penetrating) return;
        stillGoing = false;
    }

    public void move() {
        if (!stillGoing) return;
        Vector2d potentialNewPosition = position.add(ShootDirection.toVector2d(shootDirection));
        MapElement potentialMapElementToCollideWith = map.getMapElementOnField(potentialNewPosition);
        if (rubberBullet && (potentialMapElementToCollideWith.equals(MapElement.BOX) || potentialMapElementToCollideWith.equals(MapElement.MAP_BOUNDARY))) {
            if (potentialMapElementToCollideWith.equals(MapElement.BOX)) reflect(true, true);
            else reflect(!map.isXValid(potentialNewPosition.x), !map.isYValid(potentialNewPosition.y));
            potentialNewPosition = position;
            potentialMapElementToCollideWith = map.getMapElementOnField(potentialNewPosition);
        }

        if(potentialMapElementToCollideWith.equals(MapElement.MAP_BOUNDARY)){
            stillGoing = false;

        }
        else if (potentialMapElementToCollideWith.equals(MapElement.TANK) || potentialMapElementToCollideWith.equals(MapElement.BOX)){
            stillGoing = penetrating;
        }

        Vector2d oldPosition = position;
        position = potentialNewPosition;
        map.onBulletMove(this,oldPosition,position);



    }

    public ShootDirection getShootDirection() {
        return shootDirection;
    }

}
