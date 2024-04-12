import java.io.*;
import java.util.*;

public class Main {
	static class Point {
		int x;
		int y;
		
		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}
	}

	static int N, M, K, map[][], squareLen, result = 0;
	static int dx[] = {-1, 1, 0, 0}, dy[] = {0, 0, -1, 1};
	static Point exit, startPoint;
	static ArrayList<Point> players = new ArrayList<>();
	
	static void findPlayer(boolean flag) {
		for(int i = 1;i <= N;i++) {
			for(int j = 1;j <= N;j++) {
				if(map[i][j] < 0) {
					for(int k = map[i][j]; k < 0;k++) players.add(new Point(i, j));
					if(flag) map[i][j] = 0;
				}
			}
		}
	}
	 
	static void movePlayer() {
		for(int i = players.size() - 1; i >= 0;i--) {
			boolean isMoved = false;
			Point temp = players.remove(i);
			for(int d = 0;d < 4;d++) {
				int nx = temp.x + dx[d];
				int ny = temp.y + dy[d];
				
				// 범위를 벗어나거나 벽이면 가지 못함
				if(nx <= 0 || ny <= 0 || nx > N || ny > N || map[nx][ny] > 0) continue;
				
				// 출구까지의 최단 거리가 현재보다 멀거나 같으면 가지 않음
				if(Math.abs(exit.x - nx) + Math.abs(exit.y - ny) >= Math.abs(exit.x - temp.x) + Math.abs(exit.y - temp.y)) continue;
				isMoved = true;
				if(nx == exit.x && ny == exit.y) {
					result += 1;
					break;
				}
				
				map[nx][ny] -= 1;
				result += 1;
				break;
			}
			if(!isMoved) {
				map[temp.x][temp.y] -= 1;
			}
		}
	}
	
	static void findSquare() {
		// 정사각형 변의 길이
		find:
		for(int i = 2;i < N;i++) { 
			for(int r = 1; r <= N - i;r++) {
				for(int c = 1;c <= N - i;c++) {
					boolean containExit = false, containPlayer = false;
					if((exit.x >= r && exit.x < r + i) && (exit.y >= c && exit.y < c + i)) {
						containExit = true;
					}
					else continue;
					for(Point player : players) {
						if((player.x >= r && player.x < r + i) && (player.y >= c && player.y < c + i)) containPlayer = true;
					}
					
					if(containExit && containPlayer) {
						squareLen = i;
						startPoint = new Point(r, c);
						break find;
					}
				}
			}
		}
	}
	
	static void rotate() {
		// 돌릴 부분만 복사
		int temp[][] = new int[squareLen][squareLen];
		int tempX = 0;
		for(int i = startPoint.x; i < startPoint.x + squareLen;i++) {
			int tempY = 0;
			for(int j = startPoint.y; j < startPoint.y + squareLen;j++) {
				temp[tempX][tempY] = map[i][j];
				if(exit.x == i && exit.y == j) temp[tempX][tempY] = -2;
				tempY += 1;
			}
			tempX += 1;
		}
		
		// 복사본 돌리기
		tempX = 0;
		for(int i = startPoint.x; i < startPoint.x + squareLen;i++) {
			int tempY = 0;
			for(int j = startPoint.y; j < startPoint.y + squareLen;j++) {
				map[i][j] = temp[squareLen - tempY - 1][tempX];
				if(map[i][j] > 0) map[i][j] -= 1;
				if(map[i][j] == -2) {
					map[i][j] = 0;
					exit = new Point(i, j);
				}
				tempY += 1;
			}
			tempX += 1;
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str[] = br.readLine().split(" ");
		
		N = Integer.parseInt(str[0]);
		M = Integer.parseInt(str[1]);
		K = Integer.parseInt(str[2]);
		
		map = new int[N + 1][N + 1];
		for(int i = 1;i <= N;i++) {
			str = br.readLine().split(" ");
			for(int j = 0;j < N;j++) map[i][j + 1] = Integer.parseInt(str[j]);
		}
		
		for(int i = 0;i < M;i++) {
			str = br.readLine().split(" ");
			int x = Integer.parseInt(str[0]);
			int y = Integer.parseInt(str[1]);
			players.add(new Point(x, y));
		}
		
		str = br.readLine().split(" ");
		exit = new Point(Integer.parseInt(str[0]), Integer.parseInt(str[1]));
		
		for(int k = 0;k < K;k++) {
			movePlayer();
			findPlayer(false);
			if(players.isEmpty()) break;
			findSquare();
			rotate();
			players = new ArrayList<>();
			findPlayer(true);
		}
		
		System.out.println(result);
		System.out.println(exit.x + " " + exit.y);
	}

}