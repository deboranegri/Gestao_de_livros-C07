package com.gestaolivros.model;

import java.util.Date;

public class Cliente {
    private int    idCliente;
    private String nome;
    private String email;
    private Date   dataNascimento;
    private int    fkPerfilUsuario;

    public Cliente() {}

    public Cliente(int idCliente, String nome, String email, Date dataNascimento, int fkPerfilUsuario) {
        this.idCliente       = idCliente;
        this.nome            = nome;
        this.email           = email;
        this.dataNascimento  = dataNascimento;
        this.fkPerfilUsuario = fkPerfilUsuario;
    }

    public int    getIdCliente()                    { return idCliente; }
    public void   setIdCliente(int id)              { this.idCliente = id; }
    public String getNome()                         { return nome; }
    public void   setNome(String n)                 { this.nome = n; }
    public String getEmail()                        { return email; }
    public void   setEmail(String e)                { this.email = e; }
    public Date   getDataNascimento()               { return dataNascimento; }
    public void   setDataNascimento(Date d)         { this.dataNascimento = d; }
    public int    getFkPerfilUsuario()              { return fkPerfilUsuario; }
    public void   setFkPerfilUsuario(int fk)        { this.fkPerfilUsuario = fk; }

    @Override
    public String toString() {
        return String.format("Cliente [ID=%-3d | Nome=%-20s | Email=%-25s | PerfilID=%d]",
                             idCliente, nome, email, fkPerfilUsuario);
    }
}
