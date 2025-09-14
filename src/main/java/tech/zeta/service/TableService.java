package tech.zeta.service;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.utils.enums.TableStatus;
import tech.zeta.repository.TableRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service class for table management.
 * Handles table booking, automatic release of un-attended tables,
 * checking table status, and retrieving available tables.
 * Uses ScheduledExecutorService to manage booking timeouts.
 */
@Slf4j
public class TableService {
  private TableRepository tableRepository;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
  public TableService(){
    tableRepository = TableRepository.getInstance();
  }

  /**
   * Books a table for a specific customer if it is available.
   * Schedules automatic release after 20 minutes if not attended.
   *
   * @param customerId the ID of the customer
   * @param tableId the ID of the table to book
   * @return true if the table is successfully booked, false otherwise
   */
  public synchronized boolean  bookTable(int customerId, int tableId){
    if((tableRepository.giveTableStatus(tableId) == TableStatus.AVAILABLE) && tableRepository.bookTable(tableId, customerId)){
        scheduler.schedule(() -> releaseIfNotAttended(tableId), 20, TimeUnit.MINUTES);
        return true;
    }
    return false;
  }

  /**
   * Releases a table if it has been booked but not attended by the customer.
   *
   * @param tableId the ID of the table to release
   */
  public void releaseIfNotAttended(int tableId){
    if(tableRepository.giveTableStatus(tableId) == TableStatus.BOOKED){
      tableRepository.updateTableStatus(tableId, TableStatus.AVAILABLE, 0);
    }
  }

  /**
   * Retrieves a list of all currently available tables.
   *
   * @return a list of integer arrays representing available tables
   */
  public List<int[]> giveAvailableTables(){
    return tableRepository.giveAvailableTables();
  }

  /**
   * Checks whether a table is currently booked or occupied.
   *
   * @param tableId the ID of the table
   * @return true if the table is booked or occupied, false otherwise
   */
  public boolean checkTableIsBookedOrOccupied(int tableId){
    TableStatus tableStatus =  tableRepository.giveTableStatus(tableId);
    return tableStatus == TableStatus.BOOKED || tableStatus == TableStatus.OCCUPIED;
  }

  /**
   * Retrieves the customer ID associated with a booked or occupied table.
   *
   * @param tableId the ID of the table
   * @return the customer ID if the table is booked/occupied, -1 otherwise
   */
  public int giveCustomerId(int tableId){
    return checkTableIsBookedOrOccupied(tableId) ? tableRepository.getCustomerId(tableId) : -1;
  }

}
