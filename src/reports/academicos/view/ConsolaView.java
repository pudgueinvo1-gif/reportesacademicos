package reports.academicos.view;

import reports.academicos.controller.SistemaController;
import javax.swing.JOptionPane;

public class ConsolaView {
    private SistemaController controller;

    public ConsolaView() {
        this.controller = new SistemaController();
    }

    public void iniciar() {
        JOptionPane.showMessageDialog(null, 
            "SISTEMA DE REPORTES ACADEMICOS", 
            "Bienvenido", 
            JOptionPane.INFORMATION_MESSAGE);
        
        while (true) {
            String[] opciones = {"Iniciar sesion", "Salir"};
            int opcion = JOptionPane.showOptionDialog(null, 
                "Seleccione una opcion:", 
                "MENU PRINCIPAL", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                opciones, 
                opciones[0]);
            
            if (opcion == 0) {
                login();
            } else {
                controller.guardarDatos();
                JOptionPane.showMessageDialog(null, "Hasta luego!", "Salida", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
    }

    private void login() {
        String usuario = JOptionPane.showInputDialog("Usuario:");
        String password = JOptionPane.showInputDialog("Contrasena:");

        if (controller.login(usuario, password)) {
            JOptionPane.showMessageDialog(null, 
                "Bienvenido! Rol: " + controller.getRolActual().toUpperCase(), 
                "Login Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
            
            if (controller.getRolActual().equals("admin")) {
                menuAdmin();
            } else {
                menuDocente();
            }
        } else {
            JOptionPane.showMessageDialog(null, 
                "Credenciales incorrectas", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void menuAdmin() {
        while (true) {
            String[] opciones = {
                "Reporte de estudiantes",
                "Reporte de notas por curso",
                "Reporte de asistencia",
                "Reporte de rendimiento",
                "Estadisticas generales",
                "Estudiantes en riesgo",
                "Agregar estudiante",
                "Agregar curso",
                "Exportar reporte a TXT",
                "Cerrar sesion"
            };
            
            int opcion = JOptionPane.showOptionDialog(null, 
                "Seleccione una opcion:", 
                "MENU ADMINISTRADOR", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                opciones, 
                opciones[0]);
            
            switch (opcion) {
                case 0:
                    mostrarReporte(controller.reporteEstudiantes(), "Reporte de Estudiantes");
                    break;
                case 1:
                    reporteNotas();
                    break;
                case 2:
                    reporteAsistencia();
                    break;
                case 3:
                    reporteRendimiento();
                    break;
                case 4:
                    mostrarReporte(controller.reporteEstadisticasGenerales(), "Estadisticas Generales");
                    break;
                case 5:
                    reporteRiesgo();
                    break;
                case 6:
                    agregarEstudiante();
                    break;
                case 7:
                    agregarCurso();
                    break;
                case 8:
                    exportarReporte();
                    break;
                case 9:
                    controller.logout();
                    JOptionPane.showMessageDialog(null, "Sesion cerrada");
                    return;
                default:
                    return;
            }
        }
    }

    private void menuDocente() {
        while (true) {
            String[] opciones = {
                "Ver mis estudiantes",
                "Reporte de notas de mi curso",
                "Reporte de asistencia de mi curso",
                "Reporte de rendimiento de mi curso",
                "Estudiantes en riesgo",
                "Exportar reporte a TXT",
                "Cerrar sesion"
            };
            
            int opcion = JOptionPane.showOptionDialog(null, 
                "Seleccione una opcion:", 
                "MENU DOCENTE", 
                JOptionPane.DEFAULT_OPTION, 
                JOptionPane.PLAIN_MESSAGE, 
                null, 
                opciones, 
                opciones[0]);
            
            switch (opcion) {
                case 0:
                    mostrarReporte(controller.reporteEstudiantes(), "Mis Estudiantes");
                    break;
                case 1:
                    mostrarReporte(controller.reporteNotas(controller.getCursoDocente()), "Reporte de Notas");
                    break;
                case 2:
                    mostrarReporte(controller.reporteAsistencia(controller.getCursoDocente()), "Reporte de Asistencia");
                    break;
                case 3:
                    mostrarReporte(controller.reporteRendimiento(controller.getCursoDocente()), "Reporte de Rendimiento");
                    break;
                case 4:
                    mostrarReporte(controller.reporteEstudiantesEnRiesgo(controller.getCursoDocente()), "Estudiantes en Riesgo");
                    break;
                case 5:
                    exportarReporte();
                    break;
                case 6:
                    controller.logout();
                    JOptionPane.showMessageDialog(null, "Sesion cerrada");
                    return;
                default:
                    return;
            }
        }
    }

    private void mostrarReporte(String reporte, String titulo) {
        JOptionPane.showMessageDialog(null, reporte, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void reporteNotas() {
        StringBuilder cursosList = new StringBuilder("Cursos disponibles:\n");
        for (int i = 0; i < controller.getContadorCursos(); i++) {
            cursosList.append("  - ").append(controller.getCursos()[i].getNombre()).append("\n");
        }
        
        String curso = JOptionPane.showInputDialog(cursosList.toString() + "\nIngrese nombre del curso:");
        if (curso != null && !curso.trim().isEmpty()) {
            mostrarReporte(controller.reporteNotas(curso), "Reporte de Notas");
        }
    }

    private void reporteAsistencia() {
        StringBuilder cursosList = new StringBuilder("Cursos disponibles:\n");
        for (int i = 0; i < controller.getContadorCursos(); i++) {
            cursosList.append("  - ").append(controller.getCursos()[i].getNombre()).append("\n");
        }
        
        String curso = JOptionPane.showInputDialog(cursosList.toString() + "\nIngrese nombre del curso:");
        if (curso != null && !curso.trim().isEmpty()) {
            mostrarReporte(controller.reporteAsistencia(curso), "Reporte de Asistencia");
        }
    }

    private void reporteRendimiento() {
        StringBuilder cursosList = new StringBuilder("Cursos disponibles:\n");
        for (int i = 0; i < controller.getContadorCursos(); i++) {
            cursosList.append("  - ").append(controller.getCursos()[i].getNombre()).append("\n");
        }
        
        String curso = JOptionPane.showInputDialog(cursosList.toString() + "\nIngrese nombre del curso:");
        if (curso != null && !curso.trim().isEmpty()) {
            mostrarReporte(controller.reporteRendimiento(curso), "Reporte de Rendimiento");
        }
    }

    private void reporteRiesgo() {
        StringBuilder cursosList = new StringBuilder("Cursos disponibles:\n");
        cursosList.append("  - (Dejar en blanco para ver todos)\n");
        for (int i = 0; i < controller.getContadorCursos(); i++) {
            cursosList.append("  - ").append(controller.getCursos()[i].getNombre()).append("\n");
        }
        
        String curso = JOptionPane.showInputDialog(cursosList.toString() + "\nIngrese nombre del curso:");
        if (curso != null) {
            if (curso.trim().isEmpty()) {
                mostrarReporte(controller.reporteEstudiantesEnRiesgo(null), "Estudiantes en Riesgo");
            } else {
                mostrarReporte(controller.reporteEstudiantesEnRiesgo(curso), "Estudiantes en Riesgo");
            }
        }
    }

    private void agregarEstudiante() {
        String nombre = JOptionPane.showInputDialog("Nombre:");
        String apellido = JOptionPane.showInputDialog("Apellido:");
        String[] carreras = {"Ingenieria", "Medicina", "Derecho", "Administracion", "Arquitectura", "Psicologia"};
        String carrera = (String) JOptionPane.showInputDialog(null, "Carrera:", "Seleccionar",
                JOptionPane.QUESTION_MESSAGE, null, carreras, carreras[0]);
        String semestreStr = JOptionPane.showInputDialog("Semestre (1-5):");
        String email = JOptionPane.showInputDialog("Email:");
        
        try {
            int semestre = Integer.parseInt(semestreStr);
            if (semestre >= 1 && semestre <= 5) {
                controller.agregarEstudiante(nombre, apellido, carrera, semestre, email);
            } else {
                JOptionPane.showMessageDialog(null, "Semestre debe ser entre 1 y 5");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Semestre invalido");
        }
    }

    private void agregarCurso() {
        String nombre = JOptionPane.showInputDialog("Nombre del curso:");
        String codigo = JOptionPane.showInputDialog("Codigo:");
        String creditosStr = JOptionPane.showInputDialog("Creditos:");
        String docente = JOptionPane.showInputDialog("Docente:");
        
        try {
            int creditos = Integer.parseInt(creditosStr);
            controller.agregarCurso(nombre, codigo, creditos, docente);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Creditos invalidos");
        }
    }

    private void exportarReporte() {
        String[] opciones = {
            "Reporte de estudiantes",
            "Reporte de notas por curso",
            "Reporte de asistencia",
            "Reporte de rendimiento",
            "Estadisticas generales",
            "Estudiantes en riesgo"
        };
        
        int opcion = JOptionPane.showOptionDialog(null, 
            "Seleccione el reporte a exportar:", 
            "Exportar Reporte", 
            JOptionPane.DEFAULT_OPTION, 
            JOptionPane.PLAIN_MESSAGE, 
            null, 
            opciones, 
            opciones[0]);
        
        String contenido = "";
        String nombreArchivo = "";
        
        switch (opcion) {
            case 0:
                contenido = controller.reporteEstudiantes();
                nombreArchivo = "reporte_estudiantes.txt";
                break;
            case 1:
                String curso = JOptionPane.showInputDialog("Nombre del curso:");
                if (curso != null && !curso.trim().isEmpty()) {
                    contenido = controller.reporteNotas(curso);
                    nombreArchivo = "reporte_notas_" + curso.replace(" ", "_") + ".txt";
                }
                break;
            case 2:
                String curso2 = JOptionPane.showInputDialog("Nombre del curso:");
                if (curso2 != null && !curso2.trim().isEmpty()) {
                    contenido = controller.reporteAsistencia(curso2);
                    nombreArchivo = "reporte_asistencia_" + curso2.replace(" ", "_") + ".txt";
                }
                break;
            case 3:
                String curso3 = JOptionPane.showInputDialog("Nombre del curso:");
                if (curso3 != null && !curso3.trim().isEmpty()) {
                    contenido = controller.reporteRendimiento(curso3);
                    nombreArchivo = "reporte_rendimiento_" + curso3.replace(" ", "_") + ".txt";
                }
                break;
            case 4:
                contenido = controller.reporteEstadisticasGenerales();
                nombreArchivo = "reporte_estadisticas.txt";
                break;
            case 5:
                String curso4 = JOptionPane.showInputDialog("Nombre del curso (dejar vacio para todos):");
                if (curso4 != null) {
                    contenido = controller.reporteEstudiantesEnRiesgo(curso4.trim().isEmpty() ? null : curso4);
                    nombreArchivo = "reporte_riesgo.txt";
                }
                break;
            default:
                return;
        }
        
        if (!contenido.isEmpty() && !nombreArchivo.isEmpty()) {
            controller.exportarReporte(contenido, nombreArchivo);
        }
    }
}