package reports.academicos.controller;

import reports.academicos.model.*;
import reports.academicos.util.ArchivoUtil;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SistemaController {
    private List<Estudiante> estudiantes;
    private List<Curso> cursos;
    private List<Asistencia> asistencias;
    private int nextIdEstudiante;
    private int nextIdCurso;
    private int nextIdAsistencia;
    
    private String usuarioActual;
    private String rolActual;
    private String cursoDocente;

    public SistemaController() {
        this.estudiantes = new ArrayList<>();
        this.cursos = new ArrayList<>();
        this.asistencias = new ArrayList<>();
        cargarDatos();
        
        if (estudiantes.isEmpty()) {
            System.out.println("No se encontraron datos. Inicializando datos de prueba...");
            inicializarDatosPrueba();
            guardarDatos();
        }
    }

    // ==================== CARGA Y GUARDADO ====================
    private void cargarDatos() {
        List<String> lineasEstudiantes = ArchivoUtil.leerArchivo("estudiantes.txt");
        for (String linea : lineasEstudiantes) {
            estudiantes.add(new Estudiante(linea));
        }
        
        List<String> lineasCursos = ArchivoUtil.leerArchivo("cursos.txt");
        for (String linea : lineasCursos) {
            cursos.add(new Curso(linea));
        }
        
        List<String> lineasNotas = ArchivoUtil.leerArchivo("notas.txt");
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
        
        List<String> lineasAsistencias = ArchivoUtil.leerArchivo("asistencias.txt");
        for (String linea : lineasAsistencias) {
            asistencias.add(new Asistencia(linea));
        }
        
        nextIdEstudiante = estudiantes.stream().mapToInt(Estudiante::getId).max().orElse(0) + 1;
        nextIdCurso = cursos.stream().mapToInt(Curso::getId).max().orElse(0) + 1;
        nextIdAsistencia = asistencias.stream().mapToInt(Asistencia::getId).max().orElse(0) + 1;
    }

    public void guardarDatos() {
        List<String> lineasEstudiantes = new ArrayList<>();
        for (Estudiante e : estudiantes) {
            lineasEstudiantes.add(e.toLinea());
        }
        ArchivoUtil.guardarArchivo("estudiantes.txt", lineasEstudiantes);
        
        List<String> lineasCursos = new ArrayList<>();
        for (Curso c : cursos) {
            lineasCursos.add(c.toLinea());
        }
        ArchivoUtil.guardarArchivo("cursos.txt", lineasCursos);
        
        List<String> lineasNotas = new ArrayList<>();
        for (Curso c : cursos) {
            lineasNotas.addAll(c.getNotasLineas());
        }
        ArchivoUtil.guardarArchivo("notas.txt", lineasNotas);
        
        List<String> lineasAsistencias = new ArrayList<>();
        for (Asistencia a : asistencias) {
            lineasAsistencias.add(a.toLinea());
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
            estudiantes.add(new Estudiante(nextIdEstudiante++, nombre, apellido, carrera, semestre, email));
        }
        
        cursos.add(new Curso(nextIdCurso++, "Ingenieria de Software", "IS-301", 4, "Dr. Roberto Mendez"));
        cursos.add(new Curso(nextIdCurso++, "Anatomia Humana", "AN-201", 5, "Dra. Laura Torres"));
        cursos.add(new Curso(nextIdCurso++, "Derecho Civil", "DC-401", 4, "Dr. Carlos Ruiz"));
        cursos.add(new Curso(nextIdCurso++, "Administracion de Empresas", "AD-301", 3, "Mg. Patricia Fernandez"));
        cursos.add(new Curso(nextIdCurso++, "Diseno Arquitectonico", "DA-201", 4, "Arq. Miguel Sanchez"));
        cursos.add(new Curso(nextIdCurso++, "Psicologia General", "PS-101", 3, "Dra. Elena Gomez"));

        Random rand = new Random();
        for (Estudiante e : estudiantes) {
            int numCursos = 2 + rand.nextInt(2);
            List<Curso> cursosAsignados = new ArrayList<>();
            for (int i = 0; i < numCursos; i++) {
                Curso curso;
                do {
                    curso = cursos.get(rand.nextInt(cursos.size()));
                } while (cursosAsignados.contains(curso));
                cursosAsignados.add(curso);
                curso.agregarEstudiante(e.getId());
                
                for (int j = 0; j < 3 + rand.nextInt(3); j++) {
                    // Notas de 0 a 20 (sistema peruano)
                    double nota = 0 + rand.nextDouble() * 20;
                    curso.agregarNota(e.getId(), Math.round(nota * 10) / 10.0);
                }
            }
        }

        for (Estudiante e : estudiantes) {
            for (Curso c : cursos) {
                if (c.getEstudiantesIds().contains(e.getId())) {
                    for (int i = 0; i < 15; i++) {
                        LocalDate fecha = LocalDate.now().minusDays(i);
                        boolean presente = rand.nextDouble() > 0.15;
                        asistencias.add(new Asistencia(nextIdAsistencia++, e.getId(), c.getId(), fecha, presente));
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
        for (Curso c : cursos) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    private Curso buscarCursoPorNombre(String nombre) {
        for (Curso c : cursos) {
            if (c.getNombre().equalsIgnoreCase(nombre)) return c;
        }
        return null;
    }

    private Estudiante buscarEstudiantePorId(int id) {
        for (Estudiante e : estudiantes) {
            if (e.getId() == id) return e;
        }
        return null;
    }

    public List<Curso> getCursos() { return cursos; }

    // ==================== REPORTE 1: LISTA DE ESTUDIANTES ====================
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
                for (int id : curso.getEstudiantesIds()) {
                    Estudiante e = buscarEstudiantePorId(id);
                    if (e != null) sb.append(e).append("\n");
                }
            }
        } else {
            for (Estudiante e : estudiantes) {
                sb.append(e).append("\n");
            }
        }
        
        sb.append("-".repeat(85)).append("\n");
        sb.append("=".repeat(85)).append("\n");
        return sb.toString();
    }

    // ==================== REPORTE 2: NOTAS POR CURSO ====================
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
        sb.append("Creditos: ").append(curso.getCreditos()).append(" | Estudiantes: ").append(curso.getEstudiantesIds().size()).append("\n");
        sb.append("-".repeat(100)).append("\n");
        sb.append("ID  | ESTUDIANTE           | NOTAS        | PROMEDIO | ESTADO\n");
        sb.append("-".repeat(100)).append("\n");
        
        for (int estId : curso.getEstudiantesIds()) {
            Estudiante e = buscarEstudiantePorId(estId);
            if (e == null) continue;
            
            List<Double> notas = curso.getNotas(estId);
            double promedio = curso.getPromedio(estId);
            // NOTA MINIMA APROBATORIA EN PERU: 12
            String estado = promedio >= 12 ? "APROBADO" : "REPROBADO";
            
            String notasStr = notas.isEmpty() ? "Sin notas" : notas.stream()
                    .map(n -> String.format("%.1f", n))
                    .collect(Collectors.joining(", "));
            
            sb.append(String.format("%-4d | %-18s | %-12s | %8.1f | %s\n",
                    estId, e.getNombreCompleto(), notasStr, promedio, estado));
        }
        
        sb.append("-".repeat(100)).append("\n");
        sb.append("PROMEDIO DEL CURSO: ").append(String.format("%.2f", curso.getPromedioCurso(estudiantes))).append("\n");
        sb.append("APROBADOS: ").append(curso.getAprobados()).append(" (");
        sb.append(String.format("%.1f%%", (curso.getAprobados() * 100.0 / curso.getEstudiantesIds().size()))).append(")\n");
        sb.append("REPROBADOS: ").append(curso.getReprobados()).append(" (");
        sb.append(String.format("%.1f%%", (curso.getReprobados() * 100.0 / curso.getEstudiantesIds().size()))).append(")\n");
        sb.append("=".repeat(100)).append("\n");
        
        return sb.toString();
    }

    // ==================== REPORTE 3: ASISTENCIA ====================
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
        
        for (int estId : curso.getEstudiantesIds()) {
            Estudiante e = buscarEstudiantePorId(estId);
            if (e == null) continue;
            
            List<Asistencia> asistenciasEst = asistencias.stream()
                    .filter(a -> a.getEstudianteId() == estId && a.getCursoId() == curso.getId())
                    .collect(Collectors.toList());
            
            long presentes = asistenciasEst.stream().filter(Asistencia::isPresente).count();
            long total = asistenciasEst.size();
            double porcentaje = total > 0 ? (presentes * 100.0 / total) : 0;
            
            sb.append(String.format("%-4d | %-18s | %7d | %7d | %8.1f%%\n",
                    estId, e.getNombreCompleto(), presentes, total - presentes, porcentaje));
        }
        
        sb.append("-".repeat(90)).append("\n");
        sb.append("=".repeat(90)).append("\n");
        return sb.toString();
    }

    // ==================== REPORTE 4: RENDIMIENTO ====================
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
        
        List<Integer> aprobados = new ArrayList<>();
        List<Integer> reprobados = new ArrayList<>();
        Map<Integer, Double> promedios = new HashMap<>();
        
        for (int id : curso.getEstudiantesIds()) {
            double promedio = curso.getPromedio(id);
            promedios.put(id, promedio);
            // NOTA MINIMA APROBATORIA EN PERU: 12
            if (promedio >= 12) {
                aprobados.add(id);
            } else {
                reprobados.add(id);
            }
        }
        
        sb.append("\nAPROBADOS (").append(aprobados.size()).append("):\n");
        sb.append("-".repeat(40)).append("\n");
        for (int id : aprobados) {
            Estudiante e = buscarEstudiantePorId(id);
            if (e != null) {
                sb.append(String.format("  %-20s | %.1f\n", e.getNombreCompleto(), promedios.get(id)));
            }
        }
        
        sb.append("\nREPROBADOS (").append(reprobados.size()).append("):\n");
        sb.append("-".repeat(40)).append("\n");
        for (int id : reprobados) {
            Estudiante e = buscarEstudiantePorId(id);
            if (e != null) {
                sb.append(String.format("  %-20s | %.1f\n", e.getNombreCompleto(), promedios.get(id)));
            }
        }
        
        sb.append("\nRESULTADOS:\n");
        sb.append("  Aprobados: ").append(aprobados.size()).append(" (");
        sb.append(String.format("%.1f%%", (aprobados.size() * 100.0 / curso.getEstudiantesIds().size()))).append(")\n");
        sb.append("  Reprobados: ").append(reprobados.size()).append(" (");
        sb.append(String.format("%.1f%%", (reprobados.size() * 100.0 / curso.getEstudiantesIds().size()))).append(")\n");
        sb.append("  Promedio del curso: ").append(String.format("%.2f", curso.getPromedioCurso(estudiantes))).append("\n");
        sb.append("=".repeat(90)).append("\n");
        
        return sb.toString();
    }

    // ==================== REPORTE 5: ESTADISTICAS GENERALES ====================
    public String reporteEstadisticasGenerales() {
        if (!rolActual.equals("admin")) {
            return "Acceso denegado. Solo administradores pueden ver estadisticas generales.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(90)).append("\n");
        sb.append("           ESTADISTICAS GENERALES\n");
        sb.append("=".repeat(90)).append("\n");
        
        sb.append("\nESTUDIANTES POR CARRERA:\n");
        Map<String, Long> carreraCount = estudiantes.stream()
                .collect(Collectors.groupingBy(Estudiante::getCarrera, Collectors.counting()));
        for (Map.Entry<String, Long> entry : carreraCount.entrySet()) {
            sb.append(String.format("  %-20s: %d\n", entry.getKey(), entry.getValue()));
        }
        
        sb.append("\nESTUDIANTES POR SEMESTRE:\n");
        Map<Integer, Long> semestreCount = estudiantes.stream()
                .collect(Collectors.groupingBy(Estudiante::getSemestre, Collectors.counting()));
        for (int i = 1; i <= 5; i++) {
            sb.append(String.format("  %d° Semestre: %d\n", i, semestreCount.getOrDefault(i, 0L)));
        }
        
        sb.append("\nESTADISTICAS DE CURSOS:\n");
        for (Curso c : cursos) {
            double promedio = c.getPromedioCurso(estudiantes);
            int aprobados = c.getAprobados();
            int total = c.getEstudiantesIds().size();
            double porcentajeAprobados = total > 0 ? (aprobados * 100.0 / total) : 0;
            sb.append(String.format("  %-25s | Prom: %.2f | Aprobados: %d/%d (%.1f%%)\n", 
                    c.getNombre(), promedio, aprobados, total, porcentajeAprobados));
        }
        
        double mejorPromedio = 0, peorPromedio = 100;
        Estudiante mejor = null, peor = null;
        
        for (Estudiante e : estudiantes) {
            double promedioGeneral = 0;
            int count = 0;
            for (Curso c : cursos) {
                if (c.getEstudiantesIds().contains(e.getId())) {
                    promedioGeneral += c.getPromedio(e.getId());
                    count++;
                }
            }
            if (count > 0) {
                promedioGeneral /= count;
                if (promedioGeneral > mejorPromedio) {
                    mejorPromedio = promedioGeneral;
                    mejor = e;
                }
                if (promedioGeneral < peorPromedio) {
                    peorPromedio = promedioGeneral;
                    peor = e;
                }
            }
        }
        
        if (mejor != null) {
            sb.append(String.format("\nMejor estudiante: %s (%.2f)\n", mejor.getNombreCompleto(), mejorPromedio));
        }
        if (peor != null) {
            sb.append(String.format("Peor estudiante: %s (%.2f)\n", peor.getNombreCompleto(), peorPromedio));
        }
        
        sb.append("\nTOTALES:\n");
        sb.append("  Estudiantes: ").append(estudiantes.size()).append("\n");
        sb.append("  Cursos: ").append(cursos.size()).append("\n");
        sb.append("  Asistencias registradas: ").append(asistencias.size()).append("\n");
        
        sb.append("=".repeat(90)).append("\n");
        return sb.toString();
    }

    // ==================== REPORTE 6: ESTUDIANTES EN RIESGO ====================
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
        // EN PERU: NOTA MINIMA APROBATORIA ES 12, RIESGO = NOTA < 10
        sb.append("(Promedio menor a 10)\n\n");
        
        boolean hayRiesgo = false;
        for (int id : curso.getEstudiantesIds()) {
            double promedio = curso.getPromedio(id);
            if (promedio < 10 && promedio > 0) {
                Estudiante e = buscarEstudiantePorId(id);
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
        sb.append("(Promedio menor a 10)\n\n");
        
        boolean hayRiesgo = false;
        for (Curso c : cursos) {
            boolean cursoConRiesgo = false;
            for (int id : c.getEstudiantesIds()) {
                double promedio = c.getPromedio(id);
                if (promedio < 10 && promedio > 0) {
                    if (!cursoConRiesgo) {
                        sb.append("\n").append(c.getNombre()).append(":\n");
                        cursoConRiesgo = true;
                        hayRiesgo = true;
                    }
                    Estudiante e = buscarEstudiantePorId(id);
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
}