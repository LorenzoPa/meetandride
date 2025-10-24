# 🏎️ Meet&Ride — Versione Alpha

**Meet&Ride** è un’applicazione web sviluppata con **Spring Boot + Vaadin**, pensata per creare, gestire e partecipare a **eventi automobilistici**.  
Permette agli utenti di registrarsi, creare eventi pubblici o privati, iscriversi ai raduni, e gestire il proprio veicolo personale.

---

## 🚀 Funzionalità incluse nella versione Alpha

- ✅ **Registrazione / Login** (Username, Email, Password)
- ✅ **Creazione evento** con Titolo, Descrizione, Visibilità (Aperto / Chiuso / Privato), Località, Data e Orario  
- ✅ **Modifica evento** da parte dell’host
- ✅ **Gestione richieste di partecipazione** (per eventi chiusi)
- ✅ **Invito utente tramite username**
- ✅ **Iscrizione / ritiro da evento**
- ✅ **Visualizzazione eventi e dettagli completi**
- ✅ **Ricerca eventi** per titolo, località, data o host
- ✅ **Cronologia eventi (creati e partecipati)**
- ✅ **Registrazione veicolo** (1 per utente)

---

## 🧩 Stack Tecnologico

| Componente | Tecnologia |
|-------------|-------------|
| **Backend** | Spring Boot 3, Spring Security, JPA/Hibernate |
| **Frontend** | Vaadin Flow 24 |
| **Database** | H2 / PostgreSQL |
| **Build Tool** | Maven |
| **Linguaggio** | Java 17+ |

---

## ⚙️ Requisiti minimi

- ☕ **Java 17** o superiore  
- 🧰 **Maven 3.8+**  
- (Facoltativo) Database PostgreSQL o H2  
- 🌐 Browser moderno (Chrome, Edge, Firefox)

---

## 📂 Struttura del progetto
## 🧠 Architettura

```bash
src/
 ├── main/java/com/meetandride/
 │    ├── model/          → Entità JPA (User, Event, Vehicle)
 │    ├── repository/     → Repository JPA per accesso ai dati
 │    ├── service/        → Logica applicativa e gestione business
 │    ├── security/       → Configurazione autenticazione e ruoli
 │    ├── layout/         → Layout principale Vaadin (MainLayout)
 │    └── view/           → Interfaccia utente Vaadin (Home, Eventi, Login, ecc.)
 └── resources/
      ├── application.properties  → Configurazione database e porte
      └── static/ e templates/    → (eventuali risorse front-end)
```
## 🧾 Credenziali di test (esempio)

Per testare rapidamente l’applicazione in locale puoi utilizzare:

```bash
Username: admin
Password: admin
```
Puoi anche creare nuovi utenti direttamente dalla pagina Registrazione (/register).

## 🛠️ Istruzioni per l’esecuzione

### ▶️ Da IDE (IntelliJ, Eclipse, VS Code)

1. Clona il progetto:
```bash
   git clone https://github.com/<tuo-username>/meetandride.git
```
Apri il progetto nel tuo IDE preferito.

Esegui la classe principale:
```bash
com.meetandride.MeetAndRideApplication
```
Apri nel browser:
http://localhost:8080
▶️ Da terminale (Maven)
```bash
mvn spring-boot:run
```
L’applicazione sarà disponibile su http://localhost:8080

## 🗺️ Roadmap — Prossime versioni

### 🔹 Versione Beta (in sviluppo)
- [ ] Sistema di **notifiche** per richieste e inviti  
- [ ] **Mappa interattiva** con Google Maps (posizione eventi)  
- [ ] **Commenti / Chat evento** per i partecipanti  
- [ ] **Distinzione eventi futuri e passati**  
- [ ] Miglioramento interfaccia utente con **Vaadin responsive layout**  
- [ ] **Profilo utente** con statistiche e storico eventi  
- [ ] Gestione **ruoli avanzati (USER / ADMIN)**  

### 🔸 Versione 1.0 (release stabile)
- [ ] Dashboard personale con riepilogo eventi e notifiche  
- [ ] Sistema di reputazione o badge per utenti attivi  
- [ ] Integrazione con social (condivisione eventi)  
- [ ] Deploy su server cloud (Render / Railway / AWS)  

## 👨‍💻 Autore

**Lorenzo Paniccia**  
📍 Italia — 2025  

---


