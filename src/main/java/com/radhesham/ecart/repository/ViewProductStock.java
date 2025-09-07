package com.radhesham.ecart.repository;

public interface ViewProductStock {

    int productId = 0;
    int unitsInStock = 0;

    int getProductId();
    void setProductId(int productId);

    void setUnitsInStock(int unitsInStock);
    int getUnitsInStock();
}
