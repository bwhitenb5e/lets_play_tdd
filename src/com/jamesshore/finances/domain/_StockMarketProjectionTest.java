package com.jamesshore.finances.domain;

import static org.junit.Assert.*;
import org.junit.*;

public class _StockMarketProjectionTest {

	private static final Year STARTING_YEAR = new Year(2010);
	private static final Year ENDING_YEAR = new Year(2050);
	private static final Dollars STARTING_BALANCE = new Dollars(10000);
	private static final Dollars COST_BASIS = new Dollars(7000);
	private static final GrowthRate GROWTH_RATE = new GrowthRate(10);
	private static final TaxRate CAPITAL_GAINS_TAX_RATE = new TaxRate(25);

	private static final StockMarketYear firstYear = new StockMarketYear(STARTING_YEAR, STARTING_BALANCE, COST_BASIS, GROWTH_RATE, CAPITAL_GAINS_TAX_RATE);
	
	@Test
	public void stockMarketContainsMultipleYears() {
		StockMarketProjection account = new StockMarketProjection(firstYear, ENDING_YEAR, new Dollars(0));
		assertEquals("# of years", 41, account.numberOfYears());
		assertEquals(STARTING_BALANCE, account.getYearOffset(0).startingBalance());
		assertEquals(new Dollars(11000), account.getYearOffset(1).startingBalance());
		assertEquals(new Dollars(12100), account.getYearOffset(2).startingBalance());
		assertEquals(new Year(2050), account.getYearOffset(40).year());
	}
	
	@Test
	public void stockMarketWithdrawsAStandardAmountEveryYear() {
		StockMarketProjection account = new StockMarketProjection(firstYear, ENDING_YEAR, new Dollars(10));
		assertEquals("year 0", new Dollars(10), account.getYearOffset(0).totalSellOrders());
		assertEquals("year 1", new Dollars(10), account.getYearOffset(1).totalSellOrders());
		assertEquals("year 40", new Dollars(10), account.getYearOffset(40).totalSellOrders());
	}
		
	@Test
	public void noCumulativeRoundingErrorInInterestCalculations() {
		StockMarketProjection account = new StockMarketProjection(STARTING_YEAR, ENDING_YEAR, STARTING_BALANCE, COST_BASIS, GROWTH_RATE, CAPITAL_GAINS_TAX_RATE, new Dollars(0));
		assertEquals(new Dollars(497852), account.getYearOffset(40).endingBalance());
	}
	
	@Test
	public void capitalGainsTaxCalculationWorksTheSameWayAsSpreadsheet() {
		StockMarketProjection account = new StockMarketProjection(STARTING_YEAR, ENDING_YEAR, STARTING_BALANCE, COST_BASIS, GROWTH_RATE, CAPITAL_GAINS_TAX_RATE, new Dollars(695));
		assertEquals(new Dollars(2067), account.getYearOffset(40).endingBalance());
	}
	
}
