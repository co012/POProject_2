public interface GameInfoChangeListener {
    GameInfoChangeListener voidGameChangeListener = new GameInfoChangeListener() {
        @Override
        public void onTankDestroyed() {

        }

        @Override
        public void onPowerUpsNumberChange(PowerUp powerUp, int newNumber) {

        }

        @Override
        public void onPlayerLifesNumberChange(int newNumber) {

        }
    };

    void onTankDestroyed();
    void onPowerUpsNumberChange(PowerUp powerUp,int newNumber);
    void onPlayerLifesNumberChange(int newNumber);


}
