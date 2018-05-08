package pl.baranski.bitbay;

import org.springframework.stereotype.Component;
import pl.baranski.bitbay.so.TradesSO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.Instant;
import java.util.Arrays;

@Component
public class Trades {

    private JTable tradesTable;
    private JPanel panel1;
    private JComboBox chooseCrypto;
    private JComboBox chooseCurrency;
    private JButton button1;
    private String currentCrypto = "LSK";
    private String currentCurrency = "PLN";
    private TradesService tradesService;

    private String[] colNames = new String[] {"Date", "Amount", "Price"};

    public Trades() {
        DefaultTableModel model = new DefaultTableModel(new String[0][0], colNames);
        tradesTable.setModel(model);

        addComboListeners();
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateTradesTable();
            }
        });
    }

    private void addComboListeners() {
        chooseCrypto.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                updateTradesTable((String) itemEvent.getItem(), (String) chooseCurrency.getSelectedItem());
            }
        });

        chooseCurrency.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                updateTradesTable((String) chooseCrypto.getSelectedItem(), (String) itemEvent.getItem());
            }
        });
    }

    public void updateTradesTable() {
        updateTradesTable(currentCrypto, currentCurrency);
    }

    public void updateTradesTable(String crypto, String currency) {

        this.currentCrypto = crypto;
        this.currentCurrency = currency;
        DefaultTableModel tableModel = (DefaultTableModel) tradesTable.getModel();
        int rowCount = tableModel.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
        TradesService tradesService = (TradesService) BitBay.getContext().getBean("tradesService");
        TradesSO[] trades = tradesService.getTrades(crypto, currency);

        Arrays.asList(trades).forEach(trade -> {
            tableModel.addRow(
                    new Object[] {Instant.ofEpochSecond(trade.getDate()), trade.getAmount(), trade.getPrice()});
        });
    }

}