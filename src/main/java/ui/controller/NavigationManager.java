package ui.controller;
import javafx.stage.Stage;
import java.util.Stack;
public class NavigationManager {
    private static final Stack<String> history = new Stack<>();

    public static void push(String pageName) {
        history.push(pageName);
    }

    public static String pop() {
        if (!history.isEmpty()) {
            return history.pop();
        }
        return null;
    }

    public static String peek() {
        return history.isEmpty() ? null : history.peek();
    }

    public static void clear() {
        history.clear();
    }
}


