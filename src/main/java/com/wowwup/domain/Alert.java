package com.wowwup.domain;

import lombok.Data;

@Data
public class Alert {
    private TipoAlerta type;
    private boolean read;
    private User destinatario;
    private Topic topic;
    private long fechaExpiracion;

    public Alert(TipoAlerta type, User destinatario, Topic topic, long fechaExpiracion) {
        this.type = type;
        this.read = false;
        this.destinatario = destinatario;
        this.topic = topic;
        this.fechaExpiracion = fechaExpiracion;
    }

    // Métodos para marcar como leída, verificar expiración, etc.

    // Otros métodos y lógica...
}
