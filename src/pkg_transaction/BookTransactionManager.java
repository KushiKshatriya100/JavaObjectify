package pkg_transaction;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;

public class BookTransactionManager {
	
ObjectInputStream ois_book_transaction = null;
ObjectOutputStream oos_book_transaction = null;
File book_transaction_file = null;
ArrayList<BookTransaction> book_transaction_list = null;
@SuppressWarnings("unchecked")
public BookTransactionManager() {
	book_transaction_file = new File("book_transaction.dat");
	book_transaction_list = new ArrayList<BookTransaction>();
	if(book_transaction_file.exists()) {
		try { 
			ois_book_transaction = new ObjectInputStream(new FileInputStream(book_transaction_file));
			book_transaction_list  = (ArrayList<BookTransaction>) ois_book_transaction.readObject();
		}
		catch(IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

public boolean issueBook(int rollNo,int isbn) {
	int total_books_issued= 0;
	for(BookTransaction book_transaction : book_transaction_list ) {
		if((book_transaction.getRollNo() == rollNo) && (book_transaction.getReturnDate()==null)){
			total_books_issued +=1;
		}
		if(total_books_issued >=3) {
			return false;
		}
	}
	String issue_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	BookTransaction book_transaction = new BookTransaction(isbn,rollNo, issue_date,null);
	book_transaction_list.add(book_transaction);
	writeToFile();
	return true;
}

public boolean returnBook(int rollNo , int isbn) {
	for(BookTransaction book_transaction : book_transaction_list) {
		if((book_transaction.getRollNo() == rollNo)&& (book_transaction.getReturnDate()==null) && (book_transaction.getIsbn()  == isbn)) {
			String return_date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			book_transaction.setReturnDate(return_date);
			writeToFile();
			return true;
		}
	}
	return false;
	
}

public void showAll() {
	for(BookTransaction book_transaction : book_transaction_list) {
		System.out.println(book_transaction);
	}
}

//public void writeToFile() {
//	try {
//		oos_book_transaction = new ObjectOutputStream(new FileOutputStream(book_transaction_file));
//		oos_book_transaction.writeObject(book_transaction_list);
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//}

public void writeToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(book_transaction_file))) {
        oos.writeObject(book_transaction_list);
        System.out.println("Data written to file successfully.");
    } catch (IOException e) {
        System.out.println("Error writing to file: " + e.getMessage());
        e.printStackTrace();
    }
}
}