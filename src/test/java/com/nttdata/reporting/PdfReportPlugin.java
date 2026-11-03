package com.nttdata.reporting;

import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.Status;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PdfReportPlugin implements EventListener {
    private final List<String> results = new ArrayList<>();

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, event -> writeReport());
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        Status status = event.getResult().getStatus();
        Throwable error = event.getResult().getError();
        String details = error == null ? "" : " | " + error.getClass().getSimpleName()
                + ": " + error.getMessage();
        if (details.length() > 180) {
            details = details.substring(0, 177) + "...";
        }
        results.add(status + " | " + event.getTestCase().getName() + details);
    }

    private void writeReport() {
        Path report = Paths.get("target", "cucumber", "cucumber-report.pdf");
        try {
            Files.createDirectories(report.getParent());
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage();
                document.addPage(page);
                try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA_BOLD, 16);
                    content.newLineAtOffset(50, 750);
                    content.showText("Store regression test report");
                    content.setFont(PDType1Font.HELVETICA, 10);
                    content.newLineAtOffset(0, -22);
                    content.showText("Generated: " + LocalDateTime.now()
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    content.newLineAtOffset(0, -18);
                    content.showText("Scenarios: " + results.size());
                    content.newLineAtOffset(0, -28);
                    for (int index = 0; index < results.size(); index++) {
                        if (index == 40) {
                            content.showText("Additional scenarios omitted; see the JSON report.");
                            break;
                        }
                        content.showText(safeText(results.get(index)));
                        content.newLineAtOffset(0, -16);
                    }
                    content.endText();
                }
                document.save(report.toFile());
            }
        } catch (IOException exception) {
            throw new IllegalStateException("No se pudo generar el reporte PDF en " + report, exception);
        }
    }

    private String safeText(String text) {
        return text.replaceAll("[^\\x20-\\x7E]", "?");
    }
}
