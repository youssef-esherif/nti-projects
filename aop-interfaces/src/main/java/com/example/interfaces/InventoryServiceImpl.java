package com.example.interfaces;

public class InventoryServiceImpl implements InventoryService {
    @Override
    public int checkStock(String sku) {
        System.out.println("TARGET: checking stock for " + sku);
        return 100;
    }

    @Override
    public void reserveStock(String sku, int qty) {
        System.out.println("TARGET: reserving " + qty + " of " + sku);
        if (qty > 100) {
            throw new IllegalStateException("Only 100 items are available");
        }
        System.out.println("Reservation successful");
    }
}
