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
	
	/* DAO 
	 * - 데이터베이스와의 상호작용을 담당하는 부분
	 * - 주로 데이터베이스와의 통신을 담당하며, 데이터의 조회, 삽입, 수정, 삭제 등의 기능을 제공하고 비즈니스 로직을 실행한다
	 * - 그 다음에 결과를 컨트롤러나 서비스에게 반환하여 알린다
	 **/
	
	private Properties prop = new Properties();

	public BookstoreDao() {
		// resources/member-query.properties 파일을 읽어오기 (입력해놓은 DB정보)
		try {
			prop.load(new FileReader("resources/book-query.properties")); 
			// prop는 Properties 객체이고, resources/book-query.properties 파일의 내용을 읽어와서 저장하는 역할
			// Properties 객체는 Java의 내장 클래스로, 키-값 쌍으로 구성된 속성 파일을 처리하는데 사용
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 도서 등록
	public int insertBook(Connection conn, Book book){ // Connection 객체는 데이터베이스와의 연결을 관리하는 역할 | Book은 도서 정보를 담고 있는 객체
		String sql = prop.getProperty("insertBook"); // prop 객체에서 "insertBook" 키에 해당하는 값을 가져와서 sql 변수에 저장
		int result = 0; // result 변수를 초기화. 이 변수는 도서 등록 작업의 결과로, 처리된 행의 수를 나타낸다

		// 1. PreparedStatement 생성 & 미완성쿼리 값대입
		try (PreparedStatement pstmt = conn.prepareStatement(sql)){ // sql 변수에 저장된 SQL 쿼리문을 사용하여 PreparedStatement 객체를 생성. 
																	// conn은 전달된 Connection 객체로, DB 연결 정보를 가지고 있다
			pstmt.setInt(1, book.getBookNumber()); // PreparedStatement 객체에 도서 정보를 설정, book 객체에서 필요한 정보를 가져와서 쿼리문의 미완성 부분에 값으로 대입
			pstmt.setString(2, book.getBookName());
			pstmt.setString(3, book.getAuthor());
			pstmt.setInt(4, book.getPrice());

			// 2. 쿼리실행 PreparedStatement#executeUpdate:int (처리된 행의 수)
			result = pstmt.executeUpdate(); // PreparedStatement 객체의 executeUpdate 메서드를 호출하여 SQL 쿼리를 실행 
											// 실행된 쿼리의 결과로 처리된 행의 수를 반환받아 result 변수에 저장
											// executeUpdate 메서드는 SQL 문을 실행하여 데이터베이스에서 업데이트 작업(삽입, 수정, 삭제)을 수행하는 메서드. 
											// 이 메서드는 PreparedStatement 또는 Statement 객체에서 호출할 수 있다
		} catch (SQLException e) {
			// 예외가 발생하면 서비스로 던져서 예외발생여부를 알린다.
			// 발생한 예외를 새로운 예외(현재예외상황을 더 잘 설명할수 있는, 다루기 편한 Unchecked)로 전환해서 던진다.
			throw new MemberException("도서 등록 오류", e); 
		}
		return result; // 도서 등록 작업의 결과인 result 변수를 반환. 이 값은 처리된 행의 수
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
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) { // SQL 쿼리문을 사용하여 PreparedStatement 객체를 생성
			pstmt.setString(1, title); // PreparedStatement 객체에 도서 제목을 설정. setString 메서드를 사용하여 도서 제목을 설정하는데, 매개변수로 전달된 title 값을 사용
			result = pstmt.executeUpdate(); // PreparedStatement 객체의 executeUpdate 메서드를 호출하여 쿼리문을 실행하고, 처리된 행의 수를 반환
											// 이 경우, 삭제된 도서의 개수가 반환
		} catch (SQLException e) {
			throw e;
		}
		return result;
	}

	// 사용자 전체 도서 조회
	public List<Book> findbook(Connection conn) throws BookstoreException {
		List<Book> bookList = new ArrayList<>(); // 빈 Book 객체의 리스트인 bookList를 생성합니다. 조회된 도서들을 저장하기 위한 리스트
		String sql = prop.getProperty("findbook"); // prop 객체에서 "findbook"라는 키로 등록된 SQL 쿼리문을 가져온다

		// 1. PreparedStatement 생성 & 미완성쿼리 값대입 
		try(PreparedStatement pstmt = conn.prepareStatement(sql)){ // conn.prepareStatement(sql) 메서드를 사용하여 PreparedStatement 객체를 생성. 이 때, SQL 쿼리문을 전달

			// 2. 쿼리 실행 PreapredStatement#executeQuery:ResultSet
			try(ResultSet rset = pstmt.executeQuery()){ // pstmt.executeQuery() 메서드를 호출하여 SQL 쿼리문을 실행하고, 그 결과로 생성된 ResultSet 객체를 반환받음
														// ResultSet 객체는 데이터베이스에서 조회된 결과를 담고 있는 객체
														// 일반적으로 SQL 쿼리를 실행하여 데이터베이스로부터 데이터를 가져올 때 사용된다

				// 3. ResultSet 처리 : 행 -> dto객체
				while(rset.next()) { // ResultSet 객체를 사용하여 도서 정보를 하나씩 읽어와서 처리. rset.next() 메서드를 호출하여 다음 행으로 이동하면서 도서 정보가 있는지 확인
					// 매개변수생성자 | 기본생성자 + setter
					Book book = handleBookResultSet(rset);
					// handleBookResultSet() 메서드를 호출하여 ResultSet 객체에서 읽어온 도서 정보를 Book 객체로 변환

					// 변환된 Book 객체를 bookList에 추가
					bookList.add(book);
				}
			}

		} catch (SQLException e) {
			throw new BookstoreException("도서 조회 오류", e);
		}

		return bookList; // 메서드 실행이 완료되면, bookList를 반환. 데이터베이스에서 조회된 모든 도서 정보를 담고 있는 리스트
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
	
	/* 도서 검색과 사용자 조회의 타입(Connection, String | ResultSet)이 다른 이유 */
	
	// 'findBybook'메서드는 도서 검색을 수행하는 메서드로, Connection 객체와 도서명(bookname)을 매개변수로 받는다
	// 검색 결과로서 Book 객체를 반환한다. 즉, 주어진 도서명을 사용하여 데이터베이스에서 해당 도서를 검색하고, 검색된 도서 정보를 Book 객체로 생성하여 반환
	// 'handleMemberResultSet'메서드는 ResultSet 객체에서 관리자 사용자 정보를 추출하여 Member 객체로 변환하는 메서드이다
	// 주어진 ResultSet 객체에서 각 컬럼의 값을 가져와서 Member 객체의 필드에 설정한 후, 해당 Member 객체를 반환한다
	// 이 메서드는 관리자 사용자 조회와 관련이 있으며, ResultSet 객체에서 관리자 사용자 정보를 추출하여 Member 객체로 변환하기 위해 사용
	// 따라서, 두 메서드는 각각 독립적인 역할을 수행하며, 매개변수의 타입과 기능이 다른 이유는 해당 메서드들이 서로 다른 기능을 수행하기 때문
	

	// 관리자 사용자 조회 - ResultSet 객체에서 관리자 사용자 정보를 추출하여 Member 객체로 변환하는 메서드
	private static Member handleMemberResultSet(ResultSet rset) throws SQLException {
		// handleMemberResultSet 메서드는 ResultSet 객체를 매개변수로 받아서 해당 객체의 각 열(Column)에서 데이터를 가져와 Member 객체에 설정. 아래는 각 열에서 데이터를 추출하는 과정
		// 주로 ResultSet의 특정 행에 대한 데이터를 가져와서 Member 객체로 변환하는 공통적인 작업을 수행하는 코드를 포함
		Member member = new Member(); // Member 클래스의 객체를 생성하여 member 변수에 할당, 이를 통해 새로운 Member 객체를 생성하고, 이 객체를 member 변수에 참조
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

	// 포인트 충전
	public int earnPoint(Connection conn, String id, int point) throws SQLException { // int point는 포인트 충전량을 나타내는 매개변수
											// id에 해당하는 회원의 포인트를 증가시키는 역할을 수행. 이 때, point 매개변수는 증가시킬 포인트량을 지정하는 값
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


