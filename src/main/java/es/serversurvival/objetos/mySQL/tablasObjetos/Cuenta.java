package es.serversurvival.objetos.mySQL.tablasObjetos;

public class Cuenta {
    private int id;
    private String username;
    private String password;
    private boolean active;
    private String roles;

    public Cuenta(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;

        this.roles = "USER";
        this.active = true;
    }

    public Cuenta (){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String contra) {
        this.password = contra;
    }

    public boolean isActive() {
        return active;
    }

    public String getRoles() {
        return roles;
    }
}