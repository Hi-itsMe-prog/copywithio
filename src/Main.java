import java.io.*;

public class Main {

    public static void main(String[] args) throws Exception {
        // Создаем тестовые файлы
        try (FileOutputStream fos1 = new FileOutputStream("file1.txt")) {
            fos1.write("Содержимое файла 1".getBytes());
        }
        try (FileOutputStream fos2 = new FileOutputStream("file2.txt")) {
            fos2.write("Содержимое файла 2".getBytes());
        }

        // Последовательное копирование
        long startTime = System.currentTimeMillis();

        copyFile("file1.txt", "copy1.txt");
        copyFile("file2.txt", "copy2.txt");

        long seqTime = System.currentTimeMillis() - startTime;
        System.out.println("Последовательное копирование: " + seqTime + " мс");

        // Параллельное копирование
        startTime = System.currentTimeMillis();

        Thread thread1 = new Thread(() -> {
            try {
                copyFile("file1.txt", "parallel1.txt");
            } catch (Exception e) {}
        });

        Thread thread2 = new Thread(() -> {
            try {
                copyFile("file2.txt", "parallel2.txt");
            } catch (Exception e) {}
        });

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        long parallelTime = System.currentTimeMillis() - startTime;
        System.out.println("Параллельное копирование: " + parallelTime + " мс");
    }

    // Метод для копирования файлов
    private static void copyFile(String sourcePath, String destPath) throws IOException {
        try (FileInputStream fis = new FileInputStream(sourcePath);
             FileOutputStream fos = new FileOutputStream(destPath)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }
}