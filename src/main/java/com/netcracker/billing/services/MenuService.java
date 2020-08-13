package com.netcracker.billing.services;

import com.netcracker.billing.entity.MenuItem;

import java.util.List;
import java.util.NoSuchElementException;

public interface MenuService {
    List<MenuItem> getMenuItems();

    MenuItem get(int itemCode) throws NoSuchElementException;

    void add(MenuItem menuItem) throws IllegalArgumentException;

    void update(MenuItem menuItem) throws NoSuchElementException;

    void deleteByItemCode(int itemCode) throws NoSuchElementException;

    void clearMenu();

}
