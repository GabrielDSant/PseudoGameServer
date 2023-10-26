package org.example;

import java.io.Serializable;

public class Pacote implements Serializable {
    private static final long serialVersionUID = 1L;
    private String data;

    public Pacote(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
