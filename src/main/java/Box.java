public class Box {
    public final Vector2d position;
    private final int baseDurability;
    private int durability;

    public Box(Vector2d position) {
        this.position = position;
        this.baseDurability = 2;
        this.durability = this.baseDurability;
    }

    public void takeDamage(int damage){
        durability-=damage;
    }

    public boolean isDestroyed(){
        return durability < 1;
    }

    public boolean isDamaged(){
        return baseDurability > durability;
    }



}
