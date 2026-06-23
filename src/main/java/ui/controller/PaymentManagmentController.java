package ui.controller;

import application.AppContext;
import application.ModelConverter;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.model.Payment;
import ui.view.PaymentManagment;

public class PaymentManagmentController {
    private final PaymentManagment view;
    private final Stage stage;
    private final AppContext ctx;
    private final NavigationManager nav;
    private final AdminDashboardController dashboard;
    private final ObservableList<Payment> list = FXCollections.observableArrayList();

    public PaymentManagmentController(
            Stage stage,
            AppContext ctx,
            NavigationManager nav,
            AdminDashboardController dashboard) {
        this.stage = stage;
        this.ctx = ctx;
        this.nav = nav;
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
        list.addAll(
                ctx.paymentRepo.findAll().stream()
                        .map(ModelConverter::toOldPayment)
                        .collect(Collectors.toList()));
        view.paymentTable.setItems(list);

        // Realtime structural calculation metrics aggregation
        double total = 0;
        for (Payment p : list) {
            total += p.getTotalAmount();
        }

        view.lblTotalRevenue.setText(String.format("%.2f Birr", total));
    }

    private void handleBack() {
        dashboard.showDashboard();
    }

    public javafx.scene.layout.VBox getRootView() {
        return view.getView();
    }
}
