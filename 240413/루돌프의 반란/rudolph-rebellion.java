import java.io.*;
import java.util.*;

public class Main {
	static class Santa {
		int x;
		int y;
		int dir;
		boolean crash; // 충돌 여부
		boolean dead;

		public Santa(int x, int y, int dir, boolean crash, boolean dead) {
			super();
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.crash = crash;
			this.dead = dead;
		}
	}

	static class Point {
		int x;
		int y;
		int dir;
		int depth;

		public Point(int x, int y, int dir, int depth) {
			super();
			this.x = x;
			this.y = y;
			this.dir = dir;
			this.depth = depth;
		}
	}

	static int N, M, P, C, D, map[][], score[];
	static Santa santas[];
	static Point rudolf;
	static int dx[] = { -1, 0, 1, 0, 1, 1, -1, -1 }, dy[] = { 0, 1, 0, -1, 1, -1, 1, -1 };
	static Queue<Integer> curCrashed = new LinkedList<>();

	// 상호 작용
	static void chainCollision(int idx, int dir) {
		int nx = santas[idx].x + dx[dir];
		int ny = santas[idx].y + dy[dir];

		if (nx <= 0 || ny <= 0 || nx > N || ny > N) {
			santas[idx].dead = true;
			return;
		}

		if (map[nx][ny] != 0 && !santas[map[nx][ny]].dead)
			chainCollision(map[nx][ny], rudolf.dir);

		map[santas[idx].x][santas[idx].y] = 0;
		map[nx][ny] = idx;
		santas[idx].x = nx;
		santas[idx].y = ny;
	}

	// 루돌프가 산타에게 충돌
	static void RudolfToSanta(int idx) {
		score[idx] += C;
		int nx = santas[idx].x + (dx[rudolf.dir] * C);
		int ny = santas[idx].y + (dy[rudolf.dir] * C);

		if (nx <= 0 || ny <= 0 || nx > N || ny > N) {
//			System.out.println("루돌프가 산타에게 부딪혀 장외 홈런~");
			map[santas[idx].x][santas[idx].y] = 0;
			santas[idx].dead = true;
			return;
		}

		if (map[nx][ny] != 0 && !santas[map[nx][ny]].dead)
			chainCollision(map[nx][ny], rudolf.dir);

		map[santas[idx].x][santas[idx].y] = 0;
		map[nx][ny] = idx;
		santas[idx].x = nx;
		santas[idx].y = ny;
	}

	// 루돌프를 움직이는 함수
	static void moveRudolf() {
		// 거리가 가장 가까운 산타 찾기
		int closeSanta = 0, closeDist = Integer.MAX_VALUE;
		for (int i = 1; i <= P; i++) {
			if (santas[i].dead)
				continue;
			int dist = Math.abs(santas[i].x - rudolf.x) * Math.abs(santas[i].x - rudolf.x)
					+ Math.abs(santas[i].y - rudolf.y) * Math.abs(santas[i].y - rudolf.y);
			if (closeDist > dist) {
				closeSanta = i;
				closeDist = dist;
			} else if (closeDist == dist) {
				// 거리가 같다면 x가 크고 x가 같다면 y가 큰 산타 저장
				if (santas[i].x > santas[closeSanta].x)
					closeSanta = i;
				else if (santas[i].x == santas[closeSanta].x) {
					if (santas[i].y > santas[closeSanta].y)
						closeSanta = i;
				}
			}
		}
		
		// dir 찾기
		int moveDir = 0, minDist = Integer.MAX_VALUE;
		for(int i = 0;i < 8;i++) {
			int nx = rudolf.x + dx[i];
			int ny = rudolf.y + dy[i];
			
			if(nx <= 0 || ny <= 0 || nx > N || ny > N) continue;
			
			int dist = Math.abs(santas[closeSanta].x - nx) * Math.abs(santas[closeSanta].x - nx)
					+ Math.abs(santas[closeSanta].y - ny) * Math.abs(santas[closeSanta].y - ny);
			
			if(minDist > dist) {
				moveDir = i;
				minDist = dist;
			}
		}
		
		// 찾은 dir로 루돌프 이동
		rudolf = new Point(rudolf.x + dx[moveDir], rudolf.y + dy[moveDir], moveDir, 0);
//		System.out.println("이동한 루돌프 위치: " + rudolf.x + " " + rudolf.y + " " + rudolf.dir);

		// 루돌프가 이동한 후, 이동한 곳에 산타가 있고 산타가 죽지 않았다면 충돌
		if (map[rudolf.x][rudolf.y] != 0 && !santas[map[rudolf.x][rudolf.y]].dead) {
//			System.out.println("루돌프가 산타에게 충돌!!");
			int crashNumber = map[rudolf.x][rudolf.y];
			RudolfToSanta(map[rudolf.x][rudolf.y]);
			santas[crashNumber].crash = true;
			curCrashed.add(crashNumber);
		}
	}

