-- Book 테이블 생성
CREATE TABLE Book (
  book_number NUMBER(10) PRIMARY KEY,
  book_name VARCHAR2(50) NOT NULL,
  author VARCHAR2(50) NOT NULL,
  price NUMBER(10, 2) NOT NULL
);

-- Member 테이블 생성
CREATE TABLE Member (
  id VARCHAR2(20) PRIMARY KEY,
  password VARCHAR2(20) NOT NULL,
  gender VARCHAR2(10) NOT NULL,
  email VARCHAR2(50) NOT NULL,
  phone_number VARCHAR2(20) NOT NULL,
  address VARCHAR2(100) NOT NULL,
  points NUMBER(10) DEFAULT 0
);

-- Purchase 테이블 생성
CREATE TABLE Purchase (
  book_number NUMBER(10) REFERENCES Book(book_number),
  book_name VARCHAR2(50) NOT NULL,
  author VARCHAR2(50) NOT NULL,
  price NUMBER(10, 2) NOT NULL,
  CONSTRAINT pk_purchase PRIMARY KEY (book_number, book_name, author)
);서점

-- Book 테이블에 데이터 추가
INSERT INTO Book (book_number, book_name, author, price)
VALUES (1, '불편한 편의점','김호연','10000' );

INSERT INTO Book (book_number, book_name, author, price)
VALUES (2, '새는 날아가면서 뒤돌아보지 않는다','류시화','11000' );

INSERT INTO Book (book_number, book_name, author, price)
VALUES (3, '아낌없이 주는 나무','셸 실버스타인','12000' );

INSERT INTO Book (book_number, book_name, author, price)
VALUES (4, '내 남자친구에게 ','귀여니','9000' );

INSERT INTO Book (book_number, book_name, author, price)
VALUES (5, '구해줘','기욤뮈소','11000' );





