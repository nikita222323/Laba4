package GUI;

import db.CreatingDB;
import db.ReadDB;
import excel.WriteResults;
import Example.Parser;
import reactor.Aggregating;
import tables.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URISyntaxException;
import java.sql.SQLException;

public class Panel extends JPanel {
    JButton b_parse = new JButton("Парсинг сайта и создание БД");
    JButton b_results = new JButton("Вывести результаты");
    JButton b_exit = new JButton("Выход");
    GridLayout gr = new GridLayout(3, 1);

    Parser parser;
    CreatingDB db = new CreatingDB();
    CreatingTables creatingTables;
    FillingReactorTypes fillingReactorTypes;
    FillingRegions fillingRegions;
    FillingCountries fillingCountries;
    FillingReactrsFromPRIS fillingReactrsFromPRIS;
    FillingLoadFactor fillingLoadFactor;
    FillingConsumption fillingConsumption;
    Aggregating aggregating;
    ReadDB readDB;
    Jfilechooser jfilechooser;
    String path;

    public Panel() {
        setLayout(gr);
        add(b_parse);
        add(b_results);
        add(b_exit);

        b_parse.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jfilechooser = new Jfilechooser();
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                path = jfilechooser.openFile();
                JOptionPane.showMessageDialog(null, "Запуск парсера");
                parser = new Parser();
                JOptionPane.showMessageDialog(null, "Парсинг сайта завершен");
                db.connect();
                JOptionPane.showMessageDialog(null, "К БД подключено");
                creatingTables = new CreatingTables();
                JOptionPane.showMessageDialog(null, "Таблицы созданы");
                fillingReactorTypes = new FillingReactorTypes(path);
                JOptionPane.showMessageDialog(null, "Заполнена таблица ReactorTypes");
                fillingRegions = new FillingRegions();
                JOptionPane.showMessageDialog(null, "Заполнена таблица Regions");
                fillingCountries = new FillingCountries();
                JOptionPane.showMessageDialog(null, "Заполнена таблица Countries");
                fillingReactrsFromPRIS = new FillingReactrsFromPRIS(parser.getDataList());
                JOptionPane.showMessageDialog(null, "Заполнена таблица ReactorsFromPRIS");
                fillingLoadFactor = new FillingLoadFactor(parser.getMapForLoadMap());
                JOptionPane.showMessageDialog(null, "Заполнена таблица LoadFactor");
                fillingConsumption = new FillingConsumption();
                JOptionPane.showMessageDialog(null, "Заполнена таблица Consumption");
                try {
                    aggregating = new Aggregating();
                    JOptionPane.showMessageDialog(null, "Агрегация завершена завершена");
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        b_results.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    jfilechooser = new Jfilechooser();
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                }
                readDB = new ReadDB(jfilechooser.openDB());
                new WriteResults(readDB);
                JOptionPane.showMessageDialog(null, "Данные записаны в эксель");
                System.out.println("Данные записаны в эксель");
            }
        });

        b_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}