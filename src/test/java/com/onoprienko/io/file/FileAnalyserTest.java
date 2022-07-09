package com.onoprienko.io.file;

import com.onoprienko.io.file.FileAnalyser.RequestInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static com.onoprienko.io.file.FileAnalyser.countWordRepetition;
import static com.onoprienko.io.file.FileAnalyser.getSentences;
import static org.junit.jupiter.api.Assertions.*;

class FileAnalyserTest {
    private final String FILE_PATH_1 = "./src/test/resources/path1/file1.txt";
    private final String FILE_PATH_2 = "./src/test/resources/path1/file2.txt";
    private final String FILE_CONTENT = "Олень - северное животное. " +
            "В летнее время оленям в тайге жарко, а в горах даже в июле холодно. " +
            "Олень как бы создан для северных просторов, жёсткого ветра,длинных морозных ночей." +
            " Олень олень легко бежит вперёд по тайге,подминает под себя кусты,переплывает быстрые реки." +
            " Нос у оленя покрыт серебристой шёрсткой. Если бы шерсти на носу не было,олень бы его наверное отморозил? " +
            "Олень не тонет,потому что каждая его шерстинка-это длинная трубочка,которую внутри наполняет воздух. " +
            "Олени крутые!";

    @BeforeEach
    @SuppressWarnings("all")
    public void create() throws IOException {
        File path1 = new File("./src/test/resources/path1");
        path1.mkdirs();
        File file1 = new File(FILE_PATH_1);
        file1.createNewFile();
        File file2 = new File(FILE_PATH_2);
        file2.createNewFile();

        try (FileWriter fileWriter = new FileWriter(file2)) {
            fileWriter.write(FILE_CONTENT);
        }
    }

    @Test
    public void testAnalyseVoidFileReturnZeroResult() {
        String request = "java FileAnalyzer " + FILE_PATH_1 + " олень";
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(request.getBytes())) {
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
        FileAnalyser.AnalyseInfo analyseResult = FileAnalyser.analyse(FILE_PATH_1, "олень");
        assertEquals(0, analyseResult.getCount());
        assertEquals(0, analyseResult.getSentences().size());
    }

    @Test
    public void testAnalyseWithReturnValueFileWithoutRequestedWordReturnZeroResult() {
        FileAnalyser.AnalyseInfo analyseResult = FileAnalyser.analyse(FILE_PATH_1, "test");
        assertEquals(0, analyseResult.getCount());
        assertEquals(0, analyseResult.getSentences().size());
    }

