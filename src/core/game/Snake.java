package core.game;

import core.application.Input;
import core.math.Vector2f;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Snake {
	private ArrayList<Vector2f> body;
	private Vector2f front;
	private Vector2f direction;

	public Snake() {
		body = new ArrayList<>();
		body.add(new Vector2f(10, 10));
		body.add(new Vector2f(10, 9));
		body.add(new Vector2f(10, 8));

		direction = Vector2f.UP;
	}

	private int counter = 0;

	public void update() {
		ArrayList<Vector2f> next = new ArrayList<>();

		next.add(body.get(0).add(direction));
		for (int i = 1; i < body.size(); i++) {
			next.add(body.get(i).add(body.get(i - 1).sub(body.get(i))));
		}
		if (counter > 0) {
			Vector2f back = body.get(body.size() - 1);
			next.add(back);
			counter--;
		}
		body.clear();
		body.addAll(next);

		front = body.get(0);
	}

	public void input() {

		if (Input.getKeyDown(KeyEvent.VK_D) && !body.get(1).equals(body.get(0).add(Vector2f.RIGHT))) {
			direction = Vector2f.RIGHT;
		}
		if (Input.getKeyDown(KeyEvent.VK_A) && !body.get(1).equals(body.get(0).add(Vector2f.LEFT))) {
			direction = Vector2f.LEFT;
		}
		if (Input.getKeyDown(KeyEvent.VK_W) && !body.get(1).equals(body.get(0).add(Vector2f.UP))) {
			direction = Vector2f.UP;
		}
		if (Input.getKeyDown(KeyEvent.VK_S) && !body.get(1).equals(body.get(0).add(Vector2f.DOWN))) {
			direction = Vector2f.DOWN;
		}
	}

	public ArrayList<Vector2f> getBody() {
		return body;
	}

	public Vector2f getFront() {
		return front;
	}

	public Vector2f getDirection() {
		return direction;
	}

	public void collectFood() {
		counter += 10;
	}
}
