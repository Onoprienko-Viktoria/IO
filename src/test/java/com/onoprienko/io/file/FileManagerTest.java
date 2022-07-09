package com.onoprienko.io.file;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class FileManagerTest {

    @BeforeEach
    @SuppressWarnings("all")
    public void create() throws IOException {
        File path1 = new File("./src/test/resources/path1");
        path1.mkdirs();
        File path2 = new File("./src/test/resources/path2");
        path2.mkdir();
        File path3 = new File("./src/test/resources/path3");
        path3.mkdir();
        File path4 = new File("./src/test/resources/path4");
        path4.mkdir();
        File path5 = new File("./src/test/resources/path4/path5");
        path5.mkdir();

        File file1 = new File("./src/test/resources/path4/file1.txt");
        file1.createNewFile();
        File file2 = new File("./src/test/resources/path4/file2.txt");
        file2.createNewFile();
        File file3 = new File("./src/test/resources/path1/file3.txt");
        file3.createNewFile();
        File file4 = new File("./src/test/resources/path4/path5/file4.txt");
        file4.createNewFile();
        File file5 = new File("./src/test/resources/path4/path5/file5.txt");
        file5.createNewFile();


        try (FileWriter fileWriter = new FileWriter(file4)) {
            String text = "Олень - северное животное. " +
                    "В летнее время оленям в тайге жарко, а в горах даже в июле холодно. ";
            fileWriter.write(text);
        }

    }

    @Test
    void testCountDirsWorkCorrectly() throws IOException {
        assertEquals(5, FileManager.countDirs("./src/test/resources"));
        assertEquals(1, FileManager.countDirs("./src/test/resources/path4"));
        assertEquals(0, FileManager.countDirs("./src/test/resources/path3"));
    }

    @Test
    void testCountDirsReturnZeroOnPathToTextFile() throws IOException {
        assertEquals(0, FileManager.countDirs("./src/test/resources/path4/path5/file4.txt"));
    }

    @Test
    void testCountFilesWorkCorrectly() {
        assertEquals(2, FileManager.countFiles("./src/test/resources/path4/path5"));
        assertEquals(0, FileManager.countFiles("./src/test/resources/path2"));
        assertEquals(1, FileManager.countFiles("./src/test/resources/path1"));
    }

    @Test
    void testCountFilesThrowsNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> FileManager.countFiles("./src/test/resources/path1/file3.txt"));
    }

    @Test
    void testCopyFilesWorkCorrectly() throws IOException {
        FileManager.copy("./src/test/resources/path1/file3.txt", "./src/test/resources/path3/file3.txt");
        assertEquals(1, FileManager.countFiles("./src/test/resources/path1"));
        assertEquals(1, FileManager.countFiles("./src/test/resources/path3"));
    }

    @Test
    void testCopyDirectoriesWorkCorrectly() throws IOException {
        assertEquals(1, FileManager.countFiles("./src/test/resources/path1"));
        assertEquals(4, FileManager.countFiles("./src/test/resources/path4"));

        FileManager.copy("./src/test/resources/path4", "./src/test/resources/path1");

        assertEquals(4, FileManager.countFiles("./src/test/resources/path4"));
        assertEquals(5, FileManager.countFiles("./src/test/resources/path1"));
    }

    @Test
    void testCopyDirectoriesAlsoCopyContentInsideFiles() throws IOException {
        assertEquals(1, FileManager.countFiles("./src/test/resources/path1"));
        assertEquals(4, FileManager.countFiles("./src/test/resources/path4"));

        FileManager.copy("./src/test/resources/path4", "./src/test/resources/path1");

        assertEquals(5, FileManager.countFiles("./src/test/resources/path1"));
        assertEquals("Олень - северное животное. " +
                "В летнее время оленям в тайге жарко, а в горах даже в июле холодно. ", read("./src/test/resources/path4/path5/file4.txt"));
        assertEquals("Олень - северное животное. " +
                "В летнее время оленям в тайге жарко, а в горах даже в июле холодно. ", read("./src/test/resources/path1/path5/file4.txt"));
    }

    @Test
    void testCopyAlsoCopyContentInsideFile() throws IOException {
        FileManager.copy("./src/test/resources/path4/path5/file4.txt", "./src/test/resources/path2/file4.txt");
        assertEquals(2, FileManager.countFiles("./src/test/resources/path4/path5"));
        assertEquals(1, FileManager.countFiles("./src/test/resources/path2"));
        assertEquals("Олень - северное животное. " +
                "В летнее время оленям в тайге жарко, а в горах даже в июле холодно. ", read("./src/test/resources/path4/path5/file4.txt"));
        assertEquals("Олень - северное животное. " +
                "В летнее время оленям в тайге жарко, а в горах даже в июле холодно. ", read("./src/test/resources/path2/file4.txt"));
    }


    @Test
    void testMoveWorkCorrectly() throws IOException {
        assertEquals(4, FileManager.countFiles("./src/test/resources/path4"));
        assertEquals(0, FileManager.countFiles("./src/test/resources/path3"));

        FileManager.move("./src/test/resources/path4/file2.txt", "./src/test/resources/path3/file2.txt");

        assertEquals(3, FileManager.countFiles("./src/test/resources/path4"));
        assertEquals(1, FileManager.countFiles("./src/test/resources/path3"));
    }


    @SuppressWarnings("unused")
    private String read(String path) throws IOException {
        File file = new File(path);
        try (InputStream inputStream = new FileInputStream(file)) {
            byte[] text = new byte[(int) file.length()];
            int read = inputStream.read(text);
            return new String(text);
        }
    }


    @Test
    void testMoveDirectoriesWorkCorrectly() throws IOException {
        assertEquals(1, FileManager.countFiles("./src/test/resources/path1"));
        assertEquals(4, FileManager.countFiles("./src/test/resources/path4"));

        FileManager.move("./src/test/resources/path4", "./src/test/resources/path1");

        assertEquals(5, FileManager.countFiles("./src/test/resources/path1"));
        assertEquals(0, FileManager.countFiles("./src/test/resources/path4"));
    }

    @AfterEach
    @SuppressWarnings("all")
    public void remove() {
        File file5 = new File("./src/test/resources/path4/path5/file5.txt");
        file5.delete();
        File file4 = new File("./src/test/resources/path4/path5/file4.txt");
        file4.delete();
        File path5 = new File("./src/test/resources/path4/path5");
        path5.delete();
        File file1 = new File("./src/test/resources/path4/file1.txt");
        file1.delete();
        File file2 = new File("./src/test/resources/path4/file2.txt");
        file2.delete();
        File path4 = new File("./src/test/resources/path4");
        path4.delete();
        File movedFile = new File("./src/test/resources/path3/file3.txt");
        movedFile.delete();
        File copyFile = new File("./src/test/resources/path3/file2.txt");
        copyFile.delete();
        File file3 = new File("./src/test/resources/path1/file3.txt");
        file3.delete();
        File file31 = new File("./src/test/resources/path1/file1.txt");
        file31.delete();
        File file32 = new File("./src/test/resources/path1/file2.txt");
        file32.delete();
        File file51 = new File("./src/test/resources/path1/path5/file5.txt");
        file51.delete();
        File file41 = new File("./src/test/resources/path1/path5/file4.txt");
        file41.delete();
        File path15 = new File("./src/test/resources/path1/path5");
        path15.delete();
        File path1 = new File("./src/test/resources/path1");
        path1.delete();
        File filePath2 = new File("./src/test/resources/path2/file4.txt");
        filePath2.delete();
        File path2 = new File("./src/test/resources/path2");
        path2.delete();
        File path3 = new File("./src/test/resources/path3");
        path3.delete();
    }
}
