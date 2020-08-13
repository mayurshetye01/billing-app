package com.netcracker.billing.services;

import com.netcracker.billing.entity.MenuItem;
import com.netcracker.billing.repository.MenuRepository;
import com.netcracker.billing.services.impl.MenuServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

    @InjectMocks
    MenuServiceImpl menuService;

    @Mock
    MenuRepository menuRepository;

    private static final String TEST_ITEM_NAME = "test-item";
    private static final int TEST_ITEM_CODE = 1;
    private static final BigDecimal TEST_ITEM_PRICE = BigDecimal.TEN;


    @Test
    void testGetMenu() {
        List<MenuItem> testMenu = getTestMenu();
        when(menuRepository.findAll()).thenReturn(testMenu);

        List<MenuItem> result = menuService.getMenuItems();
        assertEquals(testMenu.size(), result.size());
        assertTrue(testMenu.containsAll(result));
    }

    @Test
    void testAddItem() {
        MenuItem testMenuItem = getTestMenuItem(TEST_ITEM_CODE, TEST_ITEM_NAME, TEST_ITEM_PRICE);
        menuService.add(testMenuItem);
        verify(menuRepository, times(1)).existsById(testMenuItem.getItemCode());
        verify(menuRepository, times(1)).save(any(MenuItem.class));

    }

    @Test
    void testAddItemWithDuplicateCode() {
        MenuItem testMenuItem = getTestMenuItem(TEST_ITEM_CODE, TEST_ITEM_NAME, TEST_ITEM_PRICE);
        when(menuRepository.existsById(TEST_ITEM_CODE)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> menuService.add(testMenuItem));
        verify(menuRepository, times(0)).save(any(MenuItem.class));
    }

    @Test
    void testUpdateItem() {
        MenuItem testMenuItem = getTestMenuItem(TEST_ITEM_CODE, TEST_ITEM_NAME, TEST_ITEM_PRICE);
        when(menuRepository.existsById(TEST_ITEM_CODE)).thenReturn(true);
        menuService.update(testMenuItem);
        verify(menuRepository, times(1)).existsById(testMenuItem.getItemCode());
        verify(menuRepository, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testUpdateNonExistentItem() {
        MenuItem testMenuItem = getTestMenuItem(TEST_ITEM_CODE, TEST_ITEM_NAME, TEST_ITEM_PRICE);
        when(menuRepository.existsById(TEST_ITEM_CODE)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> menuService.update(testMenuItem));
        verify(menuRepository, times(0)).save(any(MenuItem.class));
    }

    @Test
    void testDeleteItem() {
        when(menuRepository.existsById(TEST_ITEM_CODE)).thenReturn(true);
        menuService.deleteByItemCode(TEST_ITEM_CODE);
        verify(menuRepository, times(1)).existsById(TEST_ITEM_CODE);
        verify(menuRepository, times(1)).deleteById(TEST_ITEM_CODE);
    }

    @Test
    void testDeleteNonExistentItem() {
        when(menuRepository.existsById(TEST_ITEM_CODE)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> menuService.deleteByItemCode(TEST_ITEM_CODE));
        verify(menuRepository, times(0)).deleteById(TEST_ITEM_CODE);
    }

    @Test
    void testGetItem() {
        MenuItem testItem = getTestMenuItem(TEST_ITEM_CODE, TEST_ITEM_NAME, TEST_ITEM_PRICE);
        when(menuRepository.findById(TEST_ITEM_CODE)).thenReturn(Optional.of(testItem));

        MenuItem result = menuService.get(TEST_ITEM_CODE);

        assertEquals(testItem.getItemCode(), result.getItemCode());
        assertEquals(testItem.getItemCode(), result.getItemCode());
    }

    @Test
    void testGetNonExistentItem() {
        when(menuRepository.findById(TEST_ITEM_CODE)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> menuService.get(TEST_ITEM_CODE));
    }

    @Test
    void testClearMenu() {
        assertDoesNotThrow(() -> menuService.clearMenu());
    }

    private List<MenuItem> getTestMenu() {
        List<MenuItem> menu = new ArrayList<>();

        menu.add(getTestMenuItem(1, "item1", BigDecimal.TEN));
        menu.add(getTestMenuItem(2, "item2", BigDecimal.TEN));

        return menu;
    }

    private MenuItem getTestMenuItem(int itemCode, String name, BigDecimal pricePerUnit) {
        MenuItem menuItem = new MenuItem();
        menuItem.setItemCode(itemCode);
        menuItem.setName(name);
        menuItem.setPricePerUnit(pricePerUnit);
        menuItem.setAvailable(true);
        return menuItem;
    }
}
