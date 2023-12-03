package textura;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;

public class Textura {
    private Texture[] vetTextures;
    private float width;
    private float height;
    private int filtro;
    private int modo;
    private int wrap;
    private boolean automatica;

    public Textura(int totalTextura) {
        vetTextures = new Texture[totalTextura];
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setFiltro(int filtro) {
        this.filtro = filtro;
    }

    public int getFiltro() {
        return filtro;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }

    public int getModo() {
        return modo;
    }

    public void setWrap(int wrap) {
        this.wrap = wrap;
    }

    public int getWrap() {
        return wrap;
    }

    public void setAutomatica(boolean automatica) {
        this.automatica = automatica;
    }

    public boolean getAutomatica() {
        return automatica;
    }

    public void gerarTextura(GL2 gl, String fileName, int indice) {
        carregarTextura(fileName, indice);
        Texture tex = vetTextures[indice];
        tex.bind(gl);
        tex.enable(gl);

        tex.setTexParameteri(gl, GL2.GL_TEXTURE_MIN_FILTER, filtro);
        tex.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, filtro);

        tex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_S, wrap);
        tex.setTexParameteri(gl, GL2.GL_TEXTURE_WRAP_T, wrap);

        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, modo);
    }

    private void carregarTextura(String fileName, int indice) {
        try {
            vetTextures[indice] = TextureIO.newTexture(new File(fileName), true);
            updateTextureDimensions(indice);
        } catch (IOException e) {
            System.err.println("Erro ao carregar o arquivo de textura: " + fileName);
            e.printStackTrace();
        }
    }

    private void updateTextureDimensions(int indice) {
        Texture tex = vetTextures[indice];
        this.width = tex.getWidth();
        this.height = tex.getHeight();
    }

    public void desabilitarTexturaAutomatica(GL2 gl) {
        // Desabilita a geracao de textura
        gl.glDisable(GL2.GL_TEXTURE_GEN_S);
        gl.glDisable(GL2.GL_TEXTURE_GEN_T);
    }

    public void desabilitarTextura(GL2 gl, int indice) {
        Texture tex = vetTextures[indice];
        tex.disable(gl); // Desabilita as texturas
        tex.destroy(gl); // Destroi tudo
        if (this.automatica) {
            desabilitarTexturaAutomatica(gl);
        }
    }
}
