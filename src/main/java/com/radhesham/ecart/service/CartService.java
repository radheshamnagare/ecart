package com.radhesham.ecart.service;

import com.radhesham.ecart.bean.CartItemBean;
import com.radhesham.ecart.common.ConstantsPool;
import com.radhesham.ecart.entity.ProductEntity;
import com.radhesham.ecart.entity.TempCartEntity;
import com.radhesham.ecart.repository.CartRepository;
import com.radhesham.ecart.repository.ProductsRepository;
import com.radhesham.ecart.repository.ViewProductStock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartService.class);
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductsRepository productsRepository;

    public boolean isValidCartItemId(int id) {
        return cartRepository.existsById(id);
    }

    public List<CartItemBean> getAllCartItemByUser(int userId) {
        List<CartItemBean> cartItems = new ArrayList<>();
        try {
            List<TempCartEntity> details = cartRepository.findByUserId(userId);
            details.forEach(item -> {
                CartItemBean cartItemBean = new CartItemBean();
                BeanUtils.copyProperties(item, cartItemBean);
                cartItems.add(cartItemBean);
            });
        } catch (Exception e) {
            logger.error("Exception in getAllCartItemByUser():", e);
        }
        return cartItems;
    }

    public synchronized List<CartItemBean> saveItems(CartItemBean cartItem) {
        List<CartItemBean> cartItemsDetails = new ArrayList<>();
        try {
            logger.info("Entry in saveItems()");
            TempCartEntity cartEntity = new TempCartEntity();
            if (cartItem.getId() > 0) {
                Optional<TempCartEntity> findCartItem = cartRepository.findById(cartItem.getId());
                if (findCartItem.isPresent()) {
                    cartEntity = findCartItem.get();
                    cartEntity.setQuantity(cartEntity.getQuantity() + 1);
                }
            } else {
                BeanUtils.copyProperties(cartItem, cartEntity);
            }
            ViewProductStock viewProductStock = productsRepository.getProductEntityByProductId(cartEntity.getProductId());
            if (viewProductStock.getUnitsInStock() > 0) {
                Optional<ProductEntity> product = productsRepository.findById(cartItem.getProductId());
                if (product.isPresent()) {
                    ProductEntity productEntity = product.get();

                    if (Objects.nonNull(productEntity)) {
                        logger.warn("saveItems()::product stock is updated");
                        TempCartEntity savedCartItem = cartRepository.save(cartEntity);
                        if (Objects.nonNull(savedCartItem)) {
                            logger.warn("saveItems()::cart item is saved successfully");
                            cartItemsDetails = getAllCartItemByUser(cartItem.getUserId());
                        } else logger.warn("saveItems()::cart item is not able to saved");
                    }
                } else {
                    logger.info("saveItems():: product details not found");
                }
            }
            logger.info("Exit from saveItems()");
        } catch (Exception e) {
            logger.error("Exception in saveItems():", e.toString());
        }
        return cartItemsDetails;
    }

    public synchronized List<CartItemBean> removeItems(CartItemBean cartItem) {
        List<CartItemBean> cartItemsDetails = new ArrayList<>();
        try {
            logger.info("Entry in removeItems()");
            TempCartEntity cartEntity = new TempCartEntity();
            if (cartItem.getId() > 0) {
                Optional<TempCartEntity> findCartItem = cartRepository.findById(cartItem.getId());
                if (findCartItem.isPresent()) {
                    cartEntity = findCartItem.get();
                }
            } else {
                BeanUtils.copyProperties(cartItem, cartEntity);
            }
            Optional<ProductEntity> product = productsRepository.findById(cartItem.getProductId());
            if (product.isPresent()) {
                ProductEntity productEntity = product.get();
                if (cartEntity.getQuantity() == 1) {
                    cartRepository.delete(cartEntity);
                    productEntity.setUnitsInStock(productEntity.getUnitsInStock() + 1);
                }else if(cartItem.getAction().equals(ConstantsPool.ACTION_DELETE_ALL)){
                    cartRepository.delete(cartEntity);
                    productEntity.setUnitsInStock(productEntity.getUnitsInStock() + cartItem.getQuantity());
                }else {
                    cartEntity.setQuantity(cartEntity.getQuantity() - 1);
                    cartRepository.save(cartEntity);
                    productEntity.setUnitsInStock(productEntity.getUnitsInStock() + 1);
                    logger.info("removeItems():: more than one item in cart");
                }
                productsRepository.save(productEntity);
            } else {
                logger.info("removeItems()::product details not found for remove items");
            }
            cartItemsDetails = getAllCartItemByUser(cartItem.getUserId());
            logger.info("Exit from removeItems()");
        } catch (Exception e) {
            logger.error("Exception in removeItems():", e);
        }
        return cartItemsDetails;
    }
}
