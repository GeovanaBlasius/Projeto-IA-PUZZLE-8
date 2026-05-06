import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;

public class GeraPDF {

    public static void gerar(ResultadoBusca resultado) {

        try {
            Document doc = new Document();
            PdfWriter.getInstance(doc, new FileOutputStream("resultado.pdf"));

            doc.open();

            doc.add(new Paragraph("Resultado da Busca\n\n"));
            doc.add(new Paragraph("Tempo: " + resultado.tempo + " ms"));
            doc.add(new Paragraph("Estados expandidos: " + resultado.nosExpandidos));
            doc.add(new Paragraph("\n"));

            for (LogNo no : resultado.log) {

                doc.add(new Paragraph("Status: " + no.status));
                doc.add(new Paragraph(formatarEstado(no.estado)));
                doc.add(new Paragraph("-------------------------"));
            }

            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatarEstado(Estado e) {

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(e.tabuleiro[i][j]).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}