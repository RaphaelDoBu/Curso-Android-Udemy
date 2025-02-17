package com.android.flappybird2;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import org.w3c.dom.css.Rect;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture[] passaros;
	private Texture fundo;

	private Circle passaroCirculo;
	private Rectangle retanguloCanoTopo;
	private Rectangle retanguloCanoBaixo;
//	private ShapeRenderer shape;
	private float larguraDispositivo;
	private float alturaDispositivo;
	private Texture gameOver;
	private BitmapFont mensagem;
	private float variacao = 0;
	private float velocidadeQueda=0;
	private float posicaoInicialvertical;
	private Texture canoBaixo;
	private Texture canoTopo;
	private float posicaoMovimentoCanoHor;
	private float espacoCanos;
	private float deltaTime;
	private Random randomico;
	private float alturaCanosRandomico;
	private int estadoJogo =0;  //0-deslidado, 1-ligado, 2-game over
	private BitmapFont fonte;
	private int pontuacao=0;
	private boolean marcouPonto;
	//camera
	private OrthographicCamera camera;
	private Viewport viewport;
	private final float VIRTUAL_WIDTH = 768;
	private final float VIRTUAL_HEIGHT = 1024;

	@Override
	public void create () {
		batch = new SpriteBatch();
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		canoBaixo = new Texture("cano_baixo.png");
		canoTopo = new Texture("cano_topo.png");
		gameOver= new Texture("game_over.png");
		randomico = new Random();
		passaroCirculo=new Circle();
//		retanguloCanoBaixo=new Rectangle();
//		retanguloCanoTopo=new Rectangle();
//		shape=new ShapeRenderer();

		//configuracoes da camera
		camera= new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2,VIRTUAL_HEIGHT/2, 0);
		viewport= new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);


		mensagem= new BitmapFont();
		mensagem.setColor(Color.WHITE);
		mensagem.getData().setScale(3);
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(6);
		fundo=new Texture("fundo.png");
		larguraDispositivo=VIRTUAL_WIDTH;
		alturaDispositivo=  VIRTUAL_HEIGHT;


		posicaoInicialvertical=alturaDispositivo/2;
		posicaoMovimentoCanoHor = larguraDispositivo+100;
		espacoCanos= 200;
	}

	@Override
	public void render () {
		camera.update();

		//limpar frames anteriores
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		deltaTime = Gdx.graphics.getDeltaTime();
		variacao += deltaTime * 10;

		if (variacao > 2) {
			variacao = 0;
		}
		if(estadoJogo==0){
			if(Gdx.input.justTouched()){
				estadoJogo = 1;
			}
		}
		else {

			velocidadeQueda++;
			if (posicaoInicialvertical > 0 || velocidadeQueda < 0) {
				posicaoInicialvertical -= velocidadeQueda;
			}

			if(estadoJogo==1){
				posicaoMovimentoCanoHor -= deltaTime * 200;
				if (Gdx.input.justTouched()) {
					velocidadeQueda = -12;
				}


				//verifica se o cano saiu da tela
				if (posicaoMovimentoCanoHor < -canoTopo.getWidth()) {
					posicaoMovimentoCanoHor = larguraDispositivo;
					alturaCanosRandomico = randomico.nextInt(400) - 200;
					marcouPonto=false;
				}
				//verifica pontuaçao
				if(posicaoMovimentoCanoHor < 40){
					if(!marcouPonto){
						pontuacao++;
						marcouPonto =true;
					}
				}
			}
			else{ //tela de game over
				if(Gdx.input.justTouched()){
					estadoJogo=0;
					pontuacao=0;
					velocidadeQueda=0;
					posicaoInicialvertical=alturaDispositivo/2;
					posicaoMovimentoCanoHor= larguraDispositivo+100;
				}

			}

		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		batch.draw(fundo, 0,0, larguraDispositivo,alturaDispositivo);
		batch.draw(passaros[(int) variacao], 60, posicaoInicialvertical);
		batch.draw(canoTopo, posicaoMovimentoCanoHor, alturaDispositivo/2 + espacoCanos
		+ alturaCanosRandomico);
		batch.draw(canoBaixo, posicaoMovimentoCanoHor, alturaDispositivo/2 -canoBaixo.getHeight()
		-espacoCanos/2 + alturaCanosRandomico);
		fonte.draw(batch, String.valueOf(pontuacao),larguraDispositivo/2, alturaDispositivo-50);

		if(estadoJogo==2){
			batch.draw(gameOver, larguraDispositivo/2 - gameOver.getWidth()/2, alturaDispositivo/2);
			mensagem.draw(batch, "Toque para reiniciar",larguraDispositivo/2 - 200,
					alturaDispositivo/2 - gameOver.getHeight()/2);
		}

		batch.end();

		passaroCirculo.set(60 + passaros[0].getWidth()/2,posicaoInicialvertical + passaros[0].getHeight()/2,
				passaros[0].getWidth()/2);

		retanguloCanoTopo=new Rectangle(posicaoMovimentoCanoHor, alturaDispositivo/2 + espacoCanos
				+ alturaCanosRandomico, canoTopo.getWidth(), canoTopo.getHeight());
		retanguloCanoBaixo=new Rectangle(posicaoMovimentoCanoHor, alturaDispositivo/2 -canoBaixo.getHeight()
				-espacoCanos/2 + alturaCanosRandomico, canoBaixo.getWidth(), canoBaixo.getHeight());


		//desenhar formas
//		shape.begin(ShapeRenderer.ShapeType.Filled);
//		shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
//		shape.rect(retanguloCanoBaixo.x, retanguloCanoBaixo.y, retanguloCanoBaixo.width,
//				retanguloCanoBaixo.height);
//		shape.rect(retanguloCanoTopo.x, retanguloCanoTopo.y, retanguloCanoTopo.width,
//				retanguloCanoTopo.height);
//		shape.setColor(Color.RED);
//		shape.end();

		//teste colisao
		if(Intersector.overlaps(passaroCirculo, retanguloCanoBaixo) ||
				Intersector.overlaps(passaroCirculo, retanguloCanoTopo)
				|| posicaoInicialvertical <= 0 || posicaoInicialvertical >= alturaDispositivo){
			estadoJogo=2;
		}

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	//	@Override
//	public void dispose () {
//		batch.dispose();
//		passaros.dispose();
//	}
}
