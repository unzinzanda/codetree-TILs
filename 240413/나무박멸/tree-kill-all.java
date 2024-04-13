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

	static int n, m, k, c, map[][], result;
	static ArrayList<Point> trees = new ArrayList<>();
	static int dx[] = { 0, 0, 1, -1 }, dy[] = { 1, -1, 0, 0 };
	static int diaX[] = { 1, 1, -1, -1 }, diaY[] = { 1, -1, 1, -1 };
	static int notGrow[][];

	static void grow(Point tree) {
		int cnt = 0;
		for (int i = 0; i < 4; i++) {
			int nx = tree.x + dx[i];
			int ny = tree.y + dy[i];

			if (nx < 0 || ny < 0 || nx >= n || ny >= n || map[nx][ny] <= 0)
				continue;

			cnt++;
		}

		map[tree.x][tree.y] += cnt;
	}

	static void spread() {
		// 번지는 나무를 카운트할 배열 생성
		int temp[][] = new int[n][n];
		for (int i = 0; i < n; i++)
			temp[i] = map[i].clone();

		int size = trees.size();

		for (int s = 0; s < size; s++) {
			Point tree = trees.get(s);
			int cnt = 0;
			Queue<Point> q = new LinkedList<>();
			for (int i = 0; i < 4; i++) {
				int nx = tree.x + dx[i];
				int ny = tree.y + dy[i];

				// 범위를 벗어나거나 벽이거나 제초제가 있거나 다른 나무가 있다면 넘어가잇!
				if (nx < 0 || ny < 0 || nx >= n || ny >= n || map[nx][ny] == -1 || notGrow[nx][ny] > 0
						|| map[nx][ny] > 0)
					continue;

				cnt++;
				q.add(new Point(nx, ny));
			}

			// 번지는 칸에 나무를 심고 처음 나무가 심길 때 trees 배열에 추가
			while (!q.isEmpty()) {
				Point cur = q.remove();
				if (temp[cur.x][cur.y] == 0)
					trees.add(cur);
				temp[cur.x][cur.y] += (temp[tree.x][tree.y] / cnt);
			}
		}

		for (int i = 0; i < n; i++)
			map[i] = temp[i].clone();
	}

	static Point findTree() {
		int maxRemove = 0;
		Point remove = new Point(0, 0);

		for (Point tree : trees) {
			int cnt = map[tree.x][tree.y];
			for (int i = 0; i < 4; i++) {
				for (int j = 1; j <= k; j++) {
					int nx = tree.x + (diaX[i] * j);
					int ny = tree.y + (diaY[i] * j);

					// 범위를 넘어가거나 벽 혹은 나무가 없는 칸을 만나면 더 가지 않는다.
					if (nx < 0 || ny < 0 || nx >= n || ny >= n || map[nx][ny] <= 0)
						break;

					cnt += map[nx][ny];
				}
			}

			if (maxRemove < cnt) {
				maxRemove = cnt;
				remove = tree;
			} else if (maxRemove == cnt) {
				// 행이 더 작은거, 행이 같다면 열이 작은 나무 저장
				if (remove.x == tree.x) {
					if (tree.y < remove.y)
						remove = tree;
				} else if (tree.x < remove.x)
					remove = tree;
			}
		}

		// 박멸할 나무의 수를 여기서 더함
		result += maxRemove;
		return remove;
	}

	static void spray() {
		// 스프레이 뿌릴 칸 고르기
		Point startPoint = findTree();

		map[startPoint.x][startPoint.y] = 0;
		notGrow[startPoint.x][startPoint.y] = c;
		// 제초제 뿌려잇!
		for (int i = 0; i < 4; i++) {
			for (int j = 1; j <= k; j++) {
				int nx = startPoint.x + (diaX[i] * j);
				int ny = startPoint.y + (diaY[i] * j);

				// 범위를 넘어가거나 벽 혹은 나무가 없는 칸을 만나면 더 가지 않는다.
				if (nx < 0 || ny < 0 || nx >= n || ny >= n || map[nx][ny] == -1)
					break;

				// 제초제 뿌리기
				notGrow[nx][ny] = c;
				if (map[nx][ny] == 0)
					break;
				map[nx][ny] = 0;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String str[] = br.readLine().split(" ");
		n = Integer.parseInt(str[0]);
		m = Integer.parseInt(str[1]);
		k = Integer.parseInt(str[2]);
		c = Integer.parseInt(str[3]);

		map = new int[n][n];
		notGrow = new int[n][n]; // 제초제를 뿌린 칸

		for (int i = 0; i < n; i++) {
			str = br.readLine().split(" ");
			for (int j = 0; j < n; j++) {
				map[i][j] = Integer.parseInt(str[j]);

				if (map[i][j] > 0)
					trees.add(new Point(i, j));
			}
		}

		for(int t = 0;t < m;t++) {
			// 나무 성장!
			for (Point tree : trees) {
				if (notGrow[tree.x][tree.y] == 0) {
					grow(tree);
				}
			}

			// 나무 번식
			spread();

			// 제초제 뿌리기
			spray();

			// 제초제가 뿌려져 사라진 나무를 trees 배열에서 제거
			for (int i = trees.size() - 1; i >= 0; i--) {
				Point tree = trees.get(i);
				if (notGrow[tree.x][tree.y] > 0)
					trees.remove(i);
			}
			
			if(t != 0) {
				// 제초제 시간 줄이기
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < n; j++) {
						if (notGrow[i][j] > 0)
							notGrow[i][j] -= 1;
					}
				}
			}
		}

		System.out.println(result);
	}

}