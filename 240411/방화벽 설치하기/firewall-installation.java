import java.io.*;
import java.util.*;

public class Main {
    static class Point {
		int x;
		int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	static int N, M, map[][], empty, minus, result = 0;
	static int pick[] = new int[3];
	static int dx[] = {0, 0, -1, 1}, dy[] = {1, -1, 0, 0};
	static ArrayList<Point> fire = new ArrayList<>();
	static boolean visited[][];
	
	static void spreadFire(int x, int y) {
		visited[x][y] = true;
		
		for(int i = 0;i < 4;i++) {
			int nx = x + dx[i];
			int ny = y + dy[i];
			
			if(nx < 0 || ny < 0 || nx >= N || ny >= M || visited[nx][ny] || map[nx][ny] != 0) continue;
			
			minus++;
			spreadFire(nx, ny);
		}
	}
	
	static void comb(int idx, int cnt) {
		if(cnt == 3) {
			// 벽 세울 수 있는지 확인
			for(int i = 0;i < 3;i++) {
				int x = pick[i] / M;
				int y = pick[i] % M;
				if(map[x][y] == 2 || map[x][y] == 1) return;
			}
			
			// 벽 세우기
			for(int i = 0;i < 3;i++) {
				int x = pick[i] / M;
				int y = pick[i] % M;
				map[x][y] = 1;
			}
			
			// 불이 안 번지는 영역 확인
			visited = new boolean[N][M];
			minus = 0;
			for(Point temp : fire) {
				spreadFire(temp.x, temp.y);
			}
			
			result = Math.max(result, empty - minus);
			
			// 원상복구
			for(int i = 0;i < 3;i++) {
				int x = pick[i] / M;
				int y = pick[i] % M;
				map[x][y] = 0;
			}
			return;
		}
		
		for(int i = idx;i < N * M;i++) {
			pick[cnt] = i;
			comb(i + 1, cnt + 1);
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str[] = br.readLine().split(" ");
		
		N = Integer.parseInt(str[0]);
		M = Integer.parseInt(str[1]);
		map = new int[N][M];
		
		for(int i = 0;i < N;i++) {
			str = br.readLine().split(" ");
			for(int j = 0;j < M;j++) {
				map[i][j] = Integer.parseInt(str[j]);
				if(map[i][j] == 2) {
					fire.add(new Point(i, j));
				}
				else if(map[i][j] == 0) empty++;
			}
		}
		
		// 벽 세울 공간은 제외
		empty -= 3;
		
		comb(0, 0);
		
		System.out.println(result);
	}
}