package com.quantech.misc;

import com.quantech.model.Job;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class PdfGenerator {

    public void patientAsPdf(List<Job> jobs) throws Exception {

        // set up thymeleaf rendering engine
        //
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setOrder(1);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // prepare file and renderer
        File out = new File("pdfout.pdf" );
        ITextRenderer renderer = new ITextRenderer();

        OutputStream outputStream = new FileOutputStream(out);
        // the context data is used to fill the thymeleaf template
        Context context = new Context();
        context.setVariable("jobs", jobs);

        // make html file
        String html = templateEngine.process("templates/misc/printjobs", context);

        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(outputStream);
        outputStream.close();

        // delete patient data when the app closes
        out.deleteOnExit();
    }
}
