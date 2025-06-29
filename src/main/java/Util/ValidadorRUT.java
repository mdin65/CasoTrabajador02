package Util;

public class ValidadorRUT {

    public static String limpiarRUT(String rut) {
        return rut.replace(".", "").replace("-", "").replace(" ", "").toUpperCase();
    }

    public static boolean validarFormatoRUT(String rut) {
        String rutLimpio = limpiarRUT(rut);
        return rutLimpio.matches("^[0-9]{7,8}[0-9K]$");
    }

    public static String formatearRUT(String rut) {
        String rutLimpio = limpiarRUT(rut);
        if (rutLimpio.length() < 2) return rut;

        String cuerpo = rutLimpio.substring(0, rutLimpio.length() - 1);
        char dv = rutLimpio.charAt(rutLimpio.length() - 1);

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
