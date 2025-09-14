package tech.zeta.service;

import lombok.extern.slf4j.Slf4j;
import tech.zeta.entity.enums.TableStatus;
import tech.zeta.repository.TableRepository;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class TableService {
  private TableRepository tableRepository;
  private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
  public TableService(){
    tableRepository = TableRepository.getInstance();
  }

  public synchronized boolean  bookTable(int customerId, int tableId){
    if((tableRepository.giveTableStatus(tableId) == TableStatus.AVAILABLE) && tableRepository.bookTable(tableId, customerId)){
        scheduler.schedule(() -> releaseIfNotAttended(tableId), 20, TimeUnit.MINUTES);
        return true;
    }
    return false;
  }

  public void releaseIfNotAttended(int tableId){
    if(tableRepository.giveTableStatus(tableId) == TableStatus.BOOKED){
      tableRepository.updateTableStatus(tableId, TableStatus.AVAILABLE, null);
    }
  }

  public List<int[]> giveAvailableTables(){
    return tableRepository.giveAvailableTables();
  }

  public boolean checkTableIsBookedOrOccupied(int tableId){
    TableStatus tableStatus =  tableRepository.giveTableStatus(tableId);
    return tableStatus == TableStatus.BOOKED || tableStatus == TableStatus.OCCUPIED;
  }

  public int giveCustomerId(int tableId){
    return checkTableIsBookedOrOccupied(tableId) ? tableRepository.getCustomerId(tableId) : -1;
  }

}
