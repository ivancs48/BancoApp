package org.calderon.junit5.models;

import org.calderon.junit5.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initCuenta(){
        System.out.println("Inicializado cuenta");
        this.cuenta = new Cuenta("Ivan", new BigDecimal("10000.253"));
    }

    @AfterEach
    void tearDown() {
        this.cuenta = null;
        System.out.println("Cuenta destruida");
    }


    @Test
    @DisplayName("Prueba de nombre de cuenta")
    void testNombreCuenta() {
        String esperado = "Ivan";
        String real = cuenta.getPersona();
        assertEquals(esperado, real, () -> "El nombre de cuenta no es el esperado, deberia ser: " + esperado);
    }

    @Test
    @DisplayName("Prueba de saldo de cuenta")
    void testSaldoCuenta() {
        assertEquals(10000.253, cuenta.getSaldo().doubleValue(), "El valor esperado no es el correcto");
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
    }

    @Test
    @DisplayName("Prueba refencia de cuentas")
    void testReferenciasCuentas() {
        Cuenta cuenta = new Cuenta("Jhon doe", new BigDecimal("10000"));
        Cuenta cuenta2 = new Cuenta("Jhon doe", new BigDecimal("10000"));
        assertEquals(cuenta, cuenta2);
    }

    @Test
    @DisplayName("Prueba debito cuentas")
    void debitoCuenta() {
        cuenta.setSaldo(new BigDecimal("1000"));
        try {
            cuenta.debito(new BigDecimal("100"));
        } catch (DineroInsuficienteException e) {
            System.out.println("Saldo insuficiente");
        }
        assertEquals(900, cuenta.getSaldo().intValue());
    }

    @Test
    @DisplayName("Prueba credito cuenta")
    @Disabled
    void creditoCuenta() {
        fail(); // fuerza el error de la prueba
        cuenta.setSaldo(new BigDecimal("1000.923"));
        cuenta.credito(new BigDecimal("100"));
        assertEquals("1100.923", cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Prueba de debitar valor mayor a cuenta")
    void testDineroInsuficiente() {
        cuenta.setSaldo(new BigDecimal("100"));
        assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(new BigDecimal("150")));
    }

    @Test
    @DisplayName("Prueba de transferir monto entre cuentas")
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
    @DisplayName("Prueba relacion de cuenta banco")
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

    @Test
    @DisplayName("Prueba solo windows")
    @EnabledOnOs(OS.WINDOWS)
    void testSoloWindows() {

    }

    @Test
    @DisplayName("Prueba no habilitada en windows")
    @DisabledOnOs(OS.WINDOWS)
    void desactivadoEnWindows() {

    }

    @Test
    @DisplayName("Prueba no windows")
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void testNoWindows() {

    }

    @Test
    @DisplayName("Solo java 20")
    @EnabledOnJre(JRE.JAVA_20)
    void desactivaddoEnWindows() {

    }

    @Test
    @DisplayName("Teste con propiedades")
    void imprimirPropiedades() {
        Properties properties = System.getProperties();
        properties.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    @Test
    @DisplayName("test de propiedad")
    @EnabledIfSystemProperty(named = "user.language", matches = "en")
    void testConPropiedad() {

    }

    @Test
    @DisplayName("test no arch32")
    @DisabledIfSystemProperty(named = "os.arch", matches = ".*32")
    void testPropiedad32() {

    }
}