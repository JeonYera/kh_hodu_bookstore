package hodu.bookstore.view;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import hodu.bookstore.controller.BookstoreController;
import hodu.bookstore.model.dao.BookstoreDao;
import hodu.bookstore.model.dto.Book;
import hodu.bookstore.model.dto.Member;
import hodu.bookstore.model.exception.BookstoreException;
import hodu.bookstore.model.service.BookstoreService;

public class BookstoreMain {

	private Scanner sc = new Scanner(System.in);
	private BookstoreController bookstoreController = new BookstoreController();

	public void mainMenu() {
		while(true) {
			System.out.println("======= 호두서점 =======");
			System.out.println("1. 로그인");
			System.out.println("2. 회원가입");
			System.out.println("0. 프로그램 종료");
			System.out.println("======================");
			System.out.print("원하시는 번호를 선택해 주세요 : ");
			int menuInput=-1;
			try {
				menuInput = sc.nextInt();
			}catch(InputMismatchException e) {
				sc.nextLine();
			}

			switch (menuInput) {
			case 1:
				System.out.println("======= 로그인 =======");
				Member member = signInForm();
				break;
				// 로그인 유저의 권한이 ROLE_USER 라면 일반 사용자 페이지로 연결
				// 로그인 유저의 권한이 ROLE_ADMIN 라면 관리자 페이지로 연결
			case 2:
				System.out.println("======= 회원가입 =======");
				member = signUpForm();
				bookstoreController.insertMember(member);
				System.out.println("회원가입 성공!");
				break;
			case 0:
				System.out.println("프로그램을 종료합니다.");
				System.exit(menuInput);
				break;

			default:
				System.out.println("잘못 입력하셨습니다.");
			}
		}
	}

	// 로그인
	public Member signInForm() {
		Member member = new Member();
		List<Member> memberList = bookstoreController.findmember();

		while(true) {
			System.out.print("아이디를 입력하세요: ");
			String id = sc.next();
			member.setId(id);

			System.out.print("비밀번호를 입력하세요: ");
			String password = sc.next();
			member.setPassword(password);

			boolean flag = false;
			for(Member mem : memberList) {
				if(mem.getId().equals(id) && mem.getPassword().equals(password)) {
					flag = true;
					if(id.equals("admin") && password.equals("admin")) {
						System.out.println("로그인 성공!");
						adminMainView(member);
					} else {
						System.out.println("로그인 성공!");
						userMainView(member);
					}
					return mem;
				}
			}

			if(!flag) {
				System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
			}
		}
	}

	// 회원가입
	public Member signUpForm() {
		Member member = new Member();

		System.out.println("회원정보를 입력하세요");
		System.out.print("아이디 : ");
		member.setId(sc.next());
		System.out.print("비밀번호 : ");
		member.setPassword(sc.next());
		System.out.print("이름 : ");
		member.setName(sc.next());
		System.out.print("이메일 : ");
		member.setEmail(sc.next());
		System.out.print("핸드폰 번호 : ");
		member.setPhoneNumber(sc.next());
		System.out.print("주소 : ");
		member.setAddress(sc.next());

		sc.nextLine(); // 버퍼 개행문자 제거용

		return member;

	}


	public void userMainView(Member member) {
		/*로그인 후 일반 사용자 메인 페이지*/
		/* 1. 도서 조회
		 * 2. 도서 검색
		 * 3. 도서 구매
		 * 4. 포인트 충전
		 * 5. 포인트 조회
		 * 0. 프로그램 종료
		 * */


		String islogin = member.getId();
		String userMenu = "\n========== 사용자 메뉴 ==========\n"
				+ "1. 도서 조회\n"
				+ "2. 도서 검색\n"
				+ "3. 도서 구매\n"
				+ "4. 포인트 충전\n"
				+ "5. 포인트 조회\n"
				+ "0. 프로그램 종료\n"
				+ "================================\n"
				+ "사용하실 메뉴 번호를 입력하세요 : ";

		while(true) {

			System.out.print(userMenu);
			String menuNum = sc.next();
			List<Book> bookList = null;
			List<Member> memberList = null;
			Member name = null;
			int result = 0;
			int amount = 0;
			int point = 0;

			switch(menuNum) {
			case "1" : 
				// 도서 전체 조회
				bookList = bookstoreController.findbook();
				displaybookList(bookList);
				break;
			case "2" :
				// 도서 검색
				String title = inputBook();
				bookList = bookstoreController.searchBook(title);
				displaybookList(bookList);
				break;
			case "3" :
				// 도서 구매
				bookBuy(islogin);
				break;
			case "4" : 
				// 포인트 충전
				int point2 = chargeId(member);
				amount = bookstoreController.earnPoint(islogin, point2);
				System.out.println("포인트가 충전되었습니다.");
				break;
			case "5":
				// 포인트 조회
				memberList = bookstoreController.findmember();
				inputId(memberList);
				break;
			case "0" : return; // 프로그램 종료
			default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요."); 
			}
		}

	}

