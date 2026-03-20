package process.factory;

import data.player.Asset;
import data.player.Player;

public class PlayerFactory {

    private static Double checkPlayerNote(String playerNote) {
        if (playerNote.equals("")) {
            return 0.01;
        }
        return Double.parseDouble(playerNote);
    }

    private static Double checkPlayerTrueShooting(String trueShooting) {
        if (trueShooting.equals("")) {
            return 0.01;
        }
        return Double.parseDouble(trueShooting);
    }

    public static Player createPlayer(String line) {
        String[] data = line.split(",", -1);
        String playerId = data[0];
        String playerName = data[1];
        boolean playerIsStar = Boolean.parseBoolean(data[13]);
        double playerNote = checkPlayerNote(data[14]);

        double minutesPlayedPerMatch = Float.valueOf(data[24]);
        double pointPerMatch = Double.parseDouble(data[25]);
        double reboundPerMatch = Double.parseDouble(data[26]);
        double assistPerMatch = Double.parseDouble(data[27]);
        double interceptionPerMatch = Double.parseDouble(data[28]);
        double blockPerMatch = Double.parseDouble(data[29]);
        double lostBallPerMatch = Double.parseDouble(data[30]);
        double salary = Double.parseDouble(data[31]) / 1_000_000;
        String playerPosition = data[32];
        double trueShooting = checkPlayerTrueShooting(data[23]);

        Asset preSeasonAsset = new Asset(playerNote, minutesPlayedPerMatch, pointPerMatch, reboundPerMatch,
                assistPerMatch,
                interceptionPerMatch, blockPerMatch, lostBallPerMatch, trueShooting);
        Player player = new Player(playerId, playerName, playerNote, playerPosition, preSeasonAsset, salary,
                playerIsStar);

        return player;

    }
}
