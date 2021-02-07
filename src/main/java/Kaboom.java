public class Kaboom {
    private int explosionDamage;
    public final Vector2d position;

    public Kaboom(int explosionDamage, Vector2d position) {
        this.explosionDamage = explosionDamage;
        this.position = position;
    }

    public void changeExplosionDamage(int delta){
        explosionDamage += delta;
    }

    public int getExplosionDamage() {
        return explosionDamage;
    }

}
