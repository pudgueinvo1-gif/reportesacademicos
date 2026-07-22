package reports.academicos.model;

public class Estudiante {
    private int id;
    private String nombre;
    private String apellido;
    private String carrera;
    private int semestre;
    private String email;
    private boolean activo;

    public Estudiante(int id, String nombre, String apellido, String carrera, int semestre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.carrera = carrera;
        this.semestre = semestre;
        this.email = email;
        this.activo = true;
    }

    public Estudiante(String linea) {
        String[] datos = linea.split("\\|");
        this.id = Integer.parseInt(datos[0].trim());
        this.nombre = datos[1].trim();
        this.apellido = datos[2].trim();
        this.carrera = datos[3].trim();
        this.semestre = Integer.parseInt(datos[4].trim());
        this.email = datos[5].trim();
        this.activo = Boolean.parseBoolean(datos[6].trim());
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getNombreCompleto() { return nombre + " " + apellido; }
    public String getCarrera() { return carrera; }
    public int getSemestre() { return semestre; }
    public String getEmail() { return email; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getSemestreTexto() {
        switch (semestre) {
            case 1: return "1er Semestre";
            case 2: return "2do Semestre";
            case 3: return "3er Semestre";
            case 4: return "4to Semestre";
            case 5: return "5to Semestre";
            default: return semestre + "° Semestre";
        }
    }

    public String toLinea() {
        return id + " | " + nombre + " | " + apellido + " | " + carrera + " | " + 
               semestre + " | " + email + " | " + activo;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-20s | %-15s | %s | %s", 
                id, getNombreCompleto(), carrera, getSemestreTexto(), email);
    }
}