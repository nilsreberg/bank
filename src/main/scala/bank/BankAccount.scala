package bank
import java.util.Random
/**
 * A bank account for hold by a specific customer.
 * The account is given a unique account number and initially
 * has a balance of 0 kr.
 */
class BankAccount(val holder: Customer):
  val random = new Random()
  val accountNumber: Int = 

  private var _balance: BigInt = 0
  /**
   * Deposits the provided amount in this account.
   */
  def deposit(amount: BigInt): Unit = _balance = _balance + amount

  /**
   * Returns the balance of this account.
   */
  def balance: BigInt = _balance

  /**
   * Withdraws the provided amount from this account,
   * if there is enough money in the account. Returns true
   * if the transaction was successful, otherwise false.
   */
  def withdraw(amount: BigInt): Boolean = 
    if amount <= _balance then 
      _balance = _balance - amount
      true
    else false
  

  override def toString(): String = s"Account $accountNumber (${holder.name}, ID ${holder.id}) \n Saldo : $balance"