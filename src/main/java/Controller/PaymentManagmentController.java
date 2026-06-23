package Controller;
import application.AppContext;
import DAO.PaymentDAO;
import DAO.PaymentDAOimp;
import Model.Payment;
import View.PaymentManagment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PaymentManagmentController {
    private final PaymentManagment view;
    private final Stage stage;
    private final AppContext ctx;
    private final AdminDashboardController dashboard;
    private final PaymentDAO paymentDAO = new PaymentDAOimp();
    private final ObservableList<Payment> list = FXCollections.observableArrayList();

    public PaymentManagmentController(Stage stage, AppContext ctx, AdminDashboardController dashboard) {
        this.stage = stage;
        this.ctx = ctx;
        this.dashboard = dashboard;
        this.view = new PaymentManagment();

        initController();
    }

    private void initController() {
        refreshData();
        view.btnRefresh.setOnAction(e -> refreshData());
        view.btnBack.setOnAction(e -> handleBack());
    }

    private void refreshData() {
        list.clear();
        list.addAll(paymentDAO.getAllPayments());
        view.paymentTable.setItems(list);

        // Realtime structural calculation metrics aggregation
        double total = 0;
        for (Payment p : list) {
            total += p.getTotalAmount();
        }

        view.lblTotalRevenue.setText(String.format("%.2f Birr", total));
    }

    private void handleBack() {
        if (dashboard != null) {
            dashboard.showDashboard();
        } else {
            stage.setScene(new Scene(dashboard.getView(), 1200, 700));
        }
    }

    public javafx.scene.layout.VBox getRootView() { return view.getView(); }
}