package reports.academicos.model;

import java.util.*;

public class Curso {
    private int id;
    private String nombre;
    private String codigo;
    private int creditos;
    private String docente;
    private List<Integer> estudiantesIds;
    private Map<Integer, List<Double>> notas;

    public Curso(int id, String nombre, String codigo, int creditos, String docente) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.docente = docente;
        this.estudiantesIds = new ArrayList<>();
        this.notas = new HashMap<>();
    }

    public Curso(String linea) {
        String[] datos = linea.split("\\|");
        this.id = Integer.parseInt(datos[0].trim());
        this.nombre = datos[1].trim();
        this.codigo = datos[2].trim();
        this.creditos = Integer.parseInt(datos[3].trim());
        this.docente = datos[4].trim();
        this.estudiantesIds = new ArrayList<>();
        this.notas = new HashMap<>();
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getCreditos() { return creditos; }
    public String getDocente() { return docente; }
    public List<Integer> getEstudiantesIds() { return estudiantesIds; }
    
    public void agregarEstudiante(int estudianteId) {
        if (!estudiantesIds.contains(estudianteId)) {
            estudiantesIds.add(estudianteId);
            notas.put(estudianteId, new ArrayList<>());
        }
    }

    public void agregarNota(int estudianteId, double nota) {
        if (notas.containsKey(estudianteId)) {
            notas.get(estudianteId).add(nota);
        }
    }

    public List<Double> getNotas(int estudianteId) {
        return notas.getOrDefault(estudianteId, new ArrayList<>());
    }

    public double getPromedio(int estudianteId) {
        List<Double> notasEst = getNotas(estudianteId);
        if (notasEst.isEmpty()) return 0.0;
        return notasEst.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    public double getPromedioCurso(List<Estudiante> todosEstudiantes) {
        if (estudiantesIds.isEmpty()) return 0.0;
        double suma = 0.0;
        for (int id : estudiantesIds) {
            suma += getPromedio(id);
        }
        return suma / estudiantesIds.size();
    }

    public int getAprobados() {
        int count = 0;
        for (int id : estudiantesIds) {
            if (getPromedio(id) >= 70) count++;
        }
        return count;
    }

    public int getReprobados() {
        return estudiantesIds.size() - getAprobados();
    }

    public String toLinea() {
        return id + " | " + nombre + " | " + codigo + " | " + creditos + " | " + docente;
    }

    public List<String> getNotasLineas() {
        List<String> lineas = new ArrayList<>();
        for (Map.Entry<Integer, List<Double>> entry : notas.entrySet()) {
            int estudianteId = entry.getKey();
            List<Double> notasEst = entry.getValue();
            String notasStr = notasEst.stream()
                    .map(n -> String.format("%.1f", n))
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
            lineas.add(id + " | " + estudianteId + " | " + notasStr);
        }
        return lineas;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-25s | %-10s | %d créditos | %s", 
                id, nombre, codigo, creditos, docente);
    }
}