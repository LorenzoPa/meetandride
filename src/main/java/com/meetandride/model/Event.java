package com.meetandride.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "eventi")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;

    @Column(length = 1000)
    private String descrizione;

    private String visibilita;
    private String localita;
    private LocalDate data;
    private String orario;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Event(){

    }

    public Event(String titolo, String descrizione, String visibilita,
                 String localita, LocalDate data, String orario, User user) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.visibilita = visibilita;
        this.localita = localita;
        this.data = data;
        this.orario = orario;
        this.user = user;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitolo() { return titolo; }
    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getVisibilita() { return visibilita; }
    public void setVisibilita(String visibilita) { this.visibilita = visibilita; }

    public String getLocalita() { return localita; }
    public void setLocalita(String localita) { this.localita = localita; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public String getOrario() { return orario; }
    public void setOrario(String orario) { this.orario = orario; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
