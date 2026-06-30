package ui.controller.admin;

import application.AppContext;
import domain.model.Payment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ui.view.admin.PaymentManagementPage;

public class PaymentManagementController {
    private final PaymentManagementPage view;
    private final AppContext ctx;
    private final AdminDashboardController dashboard;
    private final ObservableList<Payment> list = FXCollections.observableArrayList();

    public PaymentManagementController(AppContext ctx, AdminDashboardController dashboard) {
        this.ctx = ctx;
        this.dashboard = dashboard;
        this.view = new PaymentManagementPage();

        initController();
    }

    private void initController() {
        refreshData();
        view.btnRefresh.setOnAction(e -> refreshData());
        view.btnBack.setOnAction(e -> handleBack());
    }

    private void refreshData() {
        list.clear();
        list.addAll(ctx.paymentRepo.findAll());
        view.paymentTable.setItems(list);

        // Realtime structural calculation metrics aggregation
        double total = 0;
        for (Payment p : list) {
            total += p.getTotalAmount();
        }

        view.lblTotalRevenue.setText(String.format("%.2f Birr", Double.valueOf(total)));
    }

    private void handleBack() {
        dashboard.showDashboard();
    }

    public javafx.scene.layout.VBox getRootView() {
        return view.getView();
    }
}