	// 포인트 충전
	public int chargeId(Member member) {

		int point = 0;
		if (member == null) {
			System.out.println("조회된 결과가 없습니다.");
		} else {
			System.out.print("충전할 포인트를 입력하세요 : ");
			point = sc.nextInt();
		}
		return point;
	}

	// 포인트 조회
	public void inputId(List<Member> memberList) {
		System.out.print("아이디를 입력하세요 : ");
		String id = sc.next();

		Member targetMember = null;
		for(Member member : memberList) {
			if(member.getId().equals(id)) {
				targetMember = member;
				break;
			}
		}

		if(targetMember == null) {
			System.out.println("조회된 결과가 없습니다.");
		}
		else {
			System.out.println("아이디: " + targetMember.getId() + ", 포인트: " + targetMember.getPoints());
		}
	}

	// 사용자 도서 전체 조회
	public void displaybookList(List<Book> bookList) {
		if(bookList == null || bookList.isEmpty()) {
			System.out.println("조회된 결과가 없습니다.");
		}
		else {
			System.out.println("-------------------------------------------------------------");
			System.out.printf("%s	%s	 %s	  %s\n", 
					"booknumber", "bookname", "author", "price");
			System.out.println("-------------------------------------------------------------");
			for(Book book : bookList) {
				System.out.printf("%s	%s	 %s	  %s\n", 
						book.getBookNumber(), book.getBookName(), book.getAuthor(),
						book.getPrice());
			}
			System.out.println("-------------------------------------------------------------");

		}
	}

	// 사용자 도서 검색
	private String inputBook() {
		System.out.print("찾고자하는 도서 이름을 검색해주세요 : ");
		return sc.next();

	}

	// 사용자 이름 검색
	private String inputMember() {
		System.out.print("찾고자하는 회원 이름을 검색해주세요 : ");
		return sc.next();

	}

	// 사용자 수정
	private String inputMemberId() {
		System.out.print("찾고자하는 회원 아이디를 검색해주세요 : ");
		return sc.next();

	}

	// 도서 구매
	private void bookBuy(String islogId) {
		int result = 0; 
		System.out.print("도서 이름을 입력하세요: ");
		String bookName = sc.next();

		// 도서할 구매 -> 북테이블에서 구매 되면 삭제 
		List<Book> book= bookstoreController.searchBook(bookName);
		int bookPrice = book.get(0).getPrice();
		result = bookstoreController.bookstoreBuy(bookName);
		System.out.println("도서 구매가 완료되었습니다.");

		// 구매완료

		// 회원 포인트 차감
		int resultP = 0;
		resultP = bookstoreController.updatePoint(islogId, bookPrice);

	}

	//로그인 후 관리자 메인 view 페이지
	public void adminMainView(Member member) {

		String userMenu = "\n========== 관리자 메뉴 ==========\n"
				+ "1. 도서 조회\n"
				+ "2. 도서 검색\n"
				+ "3. 도서 등록\n"
				+ "4. 도서 수정\n"
				+ "5. 도서 삭제\n"
				+ "6. 사용자 조회\n"
				+ "7. 사용자 등록\n"
				+ "8. 사용자 수정\n"
				+ "9. 사용자 삭제\n"
				+ "0. 프로그램 종료\n"
				+ "================================\n"
				+ "사용하실 메뉴 번호를 입력하세요 : ";


		while(true) {

			System.out.print(userMenu);
			String menuNum = sc.next();
			List<Member> memberList = null;
			int result = 0;


			switch(menuNum) {
			case "1" : 
				// 관리자 도서 조회
				List<Book> bookList = null;
				bookList = bookstoreController.findbook();
				adminbookList(bookList);
				break;
			case "2" :
				// 관리자 도서 검색
				String title = inputBook();
				bookList = bookstoreController.searchBook(title);
				displaybookList(bookList);
				break;
			case "3" :
				// 관리자 도서 등록
				Book book = insertAdminBook();
				bookstoreController.insertBook(book);
				System.out.println("도서가 등록되었습니다.");
				break;
			case "4" : 
				// 관리자 도서 수정
				updatebookMenu();
				break;
			case "5" : 
				// 관리자 도서 삭제
				bookList = bookstoreController.findbook();
				deleteBook(bookList);
				break;
			case "6" : 
				// 관리자 사용자 조회
				memberList = bookstoreController.findmember();
				displayMember(memberList);
				break;
			case "7" : 
				// 관리자 사용자 등록
				Member member2 = signUpAdminMember();
				bookstoreController.insertMember(member2);
				System.out.println("회원이 등록되었습니다.");
				break;
			case "8" :
				// 관리자 사용자 수정
				updateMenu();
				break;
			case "9" :
				// 관리자 사용자 삭제
				memberList = bookstoreController.findmember();
				deleteMemberId(memberList);
				break;
			case "0" : return;
			}
		}

	}

