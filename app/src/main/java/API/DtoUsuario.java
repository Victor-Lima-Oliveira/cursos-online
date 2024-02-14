package API;

public class DtoUsuario {
    public String status;
    public long usuarioId;
    public Boolean usuarioAcesso;
    public String getStatus() {
        return status;
    }
    public Boolean getAcesso() {
        return usuarioAcesso;
    }

    public long getId() {
        return usuarioId;
    }


}
