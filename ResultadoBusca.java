import java.util.List;

public class ResultadoBusca {

    public Estado solucao;
    public List<LogNo> log;

    public long tempo;
    public int nosExpandidos;
    public int movimentos;

    public ResultadoBusca(Estado solucao, List<LogNo> log, long tempo, int nosExpandidos) {
        this.solucao = solucao;
        this.log = log;
        this.tempo = tempo;
        this.nosExpandidos = nosExpandidos;
        this.movimentos = (solucao != null) ? solucao.profundidade : -1;
    }
}