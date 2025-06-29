package App;


import Persistencia.*;
import Vista.*;

public class Principal {
    public static void main(String[] args) {
        DataTrabajador dataHandler = new ArchivoTrabajadorDAO();
        MenuUI menu = new MenuSwing(dataHandler);
        menu.iniciar();
    }
}
