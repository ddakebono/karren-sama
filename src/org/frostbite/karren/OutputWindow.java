package org.frostbite.karren;

import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class OutputWindow {
    private JFrame window = new JFrame();
    private JTextArea console = new JTextArea();
    private JScrollPane consoleScroll = new JScrollPane(console);
    public OutputWindow(String title){
        window.setSize(700,500);
        window.setTitle(title);
    }
    public void displayWindow(){
        window.add(consoleScroll);
        window.setVisible(true);
        redirectSystemStreams();
    }
    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                console.append(text);
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
}