    @Test
    public void testAnalyseFileWithoutNameOfClassInRequestReturnException() {
        String request = FILE_PATH_2 + " test";
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(request.getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            RuntimeException runtimeException = assertThrows(RuntimeException.class, FileAnalyser::analyse);
            assertEquals("Request entered incorrectly. Try typing: \"java FileAnalyser *file path* *word*\"",
                    runtimeException.getMessage());
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
            assertEquals("Exception while validate request: .\\path\\file (The system cannot find the path specified)",
                    runtimeException.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testAnalyseFileReturnCorrectWordCounterAndMultipleSentences() {
        String request = "java FileAnalyzer " + FILE_PATH_2 + " олень";
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(request.getBytes())) {
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
        String request = "java FileAnalyzer " + FILE_PATH_2 + " северное";
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(request.getBytes())) {
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
        String request = "java FileAnalyzer " + FILE_PATH_2;
        try (java.io.ByteArrayOutputStream output = new java.io.ByteArrayOutputStream();
             java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(request.getBytes())) {
            System.setIn(input);
            System.setOut(new PrintStream(output));
            RuntimeException runtimeException = assertThrows(RuntimeException.class, FileAnalyser::analyse);
            assertEquals("Exception while validate request: missing requested word or path to file",
                    runtimeException.getMessage());
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
            assertEquals("Exception while validate request: missing requested word or path to file",
                    runtimeException.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void readContentReturnCorrectContent() throws IOException {
        String content = FileAnalyser.readContent(FILE_PATH_2);
        assertEquals(FILE_CONTENT, content);
    }

    @Test
    public void readContentFromFileThatDontExist() {
        FileNotFoundException fileNotFoundException = assertThrows(FileNotFoundException.class, () -> FileAnalyser.readContent("/path/file"));
        assertEquals("\\path\\file (The system cannot find the path specified)",
                fileNotFoundException.getMessage());
    }

    @Test
    public void readContentFromEmptyFileReturnEmptyString() throws IOException {
        String content = FileAnalyser.readContent(FILE_PATH_1);
        assertEquals("", content);
    }

    @Test
    public void getSentencesReturnCorrectSentencesIncludeRequestedWord() {
        List<String> sentencesWithWord = getSentences(new RequestInfo("олень", FILE_CONTENT));
        assertFalse(sentencesWithWord.isEmpty());
        assertEquals(5, sentencesWithWord.size());

        assertEquals("[Олень - северное животное, " +
                        "Олень как бы создан для северных просторов, жёсткого ветра,длинных морозных ночей, " +
                        "Олень олень легко бежит вперёд по тайге,подминает под себя кусты,переплывает быстрые реки, " +
                        "Если бы шерсти на носу не было,олень бы его наверное отморозил," +
                        " Олень не тонет,потому что каждая его шерстинка-это длинная трубочка,которую внутри наполняет воздух]",
                sentencesWithWord.toString());
    }

    @Test
    public void getSentencesReturnCorrectSentenceIncludeRequestedWord() {
        List<String> sentencesWithWord = getSentences(new RequestInfo("носу", FILE_CONTENT));
        assertFalse(sentencesWithWord.isEmpty());
        assertEquals(1, sentencesWithWord.size());

        assertEquals("[Если бы шерсти на носу не было,олень бы его наверное отморозил]",
                sentencesWithWord.toString());
    }

    @Test
    public void getSentencesReturnEmptyListOnEmptyContent() {
        List<String> sentencesWithWord = getSentences(new RequestInfo("носу", ""));
        assertTrue(sentencesWithWord.isEmpty());
    }

    @Test
    public void getSentencesReturnNullPointerExceptionIfRequestedWordNull() {
        assertThrows(NullPointerException.class, () -> getSentences(new RequestInfo(null, FILE_CONTENT)));
    }

    @Test
    public void getSentencesReturnNullPointerExceptionIfRequestedContentNull() {
        assertThrows(NullPointerException.class, () -> getSentences(new RequestInfo("word", null)));
    }

    @Test
    public void countWordRepetitionReturnCorrectResult() {
        int result = countWordRepetition(new RequestInfo("олень", FILE_CONTENT));
        assertEquals(6, result);
    }

    @Test
    public void countWordRepetitionReturnCorrectResultForOneRepetition() {
        int result = countWordRepetition(new RequestInfo("ветра", FILE_CONTENT));
        assertEquals(1, result);
    }

    @Test
    public void countWordRepetitionReturnZeroOnWordThatNotExistInContent() {
        int result = countWordRepetition(new RequestInfo("a", FILE_CONTENT));
        assertEquals(0, result);
    }

    @Test
    public void countWordRepetitionReturnZeroIfContentEmpty() {
        int result = countWordRepetition(new RequestInfo("word", ""));
        assertEquals(0, result);
    }

    @Test
    public void countWordRepetitionReturnNullPointerExceptionIfWordNull() {
        assertThrows(NullPointerException.class, () -> countWordRepetition(new RequestInfo(null, FILE_CONTENT)));
    }

    @Test
    public void countWordRepetitionReturnNullPointerExceptionIfContentNull() {
        assertThrows(NullPointerException.class, () -> countWordRepetition(new RequestInfo("word", null)));
    }

    @Test
    public void validateRequestReturnRequestInfo() {
        String request = "java FileAnalyzer " + FILE_PATH_2 + " олень";
        RequestInfo resultRequest = FileAnalyser.validateRequest(request);
        assertEquals("олень", resultRequest.getWord());
        assertEquals(FILE_CONTENT, resultRequest.getContent());
    }

    @Test
    public void validateRequestReturnRequestInfoWithEmptyContent() {
        String request = "java FileAnalyzer " + FILE_PATH_1 + " олень";
        RequestInfo resultRequest = FileAnalyser.validateRequest(request);
        assertEquals("олень", resultRequest.getWord());
        assertEquals("", resultRequest.getContent());
    }

    @Test
    public void validateRequestReturnExceptionIfWordIsMissing() {
        String request = "java FileAnalyzer " + FILE_PATH_1;
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> FileAnalyser.validateRequest(request));
        assertEquals("Exception while validate request: missing requested word or path to file", runtimeException.getMessage());
    }

    @Test
    public void validateRequestReturnExceptionIfPathToFileIsMissing() {
        String request = "java FileAnalyzer word";
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> FileAnalyser.validateRequest(request));
        assertEquals("Exception while validate request: missing requested word or path to file", runtimeException.getMessage());
    }

    @Test
    public void validateRequestReturnExceptionIfStartOfCommandIsMissing() {
        String request = FILE_PATH_2 + " word";
        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> FileAnalyser.validateRequest(request));
        assertEquals("Request entered incorrectly. Try typing: \"java FileAnalyser *file path* *word*\"",
                runtimeException.getMessage());
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