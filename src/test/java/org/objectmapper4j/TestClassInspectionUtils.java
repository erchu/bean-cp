package org.objectmapper4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.TraceClassVisitor;

public class TestClassInspectionUtils {

    private TestClassInspectionUtils() {
    }

    private static String getClassResourceName(final Class clazz) {
        return clazz.getName().replace('.', '/') + ".class";
    }

    private static byte[] getClassContent(final Class clazz)
            throws IOException {
        InputStream stream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(getClassResourceName(clazz));

        return IOUtils.toByteArray(stream);
    }

    public static void printTestClassStructureOnStandardOutput(final Class testClass) throws IOException {
        byte[] testClassContent = getClassContent(testClass);

        ClassReader classReader = new ClassReader(testClassContent);

        try (PrintWriter writer = new PrintWriter(System.out)) {
            classReader.accept(new TraceClassVisitor(writer), 0);
        }
    }
}
