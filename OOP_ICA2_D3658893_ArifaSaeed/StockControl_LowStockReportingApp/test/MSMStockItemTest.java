/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
//package uk.ac.tees.scedt.u00012604.oop.ica.solution;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author u0012604
 */
public class MSMStockItemTest {

    public MSMStockItemTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getDepartmentId method, of class MSMStockItem.
     */
    @Test
    public void testGetDepartmentId() {
        System.out.println("getDepartmentId");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country,8850,50", 1099, 10);
        int expResult = 1;
        int result = instance.getDepartmentId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getCode method, of class MSMStockItem.
     */
    @Test
    public void testGetCode() {
        System.out.println("getCode");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country,8850,50", 1099, 10);
        String expResult = "234567";
        String result = instance.getCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getName method, of class MSMStockItem.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country", 8850, 50);
        String expResult = "RunEverywhere";
        String result = instance.getName();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getDescription method, of class MSMStockItem.
     */
    @Test
    public void testGetDescription() {
        System.out.println("getDescription");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country", 8850, 50);
        String expResult = "Great trainers for running cross country";
        String result = instance.getDescription();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getUnitPrice method, of class MSMStockItem.
     */
    @Test
    public void testGetUnitPrice() {
        System.out.println("getUnitPrice");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country", 8850, 50);
        int expResult = 8850;
        int result = instance.getUnitPrice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getQuantityInStock method, of class MSMStockItem.
     */
    @Test
    public void testGetQuantityInStock() {
        System.out.println("getQuantityInStock");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country", 8850, 50);
        int expResult = 50;
        int result = instance.getQuantityInStock();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of getHumanFriendlyUnitPrice method, of class MSMStockItem.
     */
    @Test
    public void testGetHumanFriendlyUnitPrice() {
        System.out.println("getHumanFriendlyUnitPrice");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country", 8850, 50);
        String expResult = "88.50";
        String result = instance.getHumanFriendlyUnitPrice();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of setQuanity method, of class MSMStockItem.
     */
    @Test
    public void testSetQuanity() {
        System.out.println("setQuanity");
        int newQuantity = 0;
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country", 8850, 50);
        instance.setQuanity(newQuantity);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class MSMStockItem.
     */
    @Ignore
    @Test
    public void testToString() {
        System.out.println("toString");
        MSMStockItem instance = new MSMStockItem(1, "234567", "RunEverywhere                                               Great trainers for running cross country", 8850, 50);
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}

