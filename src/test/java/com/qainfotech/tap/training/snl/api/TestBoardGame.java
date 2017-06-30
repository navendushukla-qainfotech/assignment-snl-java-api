package com.qainfotech.tap.training.snl.api;

import org.testng.annotations.Test;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;

public class TestBoardGame {

	Board boardReader;
	BoardModel BoardModel;
	UUID uuid;
	Object playerObject;
	JSONObject player = (JSONObject) playerObject;
	// Board data;

	@BeforeTest
	public void loadDB() throws FileNotFoundException, UnsupportedEncodingException, IOException, PlayerExistsException,
			GameInProgressException, MaxPlayersReachedExeption {
		boardReader = new Board();
		// uuid = boardReader.getUUID();
		// boardReader = new Board(uuid);
		boardReader.registerPlayer("Navendu");
		boardReader.registerPlayer("Max");
	}

	@Test(priority = 1)
	public void test_new_player_entry() throws FileNotFoundException, UnsupportedEncodingException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {

		boardReader = new Board();
		boardReader.registerPlayer("Nachiketa");
	}

	@Test(priority = 2)
	public void test_start_position_Zero() {
		int flag = 0;
		for (int i = 0; i < boardReader.data.getJSONArray("players").length(); i++) {
			int pos = (int) boardReader.data.getJSONArray("players").getJSONObject(i).get("position");
			if (pos != 0) {
				flag++;
			}
		}
		assertThat(flag == 0);
	}

	@Test(expectedExceptions = PlayerExistsException.class)
	public void new_registerPlayer_should_throw_PlayerExistsException_for_same_player_name()
			throws FileNotFoundException, UnsupportedEncodingException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {

		boardReader.registerPlayer("Navendu");
	}

	@Test(expectedExceptions = MaxPlayersReachedExeption.class)
	public void registerPlayer_should_throw_theMaxPlayersReachedException_for_reaching_max_limit()
			throws MaxPlayersReachedExeption, FileNotFoundException, UnsupportedEncodingException,
			PlayerExistsException, GameInProgressException, IOException {

		boardReader.registerPlayer("Piyush");
		boardReader.registerPlayer("Manu");
		boardReader.registerPlayer("Avijit");
	}

	@Test // (expectedExceptions = NoUserWithSuchUUIDException.class)

	public void test_delete_Player_No_Such_UUID_Exists()
			throws FileNotFoundException, UnsupportedEncodingException, NoUserWithSuchUUIDException {
		int flag = 0;
		// String uuid1 = ((JSONObject)
		// boardReader.data.getJSONArray("players").get(0)).get("uuid").toString();
		UUID uuid2 = (UUID) boardReader.data.getJSONArray("players").getJSONObject(0).get("uuid");
		boardReader.deletePlayer(uuid2);
		for (int i = 0; i < boardReader.data.getJSONArray("players").length(); i++) {
			// JSONObject player
			// =boardReader.data.getJSONArray("players").getJSONObject(i);
			UUID uuid1 = (UUID) boardReader.data.getJSONArray("players").getJSONObject(i).get("uuid");
			if (uuid1.equals(uuid2)) {
				flag = 1;
			}
		}
		assertThat(flag == 0);

	}

	@Test(expectedExceptions = GameInProgressException.class)
	public void test_game_in_progress() throws InvalidTurnException, PlayerExistsException, GameInProgressException,
			MaxPlayersReachedExeption, IOException {

		UUID uuid1 = (UUID) boardReader.data.getJSONArray("players").getJSONObject(0).get("uuid");
		boardReader.rollDice(uuid1);
		boardReader.registerPlayer("ashika");
	}

	@Test(expectedExceptions = InvalidTurnException.class)
	public void test_invalid_Turn() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException {

		UUID uuid1 = (UUID) boardReader.data.getJSONArray("players").getJSONObject(2).get("uuid");
		boardReader.rollDice(uuid1);

	}
	
	@Test
	public void test(){
		
		
		
	}

	
	
	
	
	
	
	
	
	
	
	
}






