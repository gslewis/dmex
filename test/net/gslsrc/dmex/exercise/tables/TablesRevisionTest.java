package net.gslsrc.dmex.exercise.tables;

import net.gslsrc.dmex.exercise.AbstractExerciseTest;
import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.tables.TablesRevision.Difficulty;
import net.gslsrc.dmex.exercise.tables.render.TablesRevisionProblemRenderer;
import net.gslsrc.dmex.exercise.tables.render.TablesRevisionProblemRendererFactory;
import net.gslsrc.dmex.settings.Setting;
import net.gslsrc.dmex.settings.Settings;
import net.gslsrc.dmex.settings.EnumSelection;
import net.gslsrc.dmex.settings.MultiNumberSelection;
import net.gslsrc.dmex.settings.SingleNumberSelection;
import net.gslsrc.dmex.render.ProblemRenderer;
import net.gslsrc.dmex.render.ProblemRendererFactory;
import net.gslsrc.dmex.render.DOMRenderContext;
import net.gslsrc.dmex.render.FilePDFRenderContext;
import net.gslsrc.dmex.render.PDFRenderContext;
import net.gslsrc.dmex.render.PDFOutputType;
import net.gslsrc.dmex.render.XMLRenderContext;
import net.gslsrc.dmex.render.xsl.ProblemTemplates;
import net.gslsrc.dmex.render.xsl.ProblemTemplates.OutputType;
import net.gslsrc.dmex.render.xsl.HTMLOutputType;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
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
public class TablesRevisionTest extends AbstractExerciseTest {

    private static File testdir;

    @BeforeClass public static void setup() {
        File dir = initTempDir();

        testdir = new File(dir, "TablesRevisionText");
        if (!testdir.exists()) {
            testdir.mkdir();
        }
    }

    @Override
    protected File getTestDir() {
        return testdir;
    }

    /**
     * Test that all tablesrev classes that can be stored in a web session are
     * serializable.
     */
    //@Ignore
    @Test public void testTablesRevisionSerialize() {
        TablesRevision ex = new TablesRevision();
        checkSerial(ex);

        Settings settings = makeSettings(ex,
                new int[] { 2, 3, 4 },
                new int[] { 5 },
                Difficulty.HARD);
        checkSerial(settings);

        ExerciseSession session = ex.newSession(settings);
        checkSerial(session);

        Collection<Problem> problems = makeProblems(session, 2);
        checkSerial(problems);
    }

