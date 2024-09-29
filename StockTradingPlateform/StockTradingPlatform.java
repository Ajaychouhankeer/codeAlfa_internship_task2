package StockTradingPlateform;

import java.util.*;

class Stock {
    String symbol;
    double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public void updatePrice() {
        // Simulate price fluctuation
        double change = (Math.random() - 0.5) * 2; // -1% to +1%
        price *= (1 + change / 100);
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    double cash;

    public Portfolio(double initialCash) {
        this.cash = initialCash;
    }

    public void buyStock(Stock stock, int quantity) {
        if (cash >= stock.price * quantity) {
            holdings.put(stock.symbol, holdings.getOrDefault(stock.symbol, 0) + quantity);
            cash -= stock.price * quantity;
            System.out.printf("Bought %d shares of %s\n", quantity, stock.symbol);
        } else {
            System.out.println("Insufficient funds");
        }
    }

    public void sellStock(Stock stock, int quantity) {
        int currentHolding = holdings.getOrDefault(stock.symbol, 0);
        if (currentHolding >= quantity) {
            holdings.put(stock.symbol, currentHolding - quantity);
            cash += stock.price * quantity;
            System.out.printf("Sold %d shares of %s\n", quantity, stock.symbol);
        } else {
            System.out.println("Insufficient shares");
        }
    }

    public double getTotalValue(Map<String, Stock> market) {
        double totalValue = cash;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            totalValue += market.get(entry.getKey()).price * entry.getValue();
        }
        return totalValue;
    }

    public void displayPortfolio(Map<String, Stock> market) {
        System.out.println("\nPortfolio:");
        System.out.printf("Cash: $%.2f\n", cash);
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();
            double currentPrice = market.get(symbol).price;
            System.out.printf("%s: %d shares, Current price: $%.2f, Total value: $%.2f\n",
                    symbol, quantity, currentPrice, quantity * currentPrice);
        }
        System.out.printf("Total Portfolio Value: $%.2f\n", getTotalValue(market));
    }
}

public class StockTradingPlatform {
    private static Map<String, Stock> market = new HashMap<>();
    private static Portfolio portfolio;
    private static Scanner scanner = new Scanner(System.in);

    public static void initializeMarket() {
        market.put("AAPLE", new Stock("AAPLE", 150.0));
        market.put("GOOGLE", new Stock("GOOGLE", 2800.0));
        market.put("MICROSOFT", new Stock("MICROSOFT", 300.0));
        market.put("AMAZON", new Stock("AMAZON", 3300.0));
    }

    public static void displayMarketData() {
    	System.out.println("-----------------------------");
        System.out.println("\nCurrent Market Data:");
        
        for (Stock stock : market.values()) {
            System.out.printf("%s: $%.2f\n", stock.symbol, stock.price);
        }
    }

    public static void simulateMarket() {
        for (Stock stock : market.values()) {
            stock.updatePrice();
        }
    }

    public static void main(String[] args) {
        initializeMarket();
        portfolio = new Portfolio(10000.0); // Start with $10,000

        while (true) {
            simulateMarket();
            displayMarketData();
            portfolio.displayPortfolio(market);
            System.out.println("-----------------------------");
            System.out.println("\nOptions:");
            System.out.println("1. Buy Stock");
            System.out.println("2. Sell Stock");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1 || choice == 2) {
                System.out.print("Enter stock symbol: ");
                String symbol = scanner.nextLine().toUpperCase();
                System.out.print("Enter quantity: ");
                int quantity = scanner.nextInt();

                if (market.containsKey(symbol)) {
                    if (choice == 1) {
                        portfolio.buyStock(market.get(symbol), quantity);
                    } else {
                        portfolio.sellStock(market.get(symbol), quantity);
                    }
                } else {
                    System.out.println("Invalid stock symbol");
                }
            } else if (choice == 3) {
                break;
            } else {
                System.out.println("Invalid option");
            }
        }

        System.out.println("Thank you for using the Stock Trading Platform!");
    }
}