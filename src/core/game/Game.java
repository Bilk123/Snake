package core.game;

import core.application.Application;
import core.application.Time;
import core.math.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Application {
	private int size;
	private int[] board;
	private Snake snake;
	private Random random;

	public static final int EMPTY = 0;
	public static final int SNAKE = 1;
	public static final int FOOD = 2;
	public static final int INVALID = 3;
	private boolean endGame = false;

	public Game(int size) {
		super(800, 800, "Game");
		this.size = size;
		board = new int[size * size];
		setBuffer(EMPTY);
		random = new Random();
	}

	public void setSpace(int x, int y, int space) {
		if (x >= 0 && x < size && y >= 0 && y < size) {
			board[x + y * size] = space;
		} else {
			System.out.println("invalid space set: (" + x + ", " + y + ")");
		}
	}

	public void setBuffer(int space) {
		for (int i = 0; i < size * size; i++) {
			board[i] = space;
		}
	}

	public int getSpace(int x, int y) {
		if (x >= 0 && x < size && y >= 0 && y < size)
			return board[x + y * size];
		else
			return INVALID;
	}

	@Override
	public void start() {
		snake = new Snake();
		drawSnake();
		placeFood();
	}

	@Override
	public void render(Graphics2D g2d) {
		float s = getWindow().getWidth() / size;
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				switch (getSpace(x, y)) {
					case FOOD:
						g2d.setColor(Color.RED);
						g2d.fillRect((int) (x * s), (int) (getWindow().getHeight() - ((y + 1) * s)),
								(int) (s),
								(int) (s));
						break;
					case SNAKE:
						g2d.setColor(Color.WHITE);
						g2d.fillRect((int) (x * s), (int) (getWindow().getHeight() - ((y + 1) * s)),
								(int) (s),
								(int) (s));
						break;
				}
			}
		}
	}

	private float timer = 0;

	@Override
	public void update() {
		timer += Time.getDeltaTime();
		snake.input();
		if (timer > 0.1f) {
			if (!endGame) {
				clearSnake();
				snake.update();
				Vector2f next = snake.getFront();
				int space = getSpace((int) next.x, (int) next.y);
				drawSnake();
				if (space == INVALID) {
					endGame = true;
				} else if (space == FOOD) {
					snake.collectFood();
					placeFood();
				}
				for (int i = 1; i < snake.getBody().size(); i++) {
					if (snake.getBody().get(i).equals(next))
						endGame = true;
				}
			}
			timer = 0;
		}
	}

	private void placeFood() {
		ArrayList<Vector2f> spaces = getEmptySpaces();
		Vector2f nextSpawn = spaces.get(random.nextInt(spaces.size()));
		setSpace((int) nextSpawn.x, (int) nextSpawn.y, FOOD);
	}

	private void clearSnake() {
		for (Vector2f v : snake.getBody()) {
			setSpace((int) v.x, (int) v.y, EMPTY);
		}
	}

	private void drawSnake() {
		for (Vector2f v : snake.getBody()) {
			setSpace((int) v.x, (int) v.y, SNAKE);
		}
	}

	private ArrayList<Vector2f> getEmptySpaces() {
		ArrayList<Vector2f> spaces = new ArrayList<>();
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				if (getSpace(x, y) == EMPTY) {
					spaces.add(new Vector2f(x, y));
				}
			}
		}
		return spaces;
	}
}
