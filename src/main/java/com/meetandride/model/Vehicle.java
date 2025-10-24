package com.meetandride.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(
    name = "veicoli",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_targa", columnNames = "targa")
    }
)
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String marca;

    @NotBlank
    private String modello;

    @Column(nullable = false, unique = true, length = 12)
    private String targa;

    private String colore;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User owner;

    public Vehicle() {}

    public Vehicle(String marca, String modello, String targa, String colore, User owner) {
        this.marca = marca;
        this.modello = modello;
        this.targa = targa;
        this.colore = colore;
        this.owner = owner;
    }

    // 🔹 Getter e Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    @Override
    public String toString() {
        return marca + " " + modello + " (" + targa + ")";
    }
}
