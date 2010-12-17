package net.gslsrc.dmex.exercise.longmult;

import net.gslsrc.dmex.exercise.AbstractExerciseTest;
import net.gslsrc.dmex.exercise.ExerciseSession;
import net.gslsrc.dmex.exercise.Problem;
import net.gslsrc.dmex.exercise.longmult.LongMultiplication.Difficulty;
import net.gslsrc.dmex.exercise.longmult.render.LongMultiplicationProblemRenderer;
import net.gslsrc.dmex.exercise.longmult.render.LongMultiplicationProblemRendererFactory;
import net.gslsrc.dmex.settings.Setting;
import net.gslsrc.dmex.settings.Settings;
import net.gslsrc.dmex.settings.EnumSelection;
import net.gslsrc.dmex.settings.MultiNumberSelection;
import net.gslsrc.dmex.settings.SingleNumberSelection;
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
public class LongMultiplicationTest extends AbstractExerciseTest {

    private static File testdir;

    private static DocumentBuilder parser;

    @BeforeClass public static void setup() {
        File dir = initTempDir();

        testdir = new File(dir, "LongMultiplicationTest");
        if (!testdir.exists()) {
            testdir.mkdir();
        }
    }

    @Override
    protected File getTestDir() {
        return testdir;
    }

    @Test public void testLongMultiplicationSerialize() {
        LongMultiplication ex = new LongMultiplication();
        checkSerial(ex);

        Settings settings = makeSettings(ex, Difficulty.HARDEST);
        checkSerial(settings);

        ExerciseSession session = ex.newSession(settings);
        checkSerial(session);

        Collection<Problem> problems = makeProblems(session, 3);
        checkSerial(problems);
    }

    @Test public void testLongMultiplicationHTML() {
        LongMultiplication ex = new LongMultiplication();

        Settings settings = makeSettings(ex, Difficulty.HARDEST);

        ExerciseSession session = ex.newSession(settings);
        assertNotNull("Session is null", session);
        assertEquals("Mismatch on session settings", settings,
                session.getSettings());

        LongMultiplicationProblem problem =
                (LongMultiplicationProblem)session.nextProblem();
        System.out.println("problem=" + problem);
        Collection<LongMultiplicationProblem> list =
                Collections.singleton(problem);

        DOMRenderContext context = new DOMRenderContext();
        context.setRootTag("LongMultiplication");

        ProblemRenderer<LongMultiplicationProblem> renderer =
                ProblemRendererFactory.getFactory(
                        LongMultiplicationProblem.class)
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
                        .getTemplates(LongMultiplicationProblem.class)
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
        sb.append("td { width: 40px; height: 40px; text-align: center; }\n");
        sb.append("td input { width: 40px; height: 40px; text-align: center; font-size: 20pt; }\n");
        sb.append("td.zero { border: 1px solid black; }\n");
        sb.append("--></style>\n");

        return sb.toString();
    }

    // Test that the DOM and SAX renderers both produce the correct XML.
    //@Ignore
    @Test public void testLongMultiplicationXML() {
        LongMultiplication ex = new LongMultiplication();

        Settings settings = makeSettings(ex, Difficulty.HARD);

        ExerciseSession session = ex.newSession(settings);
        assertNotNull("Session is null", session);
        assertEquals("Mismatch on session settings", settings,
                session.getSettings());

        Collection<LongMultiplicationProblem> list =
                new LinkedList<LongMultiplicationProblem>();
        for (int i = 0; i < 10; ++i) {
            list.add((LongMultiplicationProblem)session.nextProblem());
        }

        String title = ex.getMessage("exercise.title." + ex.getId());
        assertNotNull("Exercise title is null", title);

        renderProblems(list,
                new DOMRenderContext().setTitle(title),
                "problems-xmltest-dom.xml");

        renderProblems(list,
                new XMLRenderContext().setTitle(title),
                "problems-xmltest-sax.xml");

        Document domxml = loadDocument("problems-xmltest-dom.xml");
        validate("xmltest-dom", domxml, title);

        Document saxxml = loadDocument("problems-xmltest-sax.xml");
        validate("xmltest-sax", saxxml, title);

        //compare(domxml, saxxml);
    }

    private void validate(String stub, Document doc, String title) {
        assertNotNull(stub + " document is null", doc);
        assertNotNull(stub + " document element is null",
                doc.getDocumentElement());

        assertEquals(stub + " mismatch on document element tag",
                XMLRenderContext.TAG_WORKSHEET,
                doc.getDocumentElement().getTagName());

        String s = doc.getDocumentElement().getAttribute("title");
        assertNotNull(stub + " title attribute is null", s);
        assertEquals(stub + " mismatch on title", title, s);

        Node node = doc.getDocumentElement().getFirstChild();
        int index = 0;
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                ++index;
                Element child = (Element)node;

                if (XMLRenderContext.TAG_PROBLEM.equals(child.getTagName())) {
                    validateProblem(stub + " problem[" + index + "]", child);
                }
            }

