package reports.academicos.model;

import java.time.LocalDate;

public class Asistencia {
    private int id;
    private int estudianteId;
    private int cursoId;
    private LocalDate fecha;
    private boolean presente;

    public Asistencia(int id, int estudianteId, int cursoId, LocalDate fecha, boolean presente) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.cursoId = cursoId;
        this.fecha = fecha;
        this.presente = presente;
    }

    public Asistencia(String linea) {
        String[] datos = linea.split("\\|");
        this.id = Integer.parseInt(datos[0].trim());
        this.estudianteId = Integer.parseInt(datos[1].trim());
        this.cursoId = Integer.parseInt(datos[2].trim());
        this.fecha = LocalDate.parse(datos[3].trim());
        this.presente = Boolean.parseBoolean(datos[4].trim());
    }

    public int getId() { return id; }
    public int getEstudianteId() { return estudianteId; }
    public int getCursoId() { return cursoId; }
    public LocalDate getFecha() { return fecha; }
    public boolean isPresente() { return presente; }

    public String toLinea() {
        return id + " | " + estudianteId + " | " + cursoId + " | " + fecha + " | " + presente;
    }

    @Override
    public String toString() {
        return String.format("%s | Estudiante: %d | Curso: %d | %s", 
                fecha, estudianteId, cursoId, presente ? "✅ Presente" : "❌ Ausente");
    }
}