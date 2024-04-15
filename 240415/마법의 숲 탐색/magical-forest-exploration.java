import java.io.*;
import java.util.*;

public class Main {
	static class Point {
		int x;
		int y;
		int spaceNumber;
		public Point(int x, int y, int spaceNumber) {
			super();
			this.x = x;
			this.y = y;
			this.spaceNumber = spaceNumber;
		}
	}
	
	static int R, C, K, map[][], result;
	static Point exits[];
	static int dx[] = {-1, 0, 1, 0}, dy[] = {0, 1, 0, -1};
	static Point curSpace;
	
	static boolean moveBottom() {
		int x = curSpace.x + 1;
		int y = curSpace.y;
		
		if(x < 0 || y < 0 || x >= R + 3 || y >= C || map[x][y] != 0) return false;
		
		for(int i = 0;i < 4;i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if(nx < 0 || ny < 0 || nx >= R + 3 || ny >= C || map[nx][ny] != 0) return false;
		}
		
		curSpace = new Point(x, y, curSpace.spaceNumber);
		
		return true;
	}
	
	static boolean turnLeft() {
		Point temp = new Point(curSpace.x, curSpace.y, curSpace.spaceNumber);
		for(int s = 3; s >= 2;s--) {
			int x = temp.x + dx[s];
			int y = temp.y + dy[s];
			
			if(x < 0 || y < 0 || x >= R + 3 || y >= C || map[x][y] != 0) return false;
			
			for(int i = 0;i < 4;i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				
				if(nx < 0 || ny < 0 || nx >= R + 3 || ny >= C || map[nx][ny] != 0) return false;
			}
			
			temp = new Point(x, y, temp.spaceNumber);
		}
		
		curSpace = temp;
		
		return true;
	}
	
	static boolean turnRight() {
		Point temp = new Point(curSpace.x, curSpace.y, curSpace.spaceNumber);
		for(int s = 1; s <= 2;s++) {
			int x = temp.x + dx[s];
			int y = temp.y + dy[s];
			
			if(x < 0 || y < 0 || x >= R + 3 || y >= C || map[x][y] != 0) return false;
			
			for(int i = 0;i < 4;i++) {
				int nx = x + dx[i];
				int ny = y + dy[i];
				
				if(nx < 0 || ny < 0 || nx >= R + 3 || ny >= C || map[nx][ny] != 0) return false;
			}
			
			temp = new Point(x, y, temp.spaceNumber);
		}
		
		curSpace = temp;
		
		return true;
	}
	
	static boolean checkOut() {
		int x = curSpace.x;
		int y = curSpace.y;
		
		if(map[x][y] != 0) return true;
		
		for(int i = 0;i < 4;i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if(nx < 3 || ny < 0 || ny >= C) return true;
		}
		
		return false;
	}
	
	static void moveRobot() {
		boolean visited[][] = new boolean[R + 3][C];
		Deque<Point> q = new ArrayDeque<>();
		q.add(new Point(curSpace.x, curSpace.y, curSpace.spaceNumber));
		visited[curSpace.x][curSpace.y] = true;
		
		int score = 0;
		
		while(!q.isEmpty()) {
			Point temp = q.remove();
			
			for(int i = 0;i < 4;i++) {
				int nx = temp.x + dx[i];
				int ny = temp.y + dy[i];
				
				if(nx < 3 || ny < 0 || nx >= R + 3 || ny >= C || map[nx][ny] == 0 || visited[nx][ny]) continue;
				
				// 서로 다른 우주선이라면
				if(map[temp.x][temp.y] != map[nx][ny]) {
					if(temp.x != exits[temp.spaceNumber].x || temp.y != exits[temp.spaceNumber].y) continue;
					visited[nx][ny] = true;
					score = Math.max(score, nx);
					q.add(new Point(nx, ny, map[nx][ny]));
					continue;
				}
				
				visited[nx][ny] = true;
				score = Math.max(score, nx);
				q.add(new Point(nx, ny, temp.spaceNumber));
			}
		}
		result += score - 2;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str[] = br.readLine().split(" ");
		
		R = Integer.parseInt(str[0]);
		C = Integer.parseInt(str[1]);
		K = Integer.parseInt(str[2]);
		
		map = new int[R + 3][C];
		exits = new Point[K + 1];
		
		for(int k = 1;k <= K;k++) {
			str = br.readLine().split(" ");
			int col = Integer.parseInt(str[0]) - 1;
			int exitIdx = Integer.parseInt(str[1]);
			
			curSpace = new Point(1, col, k);
			while(true) {
				if(moveBottom()) {
					continue;
				}
				if(turnLeft()) {
					exitIdx = (exitIdx -1 + 4) % 4;
					continue;
				}
				if(turnRight()) {
					exitIdx = (exitIdx + 1) % 4;
					continue;
				}
				break;
			}
			
			// 로켓이 벗어났나 확인
			if(checkOut()) {
				map = new int[R + 3][C];
				continue;
			}
			
			exits[k] = new Point(curSpace.x + dx[exitIdx], curSpace.y + dy[exitIdx], k);
			// map 채우기
			map[curSpace.x][curSpace.y] = k;
			for(int i = 0;i < 4;i++) {
				map[curSpace.x + dx[i]][curSpace.y + dy[i]] = k;
			}
			
			// 로봇 이동
			moveRobot();
		}
		
		System.out.println(result);
	}

}