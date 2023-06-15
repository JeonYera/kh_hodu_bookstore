package hodu.common;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcTemplate {
	
	/*
	 * - 데이터베이스 연동과 관련된 일반적인 작업을 수행하는 유틸리티 클래스
	 * - 데이터베이스 연결 관리: getConnection(), commit(), rollback(), close() 메서드를 통해 데이터베이스 연결을 설정하고 관리
	 * - 데이터베이스 연결 설정: driverClass, url, user, password 등의 속성을 datasource.properties 파일에서 로드하여 데이터베이스 연결에 필요한 정보를 가져온다
	 * - 시퀀스를 사용한 고유한 책 번호 생성: getNextBookNumberFromSequence() 메서드는 시퀀스를 사용하여 고유한 책 번호를 생성, 
	 * 데이터베이스와 연결을 설정하고 book_number_sequence 시퀀스를 사용하여 다음 값을 가져온다
	 * */
	
	private static String driverClass;
	private static String url;
	private static String user;
	private static String password;
	
	static{ // JdbcTemplate 클래스의 정적 초기화 블록. 이 블록은 클래스가 로드될 때 실행되며, 클래스 변수인 driverClass, url, user, password의 초기화와 드라이버 클래스의 로딩을 수행
			// 따라서 static 블록은 datasource.properties 파일에서 데이터베이스 연결 설정을 로드하고, 드라이버 클래스를 로드하여 JDBC 연결에 필요한 초기화 작업을 수행
		
		Properties prop = new Properties(); // Properties 객체 생성: Properties 클래스는 키-값 쌍으로 구성된 속성들을 관리하기 위한 컨테이너. 
											// prop 변수는 Properties 객체를 생성하여 초기화
		try {
			prop.load(new FileReader("resources/datasource.properties"));
			//datasource.properties 파일 로드: prop.load(new FileReader("resources/datasource.properties"))를 통해 
			// 파일 시스템에서 datasource.properties 파일을 읽어와 Properties 객체에 로드. 
			// 이 파일은 데이터베이스 연결 설정에 필요한 드라이버 클래스명, URL, 사용자 이름, 암호 등을 포함하고 있다
			
			driverClass = prop.getProperty("driverClass");
			url = prop.getProperty("url");
			user = prop.getProperty("user");
			password = prop.getProperty("password");
			// 속성 값 할당: prop.getProperty() 메서드를 사용하여 driverClass, url, user, password 변수에 각각에 해당하는 속성 값을 가져온다 
			// getProperty() 메서드는 주어진 키에 해당하는 값을 반환하며, 파일에서 로드한 속성들은 키-값 쌍으로 구성
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			Class.forName(driverClass);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() { // getConnection() 메서드는 JdbcTemplate 클래스에서 데이터베이스 연결을 수행하여 Connection 객체를 반환하는 역할
											   // 이 메서드를 호출하면 데이터베이스와 연결된 Connection 객체가 반환되며, 해당 연결을 사용하여 SQL 문을 실행하고 트랜잭션을 관리
		
		Connection conn = null; // Connection 객체 생성: conn 변수를 null로 초기화
		try {
			conn = DriverManager.getConnection(url, user, password);
			// DriverManager.getConnection() 호출: DriverManager.getConnection(url, user, password)을 통해 JDBC 드라이버 매니저를 사용하여 데이터베이스에 연결
			// url, user, password는 JdbcTemplate 클래스의 클래스 변수로서, 앞서 초기화되어야 하고 getConnection() 메서드는 SQLException을 던질 수 있으므로, 예외 처리 코드가 포함
			conn.setAutoCommit(false);
			// 자동 커밋 비활성화: conn.setAutoCommit(false)을 호출하여 자동 커밋을 비활성화. 트랜잭션을 관리하기 위해 사용. 비활성화되면, 각각의 SQL 문 실행은 개별적인 트랜잭션으로 처리
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
		// Connection 객체 반환: 데이터베이스에 성공적으로 연결된 Connection 객체인 conn을 반환
	}

	public static void commit(Connection conn) { // commit(Connection conn) 메서드는 주어진 Connection 객체를 사용하여 트랜잭션을 커밋하는 역할
												 // 이 메서드를 호출하면 주어진 Connection 객체의 트랜잭션이 커밋되며, 데이터베이스에 변경 내용이 영구적으로 저장
		
		try { // Connection 객체 유효성 검사: if(conn != null && !conn.isClosed())를 사용하여 conn이 null이 아니고 이미 닫힌 상태가 아닌지 확인
			if(conn != null && !conn.isClosed()) // 
				conn.commit(); // 트랜잭션 커밋: conn.commit()을 호출하여 현재 트랜잭션을 커밋. 트랜잭션 내에서 수행된 모든 데이터 변경 작업을 영구적으로 저장하고, 데이터베이스에 반영하는 역할
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void rollback(Connection conn) { // rollback(Connection conn) 메서드는 주어진 Connection 객체를 사용하여 트랜잭션을 롤백하는 역할
												   // 이 메서드를 호출하면 주어진 Connection 객체의 트랜잭션이 롤백되며, 이전 상태로 데이터베이스가 복원
		
		try { // Connection 객체 유효성 검사: if(conn != null && !conn.isClosed())를 사용하여 conn이 null이 아니고 이미 닫힌 상태가 아닌지 확인
			if(conn != null && !conn.isClosed())
				conn.rollback(); // 트랜잭션 롤백: conn.rollback()을 호출하여 현재 트랜잭션을 롤백. 롤백은 트랜잭션 내에서 수행된 모든 데이터 변경 작업을 취소하고, 이전 상태로 되돌리는 역할
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void close(Connection conn) { // close(Connection conn) 메서드는 주어진 Connection 객체를 닫는 역할을 한다
		// 이 메서드를 호출하면 주어진 Connection 객체가 닫히고, 해당 연결은 더 이상 사용할 수 없게 된다. Connection 객체는 데이터베이스와의 연결을 나타내며, 사용이 완료된 후에는 닫아야 한다
		
		try { // Connection 객체 유효성 검사: if(conn != null && !conn.isClosed())를 사용하여 conn이 null이 아니고 이미 닫힌 상태가 아닌지 확인
			if(conn != null && !conn.isClosed())
				conn.close(); // Connection 닫기: conn.close()를 호출하여 Connection 객체를 닫는다, close() 메서드는 Connection 객체의 리소스를 해제하고 연결을 종료하는 역할
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}

	public static void close(PreparedStatement pstmt) { // close(PreparedStatement pstmt) 메서드는 주어진 PreparedStatement 객체를 닫는 역할
							// 이 메서드를 호출하면 주어진 PreparedStatement 객체가 닫히고, 해당 객체는 더 이상 사용할 수 없게된다
							// PreparedStatement 객체는 SQL 문을 미리 컴파일한 후 매개 변수를 바인딩하여 반복적으로 실행할 수 있는 객체입니다. 사용이 완료된 후에는 명시적으로 닫아한다
		
		try { // PreparedStatement 객체 유효성 검사: if(pstmt != null && !pstmt.isClosed())를 사용하여 pstmt가 null이 아니고 이미 닫힌 상태가 아닌지 확인
			if(pstmt != null && !pstmt.isClosed())
				pstmt.close(); // PreparedStatement 닫기: pstmt.close()를 호출하여 PreparedStatement 객체를 닫는다. 
							   // close() 메서드는 PreparedStatement 객체의 리소스를 해제하고 해당 객체를 닫는 역할
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
	public static void close(ResultSet rset) { // close(ResultSet rset) 메서드는 주어진 ResultSet 객체를 닫는 역할
											   // ResultSet 객체는 SQL 쿼리의 결과를 보유하고, 결과 집합을 탐색하고 읽을 수 있는 객체
		
		try { // ResultSet 객체 유효성 검사: if(rset != null && !rset.isClosed())를 사용하여 rset이 null이 아니고 이미 닫힌 상태가 아닌지 확인
			if(rset != null && !rset.isClosed())
				rset.close(); // ResultSet 닫기: rset.close()를 호출하여 ResultSet 객체를 닫는다. close() 메서드는 ResultSet 객체의 리소스를 해제하고 해당 객체를 닫는 역할
		} catch (SQLException e) {
			e.printStackTrace();
		}				
	}
	
	private int getNextBookNumberFromSequence() { // getNextBookNumberFromSequence() 메서드는 시퀀스를 사용하여 고유한 도서 번호를 생성하는 역할
		
	    // Use a sequence to generate a unique book number
	    try (Connection conn = DriverManager.getConnection(url, user, password);
	    	// Connection 객체 생성: DriverManager.getConnection(url, user, password)를 사용하여 데이터베이스 연결에 필요한 Connection 객체를 생성
	    	//  이때, url, user, password는 JdbcTemplate 클래스의 정적 필드로 초기화된 값
	    		
	         PreparedStatement stmt = conn.prepareStatement("SELECT book_number_sequence.nextval FROM dual");
	    	// PreparedStatement 객체 생성: conn.prepareStatement("SELECT book_number_sequence.nextval FROM dual")를 사용하여 
	    	// SQL 문을 실행할 준비된 PreparedStatement 객체를 생성
	    		
	         ResultSet rs = stmt.executeQuery()) {
	    	// ResultSet 객체 생성 및 쿼리 실행: stmt.executeQuery()를 호출하여 SQL 문을 실행하고 결과를 담는 ResultSet 객체를 생성
	    	
	        if (rs.next()) { // 결과 확인: rs.next()를 호출하여 ResultSet의 첫 번째 행으로 이동.
	            return rs.getInt(1); // rs.getInt(1)을 호출하여 첫 번째 열의 값을 가져와서 고유한 도서 번호로 반환
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return -1; // 기본값 반환: 도서 번호를 가져올 수 없는 경우 -1을 반환
	}
}
