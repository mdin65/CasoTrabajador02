package Util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorRUTTest {
    @Test
    public void testValidarFormatoRUT() {
        // Ruts Válidos
        assertTrue(ValidadorRUT.validarFormatoRUT("12345678-9"));
        assertTrue(ValidadorRUT.validarFormatoRUT("12.345.678-9"));
        assertTrue(ValidadorRUT.validarFormatoRUT("12345678-k"));
        assertTrue(ValidadorRUT.validarFormatoRUT("12345678"));

        //Ruts Invalidos
        assertFalse(ValidadorRUT.validarFormatoRUT("123-9"));
        assertFalse(ValidadorRUT.validarFormatoRUT("abcdefg-h"));
    }

    @Test
    public void testFormatearRUT() {
        assertEquals("12.345.678-9", ValidadorRUT.formatearRUT("12345678-9"));
        assertEquals("12.345.678-9", ValidadorRUT.formatearRUT("123456789"));
        assertEquals("12.345.678-K", ValidadorRUT.formatearRUT("12345678k"));
    }

}