package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Scanner;

public class BoardExample {
	
	private Scanner scanner = new Scanner(System.in);
	private Connection conn;
	
	public BoardExample() {
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			
			conn = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1521/orcl",
				"java",
				"oracle"
			);
		} catch(Exception e) {
			e.printStackTrace();
			exit();
		}
	}
	
	public void list() {
		System.out.println();
		System.out.println("[게시물 목록]");
		System.out.println("-----------------------------------------------------");
		System.out.printf("%-6s%-12s%-16s%-40s\n", "no", "writer", "date", "title");
		System.out.println("-----------------------------------------------------");
		
		try {
			String sql = ""+
				"SELECT bno, btitle, bcontent, bwriter, bdate "+
				"FROM boards "+
				"ORDER BY bno DESC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Board board = new Board();
				board.setBno(rs.getInt("bno"));
				board.setBtitle(rs.getString("btitle"));
				board.setBcontent(rs.getString("bcontent"));
				board.setBwriter(rs.getString("bwriter"));
				board.setBdate(rs.getDate("bdate"));
				System.out.printf("%-6s%-12s%-16s%-40s \n", 
						board.getBno(),
						board.getBwriter(),
						board.getBdate(),
						board.getBcontent());
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		
		mainMenu();
	}
	

	public void login_list(String id) {
		System.out.println();
		System.out.println("[게시물 목록] 사용자: "+id);
		System.out.println("-----------------------------------------------------");
		System.out.printf("%-6s%-12s%-16s%-40s\n", "no", "writer", "date", "title");
		System.out.println("-----------------------------------------------------");
		
		try {
			String sql = ""+
				"SELECT bno, btitle, bcontent, bwriter, bdate "+
				"FROM boards "+
				"ORDER BY bno DESC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				Board board = new Board();
				board.setBno(rs.getInt("bno"));
				board.setBtitle(rs.getString("btitle"));
				board.setBcontent(rs.getString("bcontent"));
				board.setBwriter(rs.getString("bwriter"));
				board.setBdate(rs.getDate("bdate"));
				System.out.printf("%-6s%-12s%-16s%-40s \n", 
						board.getBno(),
						board.getBwriter(),
						board.getBdate(),
						board.getBcontent());
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			exit();
		}
		
		login_mainMenu();
	}
	
	
	public void mainMenu() {
		
		int no;
		
		System.out.println();
		System.out.println("------------------------------------------------------------------");
		System.out.println("메인 메뉴: 1.Create | 2.Read | 3.Clear | 4.Join | 5. Login | 6.Exit");
		while(true) {
			System.out.println("메뉴 선택: ");
			no = scanner.nextInt();
			scanner.nextLine();
			System.out.println();
			if(no>=1&&no<=6) {
				break;
			} else {
				System.out.println("잘못된 숫자입니다. 다시 입력하세요.");
			}
		}
		
		String menuNo = Integer.toString(no);
		
		switch(menuNo) {
			case "1"->create();
			case "2"->read();
			case "3"->clear();
			case "4"->join();
			case "5"->login();
			case "6"->exit();
		}
	}
	

	public void login_mainMenu() {
		
		int no;
		
		System.out.println();
		System.out.println("------------------------------------------------------------------");
		System.out.println("메인 메뉴: 1.Create | 2.Read | 3.Clear | 4.Logout | 5.Exit");
		while(true) {
			System.out.println("메뉴 선택: ");
			no = scanner.nextInt();
			scanner.nextLine();
			System.out.println();
			if(no>=1&&no<=5) {
				break;
			} else {
				System.out.println("잘못된 숫자입니다. 다시 입력하세요.");
			}
		}
		
		String menuNo = Integer.toString(no);
		
		switch(menuNo) {
			case "1"->create();
			case "2"->read();
			case "3"->clear();
			case "4"->logout();
			case "5"->exit();
		}
	}
	
	public void create() {
		Board board = new Board();
		System.out.println("[새 게시물 입력]");
		System.out.print("제목: ");
		board.setBtitle(scanner.nextLine());
		System.out.print("내용 : ");
		board.setBcontent(scanner.nextLine());
		System.out.print("작성자: ");
		board.setBwriter(scanner.nextLine());
		
		System.out.println("-----------------------------------------------------");
		System.out.println("보조 메뉴: 1.Ok | 2.Cancel");
		System.out.print("메뉴 선택: ");
		while(true) {
			String menuNo = scanner.nextLine();
			if(menuNo.equals("1")) {
				try {
					String sql = ""+
						"INSERT INTO boards(bno, btitle, bcontent, bwriter, bdate) "+
						"VALUES (SEQ_BNO.NEXTVAL, ?, ?, ?, SYSDATE)";
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, board.getBtitle());
					pstmt.setString(2, board.getBcontent());
					pstmt.setString(3, board.getBwriter());
					pstmt.executeUpdate();
					pstmt.close();
					break;
				} catch(Exception e) {
					e.printStackTrace();
					exit();
				}
			} else if(menuNo.equals("2")) {
				break;

			} else {
				System.out.print("다시 입력하세요: ");
			}
		}
		
		list();
	}
	
	public void read() {
		
		System.out.println("[게시물 읽기]");
		System.out.print("bno: ");
		int bno = Integer.parseInt(scanner.nextLine());
		
		//해당번호가 존재하는지 확인
		//check(bno);
		
		try {
			String sql = ""+
				"SELECT bno, btitle, bcontent, bwriter, bdate "+
				"FROM boards "+
				"WHERE bno=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bno);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()) {
				Board board = new Board();
				board.setBno(rs.getInt("bno"));
				board.setBtitle(rs.getString("btitle"));
				board.setBcontent(rs.getString("bcontent"));
				board.setBwriter(rs.getString("bwriter"));
				board.setBdate(rs.getDate("bdate"));
				System.out.println("##########");
				System.out.println("번호: "+board.getBno());
				System.out.println("제목: "+board.getBtitle());
				System.out.println("내용: "+board.getBcontent());
				System.out.println("작성자: "+board.getBwriter());
				System.out.println("날짜: "+board.getBdate());
				
				//보조메뉴
				System.out.println("----------");
				System.out.println("보조 메뉴: 1.Update | 2.Delete | 3.List");
				System.out.print("메뉴 선택: ");
				String menuNo = scanner.nextLine();
				System.out.println();
				
				if(menuNo.equals("1")) {
					update(board);
				} else if(menuNo.equals("2")) {
					delete(board);
				}
				System.out.println("##########");
			}
			rs.close();
			pstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
			exit();
		}
		
		list();
	}
	
	public void clear() {
		System.out.println("[게시물 전체 삭제]");
		System.out.println("-----------------------------------------------------");
		System.out.println("보조 메뉴: 1.Ok | 2.Cancel");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		if(menuNo.equals("1")) {
			try {
				String sql = "TRUNCATE TABLE boards";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		
		list();
	}
	
	public void join() {
		System.out.println("[새 사용자 입력]");
		User user = new User();
		System.out.print("아이디: ");
		user.setUserId(scanner.nextLine());
		System.out.print("이름: ");
		user.setUserName(scanner.nextLine());
		System.out.print("비밀번호: ");
		user.setUserPassword(scanner.nextLine());
		System.out.print("나이: ");
		user.setUserAge(scanner.nextInt());
		scanner.nextLine();
		System.out.print("이메일: ");
		user.setUserEmail(scanner.nextLine());
		
		System.out.println("-----------------------------------------------------");
		System.out.println("보조 메뉴: 1.Ok | 2.Cancel");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		if(menuNo.equals("1")) {
			try {
				String sql = ""+
							"INSERT INTO users(userid, username, userpassword, userage, useremail"+
							"VALUES(?,?,?,?,?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, user.getUserId());
				pstmt.setString(2, user.getUserName());
				pstmt.setString(3, user.getUserPassword());
				pstmt.setInt(4, user.getUserAge());
				pstmt.setString(5, user.getUserEmail());
				
				pstmt.executeUpdate();
				pstmt.close();
			} catch (Exception e) {
				e.printStackTrace();
				exit();
			}
		}
		
		list();
	}
	
	public void login() {
		System.out.println("[로그인]");
		System.out.print("아이디: ");
		String id = scanner.nextLine();
		System.out.print("비밀번호: ");
		String password = scanner.nextLine();
		
		//int result = check(id, password);
		
		System.out.println("-----------------------------------------------------");
		System.out.println("보조 메뉴: 1.Ok | 2.Cancel");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		if(menuNo.equals("1")) {
			int result = check("winter", "1234");
			if(result==0) {
				login_list(id);
			} else if(result==1) {
				System.out.println("비밀번호가 일치하지 않습니다.");
				System.out.println();
				login();
			} else {
				System.out.println("아이디가 존재하지 않습니다.");
				System.out.println();
				login();
			}
		}	
	}
	public void exit() {
		System.out.println("나갑니다~");
		if(conn!=null) {
			try {
				conn.close();
			} catch(SQLException e) {
			}
		}
		
		System.out.println("**게시판 종료**");
		System.exit(0);
	}
	
	public void update(Board board) {
		System.out.println("[수정 내용 입력]");
		System.out.print("제목: ");
		board.setBtitle(scanner.nextLine());
		System.out.print("내용: ");
		board.setBcontent(scanner.nextLine());
		System.out.print("작성자: ");
		board.setBwriter(scanner.nextLine());
		
		//보조 메뉴 출력
		System.out.println("-----------------------------------------------------");
		System.out.println("보조 메뉴: 1.Ok | 2.Cancel");
		System.out.print("메뉴 선택: ");
		String menuNo = scanner.nextLine();
		
		//1.Ok
		if(menuNo.equals("1")) {
			try {
				String sql = "UPDATE boards SET btitle=?, bcontent=?, bwriter=?"+"WHERE bno=?";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, board.getBtitle());
				pstmt.setString(2, board.getBcontent());
				pstmt.setString(3, board.getBwriter());
				pstmt.setInt(4, board.getBno());
				pstmt.executeUpdate();
				pstmt.close();
			} catch(Exception e) {
				e.printStackTrace();
				exit();
			}
			
		//2.Cancel은 그냥 지나가면 됨
		} 
		
		list();
	}
	
	public void delete(Board board) {
		try {
			String sql = "DELETE FROM boards WHERE bno=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getBno());
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e) {
			e.printStackTrace();
			exit();
		}
		
		list();
	}
	
	public int check(String id, String password) {
		int result=2;
		try {
			String sql = "{? = call user_login(?, ?)}";
			
			CallableStatement cstmt = conn.prepareCall(sql);
			cstmt.registerOutParameter(1, Types.INTEGER);
			cstmt.setString(2, id);
			cstmt.setString(3, password);
			
			cstmt.execute();
			result=cstmt.getInt(1);
			System.out.println("result: "+result);
			
			cstmt.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("result: "+result);
		return result;
	
	}
	
	public void logout() {
		list();
	}
	

	public static void main(String[] args) {
		BoardExample boardExample = new BoardExample();
		boardExample.list();
	}

}
