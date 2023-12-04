import java.util.Locale;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.gl2.GLUT;
import textura.Textura;

public class Cena implements GLEventListener {
	private GL2 gl;
	private GLUT glut;
	private float proporcao;
	private float xTranslateBola = 0;
	private float yTranslateBola = 1f;
	private char xDirection;
	private char yDirection = 'd';
	private float LifeAnimation = 0;
	public boolean PausedoGame = false;
	public int Fase = 0;
	private float Speed = 0.02f;
	public float BarraJogador = 0;
	private int userScore = 0;
	private int DefineVidas = 5;
	private int tonalization = GL2.GL_SMOOTH;
	private float angulo = 0;
	public float limite;
	float xMin, xMax, yMin, yMax, zMin, zMax;
	public boolean triangulo = false;
	private Textura textura;
	private int totalTextura = 1;
	public static final String Esfera = "imagens/esfera.png";
	public static final String Menu = "imagens/fundo1.png";
	public static final String NuvemGoku = "imagens/marble.jpg";
	public static final String Obstaculo = "imagens/obstaculo.jpg";
//	public static final String Heart = "./imagens/heart.png";
	public int filtro = GL2.GL_LINEAR;
	public int wrap = GL2.GL_REPEAT;
	public int modo = GL2.GL_MODULATE;
	public String texto;

	@Override
	public void init(GLAutoDrawable drawable) {
		RotaBola();

		GL2 gl = drawable.getGL().getGL2();

		angulo = 0;
		limite = 1;
		texto = " GL_DECAL";
		texto = " GL_DECAL";
		textura = new Textura(totalTextura);
		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	@Override
	public void display(GLAutoDrawable drawable) {   //Display padrão de codigo jogl, onde mapeamos todas as cenas.
		gl = drawable.getGL().getGL2();
		glut = new GLUT();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();

		switch (Fase) {
			case 0:
				ShowMenu();
				break;
			case 1:
				IniciaFaseUm();
				break;
			case 2:
				IniciaFaseDois();
				break;
			case 3:
				FimDeJogo();
				break;
		}

		iluminacaoDifusa(gl);
		iluminacaoAmbiente();
		ligaLuz(gl);
		gl.glFlush();

	}

	public void RotaBola() {
		double xRandom = -0.8f + Math.random() * 1.6f; // math random para deixar a rota da bola aleatoria
		if (xRandom > 0) {
			xDirection = 'r';
		} else {
			xDirection = 'l';
		}
		xTranslateBola = Float.valueOf(String.format(Locale.US, "%.2f", xRandom));
	}

	public void EndGame() {
		BarraJogador = 0;
		userScore = 0;
		DefineVidas = 5;
		yDirection = 'd';
		PausedoGame = false;
		Fase = 0;
		Speed = 0.028f;
		xTranslateBola = 0;
		yTranslateBola = 1f;

	}

	public void ShowMenu() {
		String size = "big";
		float left = -0.6f;
		float begin = 1.0f;


		Texto(left, begin -= 0.1f, size, "                                    Bem vindo ao Pong Jogl                                    ");
		Texto(left, begin -= 0.06f, size, "| ----------------------------------------- |");
		Texto(left, begin -= 0.06f, size, "| O objetivo principal é rebater a bola!");
		Texto(left, begin -= 0.07f, size, "| ----------------------------------------- |");
		Texto(left, begin -= 0.09f, size, "|  Teclas: ");
		Texto(left, begin -= 0.09f, size, "| - PARA INICIAR O JOGO [S]");
		Texto(left, begin -= 0.08f, size, "| - Movimentação = [<] [>] ou [A] [D] ");
		Texto(left, begin -= 0.06f, size, "| - Pause = [P] ");
		Texto(left, begin -= 0.06f, size, "| - Tela Inicial = [L] ");
		Texto(left, begin -= 0.06f, size, "| - Fechar o game = [Esc] ");
		Texto(left, begin -= 0.07f, size, "| ----------------------------------------- |");
		Texto(left, begin -= 0.09f, size, "| # Diretrizes: ");
		Texto(left, begin -= 0.08f, size, "| - Cada batida na bola serão computados 20 Pontos ");
		Texto(left, begin -= 0.06f, size, "| - Fazendo 200 Pontos avança para a Segunda Fase ");
		Texto(left, begin -= 0.06f, size, "| - Na próxima fase o sistema é de high score ");
		Texto(left, begin -= 0.07f, size, "|   BOA SORTE, VOCÊ VAI PRECISAR!");


		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Menu, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

	}

	public void IniciaFaseUm() {
		if (!PausedoGame) {
			FisicaBola();
		} else {
			Texto(-0.1f, 0, "big", "JOGO PAUSADO");
		}

		DesenhaBarra();
		DesenhaBola();

		if (userScore == 200) {
			Fase = 2;
		}
		if (DefineVidas == 0) {
			Fase = 3;
		}

		Texto(0.8f, 0.9f, "big", "Pontuação: " + userScore);

		for (int i = 1; i <= 5; i++) {
			if (DefineVidas >= i)
				DesenhaVidas(0.1f * i, true);
			else
				DesenhaVidas(0.1f * i, false);
		}

		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Menu, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

	}

	public void IniciaFaseDois() {
		Speed = 0.036f;
		if (!PausedoGame) {
			FisicaBola();
		} else {
			Texto(-0.1f, 0, "big", "JOGO PAUSADO");
		}

		DesenhaBarra();
		DesenhaBola();
		DesenhaObjetoFase2();

		if (DefineVidas == 0) {
			Fase = 3;
		}

		Texto(0.8f, 0.9f, "big", "Pontuação: " + userScore);

		for (int i = 1; i <= 5; i++) {
			if (DefineVidas >= i)
				DesenhaVidas(0.1f * i, true);
			else
				DesenhaVidas(0.1f * i, false);
		}

		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Menu, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);

	}

