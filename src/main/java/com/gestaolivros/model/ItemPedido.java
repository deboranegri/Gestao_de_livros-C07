package com.gestaolivros.model;

public class ItemPedido {
    private int    fkPedido;
    private int    fkLivro;
    private int    quantidade;
    private double precoUnitario;

    public ItemPedido() {}

    public ItemPedido(int fkPedido, int fkLivro, int quantidade, double precoUnitario) {
        this.fkPedido      = fkPedido;
        this.fkLivro       = fkLivro;
        this.quantidade    = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int    getFkPedido()                  { return fkPedido; }
    public void   setFkPedido(int fk)            { this.fkPedido = fk; }
    public int    getFkLivro()                   { return fkLivro; }
    public void   setFkLivro(int fk)             { this.fkLivro = fk; }
    public int    getQuantidade()                { return quantidade; }
    public void   setQuantidade(int q)           { this.quantidade = q; }
    public double getPrecoUnitario()             { return precoUnitario; }
    public void   setPrecoUnitario(double p)     { this.precoUnitario = p; }

    @Override
    public String toString() {
        return String.format("ItemPedido [PedidoID=%-3d | LivroID=%-3d | Qtd=%-3d | Preço=R$ %.2f]",
                             fkPedido, fkLivro, quantidade, precoUnitario);
    }
}