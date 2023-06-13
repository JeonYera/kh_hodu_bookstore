package hodu.bookstore.controller;

import java.util.List;

import hodu.bookstore.model.dao.BookstoreDao;
import hodu.bookstore.model.dto.Book;
import hodu.bookstore.model.dto.Member;
import hodu.bookstore.model.service.BookstoreService;

public class BookstoreController {
	
	 /* Controller
	 * - 사용자의 요청을 받아 해당 요청을 처리하는 부분
	 * - 모델과 뷰 사이의 상호작용을 조정하는 역할
	 * - 모든 과정은 컨트롤러을 거쳐야한다
	 * - 중재자 역할을 수행하여 모든 과정을 조정하는 역할
	 * - 처리된 결과를 뷰에게 전달해서사용자에게 응답한다
	 **/

	private BookstoreService bookstoreService = new BookstoreService(); // Service 객체 생성 -> 비즈니스 로직 처리
	private BookstoreDao bookstoreDao = new BookstoreDao(); // DAO 객체 생성 -> db와의 상호작용
	// 다오, 서비스 객체 생성하는 이유
	// 역할 분리 : 컨트롤러는 비즈니스 로직을 처리하지 않고, 해당 작업을 서비스 객체에 위임 -> 서비스 객체는 도메인 로직을 구현하고, 데이터베이스와의 상호작용은 DAO 객체에 위임
	// 단일 책임 원칙 : 객체 지향 프로그래밍에서는 한 클래스는 한 가지 책임을 가지는 것이 좋다
	// 의존성 주입 : 의존성 주입은 객체 간의 결합도를 낮추고 테스트 용이성을 높이는 데 도움이 되고, 이렇게 생성된 객체들은 컨트롤러의 다른 메서드에서 사용될 수 있다

	
	// 도서 검색 - List<Book> 사용 이유 : 도서 검색 결과가 여러 개의 도서를 포함할 수 있기 때문에 모두 반환할 수 있는 'List<Book>' 반환형을 사용
	public List<Book> searchBook(String title) { // searchBook() 메서드는 입력된 이름(title)으로 도서를 검색하는 기능을 수행
		List<Book> bookList = bookstoreService.searchBook(title); // 도서 검색에는 bookstoreService 객체의 searchBook(title) 메서드를 호출하여 도서를 검색
		return bookList; // 검색된 도서 목록을 List<Book> 형태로 반환
	}
	
	// 포인트 조회 - Member 사용 이유: 한 명의 회원 정보만을 반환하기 때문에 단일 객체인 'Member'형식으로 반환
	public Member getPoint(String id) { // getPoint() 메서드는 입력된 회원 ID(id)로 회원의 포인트를 조회하는 기능을 수행
		Member member = null; // 예외가 발생할 경우, 예외 정보를 출력하고 member 변수는 null로 유지, 
							  // 예외가 발생했을 때 member 변수를 null로 유지하는 것은 예외 상황을 알리고 처리하기 위한 일반적인 방법 중 하나
		try {
			member = bookstoreService.getPoint(id); // 포인트 조회에는 bookstoreService 객체의 getPoint(id) 메서드를 호출하여 회원의 포인트를 조회
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member; // 조회된 회원 정보(Member)를 반환
	}

	// 도서 전체 조회 - List<Book> 사용 이유: 모든 도서를 조회하여 여러 개의 도서 목록을 반환하기 때문에 'List<Book>' 반환형을 사용
	public List<Book> findbook() { // findbook() 메서드는 모든 도서를 조회하는 기능을 수행
		List<Book> bookList = null; // 예외가 발생할 경우, 예외 정보를 출력하고 bookList 변수는 null로 유지
									// 예외가 발생하지 않으면 try 블록에서 실제로 조회된 도서 목록으로 bookList 변수가 대체되고, 예외가 발생한 경우에는 null을 유지
		try {
			bookList = bookstoreService.findbook(); // bookstoreService 객체의 findbook() 메서드를 호출하여 도서를 조회
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return bookList; // 조회된 도서 목록을 List<Book> 형태로 반환
	}

	// 멤버 전체 조회 - List<Member> 사용 이유: 모든 멤버를 조회하여 여러 개의 멤버 목록을 반환하기 때문에 'List<Member>' 반환형을 사용
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

	// insert(등록), update(수정), delete(삭제)는 int로 반환해야 한다.
	
	// 사용자 등록 - int 사용 이유 : insert(등록) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
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

	// 관리자 도서 등록 - int 사용 이유 : insert(등록) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
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


	// 구매할 때 포인트 차감 - int 사용 이유 : update(수정) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
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

	// 사용자 포인트 충전 - int 사용 이유 : update(수정) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
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

	// 사용자 도서 구매 - int 사용 이유 : update(수정) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
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


	// 관리자 도서 삭제 - int 사용 이유 : delete(삭제) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
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


	// 관리자 사용자 정보 조회 - Member 사용 이유: 한 명의 회원 정보만을 반환하기 때문에 단일 객체인 'Member'형식으로 반환
	public Member findById(String id) {
		return BookstoreService.findById(id);
	}

	// 관리자 사용자 수정 - int 사용 이유 : update(수정) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
	public static int updateMember(String id, String colName, Object newValue) {
		return BookstoreService.updateMember(id, colName, newValue);
	}

	// 관리자 도서 정보 조회 - Book 사용 이유: 한 개의 도서 정보만을 반환하기 때문에 단일 객체인 'Book'형식으로 반환
	public Book findByBook(String bookname) {
		return BookstoreService.findByBook(bookname);
	}

	// 관리자 도서 수정 - int 사용 이유 : update(수정) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
	public static int updateBook(String bookname, String colName, Object newValue) {
		return BookstoreService.updateBook(bookname, colName, newValue);
		// BookstoreService 클래스의 updateBook() 메서드를 호출하여 도서를 수정. 도서 이름, 열 이름, 새 값과 같은 매개변수를 사용하여 도서를 업데이트하는 로직을 구현
		// static으로 선언돼서 해당 클래스의 인스턴스를 생성하지 않고도 호출 가능
	}

	// 관리자 사용자 삭제 - int 사용 이유 : delete(삭제) 성공하면 정수형 값인 1, 실패하거나 예외가 발생하면 0을 반환하기 때문에 'int' 반환형을 사용
	public int deleteMember(String Id) {
		int result = 0;
		try {
			result = bookstoreService.deleteMember(Id);
		} catch (Exception e) {
			System.err.println("관리자에게 연락바랍니다." + e.getMessage());
			e.printStackTrace();
		}
		return result;
		// bookstoreService 객체의 deleteMember() 메서드를 호출하여 사용자를 삭제. 사용자 ID를 매개변수로 받아와서 해당 사용자를 삭제하는 로직을 구현
		// 이 메서드는 인스턴스 메서드로 선언되어 있으므로 해당 클래스의 인스턴스를 생성한 후에만 호출할 수 있다
		
	}

}


