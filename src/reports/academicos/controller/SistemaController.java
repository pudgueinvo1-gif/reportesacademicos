package reports.academicos.controller;

import reports.academicos.model.*;
import reports.academicos.util.ArchivoUtil;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.Random;

public class SistemaController {
    private Estudiante[] estudiantes;
    private Curso[] cursos;
    private Asistencia[] asistencias;
    private int contadorEstudiantes;
    private int contadorCursos;
    private int contadorAsistencias;
    private int nextIdEstudiante;
    private int nextIdCurso;
    private int nextIdAsistencia;
    
    private String usuarioActual;
    private String rolActual;
    private String cursoDocente;

    private static final int MAX_ESTUDIANTES = 100;
    private static final int MAX_CURSOS = 20;
    private static final int MAX_ASISTENCIAS = 1000;
    private static final double NOTA_MINIMA_APROBACION = 12.0;
    private static final double NOTA_RIESGO = 10.0;

    public SistemaController() {
        this.estudiantes = new Estudiante[MAX_ESTUDIANTES];
        this.cursos = new Curso[MAX_CURSOS];
        this.asistencias = new Asistencia[MAX_ASISTENCIAS];
        this.contadorEstudiantes = 0;
        this.contadorCursos = 0;
        this.contadorAsistencias = 0;
        cargarDatos();
        
        if (contadorEstudiantes == 0) {
            System.out.println("No se encontraron datos. Inicializando datos de prueba...");
            inicializarDatosPrueba();
            guardarDatos();
        }
    }

    // ==================== CARGA Y GUARDADO ====================
    private void cargarDatos() {
        String[] lineasEstudiantes = ArchivoUtil.leerArchivo("estudiantes.txt");
        for (String linea : lineasEstudiantes) {
            estudiantes[contadorEstudiantes++] = new Estudiante(linea);
        }
        
        String[] lineasCursos = ArchivoUtil.leerArchivo("cursos.txt");
        for (String linea : lineasCursos) {
            cursos[contadorCursos++] = new Curso(linea);
        }
        
        String[] lineasNotas = ArchivoUtil.leerArchivo("notas.txt");
        for (String linea : lineasNotas) {
            String[] datos = linea.split("\\|");
            int cursoId = Integer.parseInt(datos[0].trim());
            int estudianteId = Integer.parseInt(datos[1].trim());
            String[] notasStr = datos[2].trim().split(",");
            
            Curso curso = buscarCursoPorId(cursoId);
            if (curso != null) {
                for (String notaStr : notasStr) {
                    if (!notaStr.isEmpty()) {
                        curso.agregarNota(estudianteId, Double.parseDouble(notaStr.trim()));
                    }
                }
            }
        }
        
        String[] lineasAsistencias = ArchivoUtil.leerArchivo("asistencias.txt");
        for (String linea : lineasAsistencias) {
            asistencias[contadorAsistencias++] = new Asistencia(linea);
        }
        
        calcularNextIds();
    }

    private void calcularNextIds() {
        int maxId = 0;
        for (int i = 0; i < contadorEstudiantes; i++) {
            if (estudiantes[i].getId() > maxId) maxId = estudiantes[i].getId();
        }
        nextIdEstudiante = maxId + 1;
        
        maxId = 0;
        for (int i = 0; i < contadorCursos; i++) {
            if (cursos[i].getId() > maxId) maxId = cursos[i].getId();
        }
        nextIdCurso = maxId + 1;
        
        maxId = 0;
        for (int i = 0; i < contadorAsistencias; i++) {
            if (asistencias[i].getId() > maxId) maxId = asistencias[i].getId();
        }
        nextIdAsistencia = maxId + 1;
    }

