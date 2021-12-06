package com.solvd.financialIntitution;

import com.solvd.financialIntitution.collections.MyLinkedList;
import com.solvd.financialIntitution.enums.Days;
import com.solvd.financialIntitution.enums.LoanStatus;
import com.solvd.financialIntitution.exceptions.*;
import com.solvd.financialIntitution.generics.BankDAO;
import com.solvd.financialIntitution.generics.PersonDAO;
import com.solvd.financialIntitution.generics.UnionDAO;
import com.solvd.financialIntitution.models.CentralBank;
import com.solvd.financialIntitution.models.savingsAndLoansBanks.SavingsAndLoansService;
import com.solvd.financialIntitution.models.commercialBanks.*;
import com.solvd.financialIntitution.models.creditUnions.*;
import com.solvd.financialIntitution.models.customers.*;
import com.solvd.financialIntitution.models.employees.Employee;
import com.solvd.financialIntitution.models.insuranceCompanies.InsuranceCompany;
import com.solvd.financialIntitution.models.investmentBanks.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class Main {
    public static MyLinkedList myLinkedList;
    public static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws IOException, ParseException {

        myLinkedList = new MyLinkedList();


        myLinkedList.add("1");
        myLinkedList.add("4");

        System.out.println("linked list: " + myLinkedList);

        ArrayList<String> objects = new ArrayList<String>();

        Date expireDate = DateUtils.parseDate("2030/06/13", "yyyy/MM/dd");


        CentralBank insuranceCompany = new InsuranceCompany(458778, "Security insurance", 45000, 5400.00, 120000.00);
        CentralBank investmentBank = new InvestmentService(878765, "Brothers investment", 12000, 00, 00);
        CentralBank savingsAndLoansService = new SavingsAndLoansService(554541, "National Cooperative", 15000.00, 00,
                0);
        CentralBank internetBank = new InternetBank(4848775, "World Wide Bank", 0, "www.publicurl.com", 2500.00);
        CentralBank brokerageFirms = new BrokerageFirms(5565, "O-Toro", 0.0, 1500.00, "Google");
        CentralBank commercialBank = new CommercialBank(454547, "National Bank", 5000, 450.00, 54581211, objects);
        CommercialBank creditCard = new CreditCard((CommercialBank) commercialBank, 454874545, expireDate, 565);
        CentralBank creditUnion = new CreditUnion(4545877, "Brothers Credits", 36000.00, 4500.00);

        Person javier = new Employee("javier", 45877442, "Teller");
        Person sancor = new Enterprise("Sancor S.A", 555211544, 2000);
        Person leandro = new Customer("Leandro", 39448090, "Tambourine", false, true, false);

        System.out.println(
                insuranceCompany + "\n" + investmentBank + "\n" + savingsAndLoansService + "\n" + internetBank + "\n"
                       + brokerageFirms + "\n" + commercialBank + "\n" + creditCard + "\n" + creditUnion + "\n");
        // "//***************************************************************************\\"
        System.out.println(javier + "\n" + sancor + "\n" + leandro);

        BankDAO<CommercialBank> commercialBankGeneric = new BankDAO<>();
        commercialBankGeneric.editName((CommercialBank) commercialBank, "Nation Bank");
        commercialBankGeneric.getName((CommercialBank) commercialBank);
        commercialBankGeneric.getIdAccount((CommercialBank) commercialBank);

        PersonDAO<Customer> customerPersonGeneric = new PersonDAO<>();
        customerPersonGeneric.updateName((Customer) leandro, "Leandro Rodolfo");
        customerPersonGeneric.getName((Customer) leandro);

        UnionDAO<CommercialBank, Customer> unionGeneric = new UnionDAO<>();
        System.out.println(unionGeneric.makeUnion((CommercialBank) commercialBank, (Customer) leandro));

        try {
            askForALoan(commercialBank, savingsAndLoansService, javier, leandro);
        } catch (IsNotANumberException e) {
            log.info("you must put an number in this field, please be careful");
            throw new IsNotANumberException("What did you type?");
        } catch (LowLoanException e) {
            log.info("we can't give you a loan under $600");
            throw new LowLoanException("The minimum loan is $600");
        } catch (IllegalObjectsInSecurityBox e) {
            log.info("Sorry we cannot allow those objects, you can go to jail and so am we");
            throw new IllegalObjectsInSecurityBox("That object is not allowed");
        }

    }


    public static void askForALoan(CentralBank commercialBank, CentralBank savingsAndLoansService, Person employee, Person customer) throws IOException {
        Days day = Days.MONDAY;
        if (day.equals(Days.SATURDAY) || day.equals(Days.SUNDAY)) {
            System.out.println("operations are closed");
        } else if (day.openingTime() < 7.00 || day.closingTime() > 13.30) {
            System.out.println("operations are closed");
        } else {
            //log.info("welcome, please select where do you are asking for a loan");
            System.out.println("welcome, please select where do you are asking for a loan");
            String op;
            do {
                op = "";
                System.out.println("Type 1 for a commercial bank");
                System.out.println("Type 2 for savings and loans service");
                System.out.println("Type 0 to quit");
                Scanner in = new Scanner(System.in);
                op = in.nextLine();
                switch (Integer.parseInt(op)) {
                    case 1:
                        //log.info("okay!, Please type how much do you need");
                        System.out.println("okay!, Please type how much do you need");
                        String amount = "";
                        amount = in.nextLine();
                        boolean isNumeric = amount.matches("[+-]?\\d*(\\.\\d+)?");
                        if (!isNumeric) {
                            throw new IsNotANumberException("What did you type?");
                        }
                        if (Double.parseDouble(amount) <= 600) {
                            throw new LowLoanException("The minimum loan is $600");
                        }
                        if (Objects.isNull(Double.parseDouble(amount))) {
                            throw new IsANullNumberException("The number is null");
                        }

                        System.out.println(customer.talk());
                        System.out.println(employee.talk());
                        System.out.println(commercialBank.talk());
                        System.out.println("Before continue please read the terms and conditions");
                        readDocs();
                        LoanStatus status = LoanStatus.TODO;
                        System.out.println("status: " + status.status());
                        if (commercialBank.giveLoan(Double.parseDouble(amount))) {
                            status = LoanStatus.IN_PROGRESS;
                            try {
                                ((CommercialBank) commercialBank).setSavingsAccount(Double.parseDouble(amount));

                            } catch (NoHierarchyException e) {
                                throw new NoHierarchyException("That class cannot be casted");
                            }

                            commercialBank.validate();
                            status = LoanStatus.DONE;
                            System.out.println("congrats you have $" + ((CommercialBank) commercialBank).getSavingsAccount()
                                    + " in your account");
                            Date now = new Date();
                            Date giveBack = DateUtils.addMonths(now,3);
                            System.out.println("You must give back $" + commercialBank.applyTax(Double.parseDouble(amount))
                                    + "in " + giveBack);
                        } else {
                            commercialBank.deny();
                            status = LoanStatus.CANCELED;
                            System.out.println("Sorry we couldn't give you a loan");
                            System.out.println("do you want to put some objects in your security box?");
                            String answer = in.nextLine();
                            if (answer.equals("yes")) {
                                int op1;
                                do {
                                    op1 = in.nextInt();
                                    System.out.println("type 1 if you want to put an object");
                                    System.out.println("type 0 if you want to quit");
                                    String object = "";
                                    switch (op1) {
                                        case 1:
                                            LoanStatus boxStatus = LoanStatus.TODO;
                                            System.out.println("What do you want to put?");
                                            object = in.nextLine();
                                            boxStatus = LoanStatus.IN_PROGRESS;
                                            if (object.equals("gun") || object.equals("drug")
                                                    || object.equals("another security box")) {
                                                boxStatus = LoanStatus.CANCELED;
                                                throw new IllegalObjectsInSecurityBox("That object is not allowed");
                                            } else {
                                                ((CommercialBank) commercialBank).setSecurityBox(object);
                                                boxStatus = LoanStatus.DONE;
                                                System.out.println(
                                                        "Thanks you have " + ((CommercialBank) commercialBank).getSecurityBox()
                                                                + " in your security box");
                                            }
                                            break;
                                        case 0:
                                            System.out.println("Thank you for choosing us!!");
                                            break;
                                        default:
                                            System.out.println("wrong typed");
                                            break;
                                    }

                                } while (op1 != 0);
                            }
                        }
                        break;

                    case 2:
                        System.out.println("okay!, Please type how much do you need");
                        String amount1 = in.nextLine();
                        isNumeric = amount1.matches("[+-]?\\d*(\\.\\d+)?");
                        if (!isNumeric) {
                            throw new IsNotANumberException();
                        }
                        if (Double.parseDouble(amount1) <= 0) {
                            throw new LowLoanException();
                        }
                        if (Objects.isNull(Double.parseDouble(amount1))) {
                            throw new IsANullNumberException("The number is null");
                        }
                        System.out.println(customer.talk());
                        System.out.println(employee.talk());
                        System.out.println(savingsAndLoansService.talk());
                        if (savingsAndLoansService.giveLoan(Double.parseDouble(amount1))) {
                            ((SavingsAndLoansService) savingsAndLoansService).setSavingsAccount(Double.parseDouble(amount1));
                            savingsAndLoansService.validate();
                            System.out.println("congrats you have $"
                                    + ((SavingsAndLoansService) savingsAndLoansService).getSavingsAccount()
                                    + " in your account");
                            Date now = new Date();
                            Date giveBack = DateUtils.addMonths(now,3);
                            System.out.println("You must give back $"
                                    + savingsAndLoansService.applyTax(Double.parseDouble(amount1)) + "in " + giveBack);
                        } else {
                            savingsAndLoansService.deny();
                            System.out.println("Sorry we couldn't give you a loan");
                        }
                        break;
                    case 0:
                        System.out.println("Thanks for choosing us");
                        break;
                    default:
                        System.out.println("Wrong election, please type again");
                        break;

                }

            } while (Integer.parseInt(op) != 0);
        }
    }

    public static boolean readDocs() throws IOException {
        File terms = new File("E:\\Qa Automation Course Solvd\\HomeTask 2\\src\\main\\java\\com\\solvd\\financialIntitution\\docs\\terms-and-conditions.txt");

        System.out.println(terms);

        String lines = String.valueOf(FileUtils.readLines(terms));


        StringUtils.countMatches(lines, "loan");
        StringUtils.countMatches(lines, "fees");
        StringUtils.countMatches(lines, "cancel");

        File newFile = new File("E:\\Qa Automation Course Solvd\\HomeTask 2\\src\\main\\java\\com\\solvd\\financialIntitution\\docs\\are-you-sure.txt");
        FileUtils.touch(newFile);

        String writeFile = "Be careful of what you sing \n" +
                "there are many dangerous words in these terms \n" +
                "for instance: \n" +
                StringUtils.join("loan:   ", StringUtils.countMatches(lines, "loan")) + "\n" +
                StringUtils.join("fees:   ", StringUtils.countMatches(lines, "fees")) + "\n" +
                StringUtils.join("cancel:   ", StringUtils.countMatches(lines, "cancel"));

        FileUtils.writeStringToFile(newFile, writeFile);

        System.out.println("Are you sure that you want to sign?");
        System.out.println(newFile);

        Scanner sure = new Scanner(System.in);
        sure.nextLine();
        if (sure.equals("yes")) return true;
        else return false;


    }

}
