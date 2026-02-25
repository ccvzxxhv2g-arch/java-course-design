package com.zct.listener;

import java.util.*;

public class NavigationListener {
    private static final List<Runnable> refreshListeners = new ArrayList<>();

    public static void addRefreshListener(Runnable listener) {
        refreshListeners.add(listener);
    }

    public static void fireRefreshEvent(String pageName) {
        for (Runnable listener : refreshListeners) {
            listener.run();
        }
    }
}