	// 관리자 도서 등록
	public Book insertAdminBook() {
		Book book = new Book();

		System.out.println("도서 정보를 입력하세요.");
		System.out.print("도서 번호 : ");
		book.setBookNumber(sc.nextInt());
		sc.nextLine(); // 개행문자 제거
		System.out.print("도서 이름 : ");
		book.setBookName(sc.next());
		sc.nextLine(); // 개행문자 제거
		System.out.print("저자 : ");
		book.setAuthor(sc.nextLine());
		System.out.print("가격 : ");
		book.setPrice(sc.nextInt());

		return book;
	}   

	// 관리자 도서 전체 조회
	public void adminbookList(List<Book> bookList) {
		if(bookList == null || bookList.isEmpty()) {
			System.out.println("조회된 결과가 없습니다.");
		}
		else {
			System.out.println("-------------------------------------------------------------");
			System.out.printf("%s	%s		%s	 %s\n", 
					"booknumber", "bookname", "author", "price");
			System.out.println("-------------------------------------------------------------");
			for(Book book : bookList) {
				System.out.printf("%s		%s			%s	 %s\n", 
						book.getBookNumber(), book.getBookName(), book.getAuthor(),
						book.getPrice());
			}
			System.out.println("-------------------------------------------------------------");

		}
	}

	// 관리자 사용자 전체 조회
	public void displayMember(List<Member> memberList) {
		if(memberList == null || memberList.isEmpty()) {
			System.out.println("조회된 결과가 없습니다.");
		}
		else {
			System.out.println("-----------------------------------------------------------------------------------------------------");
			System.out.printf("%s		%s		%s	     		%s	    	%s	  	%s\n", 
					"id", "name", "email", "phonenumber", "address", "points");
			System.out.println("-----------------------------------------------------------------------------------------------------");
			for(Member member : memberList) {
				System.out.printf("%s		%s		%s		%s	%s	%s\n", 
						member.getId(), member.getName(), member.getEmail(),
						member.getPhoneNumber(), member.getAddress(), member.getPoints());
			}
			System.out.println("-------------------------------------------------------------");
		}
	}


	// 관리자 사용자 등록
	public Member signUpAdminMember() {
		Member member = new Member();

		System.out.println("사용자 정보를 입력하세요.");
		System.out.print("아이디 : ");
		member.setId(sc.next());
		System.out.print("비밀번호 : ");
		member.setPassword(sc.next());
		System.out.print("이름 : ");
		member.setName(sc.next());
		System.out.print("이메일 : ");
		member.setEmail(sc.next());
		System.out.print("핸드폰 번호 : ");
		member.setPhoneNumber(sc.next());
		sc.nextLine(); // 개행문자 제거
		System.out.print("주소 : ");
		member.setAddress(sc.nextLine());
		System.out.print("포인트 : ");
		member.setPoints(sc.nextInt());

		return member;
	}

	// 관리자 도서 삭제
	public void deleteBook(List<Book> bookList) {
		while (true) {
			System.out.print("삭제 할 도서의 이름을 입력해주세요 : ");
			String bookName = sc.next();

			int count = 0;
			int result = 0;
			for (int i = 0; i < bookList.size(); i++) {
				if (bookName.equals(bookList.get(i).getBookName())) {
					count++;
					// 도서 삭제 수행
					result = bookstoreController.deleteBook(bookName);
					System.out.println("도서 삭제 완료!");
					break;
				}
			}
			if (count == 0) {
				System.out.println("도서가 존재하지 않습니다. 다시 입력하세요.");
			} else {
				break;
			}
		}
	}


