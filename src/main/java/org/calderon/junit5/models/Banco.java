package org.calderon.junit5.models;

import org.calderon.junit5.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {
    private String nombre;
    private List<Cuenta> cuentas;

    public Banco() {
        this.cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public void addCuenta(Cuenta cuenta) {
        cuenta.setBanco(this);
        this.cuentas.add(cuenta);
    }

    public void trasnferir(Cuenta origen, Cuenta destino, BigDecimal monto) throws DineroInsuficienteException {
        if(origen.getSaldo().compareTo(monto) >= 0){
            origen.debito(monto);
            destino.credito(monto);
        } else {
            throw new DineroInsuficienteException("No hay suficiente saldo en la cuenta");
        }
    }
}
