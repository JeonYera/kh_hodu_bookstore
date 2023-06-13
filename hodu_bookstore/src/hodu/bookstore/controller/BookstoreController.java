package hodu.bookstore.controller;

import java.util.List;

import hodu.bookstore.model.dao.BookstoreDao;
import hodu.bookstore.model.dto.Book;
import hodu.bookstore.model.dto.Member;
import hodu.bookstore.model.service.BookstoreService;

public class BookstoreController {

	private BookstoreService bookstoreService = new BookstoreService();
	private BookstoreDao bookstoreDao = new BookstoreDao();


	// 도서 검색
	public List<Book> searchBook(String title) {
		List<Book> bookList = bookstoreService.searchBook(title);
		return bookList;
	}

	public Member getPoint(String id) {
		Member member = null;
		try {
			member = bookstoreService.getPoint(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}

	// 도서 전체 조회
	public List<Book> findbook() {
		List<Book> bookList = null;
		try {
			bookList = bookstoreService.findbook();
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return bookList;
	}

	// 멤버 전체 조회
	public List<Member> findmember() {
		List<Member> memberList = null;
		try {
			memberList = bookstoreService.findmember();
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return memberList;
	}


	// 사용자 등록
	public int insertMember(Member member) {
		int result = 0;
		try {
			result = bookstoreService.insertMember(member);
		}  catch (Exception e) {
			System.err.println(" 관리자에게 문의하세요 ");
			e.printStackTrace();
		}
		return result;
	}

	// 관리자 도서 등록
	public int insertBook(Book book) {
		int result = 0;
		try {
			result = bookstoreService.insertBook(book);
		}  catch (Exception e) {
			System.err.println(" 관리자에게 문의하세요 ");
			e.printStackTrace();
		}
		return result;
	}


	// 구매할 때 포인트 차감
	public int updatePoint(String id, int bookPrice) {
		int result = 0;
		try {
			result = bookstoreService.updatePoint(id, bookPrice);
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	// 사용자 포인트 충전
	public int earnPoint(String id, int amount) {
		int result = 0;
		try {
			result = bookstoreService.earnPoint(id, amount);
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	// 사용자 도서 구매
	public int bookstoreBuy(String bookname) {
		int result = 0;
		try {
			result = bookstoreService.bookstoreBuy(bookname);
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}


	// 관리자 도서 삭제
	public int deleteBook(String title) {
		int result = 0;
		try {
			result = bookstoreService.deleteBook(title);
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}


	// 관리자 사용자 정보 수정
	public Member findById(String id) {
		return BookstoreService.findById(id);
	}

	// 관리자 사용자 수정
	public static int updateMember(String id, String colName, Object newValue) {
		return BookstoreService.updateMember(id, colName, newValue);
	}

	// 관리자 도서 정보 수정
	public Book findByBook(String bookname) {
		return BookstoreService.findByBook(bookname);
	}

	// 관리자 도서 수정
	public static int updateBook(String bookname, String colName, Object newValue) {
		return BookstoreService.updateBook(bookname, colName, newValue);
	}

	// 관리자 사용자 삭제
	public int deleteMember(String Id) {
		int result = 0;
		try {
			result = bookstoreService.deleteMember(Id);
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

}


