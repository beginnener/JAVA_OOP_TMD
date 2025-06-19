package src.presenter;

/* kontrak untuk GamePresenter 
 * Kontrak ini mendefinisikan metode yang harus diimplementasikan oleh GamePresenter
*/
public interface KontrakGamePresenter {
    void startGame();
    void handleKeyPress(int keyCode);   
}