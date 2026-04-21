package test.manual;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import data.player.Player;
import test.input.InputParameter;
import process.factory.PlayerFactory;

public class TestCsv {

	@Test
	public void test() {
		String filename = InputParameter.CSV_PATH;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
			String line = bufferedReader.readLine();
			int cmpt = 0;

			while ((line = bufferedReader.readLine()) != null) {
				Player player = PlayerFactory.createPlayer(line);
				assertNotNull(player);
				assertNotNull(player.getName());
				assertNotNull(player);
				assertNotNull(player.getPreSeasonAssets());
				assertTrue(player.getSalary() > 0);
				assertNotNull(player.getPosition());
				cmpt++;
			}
			assertEquals(569, cmpt);
			bufferedReader.close();

		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
