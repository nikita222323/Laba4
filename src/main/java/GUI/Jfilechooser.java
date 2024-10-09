package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

public class Jfilechooser extends Component {

    private JFileChooser fileChooser;

    public Jfilechooser() throws URISyntaxException {

        try {
            File currentDirectory = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();
            fileChooser = new JFileChooser(currentDirectory);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public String openFile() {

        fileChooser.setDialogTitle("Выбор файла");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        while (true) {
            int result = fileChooser.showOpenDialog(Jfilechooser.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                if (!(filePath.toLowerCase().endsWith(".yaml"))) {
                    JOptionPane.showMessageDialog(null, "Выбранный файл не является файлом формата yaml", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    continue; // Вернуться к выбору файла
                }
                JOptionPane.showMessageDialog(null, selectedFile);
                return fileChooser.getSelectedFile().getAbsolutePath();
            } else {
                throw new NullPointerException("Выбор файла был отменен, выберите снова");
            }
        }
    }

    public String openDB() {

        fileChooser.setDialogTitle("Выбор файла");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        while (true) {
            int result = fileChooser.showOpenDialog(Jfilechooser.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();
                if (!(filePath.toLowerCase().endsWith(".db"))) {
                    JOptionPane.showMessageDialog(null, "Выбранный файл не является файлом формата db", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    continue; // Вернуться к выбору файла
                }
                JOptionPane.showMessageDialog(null, selectedFile);
                return fileChooser.getSelectedFile().getName();
            } else {
                throw new NullPointerException("Выбор файла был отменен, выберите снова");
            }
        }
    }
}