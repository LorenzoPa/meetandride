package com.meetandride.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "veicoli")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modello;
    private String targa;
    private String colore;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    public Vehicle() {}

    public Vehicle(String marca, String modello, String targa, String colore, User owner) {
        this.marca = marca;
        this.modello = modello;
        this.targa = targa;
        this.colore = colore;
        this.owner = owner;
    }

    public Long getId() { return id; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModello() { return modello; }
    public void setModello(String modello) { this.modello = modello; }
    public String getTarga() { return targa; }
    public void setTarga(String targa) { this.targa = targa; }
    public String getColore() { return colore; }
    public void setColore(String colore) { this.colore = colore; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
}
