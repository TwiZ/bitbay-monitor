package pl.baranski.bitbay;

import org.springframework.context.annotation.Bean;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Instant;
import java.util.Arrays;

public class Trades {
    private JTable tradesTable;
    private JPanel panel1;
    private JComboBox chooseCrypto;
    private JComboBox chooseCurrency;
    private JButton button1;
    private RestTemplate restTemplate = restTemplate();
    private String currentCrypto;
    private String currentCurrency;

    private String[] colNames = new String[] {"Date", "Price", "Amount"};

    public Trades() {
        DefaultTableModel model = new DefaultTableModel(new String[0][0], colNames);
        tradesTable.setModel(model);
        updateTradesTable("LSK", "PLN");

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

    private void updateTradesTable() {
        updateTradesTable(currentCrypto, currentCurrency);
    }

    private void updateTradesTable(String crypto, String currency) {
        this.currentCrypto = crypto;
        this.currentCurrency = currency;
        DefaultTableModel tableModel = (DefaultTableModel) tradesTable.getModel();
        int rowCount = tableModel.getRowCount();
        //Remove rows one by one from the end of the table
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
        TradesSO[] trades = restTemplate.getForObject(
                "https://bitbay.net/API/Public/" + crypto + currency + "/trades.json?sort=desc",
                TradesSO[].class);

        Arrays.asList(trades).forEach(trade -> {
            tableModel.addRow(
                    new Object[] {Instant.ofEpochSecond(trade.getDate()), trade.getAmount(), trade.getPrice()});
        });
    }

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("www-le.dienste.telekom.de", 80));
        requestFactory.setProxy(proxy);

        return new RestTemplate(requestFactory);
    }

    public static void main(String args[]) {
        JFrame frame = new JFrame();
        frame.setContentPane(new Trades().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
