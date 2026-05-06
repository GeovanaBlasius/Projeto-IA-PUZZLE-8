import java.util.*;

public class Solucionador {

    public static ResultadoBusca resolver(Estado inicial, ModoBusca modo) {

        long tempoInicio = System.currentTimeMillis();

        List<LogNo> log = new ArrayList<>();
        Set<Estado> visitados = new HashSet<>();

        Queue<Estado> fila = new LinkedList<>();

        PriorityQueue<Estado> filaPrioridade = new PriorityQueue<>(
                Comparator.comparingInt(e -> custo(e, modo))
        );

        int expandidos = 0;

        if (modo == ModoBusca.BH) {
            fila.add(inicial);
            visitados.add(inicial);
        } else {
            filaPrioridade.add(inicial);
            visitados.add(inicial);
        }

        while (true) {

            Estado atual;

            if (modo == ModoBusca.BH) {
                if (fila.isEmpty()) break;
                atual = fila.poll();
            } else {
                if (filaPrioridade.isEmpty()) break;
                atual = filaPrioridade.poll();
            }

            expandidos++;

            log.add(new LogNo(atual.pai, atual, "EXPANDIDO"));

            if (atual.ehObjetivo()) {
                long tempoFim = System.currentTimeMillis();
                return new ResultadoBusca(atual, log, (tempoFim - tempoInicio), expandidos);
            }

            for (Estado vizinho : atual.gerarVizinhos()) {

                if (!visitados.contains(vizinho)) {

                    visitados.add(vizinho);

                    log.add(new LogNo(atual, vizinho, "GERADO"));

                    if (modo == ModoBusca.BH) {
                        fila.add(vizinho);
                    } else {
                        filaPrioridade.add(vizinho);
                    }
                }
            }
        }

        long tempoFim = System.currentTimeMillis();
        return new ResultadoBusca(null, log, (tempoFim - tempoInicio), expandidos);
    }

    private static int custo(Estado e, ModoBusca modo) {

        switch (modo) {

            case BH:
                return e.getProfundidade();

            case PECAS_FORA:
                return e.pecasFora();

            case COMBINADO:
                return e.getProfundidade() + e.distanciaManhattan();

            default:
                return 0;
        }
    }
}