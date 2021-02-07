public enum MoveDirection {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static MoveDirection getClockwiseNext(MoveDirection moveDirection) {
        return switch (moveDirection) {
            case UP -> RIGHT;
            case RIGHT -> DOWN;
            case DOWN -> LEFT;
            case LEFT -> UP;
        };
    }

    public static MoveDirection getClockwisePrevious(MoveDirection moveDirection) {
        return switch (moveDirection) {
            case UP -> LEFT;
            case RIGHT -> UP;
            case DOWN -> RIGHT;
            case LEFT -> DOWN;
        };
    }

    public static Vector2d toUnitVector(MoveDirection moveDirection) {
        return switch (moveDirection) {
            case LEFT -> new Vector2d(-1, 0);
            case RIGHT -> new Vector2d(1, 0);
            case DOWN -> new Vector2d(0, -1);
            case UP -> new Vector2d(0, 1);
        };
    }

    public static MoveDirection fromUnitVector(Vector2d vec){
        if (vec.x == 0){
            return vec.y == 1 ? UP:DOWN;
        }
        else return vec.x == 1 ? RIGHT : LEFT;

    }

}
