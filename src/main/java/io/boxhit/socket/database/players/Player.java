package io.boxhit.socket.database.players;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    //auto increment
    @Id
    private Long id;

    @Column(length = 50)
    private String username;

    @Column(length = 60)
    private String password;

    @Column(length = 50)
    private String mail;

    @Column(length = 100, name="\"mailToken\"")
    private String mailToken;

    @Column(length = 100, name="\"sessionToken\"")
    private String sessionToken;

    @Column(length = 10)
    private String color;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getMailToken() {
        return mailToken;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString(){
        return "Player: "+this.username+" - "+this.id+" - "+this.sessionToken;
    }
}
