package activities;

public class DtoCurso {
    private String nomeCurso;
    private Double valorCurso;
    private int duracaoCurso;
    private long idCurso;

    public DtoCurso(String nomeCurso, Double valorCurso, int duracaoCurso) {
        this.nomeCurso = nomeCurso;
        this.valorCurso = valorCurso;
        this.duracaoCurso = duracaoCurso;
    }
    public DtoCurso(long idCurso, String nomeCurso, Double valorCurso, int duracaoCurso) {
        this.idCurso = idCurso;
        this.nomeCurso = nomeCurso;
        this.valorCurso = valorCurso;
        this.duracaoCurso = duracaoCurso;
    }

    @Override
    public String toString() {
        return nomeCurso;
    }

    public DtoCurso() {
    }

    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public Double getValorCurso() {
        return valorCurso;
    }

    public void setValorCurso(Double valorCurso) {
        this.valorCurso = valorCurso;
    }

    public int getDuracaoCurso() {
        return duracaoCurso;
    }

    public void setDuracaoCurso(int duracaoCurso) {
        this.duracaoCurso = duracaoCurso;
    }

    public long getIdCurso() {
        return idCurso;
    }

    public void setIdCurso(long idCurso) {
        this.idCurso = idCurso;
    }
}
