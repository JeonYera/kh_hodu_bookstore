package hodu.bookstore.model.dto;

import java.util.ArrayList;
import java.util.List;

import oracle.net.jdbc.TNSAddress.Address;

public class Member {
	
	// 아이디, 비밀번호, 이름, 성별, 이메일, 전화번호, 주소, 포인트
	
	private String id;
	private String password;
	private String name;
	private String email;
	private String phonenumber;
	private String address;
	private int points;
	private String manager;
	private String userMainView;
	private int bookstoreBuy;
	
	private List<Member> memberList = new ArrayList<>();
	
	// 기본 생성자
	public Member() {
		super();
	}
	
	// 매개변수 생성자
	public Member(String id, String password, String name, String email, String phonenumber, String address,
			int points, String manager) {
		super();
		this.id = id;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phonenumber = phonenumber;
		this.address = address;
		this.points = points;
		this.manager = manager;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phonenumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phonenumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}
	
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}
	
	public List<Member> getMemberList() {
		return memberList;
	}
	
	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}
	
	private void memberList(Member member) {
		this.memberList.add(member);
	}

	
//	public void bookList(Book book) {
//		// TODO Auto-generated method stub
//		this.bookList.add(book);
//	}

	@Override
	public String toString() {
		return "Member [id=" + id + ", password=" + password + ", name=" + name + ", email="
				+ email + ", phone_number=" + phonenumber + ", address=" + address + ", points=" + points + ", manager=" + manager + "]";
	}

	public int getBalance() {
		return bookstoreBuy;
	}

	public void setBalance(int bookstoreBuy) {
	    this.bookstoreBuy = bookstoreBuy;
	}
}
