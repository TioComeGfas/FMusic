package frodo.fmusic.Code;

import android.support.annotation.ColorInt;
import android.support.v7.widget.Toolbar;
import android.view.Window;

public class Util {

    public Util() { }

    public static void setStatusBarColor(@ColorInt int color, Window window){
        window.setStatusBarColor(color);
    }

    public static void setNavBarColor(@ColorInt int color,Window window){
        window.setNavigationBarColor(color);
    }

    public static void setToolbarColor(@ColorInt int color, Toolbar toolbar){
        toolbar.setBackgroundColor(color);
    }

    /**
     * Cambia el color de la barra de estado y la barra de navegacion
     * @param color color de color.xml
     * @param window ventana a cambiar
     */
    public static void setColor(@ColorInt int color,Window window){
        window.setStatusBarColor(color);
        window.setNavigationBarColor(color);
    }


}
