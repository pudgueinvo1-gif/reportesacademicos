package reports.academicos;

import reports.academicos.view.ConsolaView;

/**
 * Clase principal del sistema de reportes académicos.
 * Punto de entrada del programa.
 * 
 
 * @version 1.0
 */
public class ReportesAcademicos {

    public static void main(String[] args) {
        ConsolaView vista = new ConsolaView();
        vista.iniciar();
    }
}