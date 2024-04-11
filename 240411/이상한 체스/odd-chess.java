import java.io.*;
import java.util.*;

/*
 * 생각
 * 1. 그냥 주어진 말 하나씩 방향 선택
 * 2. 마지막 말까지 고른 다음 result 결정
 * */
public class Main {
	static class Piece {
		int x;
		int y;
		int number;
		
		public Piece(int x, int y, int number) {
			this.x = x;
			this.y = y;
			this.number = number;
		}
	}
	
	static int N, M, board[][], temp[][], result = Integer.MAX_VALUE, zeroCnt = 0;
	static ArrayList<Piece> myPiece = new ArrayList<>();
	// 상: 0, 하: 1, 좌: 2, 우: 3
	static int dx[] = {-1, 1, 0, 0}, dy[] = {0, 0, -1, 1};
	static int firstPiece[][] = {{0}, {1}, {2}, {3}};
	static int secondPiece[][] = {{0, 1}, {2, 3}};
	static int thirdPiece[][] = {{0, 2}, {0, 3}, {1, 2}, {1, 3}};
	static int fourthPiece[][] = {{0, 2, 3}, {1, 2, 3}, {0, 1, 2}, {0, 1, 3}};
	static int fifthPiece[][] = {{0, 1, 2, 3}};
	static int order[];
	static int len[] = {0, 4, 2, 4, 4, 1};
	
	static int goPiece(int piece[][], int x, int y, int idx) {
		int cnt = 0;
		
		for(int i = 0;i < piece[order[idx]].length;i++) {
			int tempX = x, tempY = y;
			while(true) {
				int nx = tempX + dx[piece[order[idx]][i]];
				int ny = tempY + dy[piece[order[idx]][i]];
				
				if(nx < 0 || ny < 0 || nx >= N || ny >= M || temp[nx][ny] == 6) break;
				
				tempX = nx;
				tempY = ny;
				if(temp[tempX][tempY] == 0) {
					cnt += 1;
					temp[tempX][tempY] = -1;
				}
			}
		}
		
		return cnt;
	}
	
	static void choice(int cnt) {
		if(cnt == myPiece.size()) {
			temp = new int[N][M];
			for(int i = 0;i < N;i++) {
				for(int j = 0;j < M;j++) temp[i][j] = board[i][j];
			}
			
			int canGo = 0;
			int idx = 0;
			for(Piece p : myPiece) {
				if(p.number == 1) {
					canGo += goPiece(firstPiece, p.x, p.y, idx++);
				} else if(p.number == 2) {
					canGo += goPiece(secondPiece, p.x, p.y, idx++);
				} else if(p.number == 3) {
					canGo += goPiece(thirdPiece, p.x, p.y, idx++);
				} else if(p.number == 4) {
					canGo += goPiece(fourthPiece, p.x, p.y, idx++);
				} else canGo += goPiece(fifthPiece, p.x, p.y, idx++);
			}
			
			result = Math.min(result, zeroCnt - canGo);
			
			return;
		}
		
		for(int i = 0;i < len[myPiece.get(cnt).number];i++) {
			order[cnt] = i;
			choice(cnt + 1);
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str[] = br.readLine().split(" ");
		
		N = Integer.parseInt(str[0]);
		M = Integer.parseInt(str[1]);
		
		board = new int[N][M];
		
		for(int i = 0;i < N;i++) {
			str = br.readLine().split(" ");
			for(int j = 0;j < M;j++) {
				board[i][j] = Integer.parseInt(str[j]);
				
				if(board[i][j] != 0 && board[i][j] != 6) myPiece.add(new Piece(i, j, board[i][j]));
				else if(board[i][j] == 0) zeroCnt += 1;
			}
		}
		
		order = new int[myPiece.size()];
		choice(0);
		
		System.out.println(result);
	}

}