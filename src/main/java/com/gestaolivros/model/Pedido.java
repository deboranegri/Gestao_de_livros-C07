package com.gestaolivros.model;

import java.util.Date;

public class Pedido {
    private int  idPedido;
    private Date dataPedido;
    private int  fkCliente;

    public Pedido() {}

    public Pedido(int idPedido, Date dataPedido, int fkCliente) {
        this.idPedido   = idPedido;
        this.dataPedido = dataPedido;
        this.fkCliente  = fkCliente;
    }

    public int  getIdPedido()           { return idPedido; }
    public void setIdPedido(int id)     { this.idPedido = id; }
    public Date getDataPedido()         { return dataPedido; }
    public void setDataPedido(Date d)   { this.dataPedido = d; }
    public int  getFkCliente()          { return fkCliente; }
    public void setFkCliente(int fk)    { this.fkCliente = fk; }

    @Override
    public String toString() {
        return String.format("Pedido [ID=%-3d | Data=%-25s | ClienteID=%d]",
                             idPedido, dataPedido, fkCliente);
    }
}
