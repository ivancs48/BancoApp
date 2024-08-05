package org.calderon.junit5.models;

import org.calderon.junit5.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("10000.253"));
        String esperado = "Ivan";
        String real = cuenta.getPersona();
        assertEquals(esperado, real);
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("10000.253"));
        assertEquals(10000.253, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    void testReferenciasCuentas() {
        Cuenta cuenta = new Cuenta("Jhon doe", new BigDecimal("10000"));
        Cuenta cuenta2 = new Cuenta("Jhon doe", new BigDecimal("10000"));
        assertEquals(cuenta, cuenta2);
    }

    @Test
    void debitoCuenta() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("1000"));
        try {
            cuenta.debito(new BigDecimal("100"));
        } catch (DineroInsuficienteException e) {
            System.out.println("Saldo insuficiente");
        }
        assertEquals(900, cuenta.getSaldo().intValue());
    }

    @Test
    void creditoCuenta() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("1000.923"));
        cuenta.credito(new BigDecimal("100"));
        assertEquals("1100.923", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficiente() {
        Cuenta cuenta = new Cuenta("Ivan", new BigDecimal("100"));
        assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(new BigDecimal("150")));
    }

    @Test
    void testTransferirDinero() {
        Cuenta cuenta1 = new Cuenta("Ivan", new BigDecimal("1000"));
        Cuenta cuenta2 = new Cuenta("Jhon doe", new BigDecimal("0"));

        Banco banco = new Banco();
        banco.setNombre("Mi banco");

        try {
            banco.trasnferir(cuenta1, cuenta2, new BigDecimal("500"));
        } catch (DineroInsuficienteException e) {
            System.out.println("Saldo insuficiente");
        }
        assertEquals(500, cuenta1.getSaldo().intValue());
        assertEquals(500, cuenta2.getSaldo().intValue());
    }

    @Test
    void testRelacionBancoCuenta() {
        Cuenta cuenta1 = new Cuenta("Ivan", new BigDecimal("1000"));
        Cuenta cuenta2 = new Cuenta("Jhon doe", new BigDecimal("0"));

        Banco banco = new Banco();
        banco.setNombre("Mi banco");
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        try {
            banco.trasnferir(cuenta1, cuenta2, new BigDecimal("500"));
        } catch (DineroInsuficienteException e) {
            System.out.println("Saldo insuficiente");
        }
        assertEquals(2, banco.getCuentas().size());
        assertEquals("Mi banco", cuenta1.getBanco().getNombre());
    }


}