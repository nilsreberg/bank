package bank
import scala.util.Random
import time.Date

object BankApplication{
    val bank = new Bank
    val random = new Random()
    def main(args: Array[String]): Unit =
        bank.loadfromFile()
        while true do
        {
            println("1. Hitta konton för en given kund")
            println("2. Sök efter kunder på (del av) namn")
            println("3. Sätt in pengar.")
            println("4. Ta ut pengar.")
            println("5. Överför pengar mellan konton.")
            println("6. Skapa nytt konto.")
            println("7. Radera existerande konton.")
            println("8. Skriv ut alla konton i banken.")
            println("9. Skriv ut ändringshistoriken.")
            println("10. Återställ banken till ett tidigare datum.")
            println("11. Avsluta")

            val input = scala.io.StdIn.readInt()

            input match {
                case 1 => findAccount()
                case 2 => findCustomer()
                case 3 => deposit()
                case 4 => withdrawal()
                case 5 => transfer()
                case 6 => createAccount()
                case 7 => deleteAccount()
                case 8 => showAccounts()
                case 9 => showHistory()
                //case 10 => returnToState()
                case 11 => return
                case _ => println("Invalid input, try again.")
            }
        }
    
    def findAccount(): Unit = 
        println("Sök efter ett konto för en given kunds ID-nummer: ")
        val input = scala.io.StdIn.readInt()
        val foundAccounts = bank.findAccountsForHolder(input)
        foundAccounts.foreach(println)
        println(Date.now().toNaturalFormat)
        

    def findCustomer(): Unit =
        println("Ange kundens namn: ")
        val input = scala.io.StdIn.readLine()
        val foundNames = bank.findByName(input)
        foundNames.foreach(println)
        println(Date.now().toNaturalFormat)
        
    
    def deposit(): Unit = 
        println("Ange bankkontots nummer: ")
        val accountNumber = scala.io.StdIn.readInt()
        println("Ange summa: ")
        val depositAmount = scala.io.StdIn.readInt()
        println(bank.doEvent(Deposit(accountNumber, depositAmount)))
        println(Date.now().toNaturalFormat)
        
    def withdrawal(): Unit =
        println("Ange bankkontots nummer: ")
        val accountNumber = scala.io.StdIn.readInt()
        println("Ange hur mycket du vill ta ut: ")
        val withdrawalNumber = scala.io.StdIn.readInt()
        println(bank.doEvent(Withdraw(accountNumber, withdrawalNumber)))
        println(Date.now().toNaturalFormat)
        
    def transfer(): Unit =
        println("Ange konto att föra över från: ")
        val fromAccount = scala.io.StdIn.readInt()
        println("Ange konto att föra över till: ")
        val toAccount = scala.io.StdIn.readInt()
        println("Ange summa: ")
        val transferAmount = scala.io.StdIn.readInt()
        println(bank.doEvent(Transfer(fromAccount, toAccount, transferAmount)))
        println(Date.now().toNaturalFormat)
        
    def createAccount(): Unit =
        println("Ange för- och efternamn: ")
        val name = scala.io.StdIn.readLine()
        var id: Long = random.nextLong(math.pow(10, 10).toLong)
        while !bank.findAccountsForHolder(id).isEmpty do
            id = random.nextLong(math.pow(10, 10).toLong)
        println(bank.doEvent(NewAccount(id, name)))
        println(Date.now().toNaturalFormat)
        
    def deleteAccount(): Unit =
        println("Ange kontonummer att radera: ")
        val accountNumber = scala.io.StdIn.readInt() 
        println(bank.doEvent(DeleteAccount(accountNumber)))
        println(Date.now().toNaturalFormat)
    
    def showAccounts(): Unit =
        bank.allAccounts().foreach(println)
        println(Date.now())
    
    def showHistory(): Unit = 
        bank.history().foreach(entry => println(s"${entry.toNaturalFormat} \n"))
        println(Date.now().toNaturalFormat)
    
}
