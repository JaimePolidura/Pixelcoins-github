package es.serversurvival.mySQL.tablasObjetos;

public final class Cuenta implements TablaObjeto{
    private final int id;
    private final String username;
    private final String password;
    private final boolean active;
    private final String roles;

    public Cuenta(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;

        this.roles = "USER";
        this.active = true;
    }

    public final int getId() {
        return id;
    }

    public final String getUsername() {
        return username;
    }

    public final String getPassword() {
        return password;
    }

    public final boolean isActive() {
        return active;
    }

    public final String getRoles() {
        return roles;
    }
}