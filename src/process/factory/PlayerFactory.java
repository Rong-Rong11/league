/*
 * Decompiled with CFR 0.152.
 */
package process.factory;

import data.player.Asset;
import data.player.Player;

public class PlayerFactory {
    private static Double checkPlayerNote(String string) {
        if (string.equals("")) {
            return 0.01;
        }
        return Double.parseDouble(string);
    }

    private static Double checkPlayerTrueShooting(String string) {
        if (string.equals("")) {
            return 0.01;
        }
        return Double.parseDouble(string);
    }

    public static Player createPlayer(String string) {
        String[] stringArray = string.split(",", -1);
        String string2 = stringArray[0];
        String string3 = stringArray[1];
        boolean bl = Boolean.parseBoolean(stringArray[13]);
        double d = PlayerFactory.checkPlayerNote(stringArray[14]);
        double d2 = Float.valueOf(stringArray[24]).floatValue();
        double d3 = Double.parseDouble(stringArray[25]);
        double d4 = Double.parseDouble(stringArray[26]);
        double d5 = Double.parseDouble(stringArray[27]);
        double d6 = Double.parseDouble(stringArray[28]);
        double d7 = Double.parseDouble(stringArray[29]);
        double d8 = Double.parseDouble(stringArray[30]);
        double d9 = Double.parseDouble(stringArray[31]) / 1000000.0;
        String string4 = stringArray[32];
        double d10 = PlayerFactory.checkPlayerTrueShooting(stringArray[23]);
        Asset asset = new Asset(d, d2, d3, d4, d5, d6, d7, d8, d10);
        Player player = new Player(string2, string3, d, string4, asset, d9, bl);
        return player;
    }
}
