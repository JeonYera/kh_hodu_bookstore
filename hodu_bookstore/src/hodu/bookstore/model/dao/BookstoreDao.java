package hodu.bookstore.model.dao;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import hodu.bookstore.model.dto.Book;
import hodu.bookstore.model.dto.Member;
import hodu.bookstore.model.exception.BookstoreException;
import hodu.bookstore.model.exception.MemberException;

public class BookstoreDao {

	private Properties prop = new Properties();

	public BookstoreDao() {
		// resources/member-query.properties 파일을 읽어오기
		try {
			prop.load(new FileReader("resources/book-query.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 도서 등록
	public int insertBook(Connection conn, Book book){
		String sql = prop.getProperty("insertBook");
		int result = 0;

		// 1. PreparedStatement 생성 & 미완성쿼리 값대입
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, book.getBookNumber());
			pstmt.setString(2, book.getBookName());
			pstmt.setString(3, book.getAuthor());
			pstmt.setInt(4, book.getPrice());

			// 2. 쿼리실행 PreparedStatement#executeUpdate:int (처리된 행의 수)
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// 예외가 발생하면 서비스로 던져서 예외발생여부를 알린다.
			// 발생한 예외를 새로운 예외(현재예외상황을 더 잘 설명할수 있는, 다루기 편한 Unchecked)로 전환해서 던진다.
			throw new MemberException("도서 등록 오류", e); 
		}
		return result;
	}

	// 사용자 등록
	public int insertMember(Connection conn, Member member) {
		String sql = prop.getProperty("insertMember");
		int result = 0;

		// 1. PreparedStatement 생성 & 미완성쿼리 값대입
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhoneNumber());
			pstmt.setString(6, member.getAddress());

			// 2. 쿼리실행 PreparedStatement#executeUpdate:int (처리된 행의 수)
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			// 예외가 발생하면 서비스로 던져서 예외발생여부를 알린다.
			// 발생한 예외를 새로운 예외(현재예외상황을 더 잘 설명할수 있는, 다루기 편한 Unchecked)로 전환해서 던진다.
			throw new MemberException("회원가입오류", e); 
		}


		return result;
	}

