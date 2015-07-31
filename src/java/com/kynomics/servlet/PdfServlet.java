/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kynomics.servlet;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kynomics.control.AuftragController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author  dboehm
 */
//@WebServlet(name = "RechnungPdf", urlPatterns = "/pdfdokumente/PizzaRechnung.pdf")
public class PdfServlet extends HttpServlet {
// Name der Instanz der ManagedBean muss im Namen mit der Class-Library die injiziert wird übereinstimmen

    @Inject
    private AuftragController auftragController;

    /**
     *
     * @param req
     * @param resp
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            resp.setContentType("application/pdf");
            //benötigter Zugriff auf die im Sessionscope
            //abgelegte Bean
//            HttpSession sess = req.getSession();
//            BestellController bestellung = (BestellController) sess.getAttribute("bestellController");
            Document document = new Document();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, bos);
            document.open();
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//            Date orderDate = bestellController.getWrappedBestellung().getBestellung().getOrderDate();
//            document.add(new Paragraph("Datum: " + df.format(orderDate)));
//            document.add(new Paragraph("Bestellbestätigung"));
//            document.add(new Paragraph("\n"));
//            document.add(new Paragraph(bestellController.getCurrentKunde().getVorname() + " "
//                    + bestellController.getCurrentKunde().getNachname()));
//            document.add(new Paragraph(bestellController.getCurrentKunde().getStrasse() + " "
//                    + bestellController.getCurrentKunde().getHausnr()));
//            document.add(new Paragraph(bestellController.getCurrentKunde().getPlz() + " "
//                    + bestellController.getCurrentKunde().getOrt()));
            document.add(new Paragraph("\n"));
            document.add(new Paragraph("\n"));
            // style the table with different column sizes
            float[] relativeWidth = {0.05f, 0.25f, 0.2f, 0.1f, 0.15f, 0.15f,0.1f};
            PdfPTable table = new PdfPTable(relativeWidth);
            Integer amount = 0;
            Double price = 0.0;
            Double totalPrice = 0.0;
            // the header of the table
            table.addCell("Nr");
            table.addCell("Bezeichnung");
            table.addCell("Name");
            table.addCell("Tattoo");
             table.addCell("Chip");
             table.addCell("Zucht");
            table.addCell("Preis");
            // write the order list through the for loop
            for (int i = 0; i < auftragController.getAuftragpositionenList().size(); i++) {
                document.add(new Paragraph());
                table.addCell((i + 1) + ". ");
//                table.addCell(auftragController.getAuftragpositionenList().get(i).getAuftragpositionNr().toString());
                table.addCell(auftragController.getAuftragpositionenList().get(i).getUntersuchungId().getUntersuchungName());
                table.addCell(auftragController.getAuftragpositionenList().get(i).getPatientId().getPatientName());
                table.addCell(auftragController.getAuftragpositionenList().get(i).getPatientId().getPatientTatoonr());
                table.addCell(auftragController.getAuftragpositionenList().get(i).getPatientId().getPatientChip());
                table.addCell(auftragController.getAuftragpositionenList().get(i).getPatientId().getPatientZuchtbuchnr());
                table.addCell(auftragController.getAuftragpositionenList().get(i).getUntersuchungId().getUntersuchungPreis().toString());
                
              
                
//                price = bestellController.getBestellGerichte().get(i).getPreis();
//                amount = bestellController.getBestellGerichte().get(i).getAmount();
//                totalPrice = price * amount;
//                table.addCell(String.format("%8.2f €", price));
//                table.addCell(String.format("%8.2f €", totalPrice));
            }
            table.addCell("");
            table.addCell("");
            table.addCell("");
            table.addCell("Summe: ");
//            table.addCell(String.format("%8.2f €", bestellController.getCurrent().getTotalPay()));
            document.add(table);
            document.add(new Paragraph("\n\n\n"));
            document.add(new Paragraph("Vielen Dank für Ihren Auftrag.\n\n"));
            document.add(new Paragraph("Auftragabwicklung bis zum " + auftragController.getCurrentAuftrag().getAuftragEnde() +" zugesagt."));
            document.close();
            OutputStream os = resp.getOutputStream();
            bos.writeTo(os);
            os.flush();
            os.close();
            for (PrintService s : PrintServiceLookup.lookupPrintServices(null, null)) {
                System.out.println(s.getName());
            }

        } catch (DocumentException ex) {
            Logger.getLogger(PdfServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PdfServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }
}
