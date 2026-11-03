package com.chemquest;

import com.chemquest.app.AppLauncher;

/** Entry point. Deliberately tiny: all real work lives in other classes. */
public final class Main {
    private Main() { }

    public static void main(String[] args) {
        AppLauncher.launch();
    }
}
