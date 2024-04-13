import java.io.*;
import java.util.*;

public class Main {
	static class Point {
		int x;
		int y;
		int idx;
		public Point(int x, int y, int idx) {
			super();
			this.x = x;
			this.y = y;
			this.idx = idx;
		}
	}
	
	static class BfsPoint { 
		int x;
		int y;
		int dir;
		int depth;
		public BfsPoint(int x, int y, int dir, int depth) {
			super();
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.depth = depth;
		}
	}
	
	static int N, M, map[][], time = 1;
	static Point store[]; // 사람들이 도착해야 할 편의점 좌표 배열
	static boolean notPass[][];
	static Queue<Point> going = new LinkedList<>();
	static int dx[] = {-1, 0, 0, 1}, dy[] = {0, -1, 1, 0};
	
	// 상 좌 우 하 순서로 이동하므로 먼저 찾은 최단 거리가 우선
	static Point findBase(int idx) {
		Point s = store[idx]; // 목적지 편의점
		boolean visited[][] = new boolean[N][N];
		Queue<Point> q = new LinkedList<>();
		visited[s.x][s.y] = true;
		q.add(new Point(s.x, s.y, idx));
		
		while(!q.isEmpty()) {
			Point temp = q.remove();
			
			for(int i = 0;i < 4;i++) {
				int nx = temp.x + dx[i];
				int ny = temp.y + dy[i];
				
				if(nx < 0 || ny < 0 || nx >= N || ny >= N || visited[nx][ny] || notPass[nx][ny]) continue;
				if(map[nx][ny] == 1) return new Point(nx, ny, idx);
				visited[nx][ny] = true;
				q.add(new Point(nx, ny, idx));
			}
		}
		
		return null;
	}
	
	static void entrance(Point start) {
		// 지나갈 수 없닷!!
		notPass[start.x][start.y] = true;
		// 출발한 사람을 큐에 추가.
		going.add(start);
	}
	
	// player : x좌표, y좌표, 플레이어 번호
	// 플레이어 번호로 도착해야 할 편의점의 좌표 알 수 있음
	static void goToStore(Point player) {
		boolean visited[][] = new boolean[N][N];
		Queue<BfsPoint> q = new LinkedList<>();
		visited[player.x][player.y] = true;
		q.add(new BfsPoint(player.x, player.y, -1, 0));
		
		int minDist = Integer.MAX_VALUE;
		int moveDir = -1;
		
		while(!q.isEmpty()) {
			BfsPoint temp = q.remove();
			
			for(int i = 0;i < 4;i++) {
				int nx = temp.x + dx[i];
				int ny = temp.y + dy[i];
				
				if(nx < 0 || ny < 0 || nx >= N || ny >= N || visited[nx][ny] || notPass[nx][ny]) continue;
				// 편의점 도착 여부 확인
				if(nx == store[player.idx].x && ny == store[player.idx].y) {
					if(temp.depth + 1 > minDist) continue;
					
					minDist = temp.depth + 1;
					if(temp.dir == -1) {
						moveDir = i;
					} else moveDir = temp.dir;
					continue;
				}
				
				visited[nx][ny] = true;
				if(temp.dir == -1) {
					q.add(new BfsPoint(nx, ny, i, temp.depth + 1));
				} else q.add(new BfsPoint(nx, ny, temp.dir, temp.depth + 1));
			}
		}
		
		int nx = player.x + dx[moveDir];
		int ny = player.y + dy[moveDir];
		// 도착하면 칸 못 지나가게 처리하고 going 큐에 추가하지 않음
		if(nx == store[player.idx].x && ny == store[player.idx].y) {
			notPass[nx][ny] = true;
		}
		
		// 도착하지 못하면 아직 이동 중이므로 going 큐에 추가
		else {
			going.add(new Point(nx, ny, player.idx));
		}
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str[] = br.readLine().split(" ");
		
		N = Integer.parseInt(str[0]);
		M = Integer.parseInt(str[1]);
		
		map = new int[N][N];
		notPass = new boolean[N][N];
		store = new Point[M];
		
		for(int i = 0;i < N;i++) {
			str = br.readLine().split(" ");
			for(int j = 0;j < N;j++) {
				map[i][j] = Integer.parseInt(str[j]);
			}
		}
		
		for(int i = 0;i < M;i++) {
			str = br.readLine().split(" ");
			store[i] = new Point(Integer.parseInt(str[0]) - 1, Integer.parseInt(str[1]) - 1, i);
		}
		
		// 일단 1번 좌표에 올리기
		Point start = findBase(0);
		entrance(start);
		
		while(true) {
			time += 1;
			
			// 본인이 가려는 편의점으로 이동
			int size = going.size();
			for(int i = 0; i < size;i++) {
				Point s = going.remove();
				goToStore(s);
			}
			
			if(time <= M) {
				start = findBase(time - 1);
				entrance(start);
			}
			
			if(going.isEmpty()) break;
		}
		
		System.out.println(time);
	}

}