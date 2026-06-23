package ui.controller;

import application.AppContext;
import javafx.stage.Stage;
import java.util.ArrayDeque;
import java.util.Deque;

public class NavigationManager {
    private final Stage stage;
    private final AppContext ctx;
    private final Deque<Runnable> backStack = new ArrayDeque<>();

    public NavigationManager(Stage stage, AppContext ctx) {
        this.stage = stage;
        this.ctx = ctx;
    }

    public AppContext getCtx() { return ctx; }
    public Stage getStage() { return stage; }

    public void go(Runnable restoreCurrent, Runnable showNext) {
        backStack.push(restoreCurrent);
        showNext.run();
    }

    public void goFresh(Runnable showNext) {
        backStack.clear();
        showNext.run();
    }

    public void back() {
        if (!backStack.isEmpty()) {
            backStack.pop().run();
        }
    }
}
