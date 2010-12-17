package net.gslsrc.dmex.exercise.longdiv;

import net.gslsrc.dmex.exercise.AbstractExerciseTest;
import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.longdiv.LongDivision.Difficulty;
import net.gslsrc.dmex.exercise.longdiv.render.LongDivisionProblemRenderer;
import net.gslsrc.dmex.exercise.longdiv.render.LongDivisionProblemRendererFactory;
import net.gslsrc.dmex.settings.Setting;
import net.gslsrc.dmex.settings.Settings;
import net.gslsrc.dmex.settings.BooleanSelection;
import net.gslsrc.dmex.settings.EnumSelection;
import net.gslsrc.dmex.render.DOMRenderContext;
import net.gslsrc.dmex.render.FilePDFRenderContext;
import net.gslsrc.dmex.render.PDFRenderContext;
import net.gslsrc.dmex.render.PDFOutputType;
import net.gslsrc.dmex.render.ProblemRenderer;
import net.gslsrc.dmex.render.ProblemRendererFactory;
import net.gslsrc.dmex.render.XMLRenderContext;
import net.gslsrc.dmex.render.xsl.ProblemTemplates;
import net.gslsrc.dmex.render.xsl.ProblemTemplates.OutputType;
import net.gslsrc.dmex.render.xsl.HTMLOutputType;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@SuppressWarnings("unchecked")
public class LongDivisionTest extends AbstractExerciseTest {

    private static File testdir;

    private static DocumentBuilder parser;

    @BeforeClass public static void setup() {
        File dir = initTempDir();

        testdir = new File(dir, "LongDivisionTest");
        if (!testdir.exists()) {
            testdir.mkdir();
        }
    }

    @Override
    protected File getTestDir() {
        return testdir;
    }

    @Test public void testLongDivisionSerialize() {
        LongDivision ex = new LongDivision();
        checkSerial(ex);

        Settings settings = makeSettings(ex, Difficulty.HARDEST, true);
        checkSerial(settings);

        ExerciseSession session = ex.newSession(settings);
        checkSerial(session);

        Collection<Problem> problems = makeProblems(session, 3);
        checkSerial(problems);
    }

    @Test public void testLongDivisionHTML() {
        LongDivision ex = new LongDivision();

        Settings settings = makeSettings(ex, Difficulty.EASY, true);

        ExerciseSession session = ex.newSession(settings);
        assertNotNull("Session is null", session);
        assertEquals("Mismatch on session settings", settings,
                session.getSettings());

        LongDivisionProblem problem =
                (LongDivisionProblem)session.nextProblem();
        System.out.println("problem=" + problem);
        Collection<LongDivisionProblem> list = Collections.singleton(problem);

        String title = ex.getMessage("exercise.title." + ex.getId());
        assertNotNull("Exercise title is null", title);

        DOMRenderContext context = new DOMRenderContext();
        context.setRootTag("LongDivision");
        context.setTitle(title);

        ProblemRenderer<LongDivisionProblem> renderer =
                ProblemRendererFactory.getFactory(LongDivisionProblem.class)
                .getRenderer(context.getClass());
        assertNotNull("Renderer is null for DOMRenderContext", renderer);

        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render to DOM context with " + e);
        }

        writeXML(context.getSource(), "htmltest-problem-dom.xml");

