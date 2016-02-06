package com.meters.shooty;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class Shooty extends ApplicationAdapter {

	static final int HEALTH_DEFAULT = 3;

	static final float SPEED_DEFAULT = 5f;
	static final float SIZE_PLAYER = 25;

	static final float SPREAD_ARC = .2f;
	static final float SPEED_BULLET_MULTIPLYER = 5f;

	static final float TIME_INVULNERBILITY = 120;
	static final float TIME_DEAD = 60;
	static final float TIME_EXPLODE = 10;

	SpriteBatch batch;
//	Texture img;
	Texture t;
	ShapeRenderer shapeRenderer;
	BitmapFont font;

	Tile player;
	ArrayList<Tile> enemies;
	ArrayList<Tile> bullets;
	ArrayList<Tile> enemyBullets;

	int screenWidth;
	int screenHeight;
	int tick;
	int fps;
	long lastTick;
	long lastBulletTick;
	long lastEnemyTick;

	int playerLevel = 1;

	Random rand;

	Color redColor;
	Color greenColor;

	float lastX;
	float lastY;

	@Override
	public void create () {

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
//		img = new Texture("badlogic.jpg");
		font = new BitmapFont();
		font.setColor(new Color(1,1,1,1));

		bullets = new ArrayList<Tile>();
		enemies = new ArrayList<Tile>();
		enemyBullets = new ArrayList<Tile>();

		redColor = new Color(1,0,0,1);
		greenColor = new Color(0,1,0,1);

		preparePlayer();

	}

	@Override
	public void render () {

		clearScreen();
		handleControls();
		calcFPS();

		spawnEnemy();

		handleEnemy();
		handleBullets();
		handleCollisions();

		handlePlayer();

		draw();

//		limitFPS();

	}

	private void handlePlayer(){
		if(player.state == Tile.STATE_DED){

			player.clock--;
			if(player.clock > TIME_DEAD - TIME_EXPLODE){
				player.rect.width = SIZE_PLAYER * (TIME_DEAD - player.clock);
				player.rect.height = SIZE_PLAYER * (TIME_DEAD - player.clock);
			}
			else{
				player.rect.width = 0;
				player.rect.height = 0;
			}

			if(player.clock <= 0){
				player.rect.width = SIZE_PLAYER;
				player.rect.height = SIZE_PLAYER;
				player.health = HEALTH_DEFAULT;
				player.state = Tile.STATE_BLINKING;
				player.clock = (int)TIME_INVULNERBILITY;
			}
		}
		else if(player.state == Tile.STATE_BLINKING){
			player.clock--;
			if(player.clock <= 0){
				player.state = Tile.STATE_PLAYING;
			}
		}
	}

	private void spawnEnemy(){
		if(System.currentTimeMillis() - lastEnemyTick < 200){
			return;
		}
		lastEnemyTick = System.currentTimeMillis();

		Tile newEnemy = new Tile();
		newEnemy.rect = new Rectangle(screenWidth / 2, screenHeight - 20, 20, 20);
		newEnemy.c = new Color(1,.5f,0,1);
		newEnemy.speed = SPEED_DEFAULT;

		enemies.add(newEnemy);

	}

	private void handleEnemy(){
		ArrayList<Tile> toRemove = new ArrayList<Tile>();
		for (Tile enemy : enemies) {
			enemy.rect.x += enemy.speedX;
			enemy.rect.y += enemy.speedY;

			enemy.tick();
			if(player.state == Tile.STATE_PLAYING){
				if(Math.random() < 0.003){
					addEnemyBulletToUser(enemy);
				}
			}

			if(enemy.rect.y < 0){
				toRemove.add(enemy);
			}
		}

		enemies.removeAll(toRemove);

//		System.out.println("enemies: " + enemies.size());
	}

	private void handleCollisions(){

		ArrayList<Tile> toRemove = new ArrayList<Tile>();

		for (Tile bullet : bullets) {
			for (Tile enemy : enemies) {
				if(bullet.rect.overlaps(enemy.rect)){
					toRemove.add(bullet);
					toRemove.add(enemy);
				}
			}
		}

		bullets.removeAll(toRemove);
		enemies.removeAll(toRemove);

		toRemove.clear();
		for (Tile bullet : enemyBullets) {
			if (bullet.rect.overlaps(player.rect)) {
				toRemove.add(bullet);

				if(player.state == Tile.STATE_PLAYING) {

					player.health--;

					if(player.health <= 0){
						lastX = player.rect.x;
						lastY = player.rect.y;
						player.state = Tile.STATE_DED;
						player.clock = (int) TIME_DEAD;
					}
					else {
						player.state = Tile.STATE_BLINKING;
						player.clock = (int) TIME_INVULNERBILITY;
					}

				}
			}
		}

		enemyBullets.removeAll(toRemove);
	}

	private void limitFPS(){
		try{
//			Thread.sleep((long)(12));

		}
		catch (Exception e){

		}
	}

	private void addEnemyBulletToUser(Tile enemy){
		float diffX = (enemy.rect.x + enemy.rect.width / 2) - (player.rect.x + player.rect.width / 2);
		float diffY = (enemy.rect.y + enemy.rect.height / 2) - (player.rect.y + player.rect.height / 2);

		boolean flip = diffY > 0;

		float angle = (float)Math.atan(diffX / diffY);

		diffX = (float) (Math.sin(angle) * SPEED_DEFAULT / 2.5f);
		diffY = (float) (Math.cos(angle) * SPEED_DEFAULT / 2.5f);

		Tile newBullet = new Tile();
		newBullet.rect = new Rectangle(enemy.rect.x, enemy.rect.y, 5, 5);
		newBullet.c = new Color(0,1,0,1);
		newBullet.speedX = -diffX * (flip ? 1 : -1);
		newBullet.speedY = -diffY * (flip ? 1 : -1);
		enemyBullets.add(newBullet);

	}

	private void calcFPS(){
		if(System.currentTimeMillis() - lastTick > 1000){
			fps = tick;
			tick = 0;
			lastTick = System.currentTimeMillis();
		}

		tick++;
	}

	private void preparePlayer(){
		player = new Tile();
		player.rect = new Rectangle(screenWidth / 2 - SIZE_PLAYER / 2, 10, SIZE_PLAYER, SIZE_PLAYER);
		player.c = new Color(0,0,1,1);
		player.speed = SPEED_DEFAULT;
		player.health = HEALTH_DEFAULT;
	}

	private void clearScreen(){
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	private void handleControls(){

		if(player.state == Tile.STATE_DED){
			return;
		}

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			player.rect.x -= player.speed;
			if(player.rect.x < 0){
				player.rect.x = 0;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			player.rect.x += player.speed;
			if(player.rect.x > screenWidth - SIZE_PLAYER){
				player.rect.x = screenWidth - SIZE_PLAYER;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			player.rect.y -= player.speed;
			if(player.rect.y < 0){
				player.rect.y = 0;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			player.rect.y += player.speed;
			if(player.rect.y > screenHeight - SIZE_PLAYER){
				player.rect.y = screenHeight - SIZE_PLAYER;
			}
		}

		if(Gdx.input.isKeyPressed(Input.Keys.Z)){
			addBullet();
		}


		if(Gdx.input.isKeyPressed(Input.Keys.X)){
			playerLevel++;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.C)){
			playerLevel--;
		}
	}

	private void addBullet(){

		if(System.currentTimeMillis() - lastBulletTick < 200){
			return;
		}
		lastBulletTick = System.currentTimeMillis();

		Tile newBullet = new Tile();
		newBullet.rect = new Rectangle(player.rect.x + SIZE_PLAYER / 2 - 3, player.rect.y + SIZE_PLAYER - 30, 6, 30);
		newBullet.c = new Color(1,0,0,1);
		newBullet.speedY = SPEED_DEFAULT * SPEED_BULLET_MULTIPLYER;
		bullets.add(newBullet);

		if(playerLevel >= 2){
			newBullet = new Tile();
			newBullet.rect = new Rectangle(player.rect.x + SIZE_PLAYER / 2 - 3, player.rect.y + SIZE_PLAYER - 30, 6, 30);
			newBullet.c = new Color(1,0.1f,0.1f,1);
			newBullet.speedY = SPEED_DEFAULT * SPEED_BULLET_MULTIPLYER;
			newBullet.speedX = SPEED_DEFAULT * SPREAD_ARC;
			bullets.add(newBullet);

			newBullet = new Tile();
			newBullet.rect = new Rectangle(player.rect.x + SIZE_PLAYER / 2 - 3, player.rect.y + SIZE_PLAYER - 30, 6, 30);
			newBullet.c = new Color(1,0.1f,0.1f,1);
			newBullet.speedY = SPEED_DEFAULT * SPEED_BULLET_MULTIPLYER;
			newBullet.speedX = SPEED_DEFAULT * -SPREAD_ARC;
			bullets.add(newBullet);
		}

		if(playerLevel >= 3){
			newBullet = new Tile();
			newBullet.rect = new Rectangle(player.rect.x + SIZE_PLAYER / 2 - 3, player.rect.y + SIZE_PLAYER - 30, 6, 30);
			newBullet.c = new Color(1,0.3f,0.3f,1);
			newBullet.speedY = SPEED_DEFAULT * SPEED_BULLET_MULTIPLYER;
			newBullet.speedX = SPEED_DEFAULT * SPREAD_ARC * 2;
			bullets.add(newBullet);

			newBullet = new Tile();
			newBullet.rect = new Rectangle(player.rect.x + SIZE_PLAYER / 2 - 3, player.rect.y + SIZE_PLAYER - 30, 6, 30);
			newBullet.c = new Color(1,0.3f,0.3f,1);
			newBullet.speedY = SPEED_DEFAULT * SPEED_BULLET_MULTIPLYER;
			newBullet.speedX = SPEED_DEFAULT * -SPREAD_ARC * 2;
			bullets.add(newBullet);
		}

	}

	private void handleBullets(){
		ArrayList<Tile> toRemove = new ArrayList<Tile>();
		for (Tile bullet : bullets) {
			bullet.rect.x += bullet.speedX;
			bullet.rect.y += bullet.speedY;

			if(bullet.rect.y > screenHeight + 30){
				toRemove.add(bullet);
			}
		}
		bullets.removeAll(toRemove);

		toRemove.clear();
		for (Tile bullet : enemyBullets) {
			bullet.rect.x += bullet.speedX;
			bullet.rect.y += bullet.speedY;

			if(bullet.rect.y > screenHeight
					|| bullet.rect.y < 0
					|| bullet.rect.x > screenWidth
					|| bullet.rect.x < 0){
				toRemove.add(bullet);
			}
		}
		bullets.removeAll(toRemove);


	}

	private void draw(){

		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

		//draw player

		for (Tile bullet : enemyBullets) {
			shapeRenderer.setColor(bullet.c);
			shapeRenderer.rect(bullet.rect.x, bullet.rect.y, bullet.rect.width, bullet.rect.height);
		}

		for (Tile bullet : bullets) {
			shapeRenderer.setColor(bullet.c);
			shapeRenderer.rect(bullet.rect.x, bullet.rect.y, bullet.rect.width, bullet.rect.height);
		}

		for (Tile enemy : enemies) {
			shapeRenderer.setColor(enemy.c);
			shapeRenderer.rect(enemy.rect.x, enemy.rect.y, enemy.rect.width, enemy.rect.height);
		}

		if(player.state == Tile.STATE_BLINKING){

			if(player.clock % 2 == 0){
				shapeRenderer.setColor(player.c);
				shapeRenderer.rect(player.rect.x, player.rect.y, player.rect.width, player.rect.height);
			}

			shapeRenderer.setColor(redColor);
			shapeRenderer.rect(player.rect.x, player.rect.y + player.rect.height + 10, SIZE_PLAYER, 10);

			shapeRenderer.setColor(greenColor);
			shapeRenderer.rect(player.rect.x, player.rect.y + player.rect.height + 10, SIZE_PLAYER * ((float)player.health / (float)HEALTH_DEFAULT), 10);

		}
		else {
			shapeRenderer.setColor(player.c);
			shapeRenderer.rect(player.rect.x, player.rect.y, player.rect.width, player.rect.height);
		}

		shapeRenderer.end();

		batch.begin();
//		batch.draw(img, 0, 0);
		font.draw(batch, ""+fps+" fps (press Z to fire. X to level up, C to level down) ST: " + player.state + " HP: " + player.health, 5, 15);
		batch.end();
	}
}
