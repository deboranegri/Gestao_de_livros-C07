package com.gestaolivros.model;

public class PerfilUsuario {
    private int    idPerfilUsuario;
    private String userName;
    private String preferenciasDeLeitura;

    public PerfilUsuario() {}

    public PerfilUsuario(int idPerfilUsuario, String userName, String preferenciasDeLeitura) {
        this.idPerfilUsuario       = idPerfilUsuario;
        this.userName              = userName;
        this.preferenciasDeLeitura = preferenciasDeLeitura;
    }

    public int    getIdPerfilUsuario()              { return idPerfilUsuario; }
    public void   setIdPerfilUsuario(int id)        { this.idPerfilUsuario = id; }
    public String getUserName()                     { return userName; }
    public void   setUserName(String u)             { this.userName = u; }
    public String getPreferenciasDeLeitura()        { return preferenciasDeLeitura; }
    public void   setPreferenciasDeLeitura(String p){ this.preferenciasDeLeitura = p; }

    @Override
    public String toString() {
        return String.format("PerfilUsuario [ID=%-3d | User=%-20s | Prefs=%s]",
                             idPerfilUsuario, userName, preferenciasDeLeitura);
    }
}
