package com.onoprienko.io.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileAnalyserTest {
    @BeforeEach
    @SuppressWarnings("all")
    public void create() throws IOException {
        File path1 = new File("./src/test/resources/path1");
        path1.mkdirs();
        File file1 = new File("./src/test/resources/path1/file1.txt");
        file1.createNewFile();
        File file2 = new File("./src/test/resources/path1/file2.txt");
        file2.createNewFile();

        try (FileWriter fileWriter = new FileWriter(file2)) {
            String text = "Олень - северное животное. " +
                    "В летнее время оленям в тайге жарко, а в горах даже в июле холодно. " +
                    "Олень как бы создан для северных просторов, жёсткого ветра,длинных морозных ночей." +
                    " Олень олень легко бежит вперёд по тайге,подминает под себя кусты,переплывает быстрые реки." +
                    " Нос у оленя покрыт серебристой шёрсткой. Если бы шерсти на носу не было,олень бы его наверное отморозил? " +
                    "Олень не тонет,потому что каждая его шерстинка-это длинная трубочка,которую внутри наполняет воздух. " +
                    "Олени крутые!";
            fileWriter.write(text);
        }
    }

    @Test
    public void testAnalyseVoidFileReturnZeroResult() {
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream("java FileAnalyzer ./src/test/resources/path1/file1.txt олень".getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            FileAnalyser.analyse();
            String outputContent = output.toString();
            assertEquals("""
                            Print file path and word:\r
                            Word repetition: 0\r
                            Sentences with word олень:\r
                            """,
                    outputContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAnalyseWithReturnValueVoidFileReturnZeroResult() {
        FileAnalyser.AnalyseInfo analyseResult = FileAnalyser.analyse("./src/test/resources/path1/file1.txt", "олень");
        assertEquals(0, analyseResult.getCount());
        assertEquals(0 , analyseResult.getSentences().size());
    }

    @Test
    public void testAnalyseWithReturnValueFileWithoutRequestedWordReturnZeroResult() {
        FileAnalyser.AnalyseInfo analyseResult = FileAnalyser.analyse("./src/test/resources/path1/file1.txt", "test");
        assertEquals(0, analyseResult.getCount());
        assertEquals(0 , analyseResult.getSentences().size());
    }

    @Test
    public void testAnalyseFileWithoutNameOfClassInRequestReturnException() {
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream("./src/test/resources/path1/file2.txt test".getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            RuntimeException runtimeException = assertThrows(RuntimeException.class, FileAnalyser::analyse);
            String outputContent = runtimeException.toString();
            assertEquals("java.lang.RuntimeException: Request entered incorrectly. Try typing: \"java FileAnalyser *file path* *word*\"",
                    outputContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testAnalyseFileThatNotExistReturnException() {
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream("java FileAnalyzer ./path/file олень".getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            RuntimeException runtimeException = assertThrows(RuntimeException.class, FileAnalyser::analyse);
            String outputContent = runtimeException.toString();
            assertEquals("java.lang.RuntimeException: Exception while validate request: .\\path\\file (The system cannot find the path specified)",
                    outputContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @Test
    public void testAnalyseFileReturnCorrectWordCounterAndMultipleSentences() {
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream("java FileAnalyzer ./src/test/resources/path1/file2.txt олень".getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            FileAnalyser.analyse();
            String outputContent = output.toString();
            assertEquals("""
                            Print file path and word:\r
                            Word repetition: 6\r
                            Sentences with word олень:\r
                            Олень - северное животное\r
                            Олень как бы создан для северных просторов, жёсткого ветра,длинных морозных ночей\r
                            Олень олень легко бежит вперёд по тайге,подминает под себя кусты,переплывает быстрые реки\r
                            Если бы шерсти на носу не было,олень бы его наверное отморозил\r
                            Олень не тонет,потому что каждая его шерстинка-это длинная трубочка,которую внутри наполняет воздух\r
                            """,
                    outputContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAnalyseFileReturnCorrectWordCounterAndSingleSentence() {
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream("java FileAnalyzer ./src/test/resources/path1/file2.txt северное".getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            FileAnalyser.analyse();
            String outputContent = output.toString();
            assertEquals("""
                            Print file path and word:\r
                            Word repetition: 1\r
                            Sentences with word северное:\r
                            Олень - северное животное\r
                            """,
                    outputContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAnalyseFileWithoutWordInRequestReturnExceptionInConsole() {
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream("java FileAnalyzer ./src/test/resources/path1/file2.txt".getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            RuntimeException runtimeException = assertThrows(RuntimeException.class, FileAnalyser::analyse);
            String outputContent = runtimeException.toString();
            assertEquals("java.lang.RuntimeException: Exception while validate request: begin 0, end -1, length 36",
                    outputContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAnalyseFileWithoutPathInRequestReturnExceptionInConsole() {
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream("java FileAnalyzer test".getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            RuntimeException runtimeException = assertThrows(RuntimeException.class, FileAnalyser::analyse);
            String outputContent = runtimeException.toString();
            assertEquals("java.lang.RuntimeException: Exception while validate request: begin 0, end -1, length 4", outputContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    @SuppressWarnings("all")
    public void remove() {
        File file1 = new File("./src/test/resources/path1/file1.txt");
        file1.delete();
        File file2 = new File("./src/test/resources/path1/file2.txt");
        file2.delete();
        File path1 = new File("./src/test/resources/path1");
        path1.delete();
    }
}