        for (OutputType outputType : HTMLOutputType.values()) {
            Templates templates = null;
            try {
                templates = ProblemTemplates
                        .getTemplates(LongDivisionProblem.class)
                        .getTemplates(outputType);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Failed to get templates for " + outputType
                        + " with " + e);
            }
            assertNotNull("Templates is null", templates);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                templates.newTransformer().transform(context.getSource(),
                                                     new StreamResult(baos));
            } catch (Exception e) {
                fail("Failed to transform with " + e);
            }

            writeHTML(baos.toString(),
                    "htmltest-problem-" + outputType.name().toLowerCase()
                    + ".html");
        }
    }

    @Override
    protected String css() {
        StringBuilder sb = new StringBuilder();

        sb.append("<style type='text/css'><!--\n");
        sb.append("table { font-size: 20pt; }\n");
        sb.append("td.digit { width: 40px; height: 40px; text-align:center; }\n");
        sb.append("td input { width: 40px; height: 40px; text-align:center; font-size: 20pt; }\n");
        sb.append("td.remainder { text-align: right; }\n");
        sb.append("span.remainder { font-size: 10pt; font-style: italic; }\n");
        sb.append("--></style>\n");

        return sb.toString();
    }

    // Test that the DOM and SAX renderers both produce the correct XML.
    //@Ignore
    @Test public void testLongDivisionXML() {
        LongDivision ex = new LongDivision();

        Settings settings = makeSettings(ex, Difficulty.HARD, true);

        ExerciseSession session = ex.newSession(settings);
        assertNotNull("Session is null", session);
        assertEquals("Mismatch on session settings", settings,
                session.getSettings());

        Collection<LongDivisionProblem> list =
                new LinkedList<LongDivisionProblem>();
        for (int i = 0; i < 10; ++i) {
            list.add((LongDivisionProblem)session.nextProblem());
        }

        String title = ex.getMessage("exercise.title." + ex.getId());
        assertNotNull("Exercise title is null", title);

        renderProblems(list,
                new DOMRenderContext().setTitle(title),
                "problems-xmltest-dom.xml");

        renderProblems(list,
                new XMLRenderContext().setTitle(title),
                "problems-xmltest-sax.xml");
    }

    //@Ignore
    @Test public void testLongDivisionPDF() {
        LongDivision ex = new LongDivision();

        Settings settings = makeSettings(ex, Difficulty.HARD, true);

        ExerciseSession session = ex.newSession(settings);

        Collection<LongDivisionProblem> list =
                new LinkedList<LongDivisionProblem>();
        for (int i = 0; i < 10; ++i) {
            list.add((LongDivisionProblem)session.nextProblem());
        }

        String title = ex.getMessage("exercise.title." + ex.getId());
        renderProblems(list,
                new DOMRenderContext().setTitle(title),
                "problems-test-dom.xml");

        File file = new File(testdir, "pdftest.pdf");
        if (file.exists() && !file.delete()) {
            fail("Failed to delete output file " + file);
        }
        PDFOutputType outputType = PDFOutputType.PROBLEMS_WITH_ANSWERS;
        //OutputType outputType = PDFOutputType.PROBLEMS;
        //OutputType outputType = PDFOutputType.ANSWERS;
        PDFRenderContext context = new FilePDFRenderContext(outputType, file);
        context.setTitle(title);

        LongDivisionProblemRenderer renderer =
                LongDivisionProblemRendererFactory.getInstance()
                    .getRenderer(context.getClass());

        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render with " + e);
        }
    }

    // test a particular problem
    //@Ignore
    @Test public void testLongDivisionProblem() {
        int dividend = 9605;
        int divisor = 79;

        Collection<LongDivisionProblem> list = Collections.singletonList(
                new LongDivisionProblem(dividend, divisor, true));

        String title = "Render Test";
        renderProblems(list,
                new DOMRenderContext().setTitle(title),
                "render-test.xml");

        File file = new File(testdir, "render-test.pdf");
        if (file.exists() && !file.delete()) {
            fail("Failed to delete output file " + file);
        }

        PDFOutputType outputType = PDFOutputType.ANSWERS;
        //OutputType outputType = PDFOutputType.PROBLEMS;
        PDFRenderContext context = new FilePDFRenderContext(outputType, file);
        context.setTitle(title);

        LongDivisionProblemRenderer renderer =
                LongDivisionProblemRendererFactory.getInstance()
                    .getRenderer(context.getClass());

        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render with " + e);
        }
    }

    private void renderProblems(Collection<LongDivisionProblem> list,
            XMLRenderContext context, String outputFile) {
        LongDivisionProblemRenderer renderer =
                LongDivisionProblemRendererFactory.getInstance()
                    .getRenderer(context.getClass());
        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render with " + e);
        }

        writeXML(context.getSource(), outputFile);
    }

    private Settings makeSettings(LongDivision ex,
            Difficulty tdiff, boolean showHints) {
        assertNotNull("Exercise is null", ex);
        assertNotNull("Difficulty is null", tdiff);

        Settings settings = ex.newSettings();
        assertNotNull("Settings are null", settings);

        EnumSelection<Difficulty> setting =
                (EnumSelection<Difficulty>)settings.getSetting(
                        "longdiv.difficulty");
        assertNotNull("Difficulty setting is null", setting);
        assertEquals("Mismatch on default difficulty setting",
                Difficulty.EASIEST, setting.getSelection());

        setting.setSelection(tdiff);
        assertEquals("Mismatch on difficulty setting",
                tdiff, setting.getSelection());

        BooleanSelection hints =
                (BooleanSelection)settings.getSetting("longdiv.hints");
        assertNotNull("Hints setting is null", hints);
        hints.setSelection(Boolean.valueOf(showHints));

        return settings;
    }
}