            node = node.getNextSibling();
        }
    }

    private void validateProblem(String stub, Element element) {
        assertNotNull(stub + " element is null", stub);
        assertEquals(stub + " mismatch on problem tag",
                XMLRenderContext.TAG_PROBLEM, element.getTagName());

        int[] terms = validateMultiply(stub, element);
        //validateWorking(stub, element);
        //validateAnswer(stub, element);
    }

    private int[] validateMultiply(String stub, Element parent) {
        Element element = findChild(parent, "multiply");
        assertNotNull(stub + " missing <multiply> child", element);

        int[] terms = new int[2];

        Element child = findChild(element, "multiplicand");
        assertNotNull(stub + " missing <multiplicand> element", child);
        terms[0] = validateNumber(stub + "-multiplicand", child);

        child = findChild(element, "multiplier");
        assertNotNull(stub + " missing <multiplier> element", child);
        terms[1] = validateNumber(stub + "-multiplier", child);

        return terms;
    }

    private int validateNumber(String stub, Element element) {
        assertNotNull(stub + " element is null", element);

        StringBuilder sb = new StringBuilder();

        Node node = element.getFirstChild();
        int index = 0;
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element child = (Element)node;
                ++index;

                assertEquals(stub + " child element not a <digit>", "digit",
                        child.getTagName());

                String text = child.getTextContent();
                assertNotNull(stub + " content of <digit> " + index
                        + " is null", text);

                text = text.trim();
                assertFalse(stub + " content of <digit> " + index
                        + " is empty", text.isEmpty());

                try {
                    sb.append(Integer.parseInt(text));
                } catch (NumberFormatException nfe) {
                    fail(stub + " content of <digit> " + index
                            + " (" + text + ") is not an integer");
                }
            }

            node = node.getNextSibling();
        }

        int result = -1;
        try {
            result = Integer.parseInt(sb.toString());
        } catch (NumberFormatException nfe) {
            fail(stub + " concatentation of digits (" + sb
                    + ") is not an integer");
        }

        return result;
    }

    private Element findChild(Element parent, String tag) {
        assertNotNull("Parent element is null", parent);

        Node node = parent.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.getNodeName().equals(tag)) {
                return (Element)node;
            }

            node = node.getNextSibling();
        }

        return null;
    }

    //@Ignore
    @Test public void testLongMultiplicationPDF() {
        LongMultiplication ex = new LongMultiplication();

        Settings settings = makeSettings(ex, Difficulty.HARDEST);

        ExerciseSession session = ex.newSession(settings);

        Collection<LongMultiplicationProblem> list =
                new LinkedList<LongMultiplicationProblem>();
        for (int i = 0; i < 20; ++i) {
            list.add((LongMultiplicationProblem)session.nextProblem());
        }

        String title = ex.getMessage("exercise.title." + ex.getId());
        renderProblems(list,
                new DOMRenderContext().setTitle(title),
                "problems-test-dom.xml");

        File file = new File(testdir, "pdftest.pdf");
        if (file.exists() && !file.delete()) {
            fail("Failed to delete output file " + file);
        }
        //PDFOutputType outputType = PDFOutputType.PROBLEMS_WITH_ANSWERS;
        PDFOutputType outputType = PDFOutputType.PROBLEMS;
        //OutputType outputType = PDFOutputType.ANSWERS;
        PDFRenderContext context = new FilePDFRenderContext(outputType, file);
        context.setTitle(title);

        LongMultiplicationProblemRenderer renderer =
                LongMultiplicationProblemRendererFactory.getInstance()
                    .getRenderer(context.getClass());

        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render with " + e);
        }
    }

    private void renderProblems(Collection<LongMultiplicationProblem> list,
            XMLRenderContext context, String outputFile) {
        LongMultiplicationProblemRenderer renderer =
                LongMultiplicationProblemRendererFactory.getInstance()
                    .getRenderer(context.getClass());
        try {
            renderer.render(list, context);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to render with " + e);
        }

        writeXML(context.getSource(), outputFile);
    }

    private Document loadDocument(String filename) {
        File file = new File(testdir, filename);
        assertTrue("Missing " + file, file.exists());

        Document doc = null;
        try {
            doc = getParser().parse(new FileInputStream(file));
        } catch (Exception e) {
            fail("Failed to parse " + file + " with " + e);
        }

        return doc;
    }

    private Settings makeSettings(LongMultiplication ex, Difficulty tdiff) {
        assertNotNull("Exercise is null", ex);
        assertNotNull("Difficulty is null", tdiff);

        Settings settings = ex.newSettings();
        assertNotNull("Settings are null", settings);

        EnumSelection<Difficulty> setting =
                (EnumSelection<Difficulty>)settings.getSetting(
                        "longmult.difficulty");
        assertNotNull("Difficulty setting is null", setting);
        assertEquals("Mismatch on default difficulty setting",
                Difficulty.EASIEST, setting.getSelection());

        setting.setSelection(tdiff);
        assertEquals("Mismatch on difficulty setting",
                tdiff, setting.getSelection());

        return settings;
    }

    private static DocumentBuilder getParser() {
        if (parser == null) {
            try {
                parser = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
            } catch (Exception e) {
                fail("Failed to create parser with " + e);
            }
        }

        return parser;
    }
}