    public void guardarDatos() {
        String[] lineasEstudiantes = new String[contadorEstudiantes];
        for (int i = 0; i < contadorEstudiantes; i++) {
            lineasEstudiantes[i] = estudiantes[i].toLinea();
        }
        ArchivoUtil.guardarArchivo("estudiantes.txt", lineasEstudiantes);
        
        String[] lineasCursos = new String[contadorCursos];
        for (int i = 0; i < contadorCursos; i++) {
            lineasCursos[i] = cursos[i].toLinea();
        }
        ArchivoUtil.guardarArchivo("cursos.txt", lineasCursos);
        
        int totalNotas = 0;
        for (int i = 0; i < contadorCursos; i++) {
            totalNotas += cursos[i].getContadorEstudiantes();
        }
        String[] lineasNotas = new String[totalNotas];
        int idx = 0;
        for (int i = 0; i < contadorCursos; i++) {
            String[] notasCurso = cursos[i].getNotasLineas();
            for (String nota : notasCurso) {
                lineasNotas[idx++] = nota;
            }
        }
        ArchivoUtil.guardarArchivo("notas.txt", lineasNotas);
        
        String[] lineasAsistencias = new String[contadorAsistencias];
        for (int i = 0; i < contadorAsistencias; i++) {
            lineasAsistencias[i] = asistencias[i].toLinea();
        }
        ArchivoUtil.guardarArchivo("asistencias.txt", lineasAsistencias);
        
        System.out.println("Datos guardados correctamente");
    }

    // ==================== DATOS DE PRUEBA ====================
    private void inicializarDatosPrueba() {
        String[] nombres = {"Ana", "Carlos", "Maria", "Juan", "Laura", "Pedro", "Sofia", "Miguel", 
                           "Isabel", "Roberto", "Patricia", "Andres", "Carmen", "Jose", "Elena",
                           "Diego", "Paula", "Ricardo", "Valentina", "Felipe"};
        String[] apellidos = {"Martinez", "Perez", "Gomez", "Lopez", "Sanchez", "Ramirez", "Rodriguez", 
                             "Fernandez", "Garcia", "Torres", "Ruiz", "Diaz", "Morales", "Ortiz", "Cruz"};
        String[] carreras = {"Ingenieria", "Medicina", "Derecho", "Administracion", "Arquitectura", "Psicologia"};
        
        for (int i = 0; i < 20; i++) {
            String nombre = nombres[i % nombres.length];
            String apellido = apellidos[i % apellidos.length];
            String carrera = carreras[i % carreras.length];
            int semestre = 1 + (i % 5);
            String email = nombre.toLowerCase() + "." + apellido.toLowerCase() + "@instituto.edu";
            estudiantes[contadorEstudiantes++] = new Estudiante(nextIdEstudiante++, nombre, apellido, carrera, semestre, email);
        }
        
        cursos[contadorCursos++] = new Curso(nextIdCurso++, "Ingenieria de Software", "IS-301", 4, "Dr. Roberto Mendez");
        cursos[contadorCursos++] = new Curso(nextIdCurso++, "Anatomia Humana", "AN-201", 5, "Dra. Laura Torres");
        cursos[contadorCursos++] = new Curso(nextIdCurso++, "Derecho Civil", "DC-401", 4, "Dr. Carlos Ruiz");
        cursos[contadorCursos++] = new Curso(nextIdCurso++, "Administracion de Empresas", "AD-301", 3, "Mg. Patricia Fernandez");
        cursos[contadorCursos++] = new Curso(nextIdCurso++, "Diseno Arquitectonico", "DA-201", 4, "Arq. Miguel Sanchez");
        cursos[contadorCursos++] = new Curso(nextIdCurso++, "Psicologia General", "PS-101", 3, "Dra. Elena Gomez");

        Random rand = new Random();
        for (int i = 0; i < contadorEstudiantes; i++) {
            Estudiante e = estudiantes[i];
            int numCursos = 2 + rand.nextInt(2);
            for (int j = 0; j < numCursos; j++) {
                Curso curso = cursos[rand.nextInt(contadorCursos)];
                curso.agregarEstudiante(e);
                for (int k = 0; k < 3 + rand.nextInt(3); k++) {
                    double nota = 0 + rand.nextDouble() * 20;
                    curso.agregarNota(e.getId(), Math.round(nota * 10) / 10.0);
                }
            }
        }

        for (int i = 0; i < contadorEstudiantes; i++) {
            Estudiante e = estudiantes[i];
            for (int j = 0; j < contadorCursos; j++) {
                Curso c = cursos[j];
                boolean encontrado = false;
                for (int k = 0; k < c.getContadorEstudiantes(); k++) {
                    if (c.getEstudiantes()[k] != null && c.getEstudiantes()[k].getId() == e.getId()) {
                        encontrado = true;
                        break;
                    }
                }
                if (encontrado) {
                    for (int k = 0; k < 15; k++) {
                        LocalDate fecha = LocalDate.now().minusDays(k);
                        boolean presente = rand.nextDouble() > 0.15;
                        asistencias[contadorAsistencias++] = new Asistencia(nextIdAsistencia++, e.getId(), c.getId(), fecha, presente);
                    }
                }
            }
        }
    }

