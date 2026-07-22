package reports.academicos.model;

public class Curso {
    private int id;
    private String nombre;
    private String codigo;
    private int creditos;
    private String docente;
    private Estudiante[] estudiantes;      // ← ARREGLO de objetos
    private int contadorEstudiantes;
    private double[][] notas;              // ← ARREGLO BIDIMENSIONAL
    private int[] cantidadNotas;           // ← ARREGLO
    private int maxEstudiantes;

    public Curso(int id, String nombre, String codigo, int creditos, String docente) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.creditos = creditos;
        this.docente = docente;
        this.maxEstudiantes = 50;
        this.estudiantes = new Estudiante[maxEstudiantes];
        this.notas = new double[maxEstudiantes][10];
        this.cantidadNotas = new int[maxEstudiantes];
        this.contadorEstudiantes = 0;
    }

    public Curso(String linea) {
        String[] datos = linea.split("\\|");
        this.id = Integer.parseInt(datos[0].trim());
        this.nombre = datos[1].trim();
        this.codigo = datos[2].trim();
        this.creditos = Integer.parseInt(datos[3].trim());
        this.docente = datos[4].trim();
        this.maxEstudiantes = 50;
        this.estudiantes = new Estudiante[maxEstudiantes];
        this.notas = new double[maxEstudiantes][10];
        this.cantidadNotas = new int[maxEstudiantes];
        this.contadorEstudiantes = 0;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCodigo() { return codigo; }
    public int getCreditos() { return creditos; }
    public String getDocente() { return docente; }
    public Estudiante[] getEstudiantes() { return estudiantes; }
    public int getContadorEstudiantes() { return contadorEstudiantes; }

    public void agregarEstudiante(Estudiante e) {
        if (contadorEstudiantes < maxEstudiantes) {
            estudiantes[contadorEstudiantes] = e;
            contadorEstudiantes++;
        }
    }

    public void agregarNota(int estudianteId, double nota) {
        for (int i = 0; i < contadorEstudiantes; i++) {
            if (estudiantes[i] != null && estudiantes[i].getId() == estudianteId) {
                int pos = cantidadNotas[i];
                if (pos < 10) {
                    notas[i][pos] = nota;
                    cantidadNotas[i]++;
                }
                break;
            }
        }
    }

    private int buscarPosicionEstudiante(int estudianteId) {
        for (int i = 0; i < contadorEstudiantes; i++) {
            if (estudiantes[i] != null && estudiantes[i].getId() == estudianteId) {
                return i;
            }
        }
        return -1;
    }

    public double[] getNotas(int estudianteId) {
        int pos = buscarPosicionEstudiante(estudianteId);
        if (pos == -1) return new double[0];
        double[] resultado = new double[cantidadNotas[pos]];
        System.arraycopy(notas[pos], 0, resultado, 0, cantidadNotas[pos]);
        return resultado;
    }

    public double getPromedio(int estudianteId) {
        int pos = buscarPosicionEstudiante(estudianteId);
        if (pos == -1 || cantidadNotas[pos] == 0) return 0.0;
        double suma = 0.0;
        for (int i = 0; i < cantidadNotas[pos]; i++) {
            suma += notas[pos][i];
        }
        return suma / cantidadNotas[pos];
    }

    public double getPromedioCurso() {
        if (contadorEstudiantes == 0) return 0.0;
        double suma = 0.0;
        for (int i = 0; i < contadorEstudiantes; i++) {
            suma += getPromedio(estudiantes[i].getId());
        }
        return suma / contadorEstudiantes;
    }

    public int getAprobados() {
        int count = 0;
        for (int i = 0; i < contadorEstudiantes; i++) {
            if (getPromedio(estudiantes[i].getId()) >= 12) count++;
        }
        return count;
    }

    public int getReprobados() {
        return contadorEstudiantes - getAprobados();
    }

    public Estudiante buscarEstudiantePorId(int id) {
        for (int i = 0; i < contadorEstudiantes; i++) {
            if (estudiantes[i] != null && estudiantes[i].getId() == id) {
                return estudiantes[i];
            }
        }
        return null;
    }

    public String toLinea() {
        return id + " | " + nombre + " | " + codigo + " | " + creditos + " | " + docente;
    }

    public String[] getNotasLineas() {
        String[] lineas = new String[contadorEstudiantes];
        int idx = 0;
        for (int i = 0; i < contadorEstudiantes; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(id).append(" | ").append(estudiantes[i].getId()).append(" | ");
            for (int j = 0; j < cantidadNotas[i]; j++) {
                if (j > 0) sb.append(",");
                sb.append(String.format("%.1f", notas[i][j]));
            }
            lineas[idx++] = sb.toString();
        }
        return lineas;
    }

    @Override
    public String toString() {
        return String.format("%-4d | %-25s | %-10s | %d creditos | %s", 
                id, nombre, codigo, creditos, docente);
    }
}