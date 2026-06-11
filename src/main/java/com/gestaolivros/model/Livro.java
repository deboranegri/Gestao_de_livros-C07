package com.gestaolivros.model;

public class Livro {
    private int idLivro;
    private String titulo;
    private double preco;
    private int estoque;

    public Livro() {}

    public Livro(int idLivro, String titulo, double preco, int estoque) {
        this.idLivro  = idLivro;
        this.titulo   = titulo;
        this.preco    = preco;
        this.estoque  = estoque;
    }

    public int    getIdLivro()              { return idLivro; }
    public void   setIdLivro(int id)        { this.idLivro = id; }
    public String getTitulo()               { return titulo; }
    public void   setTitulo(String t)       { this.titulo = t; }
    public double getPreco()                { return preco; }
    public void   setPreco(double p)        { this.preco = p; }
    public int    getEstoque()              { return estoque; }
    public void   setEstoque(int e)         { this.estoque = e; }

    @Override
    public String toString() {
        return String.format("Livro [ID=%-3d | Titulo=%-30s | Preco=R$ %6.2f | Estoque=%d]",
                             idLivro, titulo, preco, estoque);
    }
}
