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

	/* View
	 * 
	 * -  사용자에게 데이터를 시각적으로 표현하고 화면을 구성
	 * - 사용자의 입력을 받아 컨트롤러에 전달. 사용자가 버튼을 클릭하거나 양식을 제출하는 등의 인터랙션을 처리하여 컨트롤러에 알린다
	 * 
	 **/
	private Scanner sc = new Scanner(System.in);
	private BookstoreController bookstoreController = new BookstoreController(); // 컨트롤러와 뷰를 연결하기 위한 컨트롤러 객체 생성

	public void mainMenu() {
		while(true) {
			System.out.println("======= 호두서점 =======");
			System.out.println("1. 로그인");
			System.out.println("2. 회원가입");
			System.out.println("0. 프로그램 종료");
			System.out.println("======================");
			System.out.print("원하시는 번호를 선택해 주세요 : ");
			int menuInput=-1; // -1로 초기화한 이유는 사용자의 메뉴 선택 입력을 받을 때, 유효한 메뉴 번호와 매칭되지 않는 값이 입력된 경우를 대비하기 위해서
							  // 잘못된 입력이 발생한 경우를 예외로 처리할 수 있고, switch 문의 default 블록에서 "잘못 입력하셨습니다." 메시지를 출력하도록 처리
			try {
				menuInput = sc.nextInt();
			}catch(InputMismatchException e) {
				sc.nextLine();
			}

			switch (menuInput) {
			case 1:
				System.out.println("======= 로그인 =======");
				Member member = signInForm(); // signInForm() 메서드를 호출하여 로그인 폼을 표시하고, member 객체에 로그인 정보를 저장
				break;
				// 로그인 유저의 권한이 ROLE_USER 라면 일반 사용자 페이지로 연결
				// 로그인 유저의 권한이 ROLE_ADMIN 라면 관리자 페이지로 연결
			case 2:
				System.out.println("======= 회원가입 =======");
				member = signUpForm(); //  signUpForm() 메서드를 호출하여 회원가입 폼을 표시하고, member 객체에 회원 정보를 저장
				bookstoreController.insertMember(member); // bookstoreController.insertMember(member)를 호출하여 회원 정보를 컨트롤러에 전달하고, "회원가입 성공!" 출력
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
		Member member = new Member(); ; // 새로운 Member 객체를 생성하여 로그인 정보를 저장할 변수를 준비
		List<Member> memberList = bookstoreController.findmember(); // // bookstoreController를 통해 회원 목록을 가져온다

		while(true) {
			System.out.print("아이디를 입력하세요: ");
			String id = sc.next(); // // 사용자로부터 아이디를 입력
			member.setId(id); // 입력받은 아이디를 member 객체에 설정

			System.out.print("비밀번호를 입력하세요: ");
			String password = sc.next(); // 사용자로부터 비밀번호를 입력
			member.setPassword(password); // 입력받은 비밀번호를 member 객체에 설정

			boolean flag = false; // 로그인 성공 여부를 나타내는 플래그 변수
			for(Member mem : memberList) { // 회원 목록을 순회하면서 입력받은 아이디와 비밀번호와 일치하는 회원을 찾는다
				if(mem.getId().equals(id) && mem.getPassword().equals(password)) {
					flag = true; // 일치하는 회원을 찾았으면 flag를 true로 설정
					if(id.equals("admin") && password.equals("admin")) {
						System.out.println("로그인 성공!");
						adminMainView(member); // 아이디와 비밀번호가 "admin"일 경우 관리자 메인 화면으로 이동
					} else {
						System.out.println("로그인 성공!");
						userMainView(member); // 일반 사용자 메인 화면으로 이동
					}
					return mem; // 일치하는 회원을 반환
				}
			}

			if(!flag) {
				System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
			}
		}
	}

	// 회원가입
	public Member signUpForm() {
		Member member = new Member(); // Member 클래스는 회원의 정보를 담는 객체를 나타내며, 해당 객체를 생성하여 회원가입 폼에서 입력받은 정보를 저장하기 위해 사용
									  //new Member()는 Member 클래스의 인스턴스를 생성하는 역할. 이를 통해 새로운 회원 객체를 만들고, 입력된 정보를 해당 객체의 속성에 설정
		
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


	public void userMainView(Member member) { // Member member는 메소드의 매개변수. 매개변수를 선언함으로써, userMainView 메소드가 호출될 때 외부에서 전달된 Member 객체를 사용
											  // 즉, 로그인한 회원 정보를 전달받는 역할을 한다
		/*로그인 후 일반 사용자 메인 페이지*/
		/* 1. 도서 조회
		 * 2. 도서 검색
		 * 3. 도서 구매
		 * 4. 포인트 충전
		 * 5. 포인트 조회
		 * 0. 프로그램 종료
		 * */


		String islogin = member.getId(); // 현재 로그인한 사용자의 아이디를 가져옴
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

			System.out.print(userMenu); // 사용자 메뉴 출력
			String menuNum = sc.next(); // 사용자로부터 메뉴 번호 입력 받음
			List<Book> bookList = null;
			List<Member> memberList = null;
			Member name = null;
			int result = 0;
			int amount = 0;
			int point = 0;

			switch(menuNum) { // 사용자한테 입력받은 번호 보여주기
			case "1" : 
				// 도서 전체 조회
				bookList = bookstoreController.findbook(); // 도서 전체 목록을 조회
				displaybookList(bookList); // 도서 목록 출력
				break;
			case "2" :
				// 도서 검색
				String title = inputBook(); // 검색할 도서 제목 입력 받음
				bookList = bookstoreController.searchBook(title); // 도서 검색
				displaybookList(bookList); // 검색된 도서 목록 출력
				break;
			case "3" :
				// 도서 구매
				bookBuy(islogin); // 도서 구매 메뉴 호출
				break;
			case "4" : 
				// 포인트 충전
				int point2 = chargeId(member); // 충전할 포인트 입력 받음
				amount = bookstoreController.earnPoint(islogin, point2); // 포인트 충전
				System.out.println("포인트가 충전되었습니다.");
				break;
			case "5":
				// 포인트 조회
				memberList = bookstoreController.findmember(); // 회원 목록 조회
				inputId(memberList); // 회원 아이디 입력
				break;
			case "0" : return; // 프로그램 종료
			default : System.out.println("잘못 입력하셨습니다. 다시 입력해주세요."); 
			}
		}

	}

	// 포인트 충전
	public int chargeId(Member member) { // 메소드의 매개변수로 전달된 Member member는 충전을 진행하는 회원의 정보

		int point = 0;
		if (member == null) { // 먼저 전달된 member 객체가 null인지 확인하여 회원 정보가 있는지를 검사
			System.out.println("조회된 결과가 없습니다.");
		} else { // 만약 member 객체가 null이 아니라면, 사용자에게 충전할 포인트를 입력받음
			System.out.print("충전할 포인트를 입력하세요 : ");
			point = sc.nextInt(); // 사용자가 입력한 포인트는 point 변수에 저장
		}
		return point; // 사용자가 입력한 포인트가 반환
	}

	// 포인트 조회
	public void inputId(List<Member> memberList) { // 사용자로부터 아이디를 입력받고, 입력받은 아이디와 회원 목록에 저장된 아이디를 비교하여 일치하는 회원을 찾는다
		System.out.print("아이디를 입력하세요 : ");
		String id = sc.next();

		Member targetMember = null; // 검색된 회원을 저장할 변수. 초기값은 null로 설정
		for(Member member : memberList) { // for문을 사용하여 회원 목록을 순회하면서 입력받은 아이디와 일치하는 회원을 찾고, targetMember에 해당 회원을 저장하고 반복문을 종료
			// Member member: 반복문에서 현재 순서에 해당하는 회원 객체를 가리키는 변수 / memberList: 회원 목록을 담고 있는 리스트
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
		if(bookList == null || bookList.isEmpty()) { // isEmpty()는 문자열이 비어 있는지 여부를 확인하는 메서드
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
		// bookstoreController를 통해 searchBook() 메서드를 호출하여 도서 이름으로 도서를 검색. 검색 결과는 List<Book> 형태로 반환되며, 이를 book 변수에 저장
		
		int bookPrice = book.get(0).getPrice(); // 검색된 도서 목록에서 첫 번째 도서의 가격을 가져와서 변수 bookPrice에 저장
		result = bookstoreController.bookstoreBuy(bookName);
		// bookstoreBuy() 메서드를 호출하여 도서를 구매. 구매가 성공하면 결과값으로 양수가 반환되며, result 변수에 저장
		
		System.out.println("도서 구매가 완료되었습니다.");

		// 구매완료

		// 회원 포인트 차감
		int resultP = 0;
		resultP = bookstoreController.updatePoint(islogId, bookPrice); // islogId는 로그인된 회원의 아이디, bookPrice는 구매한 도서의 가격
		// bookstoreController를 통해 updatePoint() 메서드를 호출하여 회원의 포인트를 차감, 차감 결과는 양수로 반환되며, resultP 변수에 저장
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
				bookList = bookstoreController.findbook(); // findbook() 메소드를 호출하여 도서 목록을 검색, 이 결과를 bookList에 할당
				adminbookList(bookList); // adminbookList() 메소드를 호출하여 bookList를 인자로 전달. 이는 도서 목록을 출력하고 표시하는 역할을 담당하는 메소드
				break;
			case "2" :
				// 관리자 도서 검색
				String title = inputBook(); // inputBook() 메소드를 호출하여 관리자에게 도서 제목을 입력 받고, 입력받은 제목을 title 변수에 할당
				bookList = bookstoreController.searchBook(title); // searchBook() 메소드를 호출하여 입력받은 제목을 기준으로 도서를 검색하고 검색 결과를 bookList에 할당
				displaybookList(bookList); // displaybookList() 메소드를 호출하여 검색된 도서 목록을 출력. 이는 도서 정보를 표시하는 역할을 담당하는 메소드
				break;
			case "3" :
				// 관리자 도서 등록
				Book book = insertAdminBook(); // insertAdminBook() 메소드를 호출하여 관리자가 입력한 도서 정보를 받고, 이 결과를 book에 할당
				bookstoreController.insertBook(book); // insertBook() 메소드를 호출하여 도서를 등록. book은 새로 등록할 도서의 정보를 담고있다
				System.out.println("도서가 등록되었습니다.");
				break;
			case "4" : 
				// 관리자 도서 수정
				updatebookMenu(); // updatebookMenu() 메소드는 도서 수정에 대한 메뉴를 표시하고 사용자로부터 선택을 받아 실제 도서 수정 기능을 호출하는 역할을 수행
				break;
			case "5" : 
				// 관리자 도서 삭제
				bookList = bookstoreController.findbook(); // findbook() 메소드를 호출하여 도서 목록을 가져오고, 이 결과를 bookList에 할당
				deleteBook(bookList); // deleteBook() 메소드를 호출하여 도서 목록을 출력하고 사용자로부터 삭제할 도서의 정보를 입력받아 도서를 삭제
				break;
			case "6" : 
				// 관리자 사용자 조회
				memberList = bookstoreController.findmember(); // findmember() 메소드를 호출하여 사용자 목록을 가져온다. 이 결과를 memberList에 할당
				displayMember(memberList); // displayMember() 메소드를 호출하여 사용자 목록을 출력합니다. 이는 사용자 정보를 표시하는 역할을 담당하는 메소드
				break;
			case "7" : 
				// 관리자 사용자 등록
				Member member2 = signUpAdminMember(); // signUpAdminMember() 메소드를 호출하여 관리자가 입력한 사용자 정보를 받는다. 이 결과를 member2에 할당
				bookstoreController.insertMember(member2); // insertMember() 메소드를 호출하여 사용자를 등록. member2는 새로 등록할 사용자의 정보를 담고 있다
				System.out.println("회원이 등록되었습니다.");
				break;
			case "8" :
				// 관리자 사용자 수정
				updateMenu(); // updateMenu() 메소드를 호출하여 사용자 정보 수정에 대한 메뉴를 표시하고 사용자로부터 선택을 받는다. 사용자 정보 수정 기능은 updateMenu() 메소드 내부에서 처리
				break;
			case "9" :
				// 관리자 사용자 삭제
				memberList = bookstoreController.findmember(); // findmember() 메소드를 호출하여 사용자 목록을 가져옵니다. 이 결과를 memberList에 할당
				deleteMemberId(memberList); // deleteMemberId() 메소드를 호출하여 사용자 목록을 출력하고 사용자로부터 삭제할 사용자의 아이디를 입력받아 사용자를 삭제
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
			
			// 입력받은 도서 이름과 일치하는 도서를 찾는다
			for (int i = 0; i < bookList.size(); i++) {
				if (bookName.equals(bookList.get(i).getBookName())) { // 위에서 입력받은 bookName과 bookList에서 가져온 도서 이름이 일치하는지 비교
					count++; // 일치하는 경우 count 변수를 증가
					// 도서 삭제 수행
					result = bookstoreController.deleteBook(bookName); // 일치하는 도서가 있다면 해당 도서를 삭제하기 위해 deleteBook() 메소드를 호출
					System.out.println("도서 삭제 완료!");
					break;
				}
			}
			 // 입력한 도서 이름과 일치하는 도서가 없을 경우 메시지를 출력
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
	public void displayinputMember(Member member) { // displayinputMember 메서드는 Member 객체를 매개변수로 받는다
		List<Member> memberList = new ArrayList<>(); // 새로운 ArrayList 객체인 memberList를 생성
		memberList.add(member); // memberList에 매개변수로 받은 member를 추가
		displayMember(memberList); // displayMember 메서드를 호출하여 memberList를 전달하여 사용자 정보를 출력
	}

	// 관리자 도서 정보 수정
	private void displayinputBook(Book book) {
		List<Book> bookList = new ArrayList<>();
		bookList.add(book);
		adminbookList(bookList);
	}

}

