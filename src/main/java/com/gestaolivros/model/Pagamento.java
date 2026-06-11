package com.gestaolivros.model;

import java.util.Date;

public class Pagamento {
    private int    idPagamento;
    private Date   dataPagamento;
    private String formaPagamento;
    private double valorTotal;
    private int    fkPedido;

    public Pagamento() {}

    public Pagamento(int idPagamento, Date dataPagamento, String formaPagamento, double valorTotal, int fkPedido) {
        this.idPagamento    = idPagamento;
        this.dataPagamento  = dataPagamento;
        this.formaPagamento = formaPagamento;
        this.valorTotal     = valorTotal;
        this.fkPedido       = fkPedido;
    }

    public int    getIdPagamento()                  { return idPagamento; }
    public void   setIdPagamento(int id)            { this.idPagamento = id; }
    public Date   getDataPagamento()                { return dataPagamento; }
    public void   setDataPagamento(Date d)          { this.dataPagamento = d; }
    public String getFormaPagamento()               { return formaPagamento; }
    public void   setFormaPagamento(String f)       { this.formaPagamento = f; }
    public double getValorTotal()                   { return valorTotal; }
    public void   setValorTotal(double v)           { this.valorTotal = v; }
    public int    getFkPedido()                     { return fkPedido; }
    public void   setFkPedido(int fk)               { this.fkPedido = fk; }

    @Override
    public String toString() {
        return String.format("Pagamento [ID=%-3d | PedidoID=%-3d | Forma=%-20s | Total=R$ %8.2f | Data=%s]",
                             idPagamento, fkPedido, formaPagamento, valorTotal, dataPagamento);
    }
}