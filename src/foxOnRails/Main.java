package foxOnRails;

import foxOnRails.engine.CoreEngine;

public class Main {

    private static CoreEngine coreEngine;

    public static void main(String[] args) {
        coreEngine = new CoreEngine(new FoxOnRails());
        coreEngine.start();
    }
}
