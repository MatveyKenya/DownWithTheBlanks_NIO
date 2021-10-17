package ru.matveykenya;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Thread server = new Thread(Main::server);
        Thread client = new Thread(Main::client);

        server.start();
        client.start();


    }

    static void server(){
        try {
            //  Занимаем порт, определяя серверный сокет
            final ServerSocketChannel serverChannel = ServerSocketChannel.open();
            serverChannel.bind(new InetSocketAddress("127.0.0.1", 23334));
            while (true) {
                // Ждем подключения клиента и получаем потоки для дальнейшей работы
                try (SocketChannel socketChannel = serverChannel.accept()) {
                    // Определяем буфер для получения данных
                    final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                    while (socketChannel.isConnected()) {
                        // читаем данные из канала в буфер
                        int bytesCount = socketChannel.read(inputBuffer);
                        System.out.println("сервер прочитал");
                        // если из потока читать нельзя, перестаем работать с этим клиентом
                        if (bytesCount == -1) {
                            break;
                        }
                        //  получаем переданную от клиента строку в нужной кодировке и очищаем буфер
                        final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                        inputBuffer.clear();
                        if ("end".equals(msg)) {
                            return;
                        }
                        Thread.sleep(5000); // эмитируем сложную операцию вычисления
                        socketChannel.write(ByteBuffer.wrap((msg.replaceAll("\\s","")).getBytes(StandardCharsets.UTF_8)));
                    }
                }
            }
        } catch (IOException | InterruptedException err) {
            System.out.println(err.getMessage());
        }
    }

    static void client() {
        try (SocketChannel socketChannel = SocketChannel.open()) {
            // Определяем сокет сервера
            InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 23334);
            //  подключаемся к серверу
            socketChannel.connect(socketAddress);

            // Получаем входящий и исходящий потоки информации
            try (Scanner scanner = new Scanner(System.in)) {
                //  Определяем буфер для получения данных
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
                String msg;
                while (true) {
                    System.out.println("Введите строчку с пробелами (или 'end' для выхода):");
                    msg = scanner.nextLine();
                    socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                    if ("end".equals(msg)) {
                        break;
                    }
                    // клиент проверяет - если буфер не пуст то читает его и выводит сообщение
                    if (inputBuffer. ??????? ) {
                        int bytesCount = socketChannel.read(inputBuffer);
                        System.out.println(new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8).trim());
                        inputBuffer.clear();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
