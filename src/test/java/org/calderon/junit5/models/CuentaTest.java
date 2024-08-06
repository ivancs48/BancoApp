package org.calderon.junit5.models;

import org.calderon.junit5.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initCuenta() {
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
        DineroInsuficienteException excepcion = assertThrows(DineroInsuficienteException.class, () -> cuenta.debito(new BigDecimal("150")));
        assertEquals("No hay suficiente saldo en la cuenta", excepcion.getMessage());
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

    @Nested
    @DisplayName("Test de windows")
    class SystemaOperativo {
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
    }

    @Nested
    @DisplayName("Test de la version de java")
    class JavaVersionTest {
        @Test
        @DisplayName("Solo java 20")
        @EnabledOnJre(JRE.JAVA_20)
        void desactivaddoEnWindows() {

        }
    }

    @Nested
    @DisplayName("Test de Propiedades")
    class SystemPropiertis {

        @Test
        @DisplayName("Imprimir propiedades")
        void imprimirPropiedades() {
            System.getProperties().forEach((k, v) -> System.out.println(k + ": " + v));
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

        @Test
        @DisplayName("Valida propiedad programacion")
        void creditoCuentaJava20() {
            boolean isJava20 = "20.0.1".equals(System.getProperty("java.version"));
            assumeTrue(isJava20);
            cuenta.setSaldo(new BigDecimal("1000.923"));
            cuenta.credito(new BigDecimal("100"));
            assertEquals("1100.923", cuenta.getSaldo().toPlainString());
        }

        @Test
        @DisplayName("Valida propiedad programacion 2")
        void creditoCuentaJava20_2() {
            boolean isJava21 = "21.0.1".equals(System.getProperty("java.version"));
            assumingThat(isJava21, () -> {
                System.out.println("Ingresa validacion de java");
                cuenta.setSaldo(new BigDecimal("1000.923"));
                cuenta.credito(new BigDecimal("200"));
                assertEquals(1200.923, cuenta.getSaldo().doubleValue());
            });
            System.out.println("Realiza proceso despues de validacion java");
            cuenta.setSaldo(new BigDecimal("1000.923"));
            cuenta.credito(new BigDecimal("100"));
            assertEquals("1100.923", cuenta.getSaldo().toPlainString());
        }

    }

    @Nested
    @DisplayName("Test de ambiente")
    class TestAmbiente {
        @Test
        @DisplayName("Imprimier datos de ambiente")
        void imprimirVariablesAmbiente() {
            System.getenv().forEach((k, v) -> System.out.println(k + ": " + v));
        }


        @Test
        @DisplayName("Test procesadores8")
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testAmbienteProcesadores() {

        }
    }

    @RepeatedTest(5)
    @DisplayName("Prueba repetida debito cuentas")
    void debitoCuentaRepetido(RepetitionInfo info) {
        cuenta.setSaldo(new BigDecimal("1000"));
        if(info.getCurrentRepetition() == 3){
            cuenta.setSaldo(new BigDecimal("100"));
            System.out.println("Repeticion numero 3");
        }

        try {
            cuenta.debito(new BigDecimal("100"));
        } catch (DineroInsuficienteException e) {
            System.out.println("Saldo insuficiente");
        }
        if(info.getCurrentRepetition() == 3) {
            assertEquals(0, cuenta.getSaldo().intValue());
        }else {
            assertEquals(900, cuenta.getSaldo().intValue());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"100", "200", "300", "500", "1000"})
    @DisplayName("Prueba debito parametrizado")
    void debitoCuentaParametrizada(String monto) {
        try {
            cuenta.debito(new BigDecimal(monto));
        } catch (DineroInsuficienteException e) {
            System.out.println("Saldo insuficiente");
        }
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "numero {index} ejecutado con valor {0} - {argumentsWithNames}")
    @CsvSource({ "200,100,John,Andres", "250,200,Ivan,Ivan", "299,300,Ivan,Ivan","400,500,Ivan,Ivan", "750,700,Ivan,Ivan","1000.12345,1000.12345,Ivan,Ivan"})
    void debitoCuentaParametrizadaCsv2(String saldo, String monto, String esperado, String actual) throws DineroInsuficienteException {
        System.out.println(saldo + " -> " + monto);
        cuenta.setPersona(actual);
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo(), "Valor de la cuenta nula");
        assertNotNull(cuenta.getPersona(), "Valor del nombre nulo");
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, "Fallo por valor de la cuenta");
        assertEquals(esperado, cuenta.getPersona(), "Fallo por Nombre de la cuenta");
    }

    @ParameterizedTest(name = "numero {index} ejecutado con valor {0} - {argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void debitoCuentaParametrizadaCsv(String saldo, String monto, String esperado, String actual) throws DineroInsuficienteException {
        System.out.println(saldo + " -> " + monto);
        cuenta.setPersona(actual);
        cuenta.setSaldo(new BigDecimal(saldo));
        cuenta.debito(new BigDecimal(monto));
        assertNotNull(cuenta.getSaldo(), "Valor de la cuenta nula");
        assertNotNull(cuenta.getPersona(), "Valor del nombre nulo");
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, "Fallo por valor de la cuenta");
        assertEquals(esperado, cuenta.getPersona(), "Fallo por Nombre de la cuenta");
    }
}