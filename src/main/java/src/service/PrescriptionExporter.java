package src.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.log4j.Log4j2;
import src.model.Appointment;
import src.model.Prescription;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Log4j2
public class PrescriptionExporter {
    // Represents the path to the directory where the prescriptions are stored.
    private static final String outputDirPath = "prescriptions/";
    // Represent some predefined fonts used to style the text of the pdf
    private static final Font FOOTER_FONT = FontFactory.getFont(FontFactory.TIMES, 20, Font.NORMAL, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.TIMES, 12, Font.ITALIC, BaseColor.BLACK);
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.TIMES, 18, Font.BOLD, BaseColor.BLACK);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD, BaseColor.BLACK);

    public void exportPrescriptionAsPDF(Prescription prescription) {
        try {
            Document document = new Document();
            String documentName = "prescription_#" + prescription.getId() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(outputDirPath + documentName));
            document.open();
            Chunk title = new Chunk("Prescription");
            title.setFont(TITLE_FONT);
            Paragraph titleParagraph = new Paragraph();
            titleParagraph.add(title);
            document.add(titleParagraph);

            Paragraph appointmentParagraph = new Paragraph();
            Phrase appointmentSubTitle = new Phrase("Appointment details:");
            appointmentSubTitle.setFont(HEADER_FONT);
            appointmentParagraph.add(appointmentSubTitle);
            appointmentParagraph.add(getDetailsOfAppointment(prescription.getAppointment()));
            document.add(appointmentParagraph);

            Paragraph medicationsParagraph = new Paragraph();
            Phrase medicationsSubTitle = new Phrase("Medications:");
            medicationsSubTitle.setFont(HEADER_FONT);
            medicationsParagraph.add(medicationsSubTitle);
            medicationsParagraph.add(createParagraphWithData(prescription.getMedication()));
            document.add(medicationsParagraph);

            Paragraph indicationsParagraph = new Paragraph();
            Phrase indicationsSubTitle = new Phrase("Indications:");
            indicationsSubTitle.setFont(HEADER_FONT);
            indicationsParagraph.add(indicationsSubTitle);
            indicationsParagraph.add(createParagraphWithData(prescription.getIndications()));
            document.add(indicationsParagraph);

            Paragraph footer = new Paragraph();
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setFont(FOOTER_FONT);
            footer.add(new Phrase("Take care! Health comes first!"));
            document.add(footer);

            document.close();
        } catch (FileNotFoundException | DocumentException e) {
            log.error("MenuExporter: exportMenuAsPDF {}", e.getMessage());
        }
    }

    private Paragraph createParagraphWithData(String name) {
        Paragraph paragraph = new Paragraph();
        paragraph.setSpacingBefore(10);
        paragraph.setSpacingAfter(10);
        Chunk title = new Chunk(name + "\n");
        title.setFont(SECTION_FONT);
        paragraph.add(title);
        return paragraph;
    }

    private PdfPTable getDetailsOfAppointment(Appointment appointment) {
        PdfPTable table = new PdfPTable(new float[]{25, 75});
        PdfPCell cell;

        cell = new PdfPCell(new Phrase("Appointment date"));
        cell.setExtraParagraphSpace(20.0f);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell(new PdfPCell(new Phrase(appointment.getAppointmentDate().toString())));

        cell = new PdfPCell(new Phrase("Patient name"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell(new PdfPCell(new Phrase(appointment.getPatient().getFirstName() + " " + appointment.getPatient().getLastName())));

        cell = new PdfPCell(new Phrase("Doctor name"));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(cell);
        table.addCell(new PdfPCell(new Phrase(appointment.getDoctor().getFirstName() + " " + appointment.getDoctor().getLastName())));

        return table;
    }
}
