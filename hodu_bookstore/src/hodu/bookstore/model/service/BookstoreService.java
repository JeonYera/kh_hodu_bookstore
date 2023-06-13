package hodu.bookstore.model.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static hodu.common.JdbcTemplate.close;
import static hodu.common.JdbcTemplate.commit;
import static hodu.common.JdbcTemplate.getConnection;
import static hodu.common.JdbcTemplate.rollback;

import hodu.bookstore.model.dao.BookstoreDao;
import hodu.bookstore.model.dto.Book;
import hodu.bookstore.model.dto.Member;
import hodu.bookstore.model.exception.BookstoreException;
import hodu.bookstore.model.exception.MemberException;
import oracle.net.jdbc.TNSAddress.Address;

public class BookstoreService {

	BookstoreDao bookstoreDao = new BookstoreDao();

	// 관리자 사용자 등록
	public int insertMember(Member member) {
		int result = 0;
		// 1. Connection 가져오기
		Connection conn = getConnection();
		try {
			// 2. Dao 호출
			result = bookstoreDao.insertMember(conn, member);
			List<Member> memberList = member.getMemberList();
			if(!memberList.isEmpty()) {
				Member members = memberList.get(0);
				result = bookstoreDao.insertMember(conn, members);
			}

			// 3. 트랜잭션 처리
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e; // controller에 예외사실 알림
		} finally {
			// 4. 자원반납
			close(conn);
		}
		return result;
	}

	// 관리자 도서 등록
	public int insertBook(Book book) throws Exception {
		int result = 0;
		// 1. Connection 가져오기
		Connection conn = getConnection();
		try {
			// 2. Dao 호출
			result = bookstoreDao.insertBook(conn, book);
			List<Book> bookList = book.getBookList();
			if(!bookList.isEmpty()) {
				Book books = bookList.get(0);
				result = bookstoreDao.insertBook(conn, books);
			}

			// 3. 트랜잭션 처리
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e; // controller에 예외사실 알림
		} finally {
			// 4. 자원반납
			close(conn);
		}
		return result;
	}

	// 일반 사용자 도서 전체 조회
	public List<Book> findbook() throws BookstoreException{
		Connection conn = getConnection();
		List<Book> findbook = bookstoreDao.findbook(conn);

		close(conn);
		return findbook;
	}

	// 일반 사용자 전체 검색
	public List<Member> findmember() throws BookstoreException{
		Connection conn = getConnection();
		List<Member> findmember = bookstoreDao.findmember(conn);

		close(conn);
		return findmember;
	}


	// 관리자 도서 구매
	public int bookstoreBuy(String bookname) throws BookstoreException {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = bookstoreDao.bookstoreBuy(conn, bookname);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	// 관리자 도서 삭제
	public int deleteBook(String title) throws Exception {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = bookstoreDao.deleteBook(conn, title);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}


	// 사용자 포인트 충전
	public int earnPoint(String id, int point) throws Exception {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = bookstoreDao.earnPoint(conn, id, point);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);         
		}
		return result;
	}

	// 사용자 포인트 조회
	public Member getPoint(String id) throws Exception {
		Connection conn = getConnection();
		Member member = null;
		try {
			member = bookstoreDao.getPoint(conn, id);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);         
		}
		return member;
	}

	// 도서 일부 검색
	public List<Book> searchBook(String title) {
		Connection conn = getConnection();
		List<Book> bookList = bookstoreDao.searchBook(conn, title);
		close(conn);
		return bookList;
	}

	// 관리자 사용자 수정
	public static Member findById(String id) {
		Connection conn = getConnection();
		Member member = BookstoreDao.findById(conn, id);
		close(conn);
		return member;
	}

	// 관리자 사용자 정보 수정
	public static int updateMember(String id, String colName, Object newValue) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = BookstoreDao.updateMember(conn, id, colName, newValue);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	// 관리자 도서 수정

	public static Book findByBook(String bookname) {
		Connection conn = getConnection();
		Book book = BookstoreDao.findByBook(conn, bookname);
		close(conn);
		return book;
	}

	// 관리자 도서 정보 수정
	public static int updateBook(String bookname, String colName, Object newValue) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = BookstoreDao.updateBook(conn, bookname, colName, newValue);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

	// 도서 구매 포인트 차감 
	public int updatePoint(String id, int bookPrice) {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = BookstoreDao.updatePoint(conn, id, bookPrice);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}

		return result;
	}

	// 관리자 사용자 삭제
	public int deleteMember(String Id) throws Exception {
		Connection conn = getConnection();
		int result = 0;
		try {
			result = bookstoreDao.deleteMember(conn, Id);
			commit(conn);
		} catch (Exception e) {
			rollback(conn);
			throw e;
		} finally {
			close(conn);
		}
		return result;
	}

}




