package bank

import time.Date
import java.nio.file.*
import java.nio.charset.StandardCharsets
import scala.io.Source.*


/**
 * A bank with no accounts and no history.
 */
class Bank():
  private var accounts = Vector.empty[BankAccount]
  private var historyentries = List.empty[HistoryEntry]
  val file = scala.io.Source.fromFile("/Users/nilsr/pgk/bank/banklog.txt")
 /**
   * Returns a list of every bank account in the bank.
   * The returned list is sorted in alphabetical order based
   * on customer name.
   */
  def allAccounts(): Vector[BankAccount] = 
    accounts.sortWith(_.holder.name > _.holder.name)

  /**
   * Returns the account holding the provided account number.
   */
  def findByNumber(accountNbr: Int): Option[BankAccount] = 
    accounts.find(_.accountNumber == accountNbr)

  /**
   * Returns a list of every account belonging to
   * the customer with the provided id.
   */
  def findAccountsForHolder(id: Long): Vector[BankAccount] = 
    accounts.filter(_.holder.id == id)

  /**
   * Returns a list of all customers whose names match
   * the provided name pattern.
   */
  def findByName(namePattern: String): Vector[Customer] = 
    val found_accounts = accounts.filter(account => account.holder.name.contains(namePattern))
    found_accounts.map(account => account.holder)

  def addAccount(holder: Customer): Unit =
    val account = BankAccount(holder)
    accounts = accounts :+ account
  /**
   * Executes an event in the bank.
   * Returns a string describing whether the
   * event was successful or failed.
   */

  def storeEvent(event: BankEvent): Unit =
    val entry = HistoryEntry(Date.now(), event)
    historyentries = historyentries :+ entry
    writetoFile(entry.toLogFormat)

  def doEvent(event: BankEvent): String = 
    event match{
      case Deposit(account, amount) => 
        accounts.find(_.accountNumber == account) match {
          case Some(foundAccount) => 
            foundAccount.deposit(amount)
            storeEvent(event)
            event.toNaturalFormat
            
          case None => "Kontot fanns inte, försök igen." 
        }         
      case Withdraw(account, amount) => 
        accounts.find(_.accountNumber == account) match {
          case Some(foundAccount) => 
            if foundAccount.withdraw(amount) then
              storeEvent(event)
              event.toNaturalFormat

            else "Otillräckligt saldo."
          case None => "Kontot fanns inte, försök igen."
        }
      case Transfer(accFrom, accTo, amount) =>
        accounts.find(_.accountNumber == accFrom) match {
          case Some(fromAccount) =>
            accounts.find(_.accountNumber == accTo) match {
              case Some(toAccount) =>
                if (fromAccount.withdraw(amount)) {
                  toAccount.deposit(amount)
                  storeEvent(event)
                  event.toNaturalFormat
                } 
                else "Överföring misslyckades"

          case None => "Error: mottagarens konto hittades inte"
        }
        case None => "Error: avsändarens konto hittades inte"
        }
      case NewAccount(id, name) => 
        val customer = Customer(name, id)
        addAccount(customer)
        storeEvent(event)
        event.toNaturalFormat
      case DeleteAccount(account) =>
        accounts.find(_.accountNumber == account) match {
          case Some(foundAccount) =>
            accounts = accounts.filterNot(_.accountNumber == account)
            storeEvent(event)
            event.toNaturalFormat
          case None => "Error: kontot hittades inte"
  }

    }

  /**
   * Returns a log of all changes to the bank's state.
   */
  def history(): Vector[HistoryEntry] = 
    historyentries.toVector
  
  def writetoFile(entry: String): Unit =
    val path = Paths.get("/Users/nilsr/pgk/bank/banklog.txt")
    Files.write(path, (entry + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE)
  def loadfromFile(): Unit =    
    val history: List[HistoryEntry] = file.getLines().toList.map(line => HistoryEntry.fromLogFormat(line))
    history.foreach(entry => doEvent(entry.event))

  /**
   * Resets the bank to the state it had at the provided date.
   * Returns a string describing whether the event was
   * successful or failed.
  */
  def returnToState(returnDate: Date): String = 
    var dates = file.getLines().toList.map(line => HistoryEntry.fromLogFormat(line))
    dates = dates.filter(_.date.compare(returnDate) < 1) 
    accounts = accounts.empty
    "hej"
