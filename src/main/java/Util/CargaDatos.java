package Util;

import Modelo.Trabajador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

public class CargaDatos {

    private static final String ARCHIVO_TRABAJADORES = "Trabajadores.txt";
    private static final String DIR_BACKUPS = "backups_trabajadores";
    private static final int MAX_BACKUPS = 5;


    public static Map<String, Trabajador> cargaTrabajadoresDesdeResources() {
        Map<String, Trabajador> trabajadores = new HashMap<>();
        Path archivoPrincipal = Paths.get("src/main/resources/" + ARCHIVO_TRABAJADORES);

        try {
            // 1. Intentar cargar desde archivo principal
            if (Files.exists(archivoPrincipal)) {
                try (BufferedReader br = Files.newBufferedReader(archivoPrincipal)) {
                    cargarDesdeArchivo(br, trabajadores);
                    return trabajadores;
                }
            }

            // 2. Si no existe, buscar el último backup
            Path ultimoBackup = obtenerUltimoBackup();
            if (ultimoBackup != null) {
                System.out.println("Cargando desde backup: " + ultimoBackup);
                try (BufferedReader br = Files.newBufferedReader(ultimoBackup)) {
                    cargarDesdeArchivo(br, trabajadores);

                    // Restaurar backup como nuevo archivo principal
                    Files.copy(ultimoBackup, archivoPrincipal, StandardCopyOption.REPLACE_EXISTING);
                    return trabajadores;
                }
            }

            // 3. Si no hay backups, crear archivo vacío
            Files.createFile(archivoPrincipal);
            System.out.println("Archivo creado: " + archivoPrincipal);

        } catch (Exception e) {
            System.err.println("Error cargando trabajadores: " + e.getMessage());
        }
        return trabajadores;
    }

    private static Path obtenerUltimoBackup() throws IOException {
        if (!Files.exists(Paths.get(DIR_BACKUPS))) {
            return null;
        }

        try (Stream<Path> stream = Files.list(Paths.get(DIR_BACKUPS))) {
            return stream.filter(p -> p.getFileName().toString().startsWith("backup_"))
                    .max(Comparator.comparing(p -> p.getFileName().toString()))
                    .orElse(null);
        }
    }


    public static void guardarTrabajadores(Map<String, Trabajador> trabajadores) {
        try {

            crearBackup();
            limpiarBackupsExcedentes();

            Path archivo = Paths.get("src/main/resources/" + ARCHIVO_TRABAJADORES);
            try (BufferedWriter writer = Files.newBufferedWriter(
                    archivo,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING)) {

                for (Trabajador t : trabajadores.values()) {
                    writer.write(String.join(",",
                            t.getNombre(),
                            t.getApellido(),
                            t.getRut(),
                            t.getIsapre(),
                            t.getAfp()));
                    writer.newLine();
                }
            }

            System.out.println("Datos guardados exitosamente en " + archivo);
        } catch (IOException e) {
            System.err.println("Error crítico al guardar cambios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void cargarDesdeArchivo(BufferedReader br,
                                           Map<String, Trabajador> trabajadores) throws IOException {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(",");
            if (datos.length == 5) {
                // Validar el RUT antes de cargar
                if (ValidadorRUT.validarFormatoRUT(datos[2])) {
                    String rutFormateado = ValidadorRUT.formatearRUT(datos[2]);
                    Trabajador t = new Trabajador(
                            datos[0], datos[1], rutFormateado, datos[3], datos[4]);
                    trabajadores.put(rutFormateado, t);
                } else {
                    System.err.println("RUT inválido omitido: " + datos[2]);
                }
            }
        }
    }



    private static void crearBackup() throws IOException {
        Path archivoOriginal = Paths.get("src/main/resources/" + ARCHIVO_TRABAJADORES);
        Path dirBackups = Paths.get(DIR_BACKUPS);

        // Crear directorio si no existe
        if (!Files.exists(dirBackups)) {
            try {
                Files.createDirectories(dirBackups);
            } catch (IOException e) {
                System.err.println("No se pudo crear directorio de backups: " + e.getMessage());
                return; // Continuar sin backup si no se puede crear directorio
            }
        }

        if (Files.exists(archivoOriginal)) {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            Path backup = dirBackups.resolve("backup_"+ timestamp + ".txt");

            try {
                Files.copy(archivoOriginal, backup);
            } catch (IOException e) {
                System.err.println("Error creando backup: " + e.getMessage());
                // Continuar sin backup
            }
        }
    }

    private static void limpiarBackupsExcedentes() throws IOException {
        try (Stream<Path> stream = Files.list(Paths.get(DIR_BACKUPS))) {
            List<Path> backups = stream
                    .filter(p -> p.getFileName().toString().startsWith("backup_"))
                    .sorted(Comparator.reverseOrder())
                    .collect(java.util.stream.Collectors.toList());

            if (backups.size() > MAX_BACKUPS) {
                for (Path p : backups.subList(MAX_BACKUPS, backups.size())) {
                    Files.delete(p);
                }
            }
        }
    }
}