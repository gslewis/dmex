package net.gslsrc.dmex.exercise;

import java.io.Closeable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.junit.Ignore;
import static org.junit.Assert.*;

@Ignore
public abstract class AbstractExerciseTest {

    protected static File tempdir;

    protected void writeXML(Source source, String filename) {
        OutputStream out = null;
        try {
            Transformer trans =
                    TransformerFactory.newInstance().newTransformer();
            trans.setOutputProperty(OutputKeys.METHOD, "xml");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            File file = new File(getTestDir(), filename);
            if (file.exists() && !file.delete()) {
                fail("Unable to delete the test file " + file);
            }
            out = new FileOutputStream(file);
            trans.transform(source, new StreamResult(out));
        } catch (Exception e) {
            fail("Failed to do SAX transform with " + e);
        } finally {
            closeQuietly(out);
        }
    }

    protected void writeHTML(String content, String filename) {
        File file = new File(getTestDir(), filename);
        if (file.exists() && !file.delete()) {
            fail("Unable to delete test file " + file);
        }

        String html = "<html>\n<head>\n" + css() + "\n</head>\n<body>\n"
                + content + "\n</body></html>";

        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileWriter(file));
            out.println(html);
        } catch (IOException ioe) {
            fail("Failed to write HTML with " + ioe);
        } finally {
            closeQuietly(out);
        }
    }

    protected String css() {
        return "";
    }

    protected abstract File getTestDir();

    /**
     * Check that an object can be serialized and deserialized.
     */
    protected Object checkSerial(Object obj) {
        assertNotNull("Serialization target is null", obj);

        assertTrue("Serialization target not instanceof Serializable",
                obj instanceof Serializable);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(baos);
            out.writeObject(obj);
            out.close();
        } catch (Exception e) {
            fail("Failed to write object to stream with " + e);
        }

        System.out.println("wrote " + baos.size() + " bytes for " + obj);

        Object reobj = null;
        try {
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(baos.toByteArray()));

            reobj = in.readObject();
        } catch (Exception e) {
            fail("Failed to read object from stream with " + e);
        }

        assertNotNull("Retrieved obj is null", reobj);
        assertNotSame("Retrieved obj is same", obj, reobj);

        return reobj;
    }

    protected Collection<Problem> makeProblems(ExerciseSession session,
            int count) {
        assertNotNull("Session is null", session);
        assertTrue("Count is not positive", count > 0);

        if (count == 1) {
            return Collections.singleton(session.nextProblem());
        }

        Collection<Problem> list = new LinkedList<Problem>();
        for (int i = 0; i < count; ++i) {
            list.add(session.nextProblem());
        }

        return list;
    }

    protected static void closeQuietly(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException ioe) {}
        }
    }

    protected static File initTempDir() {
        String s = System.getProperty("test.temp");

        if (s != null && !s.isEmpty()) {
            tempdir = new File(s);
        } else {
            tempdir = new File("temp/test");
        }

        if (!tempdir.exists() && !tempdir.mkdirs()) {
            fail("Failed to create temp dir " + tempdir);
        }

        return tempdir;
    }
}
