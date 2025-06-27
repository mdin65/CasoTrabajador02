package Util;

public class ValidadorRUT {


    public static boolean validarFormatoRUT(String rut) {
        // Eliminar puntos, guión y espacios
        String rutLimpio = rut.replace(".", "").replace("-", "").replace(" ", "");

        // Validar: cantidad correcta de números(7-8), que sean solo número y que el DV sea número o K
        return rutLimpio.matches("^[0-9]{7,8}[0-9kK]$");
    }

    public static String formatearRUT(String rut) {
        String rutLimpio = rut.replace(".", "").replace("-", "").replace(" ", "");
        if (rutLimpio.length() < 2) return rut;

        String cuerpo = rutLimpio.substring(0, rutLimpio.length() - 1);
        char dv = rutLimpio.charAt(rutLimpio.length() - 1);

        // Aplicar formato con puntos
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cuerpo.length(); i++) {
            if (i > 0 && (cuerpo.length() - i) % 3 == 0) {
                sb.append(".");
            }
            sb.append(cuerpo.charAt(i));
        }

        return sb.append("-").append(dv).toString();
    }
}