	// 관리자 사용자 삭제
	public void deleteMemberId(List<Member> memberList) {
		while (true) {
			System.out.print("삭제 할 사용자의 아이디 입력해주세요 : ");
			String id = sc.next();

			int count = 0;
			int result = 0;
			for (int i = 0; i < memberList.size(); i++) {
				if (id.equals(memberList.get(i).getId())) {
					count++;
					// 회원 삭제 수행
					result = bookstoreController.deleteMember(id);
					System.out.println("회원 삭제 완료");
					break;
				}
			}
			if (count == 0) {
				System.out.println("회원이 존재하지 않습니다. 다시 입력하세요.");
			} else {
				break;
			}
		}
	}

	// 관리자 도서 수정
	private void updatebookMenu() {
		String menu = "+++++++++ 도서 정보 수정 +++++++++\n"
				+ "1. 도서 이름 수정\n"
				+ "2. 도서 저자 수정\n"
				+ "3. 도서 가격 수정\n"
				+ "0. 메인메뉴로 돌아가기\n"
				+ "++++++++++++++++++++++++++++++\n"
				+ "선택 : ";

		String bookname = inputBook();

		while (true) {
			Book book = BookstoreService.findByBook(bookname);
			if (book == null) {
				System.out.println("조회된 도서가 없습니다.");
				return;
			} else {
				displayinputBook(book);
			}

			System.out.print(menu);
			String choice = sc.next();
			String colName = null;
			Object newValue = null;

			switch (choice) {
			case "1":
				System.out.print("변경할 도서 이름 : ");
				colName = "bookname";
				newValue = sc.next();
				break;
			case "2":
				System.out.print("변경할 도서 저자 : ");
				colName = "author";
				newValue = sc.next();
				break;
			case "3":
				System.out.print("변경할 도서 가격 : ");
				colName = "price";
				newValue = sc.next();
				sc.nextLine();
				break;
			case "0":
				return;
			default:
				System.out.println("잘못 입력하셨습니다.");
				continue;
			}

			int result = BookstoreController.updateBook(bookname, colName, newValue);
			System.out.println(result > 0 ? "도서 정보 수정 성공!" : "도서 수정 실패!");
		}
	}

	// 관리자 사용자 수정
	private void updateMenu() {
		String menu = "+++++++++ 사용자 정보 수정 +++++++++\n"
				+ "1. 이름 수정\n"
				+ "2. 비밀번호 수정\n"
				+ "3. 이메일 수정\n"
				+ "4. 핸드폰 번호 수정\n"
				+ "5. 주소 수정\n"
				+ "0. 메인메뉴로 돌아가기\n"
				+ "++++++++++++++++++++++++++++++\n"
				+ "선택 : ";

		String id = inputMemberId();

		while(true) {
			Member member = BookstoreService.findById(id);
			if(member == null) {
				System.out.println("조회된 회원이 없습니다.");
				return;
			}
			else {
				displayinputMember(member);
			}

			System.out.print(menu);
			String choice = sc.next();
			String colName = null;
			Object newValue = null; // String, Date도 동시에 처리하기 위함.

			switch(choice) {
			case "1" : 
				System.out.print("변경할 이름 : ");
				colName = "name";
				newValue = sc.next();
				break;
			case "2" : 
				System.out.print("변경할 비밀번호 : ");
				colName = "password";
				newValue = sc.next();
				break;
			case "3" : 
				System.out.print("변경할 이메일 : ");
				colName = "email";
				newValue = sc.next();
				break;
			case "4" : 
				System.out.print("변경할 핸드폰 번호 : ");
				colName = "phonenumber";
				newValue = sc.next();
				break;
			case "5" : 
				System.out.print("변경할 주소 : ");
				colName = "address";
				newValue = sc.next();
				sc.nextLine();
				break;
			case "0" : return;
			default : 
				System.out.println("잘못 입력하셨습니다.");
				continue;
			}

			int result = BookstoreController.updateMember(id, colName, newValue);
			System.out.println(result > 0 ? "회원정보 수정 성공!" : "회원정보 수정 실패!");
		}

	}


	// 관리자 사용자 정보 검색
	public void displayinputMember(Member member) {
		List<Member> memberList = new ArrayList<>();
		memberList.add(member);
		displayMember(memberList);
	}

	// 관리자 도서 정보 수정
	private void displayinputBook(Book book) {
		List<Book> bookList = new ArrayList<>();
		bookList.add(book);
		adminbookList(bookList);
	}

}

