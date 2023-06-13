package hodu.bookstore.model.dto;

import java.util.ArrayList;
import java.util.List;

public class Book {
	
	// 도서 번호, 책 이름, 저자, 가격
	
	private int booknumber;
	private String bookname;
	private String author;
	private int price;
	
	private List<Book> bookList = new ArrayList<>();
	
	
	public Book() {
		super();
	}


	public Book(int booknumber, String bookname, String author, int price) {
		super();
		this.booknumber = booknumber;
		this.bookname = bookname;
		this.author = author;
		this.price = price;
	}


	public int getBookNumber() {
		return booknumber;
	}


	public void setBookNumber(int booknumber) {
		this.booknumber = booknumber;
	}


	public String getBookName() {
		return bookname;
	}


	public void setBookName(String bookname) {
		this.bookname = bookname;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}
	
	

    public List<Book> getBookList() {
        return bookList;
    }

	
//	public void bookList(Book book) {
//		// TODO Auto-generated method stub
//		this.bookList.add(book);
//	}


	@Override
	public String toString() {
		return "Book [booknumber=" + booknumber + ", bookname=" + bookname + ", author=" + author + ", price="
				+ price + "]";
	}
	
}
