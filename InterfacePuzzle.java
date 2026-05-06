import javax.swing.*;
import java.awt.*;

public class InterfacePuzzle extends JFrame {

    private JButton[][] botoes = new JButton[3][3];
    private Estado estadoAtual;

    private ResultadoBusca ultimoResultado; // guardar resultado

    private final int[][] estadoInicial = {
            {2,7,5},
            {6,1,8},
            {4,0,3}
    };

    private JLabel info;

    public InterfacePuzzle() {

        setTitle("Puzzle 8 - IA");
        setSize(350, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel grade = new JPanel(new GridLayout(3,3));

        estadoAtual = new Estado(estadoInicial);

        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                botoes[i][j] = new JButton();
                botoes[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                grade.add(botoes[i][j]);
            }
        }

        atualizarTela();
        add(grade, BorderLayout.CENTER);

        info = new JLabel("Escolha uma busca", SwingConstants.CENTER);
        add(info, BorderLayout.NORTH);

        JPanel controles = new JPanel(new GridLayout(2,2,5,5));

        controles.add(criarBotao("BH", ModoBusca.BH));
        controles.add(criarBotao("f(x)=g(x)", ModoBusca.PECAS_FORA));
        controles.add(criarBotao("f(x)=g(x)+h(x)", ModoBusca.COMBINADO));
        // BOTÃO PDF
        JButton btnPDF = new JButton("Gerar PDF");
        btnPDF.setBackground(Color.RED);
        btnPDF.setForeground(Color.WHITE);
        btnPDF.setFocusPainted(false);
        btnPDF.setFont(new Font("Arial", Font.BOLD, 12));

        btnPDF.addActionListener(e -> gerarPDF());

        controles.add(btnPDF);

        add(controles, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton criarBotao(String nome, ModoBusca modo) {
        JButton botao = new JButton(nome);
        botao.addActionListener(e -> executarBusca(modo));
        return botao;
    }

    private void executarBusca(ModoBusca modo) {

        estadoAtual = new Estado(estadoInicial);
        atualizarTela();

        new Thread(() -> {

            ultimoResultado = Solucionador.resolver(estadoAtual, modo); //salva

            ResultadoBusca resultado = ultimoResultado;

            SwingUtilities.invokeLater(() -> {
                info.setText(
                        "<html>Tempo: " + resultado.tempo + " ms<br>" +
                                "Estados expandidos: " + resultado.nosExpandidos + "<br>" +
                                "</html>"
                );
            });

            for (LogNo no : resultado.log) {
                if (no.status.equals("EXPANDIDO")) {

                    estadoAtual = no.estado;

                    SwingUtilities.invokeLater(this::atualizarTela);

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException ignored) {}
                }
            }

        }).start();
    }

    private void gerarPDF() {
        if (ultimoResultado != null) {
            GeraPDF.gerar(ultimoResultado);
            JOptionPane.showMessageDialog(this, "PDF gerado com sucesso!");
        } else {
            JOptionPane.showMessageDialog(this, "Execute uma busca primeiro!");
        }
    }

    private void atualizarTela() {

        int[][] t = estadoAtual.tabuleiro;

        for (int i=0;i<3;i++) {
            for (int j=0;j<3;j++) {
                int val = t[i][j];
                botoes[i][j].setText(val == 0 ? "" : String.valueOf(val));
            }
        }
    }
}