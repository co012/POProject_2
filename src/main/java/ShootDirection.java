import java.util.*;

public enum ShootDirection {
    NORTH( new Vector2d(0, 1)),
    NORTH_EAST( new Vector2d(1, 1)),
    EAST( new Vector2d(1, 0)),
    SOUTH_EAST(new Vector2d(1, -1)),
    SOUTH(new Vector2d(0, -1)),
    SOUTH_WEST(new Vector2d(-1, -1)),
    WEST(new Vector2d(-1, 0)),
    NORTH_WEST(new Vector2d(-1, 1));

    private final Vector2d vector2dCounterpart;

    ShootDirection(Vector2d vector2dCounterpart){
        this.vector2dCounterpart = vector2dCounterpart;
    }

    public static ShootDirection getClockwiseNext(ShootDirection shootDirection){
        int currentIndex = Arrays.asList(ShootDirection.values()).indexOf(shootDirection);
        int clockwiseNextIndex = (currentIndex + 1) % ShootDirection.values().length;
        return ShootDirection.values()[clockwiseNextIndex];
    }

    public static ShootDirection getClockwisePrevious(ShootDirection shootDirection){
        int currentIndex = Arrays.asList(ShootDirection.values()).indexOf(shootDirection);
        int clockwisePreviousIndex = (currentIndex + ShootDirection.values().length - 1) % ShootDirection.values().length;
        return ShootDirection.values()[clockwisePreviousIndex];
    }

    public static ShootDirection getRandomDirection() {
        List<ShootDirection> shootDirections = Arrays.asList(ShootDirection.values().clone());
        Collections.shuffle(shootDirections);
        return shootDirections.remove(0);
    }

    public static Vector2d toVector2d(ShootDirection shootDirection) {
        return shootDirection.vector2dCounterpart;
    }

    public static ShootDirection fromVector2d(Vector2d vector2d) {
        Optional<ShootDirection> optionalShootDirection =  Arrays.stream(values())
                .filter(shootDirection -> shootDirection.vector2dCounterpart.equals(vector2d))
                .findFirst();
        if(optionalShootDirection.isEmpty()){
            throw new RuntimeException("Given vector is not supported");
        }
        return optionalShootDirection.get();
    }

}
