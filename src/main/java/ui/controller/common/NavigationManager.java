package ui.controller.common;

import application.AppContext;
import java.util.ArrayDeque;
import java.util.Deque;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NavigationManager {
    private static final Logger log = LoggerFactory.getLogger(NavigationManager.class);
    private final Stage stage;
    private final AppContext ctx;
    private final Deque<Runnable> backStack = new ArrayDeque<>();

    public NavigationManager(Stage stage, AppContext ctx) {
        this.stage = stage;
        this.ctx = ctx;
    }

    public AppContext getCtx() {
        return ctx;
    }

    public Stage getStage() {
        return stage;
    }

    public void go(Runnable restoreCurrent, Runnable showNext) {
        backStack.push(restoreCurrent);
        log.debug("Navigating forward (stack size: {})", backStack.size());
        showNext.run();
    }

    public void goFresh(Runnable showNext) {
        backStack.clear();
        log.debug("Navigating fresh (stack cleared)");
        showNext.run();
    }

    public void back() {
        if (!backStack.isEmpty()) {
            log.debug("Navigating back (stack size before pop: {})", backStack.size());
            backStack.pop().run();
        } else {
            log.warn("Attempted to navigate back with empty stack");
        }
    }
}
