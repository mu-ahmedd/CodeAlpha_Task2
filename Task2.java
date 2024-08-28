import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Stock {
    String symbol;
    double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

class Portfolio {
    Map<String, Integer> stocks;
    double cash;

    public Portfolio() {
        this.stocks = new HashMap<>();
        this.cash = 10000.0; // initial cash
    }

    public void buyStock(Stock stock, int quantity) {
        if (cash >= stock.price * quantity) {
            cash -= stock.price * quantity;
            stocks.put(stock.symbol, stocks.getOrDefault(stock.symbol, 0) + quantity);
        }
    }

    public void sellStock(Stock stock, int quantity) {
        if (stocks.containsKey(stock.symbol) && stocks.get(stock.symbol) >= quantity) {
            cash += stock.price * quantity;
            stocks.put(stock.symbol, stocks.get(stock.symbol) - quantity);
        }
    }

    public double getPortfolioValue() {
        double value = cash;
        for (Map.Entry<String, Integer> entry : stocks.entrySet()) {
            value += getStockPrice(entry.getKey()) * entry.getValue();
        }
        return value;
    }

    private double getStockPrice(String symbol) {
        // for simplicity, assuming prices are fixed
        if (symbol.equals("AAPL"))
            return 150.0;
        if (symbol.equals("GOOG"))
            return 2500.0;
        if (symbol.equals("MSFT"))
            return 200.0;
        return 0.0;
    }
}

public class Task2{
    private JFrame frame;
    private JTextField symbolField;
    private JTextField quantityField;
    private JComboBox<String> commandComboBox;
    private JLabel portfolioValueLabel;
    private Portfolio portfolio;
    private ArrayList<Stock> marketData;

    public Task2() {
        portfolio = new Portfolio();
        marketData = new ArrayList<>();
        marketData.add(new Stock("AAPL", 150.0));
        marketData.add(new Stock("GOOG", 2500.0));
        marketData.add(new Stock("MSFT", 200.0));

        frame = new JFrame("Stock Trading Platform");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel marketDataPanel = new JPanel();
        marketDataPanel.setLayout(new GridLayout(0, 2));
        for (Stock stock : marketData) {
            marketDataPanel.add(new JLabel(stock.symbol + ": $" + stock.price));
        }
        frame.add(marketDataPanel, BorderLayout.NORTH);

        JPanel commandPanel = new JPanel();
        commandPanel.setLayout(new FlowLayout());
        commandComboBox = new JComboBox<>(new String[] { "Buy", "Sell" });
        symbolField = new JTextField(10);
        quantityField = new JTextField(10);
        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = (String) commandComboBox.getSelectedItem();
                String symbol = symbolField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                Stock stock = getStockFromMarketData(symbol);
                if (stock != null) {
                    if (command.equals("Buy")) {
                        portfolio.buyStock(stock, quantity);
                    } else if (command.equals("Sell")) {
                        portfolio.sellStock(stock, quantity);
                    }
                    updatePortfolioValueLabel();
                }
            }
        });
        commandPanel.add(commandComboBox);
        commandPanel.add(new JLabel("Symbol:"));
        commandPanel.add(symbolField);
        commandPanel.add(new JLabel("Quantity:"));
        commandPanel.add(quantityField);
        commandPanel.add(executeButton);
        frame.add(commandPanel, BorderLayout.CENTER);

        portfolioValueLabel = new JLabel("Portfolio Value: $0.0");
        frame.add(portfolioValueLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
    }

    private Stock getStockFromMarketData(String symbol) {
        for (Stock stock : marketData) {
            if (stock.symbol.equals(symbol)) {
                return stock;
            }
        }
        return null;
    }

    private void updatePortfolioValueLabel() {
        portfolioValueLabel.setText("Portfolio Value: $" + String.format("%.2f", portfolio.getPortfolioValue()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Task2();
            }
        });
    }
}