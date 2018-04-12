package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import org.w3c.dom.Text;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture passaro;
	private int movimento=0;

	@Override
	public void create () {
		batch = new SpriteBatch();
		passaro = new Texture("passaro1.png");

	}

	@Override
	public void render () {
		movimento ++;
		batch.begin();
		batch.draw(passaro, movimento, 300);
		batch.end();

	}

    @Override
    public void dispose () {
        batch.dispose();

    }

}
