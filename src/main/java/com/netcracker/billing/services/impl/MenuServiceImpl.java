package com.netcracker.billing.services.impl;

import com.netcracker.billing.entity.MenuItem;
import com.netcracker.billing.repository.MenuRepository;
import com.netcracker.billing.services.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;

    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuItem> getMenuItems() {
        return menuRepository.findAll();
    }

    @Override
    public MenuItem get(int itemCode) throws NoSuchElementException {
        Optional<MenuItem> menuItem = menuRepository.findById(itemCode);
        if (!menuItem.isPresent())
            throw new NoSuchElementException();
        return menuItem.get();
    }

    public void add(MenuItem menuItem) throws IllegalArgumentException {
        validate(menuItem);
        boolean alreadyExists = menuRepository.existsById(menuItem.getItemCode());
        if (alreadyExists)
            throw new IllegalArgumentException("Item with Code " + menuItem.getItemCode() + " is already on Menu");
        menuRepository.save(menuItem);
    }

    public void deleteByItemCode(int itemCode) throws NoSuchElementException {
        boolean exists = menuRepository.existsById(itemCode);
        if (!exists)
            throw new NoSuchElementException("Item with Code " + itemCode + " is not on Menu");
        menuRepository.deleteById(itemCode);
    }

    public void update(MenuItem menuItem) throws NoSuchElementException {
        validate(menuItem);
        boolean exists = menuRepository.existsById(menuItem.getItemCode());
        if (!exists)
            throw new NoSuchElementException("Item with Code " + menuItem.getItemCode() + " is not on Menu");
        menuRepository.save(menuItem);
    }

    public void clearMenu() {
        menuRepository.deleteAll();
    }

    private void validate(MenuItem menuItem) {
        if (menuItem == null)
            throw new IllegalArgumentException("Item is empty");
    }
}
