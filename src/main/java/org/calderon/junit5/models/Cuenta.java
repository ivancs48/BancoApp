package org.calderon.junit5.models;

import java.math.BigDecimal;
import org.calderon.junit5.exceptions.DineroInsuficienteException;

public class Cuenta {
    private String persona;
    private BigDecimal saldo;
    private Banco banco;

    public Cuenta(String persona, BigDecimal saldo) {
        this.persona = persona;
        this.saldo = saldo;
    }

    public String getPersona() {
        return persona;
    }

    public void setPersona(String persona) {
        this.persona = persona;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void debito(BigDecimal monto) throws DineroInsuficienteException {
        if(this.saldo.compareTo(monto) < 0) {
            throw new DineroInsuficienteException("No hay suficiente saldo en la cuenta");
        }
        this.saldo = this.saldo.subtract(monto);
    }

    public void credito(BigDecimal monto) {
        this.saldo = this.saldo.add(monto);
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || !(obj instanceof Cuenta)) {
            return false;
        }
        Cuenta c = (Cuenta) obj;
        if(this.persona ==   null || this.saldo == null) {
            return false;
        }
        return this.persona.equals(c.getPersona()) && this.saldo.equals(c.getSaldo());
    }
}
