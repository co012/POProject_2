import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class GameMap {

    public final Random random;

    private final int MAP_WIDTH;
    private final int MAP_HEIGHT;
    private final Vector2d MAP_DIMENSIONS;
    private final GameMapVisualiser mapVisualiser;
    private final GameInfoChangeListener gameInfoChangeListener;
    private final SpawnManager spawnManager;


    private final PlayerTank playerTank;
    private final HashMap<Vector2d, Tank> tanks;

    private final LinkedHashMap<Vector2d, Box> boxes;
    private final LinkedList<Bullet> bullets;
    private final LinkedList<Bullet> bulletsToShoot;
    private final LinkedHashMap<Vector2d, PowerUp> powerUps;
    private final LinkedHashMap<Vector2d, Kaboom> fieldsWithExplosions;


    public GameMap(int mapWidthInGameUnits, int mapHeightInGameUnits, GameMapVisualiser mapVisualiser, GameInfoChangeListener gameInfoChangeListener, GameProperties gameProperties) {
        this.random = new Random();
        this.MAP_WIDTH = mapWidthInGameUnits;
        this.MAP_HEIGHT = mapHeightInGameUnits;
        this.MAP_DIMENSIONS = new Vector2d(MAP_WIDTH, MAP_HEIGHT);
        this.mapVisualiser = mapVisualiser;
        this.gameInfoChangeListener = gameInfoChangeListener;

        this.spawnManager = new SpawnManager(gameProperties, mapElement -> switch (mapElement) {
            case TANK -> spawnEnemyTank();
            case BOX -> spawnBox();
            case POWER_UP -> spawnPowerUp();
            default -> false;
        });

        this.playerTank = new PlayerTank(new Vector2d(MAP_WIDTH / 2, MAP_HEIGHT / 2), this, gameInfoChangeListener);
        this.boxes = new LinkedHashMap<>();
        this.bullets = new LinkedList<>();
        this.bulletsToShoot = new LinkedList<>();
        this.powerUps = new LinkedHashMap<>();
        this.tanks = new HashMap<>();
        this.fieldsWithExplosions = new LinkedHashMap<>();
        tanks.put(playerTank.position, playerTank);
        draw();


    }


    public void act() {
        fieldsWithExplosions.clear();

        LinkedList<Tank> tanksAct = new LinkedList<>(tanks.values());
        tanksAct.forEach(Tank::act);
        bullets.addAll(bulletsToShoot);
        bulletsToShoot.clear();
        new LinkedList<>(bullets).forEach(Bullet::move);

        tanksAct.stream()
                .filter(tank -> tank.powerUpManager.isPowerUpActive(PowerUp.EXTRA_MOVE))
                .forEach(Tank::act);

        LinkedList<Bullet> extraMoveBullets = bullets.stream().filter(Bullet::hasExtraMove).collect(Collectors.toCollection(LinkedList::new));
        if (!extraMoveBullets.isEmpty()) {
            extraMoveBullets.forEach(Bullet::move);
        }


        spawnManager.act();
        if (tanks.values().size() < 2) spawnManager.requestTankSpawn();
        tanks.values().forEach(Tank::endTurn);
    }

    public void extraPlayerMove() {
        playerTank.act();
    }

    public PlayerTank getPlayerTank() {
        return playerTank;
    }

    public void draw() {
        mapVisualiser.clear();
        mapVisualiser.drawBoxes(boxes);
        mapVisualiser.drawExplosions(fieldsWithExplosions.values());
        mapVisualiser.drawTanks(tanks.values());
        mapVisualiser.drawBullets(bullets);
        mapVisualiser.drawPowerUps(powerUps);


    }

    public boolean spawnEnemyTank() {
        Vector2d from = playerTank.position.subtract(new Vector2d(2, 2));
        Vector2d to = playerTank.position.add(new Vector2d(3, 3));
        Optional<Vector2d> positionOptional = tryToFindFreeSpace((v) -> !v.isInsideRectangle(from, to));
        if (positionOptional.isEmpty()) return false;

        Vector2d position = positionOptional.get();
        EnemyTank tank = new EnemyTank(position, this);
        tanks.put(position, tank);
        return true;

    }

    public boolean spawnBox() {
        Optional<Vector2d> positionOptional = tryToFindFreeSpace((v) -> true);
        if (positionOptional.isEmpty()) return false;

        Vector2d position = positionOptional.get();
        Box box = new Box(position);
        boxes.put(position, box);
        return true;


    }

    public boolean spawnPowerUp() {
        Optional<Vector2d> positionOptional = tryToFindFreeSpace((v) -> true);
        if (positionOptional.isEmpty()) return false;

        powerUps.put(positionOptional.get(), PowerUp.getRandomPowerUp());
        return true;

    }

    private Optional<Vector2d> tryToFindFreeSpace(Function<Vector2d, Boolean> additionalPredicate) {
        Vector2d potentialPosition = Vector2d.getRandomVector(MAP_DIMENSIONS);

        for (int i = 0; i < MAP_DIMENSIONS.toAreaOfRectangle(); i++) {

            MapElement mapElement = getMapElementOnField(potentialPosition);
            if (mapElement.equals(MapElement.FREE_SPACE)) {
                if (additionalPredicate.apply(potentialPosition)) {
                    return Optional.of(potentialPosition);
                }
            }

            potentialPosition = potentialPosition.getLinearNextInRectangle(Vector2d.ZERO, MAP_DIMENSIONS);

        }
        return Optional.empty();

    }

    public void spawnBullet(Bullet bullet) {
        bulletsToShoot.add(bullet);
    }

    public MapElement getMapElementOnField(Vector2d position) {
        if (!isPositionInsideMapBoundary(position)) return MapElement.MAP_BOUNDARY;
        if (boxes.containsKey(position)) return MapElement.BOX;
        if (tanks.containsKey(position)) return MapElement.TANK;
        if (fieldsWithExplosions.containsKey(position)) return MapElement.EXPLOSION;
        if (bullets.stream().anyMatch(bullet -> bullet.getPosition().equals(position))) return MapElement.BULLET;
        if (powerUps.containsKey(position)) return MapElement.POWER_UP;
        return MapElement.FREE_SPACE;
    }


    public boolean isXValid(int x) {
        return x >= 0 && x < MAP_WIDTH;
    }

    public boolean isYValid(int y) {
        return y >= 0 && y < MAP_HEIGHT;
    }

    public boolean isPositionInsideMapBoundary(Vector2d position) {
        return isYValid(position.y) && isXValid(position.x);
    }

    public void onTankPositionChange(Tank tank, final Vector2d oldPosition, final Vector2d newPosition) {
        tanks.remove(oldPosition);
        MapElement mapElement = getMapElementOnField(newPosition);
        if (mapElement.equals(MapElement.TANK)) {
            Tank tank2 = tanks.remove(newPosition);
            int damage;
            if (!tank.powerUpManager.isPowerUpActive(PowerUp.INVINCIBILITY) && !tank2.powerUpManager.isPowerUpActive(PowerUp.INVINCIBILITY)) {
                damage = Math.min(tank.getLives(), tank2.getLives());
                tank2.takeDamage(damage);
                tank.takeDamage(damage);
            } else if (tank.powerUpManager.isPowerUpActive(PowerUp.INVINCIBILITY)) {
                damage = tank2.getLives();
                tank2.takeDamage(damage);
            } else if (tank2.powerUpManager.isPowerUpActive(PowerUp.INVINCIBILITY)) {
                damage = tank.getLives();
                tank.takeDamage(damage);
            } else {
                tank.forceDestroy();
                tank2.forceDestroy();
                damage = 1000;
            }

            if (fieldsWithExplosions.containsKey(newPosition))
                fieldsWithExplosions.get(newPosition).changeExplosionDamage(damage * 2);
            else fieldsWithExplosions.put(newPosition, new Kaboom(damage * 2, newPosition));

            if (tank.isAlive()) {
                tanks.put(newPosition, tank);
            } else gameInfoChangeListener.onTankDestroyed();

            if (tank2.isAlive()) {
                tanks.put(newPosition, tank2);
            } else gameInfoChangeListener.onTankDestroyed();


        } else if (mapElement.equals(MapElement.EXPLOSION)) {
            Kaboom kaboom = fieldsWithExplosions.get(newPosition);
            tank.takeDamage(kaboom.getExplosionDamage());
            if (tank.isAlive()) {
                tanks.put(newPosition, tank);
            } else {
                kaboom.changeExplosionDamage(kaboom.getExplosionDamage() + tank.getLives());
            }
        } else if (mapElement.equals(MapElement.BULLET)) {
            Bullet bullet = bullets.stream()
                    .filter(b -> b.getPosition().equals(newPosition))
                    .findFirst()
                    .orElseThrow();
            int explosionDamage = bullet.getAttackDamage();
            tank.takeDamage(bullet.getAttackDamage());
            if (tank.isAlive()) {
                tanks.put(newPosition, tank);
            } else {
                explosionDamage *= 2;
                gameInfoChangeListener.onTankDestroyed();
            }
            bullet.handleCollision();
            if (!bullet.isStillGoing()) bullets.remove(bullet);
            fieldsWithExplosions.put(bullet.getPosition(), new Kaboom(explosionDamage, bullet.getPosition()));
        } else if (mapElement.equals(MapElement.POWER_UP)) {
            PowerUp powerUp = powerUps.remove(newPosition);
            tank.powerUpManager.powerUpCollected(powerUp);
            tanks.put(newPosition, tank);
        } else tanks.put(newPosition, tank);

    }

    public void onBulletMove(Bullet bullet, Vector2d oldPosition, Vector2d newPosition) {
        if (!bullet.isStillGoing()) bullets.remove(bullet);

        MapElement mapElement = getMapElementOnField(newPosition);

        if (mapElement.equals(MapElement.BOX)) {
            Box box = boxes.get(newPosition);
            box.takeDamage(bullet.getAttackDamage());
            if (box.isDestroyed()) boxes.remove(box.position);
            fieldsWithExplosions.put(box.position, new Kaboom(bullet.getAttackDamage(), box.position));

        } else if (mapElement.equals(MapElement.TANK)) {
            Tank tank = tanks.get(newPosition);
            int explosionDamage = bullet.getAttackDamage();
            tank.takeDamage(bullet.getAttackDamage());
            if (!tank.isAlive()) {
                explosionDamage *= 2;
                tanks.remove(tank.position);
                gameInfoChangeListener.onTankDestroyed();
            }
            fieldsWithExplosions.put(bullet.getPosition(), new Kaboom(explosionDamage, bullet.getPosition()));
        }

    }
}
