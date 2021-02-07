import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.stream.Collectors;

public enum TankIntent {
    NONE,
    SHOOT,
    MOVE;

    private final static LinkedList<TankIntent> activeIntents
            = Arrays.stream(values()).filter(i -> !NONE.equals(i)).collect(Collectors.toCollection(LinkedList::new));

    public static TankIntent getRandomActiveIntent(){
        Collections.shuffle(activeIntents);
        return activeIntents.getFirst();
    }

}