    //@Ignore
    @Test public void testTablesRevisionHTML() {
        TablesRevision ex = new TablesRevision();

        Settings settings = makeSettings(ex, 
                new int[] { 3, 4, 5 },
                new int[] { 2, 3 },
                Difficulty.HARDEST);

        ExerciseSession session = ex.newSession(settings);

        TablesRevisionProblem problem =
                (TablesRevisionProblem)session.nextProblem();
        System.out.println("problem=" + problem);
        Collection<TablesRevisionProblem> list = Collections.singleton(problem);

        XMLRenderContext context = new XMLRenderContext();
        context.setRootTag("TablesRevision");

        ProblemRenderer<TablesRevisionProblem> renderer =
                ProblemRendererFactory.getFactory(TablesRevisionProblem.class)
                    .getRenderer(XMLRenderContext.class);
        assertNotNull("Renderer is null for XMLRenderContext", renderer);

        try {
            renderer.render(list, context);
        } catch (Exception e) {
            fail("Failed to render to SAX context with " + e);
        }

        for (OutputType outputType : HTMLOutputType.values()) {
            Templates templates = null;
            try {
                templates = ProblemTemplates
                        .getTemplates(TablesRevisionProblem.class)
                        .getTemplates(outputType);
            } catch (Exception e) {
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

    //@Ignore
    @Test public void testTablesRevisionPDF() {
        TablesRevision ex = new TablesRevision();

        Settings settings = makeSettings(ex,
                new int[] { 3, 4, 5 },
                new int[] { 2, 3 },
                Difficulty.HARDEST);

        ExerciseSession session = ex.newSession(settings);

        Collection<TablesRevisionProblem> list =
                new LinkedList<TablesRevisionProblem>();
        for (int i = 0; i < 23; ++i) {
            list.add((TablesRevisionProblem)session.nextProblem());
        }

        File file = new File(testdir, "pdftest.pdf");
        if (file.exists() && !file.delete()) {
            fail("Failed to delete output file " + file);
        }
        //OutputType outputType = OutputType.PROBLEMS_WITH_ANSWERS;
        PDFOutputType outputType = PDFOutputType.PROBLEMS;

        PDFRenderContext context = new FilePDFRenderContext(outputType, file);
        context.setTitle(ex.getMessage("exercise.title." + ex.getId()));

        TablesRevisionProblemRenderer renderer =
                TablesRevisionProblemRendererFactory.getInstance()
                    .getRenderer(context.getClass());

        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render with " + e);
        }
    }

    //@Ignore
    @Test public void testTablesRevision() {
        TablesRevision ex = new TablesRevision();

        Settings settings = makeSettings(ex,
                new int[] { 3, 4, 5 },
                new int[] { 2, 3 },
                Difficulty.HARDEST);

        ExerciseSession session = ex.newSession(settings);

        Collection<TablesRevisionProblem> list =
                new LinkedList<TablesRevisionProblem>();
        for (int i = 0; i < 10; ++i) {
            list.add((TablesRevisionProblem)session.nextProblem());
        }

        String title = ex.getMessage("exercise.title." + ex.getId());

        renderProblems(list, new DOMRenderContext().setTitle(title),
                "problems-test-dom.xml");
        renderProblems(list, new XMLRenderContext().setTitle(title),
                "problems-test-sax.xml");
    }

    private void renderProblems(Collection<TablesRevisionProblem> list,
            XMLRenderContext context, String outputFile) {
        TablesRevisionProblemRenderer renderer =
                TablesRevisionProblemRendererFactory.getInstance()
                    .getRenderer(context.getClass());
        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render with " + e);
        }

        writeXML(context.getSource(), outputFile);
    }

    @Override
    protected String css() {
        StringBuilder sb = new StringBuilder();
        sb.append("<style type='text/css'><!--\n");
        sb.append("table { font-size: 20pt; }");
        sb.append("td { width: 40px; height: 40px; text-align: center; }");
        sb.append("td.rule { width: 40px; height: 2px; text-align: center; }");
        sb.append("td input { width: 60px; height: 40px; text-align: center; font-size: 20pt; }");
        sb.append("--></style>\n");

        return sb.toString();
    }

    private Settings makeSettings(TablesRevision ex, int[] t1a, int[] t2a,
            Difficulty tdiff) {
        assertNotNull("Exercise is null", ex);
        assertNotNull("Term 1 array is null", t1a);
        assertFalse("Term 1 array is empty", t1a.length == 0);
        assertNotNull("Term 2 array is null", t2a);
        assertFalse("Term 2 array is empty", t2a.length == 0);
        assertNotNull("Difficulty is null", tdiff);

        Settings settings = ex.newSettings();
        assertNotNull("Settings are null", settings);

        MultiNumberSelection selection;

        selection = settings.getSetting(MultiNumberSelection.class,
                                        "tablesrev.term1");
        assertNotNull("term1 setting is null", selection);
        assertNotNull("term1 selection is null", selection.getSelection());
        assertEquals("term1 selection not empty", 0,
                selection.getSelection().length);

        selection.setSelection(t1a);
        assertArrayEquals("Mismatch on term1 selection", t1a,
                selection.getSelection());

        selection = settings.getSetting(MultiNumberSelection.class,
                                        "tablesrev.term2");
        assertNotNull("term2 setting is null", selection);
        assertNotNull("term2 selection is null", selection.getSelection());
        assertEquals("term2 selection not empty", 0,
                selection.getSelection().length);

        selection.setSelection(t2a);
        assertArrayEquals("Mismatch on term2 selection", t2a,
                selection.getSelection());

        EnumSelection<Difficulty> diff =
                (EnumSelection<Difficulty>)settings.getSetting(
                                                    "tablesrev.difficulty");
        assertNotNull("difficulty setting is null", diff);
        assertNotNull("difficulty selection is null", diff.getSelection());
        assertEquals("Mismatch on default difficulty",
                Difficulty.EASIEST, diff.getSelection());

        diff.setSelection(tdiff);
        assertEquals("Mismatch on difficulty", tdiff, diff.getSelection());

        return settings;
    }
}
