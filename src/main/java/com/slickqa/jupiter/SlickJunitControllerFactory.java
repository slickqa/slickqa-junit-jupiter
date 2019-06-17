package com.slickqa.jupiter;

/**
 * This is a factory for SlickTestNGController.  If you want to subclass and extend the default functionality to
 * override defaults you need to replace the Controller
 */
public class SlickJunitControllerFactory {

    public static SlickJunitController INSTANCE = null;

    public static synchronized SlickJunitController getControllerInstance() {
        if(SlickJunitControllerFactory.INSTANCE == null) {
            try {
                INSTANCE = new SlickJunitController();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return SlickJunitControllerFactory.INSTANCE;
    }
}