    // ==================== AUTENTICACION ====================
    public boolean login(String usuario, String password) {
        if (usuario.equals("admin") && password.equals("admin123")) {
            this.usuarioActual = usuario;
            this.rolActual = "admin";
            this.cursoDocente = null;
            return true;
        }
        
        if (usuario.equals("docente1") && password.equals("doc123")) {
            this.usuarioActual = usuario;
            this.rolActual = "docente";
            this.cursoDocente = "Ingenieria de Software";
            return true;
        }
        if (usuario.equals("docente2") && password.equals("doc123")) {
            this.usuarioActual = usuario;
            this.rolActual = "docente";
            this.cursoDocente = "Anatomia Humana";
            return true;
        }
        if (usuario.equals("docente3") && password.equals("doc123")) {
            this.usuarioActual = usuario;
            this.rolActual = "docente";
            this.cursoDocente = "Derecho Civil";
            return true;
        }
        
        return false;
    }

    public void logout() {
        this.usuarioActual = null;
        this.rolActual = null;
        this.cursoDocente = null;
    }

    public String getRolActual() { return rolActual; }
    public String getCursoDocente() { return cursoDocente; }

    private Curso buscarCursoPorId(int id) {
        for (int i = 0; i < contadorCursos; i++) {
            if (cursos[i].getId() == id) return cursos[i];
        }
        return null;
    }

    private Curso buscarCursoPorNombre(String nombre) {
        for (int i = 0; i < contadorCursos; i++) {
            if (cursos[i].getNombre().equalsIgnoreCase(nombre)) return cursos[i];
        }
        return null;
    }

    private Estudiante buscarEstudiantePorId(int id) {
        for (int i = 0; i < contadorEstudiantes; i++) {
            if (estudiantes[i].getId() == id) return estudiantes[i];
        }
        return null;
    }

    public Curso[] getCursos() { return cursos; }
    public int getContadorCursos() { return contadorCursos; }