	// 산타가 루돌프에게 충돌
	static void SantaToRudolf(int idx, int dir) {
		score[idx] += D;

		int nx = santas[idx].x + dx[(dir + 2) % 4];
		int ny = santas[idx].y + dy[(dir + 2) % 4];

		// 장외 홈런 -> 죽음
		if (nx <= 0 || ny <= 0 || nx > N || ny > N) {
//			System.out.println("산타가 루돌프에게 부딪혀 장외 홈런~");
			map[santas[idx].x][santas[idx].y] = 0;
			santas[idx].dead = true;
			return;
		}

		if (map[nx][ny] != 0 && !santas[map[nx][ny]].dead) {
			chainCollision(map[nx][ny], (dir + 2) % 4);
		}

		map[santas[idx].x][santas[idx].y] = 0;
		map[nx][ny] = idx;
		santas[idx].x = nx;
		santas[idx].y = ny;
		santas[idx].dir = (dir + 2) % 4;
	}

	// 산타를 움직이는 함수
	static void moveSanta(int idx) {
		// 루돌프에게 가까워지기 위한 dir 찾기
		// 4방향의 nx, ny를 구하고 루돌프와의 거리가 줄어들고 산타가 없는 방향이면 이동
		int dist = Math.abs(santas[idx].x - rudolf.x) * Math.abs(santas[idx].x - rudolf.x)
				+ Math.abs(santas[idx].y - rudolf.y) * Math.abs(santas[idx].y - rudolf.y);
		int minDist = Integer.MAX_VALUE, moveDir = -1;
		for (int i = 0; i < 4; i++) {
			int nx = santas[idx].x + dx[i];
			int ny = santas[idx].y + dy[i];

			if (nx <= 0 || ny <= 0 || nx > N || ny > N || map[nx][ny] != 0)
				continue;
			
			int nDist = Math.abs(nx - rudolf.x) * Math.abs(nx - rudolf.x)
					+ Math.abs(ny - rudolf.y) * Math.abs(ny - rudolf.y);
			
			if(nDist >= dist) continue;
			if(map[nx][ny] != 0) continue;
			
			if(minDist >= nDist) {
				if(minDist == nDist) continue;
				minDist = nDist;
				moveDir = i;
			}
		}
		
		if(moveDir == -1) return;
		
		map[santas[idx].x][santas[idx].y] = 0;
		int nx = santas[idx].x + dx[moveDir];
		int ny = santas[idx].y + dy[moveDir];
		// 루돌프에 부딪혔다면.
		if (rudolf.x == nx && rudolf.y == ny) {
//			System.out.println(idx + "번 산타가 루돌프에게 충돌!!!");
			SantaToRudolf(idx, moveDir);
			curCrashed.add(idx);
		} else {
			santas[idx].x = nx;
			santas[idx].y = ny;
			santas[idx].dir = moveDir;
			map[nx][ny] = idx;
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String str[] = br.readLine().split(" ");
		N = Integer.parseInt(str[0]);
		M = Integer.parseInt(str[1]);
		P = Integer.parseInt(str[2]);
		C = Integer.parseInt(str[3]);
		D = Integer.parseInt(str[4]);
		map = new int[N + 1][N + 1]; // 산타 위치 표시

		str = br.readLine().split(" ");
		rudolf = new Point(Integer.parseInt(str[0]), Integer.parseInt(str[1]), 0, 0);

		santas = new Santa[P + 1];
		score = new int[P + 1];
		for (int i = 0; i < P; i++) {
			str = br.readLine().split(" ");
			int idx = Integer.parseInt(str[0]);
			int x = Integer.parseInt(str[1]);
			int y = Integer.parseInt(str[2]);
			santas[idx] = new Santa(x, y, 0, false, false);
			map[x][y] = idx;
		}

		while (M-- > 0) {
			// 루돌프 이동
			moveRudolf();
			// 순서대로 산타 이동
			for (int i = 1; i <= P; i++) {
				if (santas[i].crash || santas[i].dead)
					continue;
				moveSanta(i);
			}
			// 현재 충돌한 산타를 제외하고 true였던 crash를 false로 돌리기
			for (int i = 1; i <= P; i++) {
				if (santas[i].crash)
					santas[i].crash = false;
			}

			while (!curCrashed.isEmpty()) {
				int temp = curCrashed.remove();
				santas[temp].crash = true;
			}

			// 살아 있는 산타에게 점수 주기
			for (int i = 1; i <= P; i++) {
				if (!santas[i].dead)
					score[i] += 1;
			}
			
//			for(int i = 1;i <= N;i++) {
//				for(int j = 1;j <= N;j++) System.out.print(map[i][j] + "\t");
//				System.out.println();
//			}
		}

		for (int i = 1; i <= P; i++) {
			System.out.print(score[i] + " ");
		}
	}
}