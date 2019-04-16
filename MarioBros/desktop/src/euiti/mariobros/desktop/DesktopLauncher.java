    package euiti.mariobros.desktop;

    import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
    import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
    import euiti.mariobros.MarioBros;

    public class DesktopLauncher {
        public static void main(String[] arg) {
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "Mario Bros";
            config.useGL30 = false;
            config.width = 640;
            config.height = 360;
            new LwjglApplication(new MarioBros(), config);
        }
    }