	// 관리자 도서 삭제
	public int deleteBook(Connection conn, String title) throws SQLException {
		String sql = "delete from book where bookname = ?"; // 처음에 delete *로 시도했으나 쿼리문에서 오류가 발생
		int result = 0;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, title);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}

	// 사용자 전체 도서 조회
	public List<Book> findbook(Connection conn) throws BookstoreException {
		List<Book> bookList = new ArrayList<>();
		String sql = prop.getProperty("findbook");

		// 1. PreparedStatement 생성 & 미완성쿼리 값대입
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){

			// 2. 쿼리 실행 PreapredStatement#executeQuery:ResultSet
			try(ResultSet rset = pstmt.executeQuery()){

				// 3. ResultSet 처리 : 행 -> dto객체
				while(rset.next()) {
					// 한행의 컬럼값을 가져와 Member객체로 변환 
					// 매개변수생성자 | 기본생성자 + setter
					Book book = handleBookResultSet(rset);

					// memberList에 추가
					bookList.add(book);
				}

			}

		} catch (SQLException e) {
			throw new BookstoreException("도서 조회 오류", e);
		}


		return bookList;
	}


	// 관리자 전체 회원 조회
	public List<Member> findmember(Connection conn) throws MemberException {
		List<Member> memberList = new ArrayList<>();
		String sql = prop.getProperty("findmember");

		// 1. PreparedStatement 생성 & 미완성쿼리 값대입
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){

			// 2. 쿼리 실행 PreapredStatement#executeQuery:ResultSet
			try(ResultSet rset = pstmt.executeQuery()){

				// 3. ResultSet 처리 : 행 -> dto객체
				while(rset.next()) {
					// 한행의 컬럼값을 가져와 Member객체로 변환 
					// 매개변수생성자 | 기본생성자 + setter
					Member member = handleMemberResultSet(rset);

					// memberList에 추가
					memberList.add(member);
				}

			}

		} catch (SQLException e) {
			throw new MemberException("전체 회원 조회 오류", e);
		}

		return memberList;
	}

	// 도서 검색
	public Book findBybook(Connection conn, String bookname) {
		Book book = null;
		String sql = "select * from book where id = ?";


		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, bookname);
			try(ResultSet rset = pstmt.executeQuery();){
				if(rset.next())
					book = handleBookResultSet(rset);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return book;
	}

	// 관리자 사용자 조회
	private static Member handleMemberResultSet(ResultSet rset) throws SQLException {
		Member member = new Member();
		member.setId(rset.getString("Id"));
		member.setPassword(rset.getString("Password"));
		member.setName(rset.getString("Name"));
		member.setEmail(rset.getString("Email"));
		member.setPhoneNumber(rset.getString("PhoneNumber"));
		member.setAddress(rset.getString("Address"));
		member.setPoints(rset.getInt("Points"));
		return member;
	}

	// 도서 전체 조회
	private static Book handleBookResultSet(ResultSet rset) {
		Book book = new Book();
		try {
			book.setBookNumber(rset.getInt("booknumber"));
			book.setBookName(rset.getString("bookname"));
			book.setAuthor(rset.getString("author"));
			book.setPrice(rset.getInt("price"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return book;
	}

	// 도서 구매
	public int bookstoreBuy(Connection conn, String bookname) {
		String sql  = "delete from Book where bookname like ?";
		int result = 0;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, bookname);
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} 
		return result;

	}

	// 도서 수정
	public static void updateBook(List<Member> member) {

	}

	// 포인트 충전
	public int earnPoint(Connection conn, String id, int point) throws SQLException {
		String sql = "UPDATE member SET points = points + ? WHERE id = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, point);
			pstmt.setString(2, id);
			return pstmt.executeUpdate();
		}
	}

	// 포인트 조회
	public Member getPoint(Connection conn, String id) throws SQLException, BookstoreException {
		Member member = null;
		String sql = "SELECT * FROM member WHERE id = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, id);
			try(ResultSet rs = pstmt.executeQuery()) {
				if(rs.next()) {
					member = handleMemberResultSet(rs);
				} 
			} catch (SQLException e) {
				throw new BookstoreException ("해당 사용자가 존재하지 않습니다.", e);
			}
		}
		return member;
	}

	// 도서 검색
	public List<Book> searchBook(Connection conn, String title) {
		List<Book> bookList = new ArrayList<>();
		String sql = "select * from book where bookname like ?";
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, "%" + title + "%");

			try (ResultSet rset = pstmt.executeQuery()) {
				while (rset.next()) {
					Book book = handleBookResultSet(rset);
					bookList.add(book);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookList;
	}

	// 관리자 사용자 수정
	public static Member findById(Connection conn, String id) {
		Member member = null;
		String sql = "select * from member where id = ?";

		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, id);
			try(ResultSet rset = pstmt.executeQuery();){
				if(rset.next())
					member = handleMemberResultSet(rset);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return member;
	}

	// 관리자 사용자 정보 수정
	public static int updateMember(Connection conn, String id, String colName, Object newValue) throws BookstoreException {
		String sql = "update member set " + colName + " = ? where id = ?";
		int result = 0;

		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setObject(1, newValue);
			pstmt.setString(2, id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BookstoreException("회원정보 수정에 실패하였습니다.");
		}
		return result;
	}

	// 관리자 도서 수정
	public static Book findByBook(Connection conn, String bookname) {
		String sql = "select * from book where bookname = ?";
		Book book = null;

		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setString(1, bookname);
			try(ResultSet rset = pstmt.executeQuery();){
				if(rset.next())
					book = handleBookResultSet(rset);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return book;
	}

	// 관리자 도서 정보 수정
	public static int updateBook(Connection conn, String bookname, String colName, Object newValue) throws BookstoreException {
		String sql = "update book set " + colName + " = ? where bookname = ?";
		int result = 0;

		try (PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setObject(1, newValue);
			pstmt.setString(2, bookname);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new BookstoreException("도서 정보 수정에 실패하였습니다.");
		}
		return result;
	}

	// 구매 후 포인트 업데이트
	public static int updatePoint(Connection conn, String id, int bookPrice) {
		int result = 0; 
		String sql = "update member set points = points - ? where id = ?";
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){
			pstmt.setInt(1, bookPrice);
			pstmt.setString(2, id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}
	
	// 관리자 사용자 삭제
	public int deleteMember(Connection conn, String Id) throws SQLException {
		String sql = "delete from member where id = ?";
		int result = 0;
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, Id);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}


}


