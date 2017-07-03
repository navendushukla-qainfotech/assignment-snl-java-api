package com.qainfotech.tap.training.snl.api;

import org.testng.annotations.Test;

import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.assertj.core.api.Assert;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import static org.assertj.core.api.Assertions.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

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
		uuid = boardReader.getUUID();
		boardReader = new Board(uuid);
		boardReader.registerPlayer("Navendu");
		boardReader.registerPlayer("Max");
	}

	@Test
	public void a_test_new_player_entry() throws FileNotFoundException, UnsupportedEncodingException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption, IOException {

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
	public void test_Dice_value_should_not_be_more_than_six()
			throws FileNotFoundException, UnsupportedEncodingException, IOException, InvalidTurnException,
			PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption {
		
		Integer dice;
		UUID uuid1 = (UUID) ((JSONObject) boardReader.data.getJSONArray("players").get(0)).get("uuid");

		dice = boardReader.rollDice(uuid1).getInt("dice");
		System.out.println(dice);
		assertThat(dice>0);
		assertThat(dice < 7);

	}


	
	@Test
	public void test_position_of_players() throws InvalidTurnException, IOException, PlayerExistsException, GameInProgressException, MaxPlayersReachedExeption{
		Board boardReader= new Board();
		boardReader.registerPlayer("a");
		boardReader.registerPlayer("b");
		boardReader.registerPlayer("c");
		boardReader.registerPlayer("d");
		for(int index=0;index<boardReader.data.getJSONArray("players").length();index++)
		{
		UUID uuid1 = (UUID) ((JSONObject) boardReader.data.getJSONArray("players").get(index)).get("uuid");
		Object current_position = ((JSONObject) boardReader.getData().getJSONArray("players").get(index)).get("position");
		int currentpos=(int)current_position;
		System.out.println("The current position for "+index+" is :"+currentpos);
		Object new_position;
		int newpos=currentpos;
		Integer dice=boardReader.rollDice(uuid1).getInt("dice");
		System.out.println("The dice value "+index+ "is"+dice);
//		Integer dice= boardReader.rollDice(uuid1).getInt("dice");
//		System.out.println(dice);
		JSONObject steps= ((JSONObject) boardReader.data.getJSONArray("steps").get(index));
		int type = (Integer) steps.get("type");
		System.out.println("the type for "+index+" is :"+type);
		if(type==2)
		{
			new_position = ((JSONObject) boardReader.getData().getJSONArray("players").get(index)).get("position");	
			newpos= (int) new_position;
			System.out.println("new position for"+index+"is"+newpos);
			assertThat(newpos>currentpos);
		}
		else if(type==1){
			new_position = ((JSONObject) boardReader.getData().getJSONArray("players").get(index)).get("position");	
			newpos= (int) new_position;
			System.out.println("new position for"+index+"is"+newpos);
			assertThat(newpos<currentpos);
			
			
		}
		else{
			new_position = ((JSONObject) boardReader.getData().getJSONArray("players").get(index)).get("position");
			int newcheck= (int)new_position;
			System.out.println("new position for "+index+ "is"+newcheck);
			newpos=currentpos+dice;
			assertThat(newcheck==newpos);
			
		}
		
		}
		
	}
	
//	@Test
//	public void test_position_95_96_97_98_99() throws FileNotFoundException, UnsupportedEncodingException, InvalidTurnException{
//		
//		
//		
//		for(int index=0;index<4;index++)
//		{
//			UUID uuid1 = (UUID) ((JSONObject) boardReader.data.getJSONArray("players").get(index)).get("uuid"); 
//			boardReader.rollDice(uuid1);
//			Object current_position = ((JSONObject) boardReader.getData().getJSONArray("players").get(index)).get("position");
//			int current_position_int= (int)current_position;
//			if(current_position_int==95)
//			{
//				
//			}
//		}
//		
//	}
}