	public void DesenhaVidas(float pos, boolean filled) { // Vida
		gl.glPushMatrix();
		if (filled)
			gl.glColor3f(1f, 0, 0f); // Cor da Vida
		else
			gl.glColor3f(1, 1, 1); // Cor quando perde vida

		gl.glTranslatef(0.4f + pos, 0.8f, 0); // Posição da vida
		gl.glRotatef(LifeAnimation, 0.05f, 0.08f, 1); //Animação de rotação
		LifeAnimation += 0.7f;

		glut.glutSolidCube(0.03f); // Forma da Vida
		gl.glPopMatrix();
	}

	public void FimDeJogo() {
		float begin = 0.6f;
		float left = -0.16f;
		Texto(left, begin -= 0.1f, "big", "FIM DE JOGO");
		Texto(left, begin -= 0.1f, "big", "NICE TRY");
		Texto(left, begin -= 0.1f, "big", "Total de pontos: " + userScore);
		Texto(left, begin -= 0.1f, "big", "Y - Menu inicial");
		Texto(left, begin -= 0.1f, "big", "K - Fechar o jogo");

		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Menu, 0);

		gl.glBegin (GL2.GL_QUADS );
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-1, 1);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(1, 1);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(1, -1);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-1, -1);
		gl.glEnd();
		textura.desabilitarTextura(gl, 0);
	}

	public void Texto(float x, float y, String size, String phrase) {
		gl.glColor3f(1, 1, 1);
		gl.glRasterPos2f(x, y);

		if (size == "small"){
			glut.glutBitmapString(GLUT.BITMAP_8_BY_13, phrase);
		}

		if (size == "big"){
			glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, phrase);
		}
	}

	public boolean BolaBarra(float xTransBolaCorrigida) {
		float LimiteEsquerdaBarra = BarraJogador - 0.2f;
		float LimiteDireitaBarra = BarraJogador + 0.2f;

		return LimiteEsquerdaBarra <= xTransBolaCorrigida && LimiteDireitaBarra >= xTransBolaCorrigida;
	}

	public boolean YBolaObjeto(float xObj, float yObj, float bLimite, float tLimite, float xPonto) {
		return tLimite >= yObj && bLimite <= yObj && xObj == xPonto;
	}

	public boolean XBolaObjeto(float xObj, float yObj, float lLimite, float rLimite, float tLimite) {
		return lLimite <= xObj && rLimite >= xObj && yObj == tLimite;
	}

	public void FisicaBola() {
		float xTransBolaCorrigida = Float.valueOf(String.format(Locale.US, "%.1f", xTranslateBola));
		float yTransBolaCorrigida = Float.valueOf(String.format(Locale.US, "%.1f", yTranslateBola));

		if (Fase == 2 && xDirection == 'l' && YBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.1f, 0.5f, 0.2f)) {
			xDirection = 'r';
		}

		if (Fase == 2 && xDirection == 'r' && YBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.1f, 0.5f, -0.2f)) {
			xDirection = 'l';
		} else if (xTransBolaCorrigida > -1f && xDirection == 'l') {
			xTranslateBola -= Speed /2;
		} else if (xTransBolaCorrigida == -1f && xDirection == 'l') {
			xDirection = 'r';
		} else if (xTransBolaCorrigida < 1f && xDirection == 'r') {
			xTranslateBola += Speed /2;
		} else if (xTransBolaCorrigida == 1f && xDirection == 'r') {
			xDirection = 'l';
		}

		if (Fase == 2 && yDirection == 'u' && XBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.2f, 0.2f, -0.2f)) {
			yDirection = 'd';
		} else if (Fase == 2 && yDirection == 'd' && XBolaObjeto(xTransBolaCorrigida, yTransBolaCorrigida, -0.2f, 0.2f, 0.6f)) {
			yDirection = 'u';
		} else if (yTransBolaCorrigida == -0.7f && yDirection == 'd' && BolaBarra(xTransBolaCorrigida)) {
			yDirection = 'u';
			tonalization = tonalization == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
			userScore += 20;
		} else if (yTransBolaCorrigida < 0.9f && yDirection == 'u') {
			yTranslateBola += Speed;
		} else if (yTransBolaCorrigida == 0.9f && yDirection == 'u') {
			yDirection = 'd';
		} else if (yTransBolaCorrigida < -1f) {
			yTranslateBola = 1f;
			xTranslateBola = 0;
			DefineVidas--;
			RotaBola();
		} else {
			yTranslateBola -= Speed;
			tonalization = tonalization == GL2.GL_SMOOTH ? GL2.GL_FLAT : GL2.GL_SMOOTH;
		}
	}

	public void DesenhaObjetoFase2() {
		gl.glPushMatrix();
		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Obstaculo, 0);

		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(1, 1, 1);
		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-0.18f, 0.53f);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(0.18f, 0.53f);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(0.18f, -0.13f);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-0.18f, -0.13f);
		gl.glEnd();

		textura.desabilitarTextura(gl, 0);

		gl.glPopMatrix();
	}

	public void DesenhaBarra() {
		gl.glPushMatrix();
		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, NuvemGoku, 0);

		gl.glTranslatef(BarraJogador, 0, 0);
		gl.glBegin(GL2.GL_QUADS);
		gl.glColor3f(1f, 1f, 1f);

		gl.glTexCoord2f(0, limite);      gl.glVertex2f(-0.2f, -0.8f);
		gl.glTexCoord2f(limite, limite); gl.glVertex2f(0.2f, -0.8f);
		gl.glTexCoord2f(limite, 0);      gl.glVertex2f(0.2f, -0.9f);
		gl.glTexCoord2f(0, 0);           gl.glVertex2f(-0.2f, -0.9f);
		gl.glEnd();

		textura.desabilitarTextura(gl, 0);

		gl.glPopMatrix();
	}

	public void DesenhaBola() {
		gl.glPushMatrix();
		textura.setAutomatica(false);
		textura.setFiltro(filtro);
		textura.setModo(modo);
		textura.setWrap(wrap);
		textura.gerarTextura(gl, Esfera, 0);

		gl.glTranslatef(xTranslateBola, yTranslateBola, 0);

		double limit = 2 * Math.PI;
		double i;
		double cX = 0;
		double cY = 0;
		double rX = 0.1f / proporcao;
		double rY = 0.1f;

		gl.glBegin(GL2.GL_POLYGON);
		for (i = 0; i < limit; i += 0.01) {
			gl.glTexCoord2f((float)Math.cos(i) / ((float)1.3 * proporcao) + ((float)1.97 * proporcao), (float)Math.sin(i) / ((float)1.3 * proporcao) + ((float)1.97 * proporcao));
			gl.glVertex2d(cX + rX * Math.cos(i), cY + rY * Math.sin(i));
		}
		gl.glEnd();

		textura.desabilitarTextura(gl, 0);

		gl.glPopMatrix();

	}
	public void iluminacaoAmbiente() {
		float luzAmbiente[] = {0.8f, 0.7f, 0.6f, 1.0f}; //cor
		float posicaoLuz[] = {0.0f, 0.0f, 100.0f, 1.0f}; //pontual

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, luzAmbiente, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
	}

	public void iluminacaoDifusa(GL2 gl) {
		float luzDifusa[] = {1.0f, 1.0f, 1.0f, 1.0f}; //cor
		float posicaoLuz[] = {-50.0f, 0.0f, 100.0f, 0.0f}; //1.0 pontual

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, luzDifusa, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);
	}

	public void ligaLuz(GL2 gl) {
		gl.glEnable(GL2.GL_COLOR_MATERIAL);

		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glShadeModel(GL2.GL_SMOOTH);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		proporcao = (float) width / height;
		gl.glOrtho(-1, 1, -1, 1, -1, 1);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
	}

}