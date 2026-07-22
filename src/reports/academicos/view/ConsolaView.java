package reports.academicos.view;

import reports.academicos.controller.SistemaController;
import java.util.Scanner;

public class ConsolaView {
    private SistemaController controller;
    private Scanner scanner;

    public ConsolaView() {
        this.controller = new SistemaController();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("    SISTEMA DE REPORTES ACADEMICOS");
        System.out.println("=".repeat(60));
        
        while (true) {
            System.out.println("\n--- MENU PRINCIPAL ---");
            System.out.println("1. Iniciar sesion");
            System.out.println("2. Salir");
            System.out.print("Seleccione: ");
            
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    login();
                    break;
                case 2:
                    System.out.println("Hasta luego!");
                    controller.guardarDatos();
                    return;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }

    private void login() {
        System.out.println("\n--- INICIAR SESION ---");
        System.out.print("Usuario: ");
        String usuario = scanner.nextLine();
        System.out.print("Contrasena: ");
        String password = scanner.nextLine();

        if (controller.login(usuario, password)) {
            System.out.println("\nBienvenido!");
            System.out.println("Rol: " + controller.getRolActual().toUpperCase());
            
            if (controller.getRolActual().equals("admin")) {
                menuAdmin();
            } else {
                menuDocente();
            }
        } else {
            System.out.println("Credenciales incorrectas");
        }
    }

    private void menuAdmin() {
        while (true) {
            System.out.println("\n--- MENU ADMINISTRADOR ---");
            System.out.println("1. Reporte de estudiantes");
            System.out.println("2. Reporte de notas por curso");
            System.out.println("3. Reporte de asistencia");
            System.out.println("4. Reporte de rendimiento");
            System.out.println("5. Estadisticas generales");
            System.out.println("6. Estudiantes en riesgo");
            System.out.println("7. Cerrar sesion");
            System.out.print("Seleccione: ");
            
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    System.out.println(controller.reporteEstudiantes());
                    break;
                case 2:
                    reporteNotas();
                    break;
                case 3:
                    reporteAsistencia();
                    break;
                case 4:
                    reporteRendimiento();
                    break;
                case 5:
                    System.out.println(controller.reporteEstadisticasGenerales());
                    break;
                case 6:
                    reporteRiesgo();
                    break;
                case 7:
                    controller.logout();
                    System.out.println("Sesion cerrada");
                    return;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }

    private void menuDocente() {
        while (true) {
            System.out.println("\n--- MENU DOCENTE ---");
            System.out.println("1. Ver mis estudiantes");
            System.out.println("2. Reporte de notas de mi curso");
            System.out.println("3. Reporte de asistencia de mi curso");
            System.out.println("4. Reporte de rendimiento de mi curso");
            System.out.println("5. Estudiantes en riesgo");
            System.out.println("6. Cerrar sesion");
            System.out.print("Seleccione: ");
            
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1:
                    System.out.println(controller.reporteEstudiantes());
                    break;
                case 2:
                    System.out.println(controller.reporteNotas(controller.getCursoDocente()));
                    break;
                case 3:
                    System.out.println(controller.reporteAsistencia(controller.getCursoDocente()));
                    break;
                case 4:
                    System.out.println(controller.reporteRendimiento(controller.getCursoDocente()));
                    break;
                case 5:
                    System.out.println(controller.reporteEstudiantesEnRiesgo(controller.getCursoDocente()));
                    break;
                case 6:
                    controller.logout();
                    System.out.println("Sesion cerrada");
                    return;
                default:
                    System.out.println("Opcion invalida");
            }
        }
    }

    private void reporteNotas() {
        System.out.println("\nCursos disponibles:");
        for (var curso : controller.getCursos()) {
            System.out.println("  - " + curso.getNombre());
        }
        System.out.print("\nIngrese nombre del curso: ");
        String curso = scanner.nextLine();
        System.out.println(controller.reporteNotas(curso));
    }

    private void reporteAsistencia() {
        System.out.println("\nCursos disponibles:");
        for (var curso : controller.getCursos()) {
            System.out.println("  - " + curso.getNombre());
        }
        System.out.print("\nIngrese nombre del curso: ");
        String curso = scanner.nextLine();
        System.out.println(controller.reporteAsistencia(curso));
    }

    private void reporteRendimiento() {
        System.out.println("\nCursos disponibles:");
        for (var curso : controller.getCursos()) {
            System.out.println("  - " + curso.getNombre());
        }
        System.out.print("\nIngrese nombre del curso: ");
        String curso = scanner.nextLine();
        System.out.println(controller.reporteRendimiento(curso));
    }

    private void reporteRiesgo() {
        System.out.println("\nCursos disponibles:");
        System.out.println("  - (Dejar en blanco para ver todos)");
        for (var curso : controller.getCursos()) {
            System.out.println("  - " + curso.getNombre());
        }
        System.out.print("\nIngrese nombre del curso: ");
        String curso = scanner.nextLine();
        if (curso.trim().isEmpty()) {
            System.out.println(controller.reporteEstudiantesEnRiesgo(null));
        } else {
            System.out.println(controller.reporteEstudiantesEnRiesgo(curso));
        }
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}