package org.calderon.junit5.models;

import org.calderon.junit5.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

public class Banco {
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
