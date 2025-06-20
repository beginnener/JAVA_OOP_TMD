package src.presenter;

import javax.swing.table.DefaultTableModel;
import src.model.UserModel;
import src.model.UserScore;
import src.view.MainMenuView;

import java.util.List;

/*
 * MainMenuPresenter.java
 * Presenter untuk MainMenuView.
 * Kelas ini mengelola logika bisnis untuk menu utama,
 */
public class MainMenuPresenter {
    private MainMenuView view;
    private UserModel userModel;

    // konstruktor
    public MainMenuPresenter(MainMenuView view) {
        this.view = view;
        this.userModel = new UserModel();
    }

    // menginisialisasi presenter
    public void loadTable() {
        List<UserScore> users = userModel.getAllUsers();
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Username", "Skor", "Count"}, 0);
        for (UserScore user : users) {
            model.addRow(new Object[]{user.getUsername(), user.getScore(), user.getCount()});
        }
        view.setTableModel(model);
    }

    // mendapatkan username dari view dan melemparnya ke presenter
    public void onStartClicked(String username) {
        if (username.isEmpty()) {
            view.showError("Username tidak boleh kosong!");
            return;
        }

        UserScore user = userModel.getUser(username);
        if (user == null) {
            userModel.insertUser(new UserScore(username, 0, 0));
        }
        view.switchToGame(username);
    }
}