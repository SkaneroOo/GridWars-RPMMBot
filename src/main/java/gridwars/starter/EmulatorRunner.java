package gridwars.starter;

import cern.ais.gridwars.Emulator;


/**
 * Instantiates the example bots and starts the game emulator.
 */
public class EmulatorRunner {

    public static void main(String[] args) {
        RPMMBot blueBot = new RPMMBot();
        ExpandBot redBot = new ExpandBot();

        Emulator.playMatch(blueBot, redBot);
    }
}