    // ==================== REPORTE 1 ====================
    public String reporteEstudiantes() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(85)).append("\n");
        sb.append("           REPORTE DE ESTUDIANTES\n");
        sb.append("=".repeat(85)).append("\n");
        sb.append("ID  | NOMBRE COMPLETO      | CARRERA         | SEMESTRE | EMAIL\n");
        sb.append("-".repeat(85)).append("\n");
        
        if (rolActual.equals("docente")) {
            Curso curso = buscarCursoPorNombre(cursoDocente);
            if (curso != null) {
                for (int i = 0; i < curso.getContadorEstudiantes(); i++) {
                    sb.append(curso.getEstudiantes()[i]).append("\n");
                }
            }
        } else {
            for (int i = 0; i < contadorEstudiantes; i++) {
                sb.append(estudiantes[i]).append("\n");
            }
        }
        
        sb.append("-".repeat(85)).append("\n");
        sb.append("=".repeat(85)).append("\n");
        return sb.toString();
    }

    // ==================== REPORTE 2 ====================
    public String reporteNotas(String nombreCurso) {
        Curso curso = buscarCursoPorNombre(nombreCurso);
        if (curso == null) return "Curso no encontrado";
        
        if (rolActual.equals("docente") && !cursoDocente.equalsIgnoreCase(nombreCurso)) {
            return "Acceso denegado. Solo puede ver su curso: " + cursoDocente;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(100)).append("\n");
        sb.append("           REPORTE DE NOTAS - ").append(curso.getNombre()).append("\n");
        sb.append("=".repeat(100)).append("\n");
        sb.append("Codigo: ").append(curso.getCodigo()).append(" | Docente: ").append(curso.getDocente()).append("\n");
        sb.append("Creditos: ").append(curso.getCreditos()).append(" | Estudiantes: ").append(curso.getContadorEstudiantes()).append("\n");
        sb.append("-".repeat(100)).append("\n");
        sb.append("ID  | ESTUDIANTE           | NOTAS        | PROMEDIO | ESTADO\n");
        sb.append("-".repeat(100)).append("\n");
        
        for (int i = 0; i < curso.getContadorEstudiantes(); i++) {
            Estudiante e = curso.getEstudiantes()[i];
            double[] notas = curso.getNotas(e.getId());
            double promedio = curso.getPromedio(e.getId());
            String estado = promedio >= NOTA_MINIMA_APROBACION ? "APROBADO" : "REPROBADO";
            
            StringBuilder notasStr = new StringBuilder();
            for (int j = 0; j < notas.length; j++) {
                if (j > 0) notasStr.append(", ");
                notasStr.append(String.format("%.1f", notas[j]));
            }
            
            sb.append(String.format("%-4d | %-18s | %-12s | %8.1f | %s\n",
                    e.getId(), e.getNombreCompleto(), notasStr.toString(), promedio, estado));
        }
        
        sb.append("-".repeat(100)).append("\n");
        sb.append("PROMEDIO DEL CURSO: ").append(String.format("%.2f", curso.getPromedioCurso())).append("\n");
        sb.append("APROBADOS: ").append(curso.getAprobados()).append(" (");
        sb.append(String.format("%.1f%%", (curso.getAprobados() * 100.0 / curso.getContadorEstudiantes()))).append(")\n");
        sb.append("REPROBADOS: ").append(curso.getReprobados()).append(" (");
        sb.append(String.format("%.1f%%", (curso.getReprobados() * 100.0 / curso.getContadorEstudiantes()))).append(")\n");
        sb.append("=".repeat(100)).append("\n");
        
        return sb.toString();
    }

    // ==================== REPORTE 3 ====================
    public String reporteAsistencia(String nombreCurso) {
        Curso curso = buscarCursoPorNombre(nombreCurso);
        if (curso == null) return "Curso no encontrado";
        
        if (rolActual.equals("docente") && !cursoDocente.equalsIgnoreCase(nombreCurso)) {
            return "Acceso denegado. Solo puede ver su curso: " + cursoDocente;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(90)).append("\n");
        sb.append("           REPORTE DE ASISTENCIA - ").append(curso.getNombre()).append("\n");
        sb.append("=".repeat(90)).append("\n");
        sb.append("-".repeat(90)).append("\n");
        sb.append("ID  | ESTUDIANTE           | PRESENTE | AUSENTE | % ASISTENCIA\n");
        sb.append("-".repeat(90)).append("\n");
        
        for (int i = 0; i < curso.getContadorEstudiantes(); i++) {
            Estudiante e = curso.getEstudiantes()[i];
            int presentes = 0;
            int total = 0;
            for (int j = 0; j < contadorAsistencias; j++) {
                if (asistencias[j].getEstudianteId() == e.getId() && asistencias[j].getCursoId() == curso.getId()) {
                    total++;
                    if (asistencias[j].isPresente()) presentes++;
                }
            }
            double porcentaje = total > 0 ? (presentes * 100.0 / total) : 0;
            
            sb.append(String.format("%-4d | %-18s | %7d | %7d | %8.1f%%\n",
                    e.getId(), e.getNombreCompleto(), presentes, total - presentes, porcentaje));
        }
        
        sb.append("-".repeat(90)).append("\n");
        sb.append("=".repeat(90)).append("\n");
        return sb.toString();
    }

    // ==================== REPORTE 4 ====================
    public String reporteRendimiento(String nombreCurso) {
        Curso curso = buscarCursoPorNombre(nombreCurso);
        if (curso == null) return "Curso no encontrado";
        
        if (rolActual.equals("docente") && !cursoDocente.equalsIgnoreCase(nombreCurso)) {
            return "Acceso denegado. Solo puede ver su curso: " + cursoDocente;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(90)).append("\n");
        sb.append("           REPORTE DE RENDIMIENTO - ").append(curso.getNombre()).append("\n");
        sb.append("=".repeat(90)).append("\n");
        
        Estudiante[] aprobados = new Estudiante[curso.getContadorEstudiantes()];
        Estudiante[] reprobados = new Estudiante[curso.getContadorEstudiantes()];
        int contAprobados = 0, contReprobados = 0;
        
        for (int i = 0; i < curso.getContadorEstudiantes(); i++) {
            Estudiante e = curso.getEstudiantes()[i];
            double promedio = curso.getPromedio(e.getId());
            if (promedio >= NOTA_MINIMA_APROBACION) {
                aprobados[contAprobados++] = e;
            } else {
                reprobados[contReprobados++] = e;
            }
        }
        
        sb.append("\nAPROBADOS (").append(contAprobados).append("):\n");
        sb.append("-".repeat(40)).append("\n");
        for (int i = 0; i < contAprobados; i++) {
            sb.append(String.format("  %-20s | %.1f\n", 
                    aprobados[i].getNombreCompleto(), 
                    curso.getPromedio(aprobados[i].getId())));
        }
        
        sb.append("\nREPROBADOS (").append(contReprobados).append("):\n");
        sb.append("-".repeat(40)).append("\n");
        for (int i = 0; i < contReprobados; i++) {
            sb.append(String.format("  %-20s | %.1f\n", 
                    reprobados[i].getNombreCompleto(), 
                    curso.getPromedio(reprobados[i].getId())));
        }
        
        sb.append("\nRESULTADOS:\n");
        sb.append("  Aprobados: ").append(contAprobados).append(" (");
        sb.append(String.format("%.1f%%", (contAprobados * 100.0 / curso.getContadorEstudiantes()))).append(")\n");
        sb.append("  Reprobados: ").append(contReprobados).append(" (");
        sb.append(String.format("%.1f%%", (contReprobados * 100.0 / curso.getContadorEstudiantes()))).append(")\n");
        sb.append("  Promedio del curso: ").append(String.format("%.2f", curso.getPromedioCurso())).append("\n");
        sb.append("=".repeat(90)).append("\n");
        
        return sb.toString();
    }

    // ==================== REPORTE 5 ====================
    public String reporteEstadisticasGenerales() {
        if (!rolActual.equals("admin")) {
            return "Acceso denegado. Solo administradores pueden ver estadisticas generales.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(90)).append("\n");
        sb.append("           ESTADISTICAS GENERALES\n");
        sb.append("=".repeat(90)).append("\n");
        
        // Contar estudiantes por carrera (con arreglos)
        String[] carreras = {"Ingenieria", "Medicina", "Derecho", "Administracion", "Arquitectura", "Psicologia"};
        int[] contadorCarreras = new int[carreras.length];
        for (int i = 0; i < contadorEstudiantes; i++) {
            for (int j = 0; j < carreras.length; j++) {
                if (estudiantes[i].getCarrera().equals(carreras[j])) {
                    contadorCarreras[j]++;
                    break;
                }
            }
        }
        
        sb.append("\nESTUDIANTES POR CARRERA:\n");
        for (int i = 0; i < carreras.length; i++) {
            sb.append(String.format("  %-20s: %d\n", carreras[i], contadorCarreras[i]));
        }
        
        // Contar estudiantes por semestre
        int[] contadorSemestres = new int[6];
        for (int i = 0; i < contadorEstudiantes; i++) {
            int sem = estudiantes[i].getSemestre();
            if (sem >= 1 && sem <= 5) contadorSemestres[sem]++;
        }
        
        sb.append("\nESTUDIANTES POR SEMESTRE:\n");
        for (int i = 1; i <= 5; i++) {
            sb.append(String.format("  %d° Semestre: %d\n", i, contadorSemestres[i]));
        }
        
        sb.append("\nESTADISTICAS DE CURSOS:\n");
        for (int i = 0; i < contadorCursos; i++) {
            Curso c = cursos[i];
            double promedio = c.getPromedioCurso();
            int aprobados = c.getAprobados();
            int total = c.getContadorEstudiantes();
            double porcentajeAprobados = total > 0 ? (aprobados * 100.0 / total) : 0;
            sb.append(String.format("  %-25s | Prom: %.2f | Aprobados: %d/%d (%.1f%%)\n", 
                    c.getNombre(), promedio, aprobados, total, porcentajeAprobados));
        }
        
        sb.append("\nTOTALES:\n");
        sb.append("  Estudiantes: ").append(contadorEstudiantes).append("\n");
        sb.append("  Cursos: ").append(contadorCursos).append("\n");
        sb.append("  Asistencias registradas: ").append(contadorAsistencias).append("\n");
        
        sb.append("=".repeat(90)).append("\n");
        return sb.toString();
    }

    // ==================== REPORTE 6 ====================
    public String reporteEstudiantesEnRiesgo(String nombreCurso) {
        if (rolActual.equals("docente")) {
            nombreCurso = cursoDocente;
        }
        
        Curso curso = buscarCursoPorNombre(nombreCurso);
        if (curso == null) {
            if (rolActual.equals("admin")) {
                return reporteEstudiantesEnRiesgoTodos();
            }
            return "Curso no encontrado";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(90)).append("\n");
        sb.append("           ESTUDIANTES EN RIESGO - ").append(curso.getNombre()).append("\n");
        sb.append("=".repeat(90)).append("\n");
        sb.append("(Promedio menor a " + NOTA_RIESGO + ")\n\n");
        
        boolean hayRiesgo = false;
        for (int i = 0; i < curso.getContadorEstudiantes(); i++) {
            Estudiante e = curso.getEstudiantes()[i];
            double promedio = curso.getPromedio(e.getId());
            if (promedio < NOTA_RIESGO && promedio > 0) {
                sb.append(String.format("  ADVERTENCIA %-20s | Promedio: %.1f\n", e.getNombreCompleto(), promedio));
                hayRiesgo = true;
            }
        }
        
        if (!hayRiesgo) {
            sb.append("  No hay estudiantes en riesgo en este curso\n");
        }
        
        sb.append("=".repeat(90)).append("\n");
        return sb.toString();
    }

    private String reporteEstudiantesEnRiesgoTodos() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(90)).append("\n");
        sb.append("           ESTUDIANTES EN RIESGO (TODOS LOS CURSOS)\n");
        sb.append("=".repeat(90)).append("\n");
        sb.append("(Promedio menor a " + NOTA_RIESGO + ")\n\n");
        
        boolean hayRiesgo = false;
        for (int i = 0; i < contadorCursos; i++) {
            Curso c = cursos[i];
            boolean cursoConRiesgo = false;
            for (int j = 0; j < c.getContadorEstudiantes(); j++) {
                Estudiante e = c.getEstudiantes()[j];
                double promedio = c.getPromedio(e.getId());
                if (promedio < NOTA_RIESGO && promedio > 0) {
                    if (!cursoConRiesgo) {
                        sb.append("\n").append(c.getNombre()).append(":\n");
                        cursoConRiesgo = true;
                        hayRiesgo = true;
                    }
                    sb.append(String.format("  ADVERTENCIA %-20s | Promedio: %.1f\n", e.getNombreCompleto(), promedio));
                }
            }
        }
        
        if (!hayRiesgo) {
            sb.append("  No hay estudiantes en riesgo en ningun curso\n");
        }
        
        sb.append("=".repeat(90)).append("\n");
        return sb.toString();
    }

    // ==================== MODULO DE INGRESO DE DATOS ====================
    public void agregarEstudiante(String nombre, String apellido, String carrera, int semestre, String email) {
        if (contadorEstudiantes < MAX_ESTUDIANTES) {
            estudiantes[contadorEstudiantes++] = new Estudiante(nextIdEstudiante++, nombre, apellido, carrera, semestre, email);
            guardarDatos();
            JOptionPane.showMessageDialog(null, "Estudiante agregado correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "No hay espacio para mas estudiantes");
        }
    }

    public void agregarCurso(String nombre, String codigo, int creditos, String docente) {
        if (contadorCursos < MAX_CURSOS) {
            cursos[contadorCursos++] = new Curso(nextIdCurso++, nombre, codigo, creditos, docente);
            guardarDatos();
            JOptionPane.showMessageDialog(null, "Curso agregado correctamente");
        } else {
            JOptionPane.showMessageDialog(null, "No hay espacio para mas cursos");
        }
    }

    // ==================== EXPORTAR REPORTE A TXT ====================
    public void exportarReporte(String contenido, String nombreArchivo) {
        String[] lineas = contenido.split("\n");
        ArchivoUtil.guardarArchivo(nombreArchivo, lineas);
        JOptionPane.showMessageDialog(null, "Reporte exportado a: datos/" + nombreArchivo);
    }
}