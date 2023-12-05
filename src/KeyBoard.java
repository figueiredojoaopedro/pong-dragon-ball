import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;

public class KeyBoard implements KeyListener {
    private Cena cena;

    public KeyBoard(Cena cena) {
        this.cena = cena;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Check if the game is paused
            // Handle keys when the game is not paused
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                moveBarraJogador(-0.1f);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                moveBarraJogador(0.1f);
                break;
            case KeyEvent.VK_P:
                cena.PausedoGame = !cena.PausedoGame;
                break;
            case KeyEvent.VK_S:
                if (cena.Fase == 0) {
                    cena.Fase = 1;
                }
                break;
            case KeyEvent.VK_L:
                if (cena.Fase > 0) {
                    cena.Fase = 0;
                    cena.EndGame();
                }
                break;
            case KeyEvent.VK_Y:
                if (cena.Fase == 3) {
                    cena.Fase = 0;
                    cena.EndGame();
                }
                break;
            case KeyEvent.VK_K:
                if (cena.Fase == 3) {
                    System.exit(0);
                }
                break;
        }
        }


    @Override
    public void keyReleased(KeyEvent e) {

    }

    private void moveBarraJogador(float delta) {
        if (cena.BarraJogador + delta >= -0.8 && cena.BarraJogador + delta <= 0.8) {
            cena.BarraJogador += delta;
        }
    }}

