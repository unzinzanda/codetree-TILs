import java.io.*;
import java.util.*;

/*
 * n 차 드래곤 커브 = n - 1 차 드래곤 커브 dir을 역순으로 탐색하며 시계 90도 돌려서 이어붙인 거
 * */

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int N = Integer.parseInt(br.readLine());
		boolean board[][] = new boolean[100][100];
		int dx[] = {0, -1, 0, 1}, dy[] = {1, 0, -1, 0};
		
		// 드래곤 커브 만들기
		for(int n = 0;n < N;n++) {
			String str[] = br.readLine().split(" ");
			int x = Integer.parseInt(str[0]);
			int y = Integer.parseInt(str[1]);
			int d = Integer.parseInt(str[2]);
			int g = Integer.parseInt(str[3]);
			
			board[x][y] = true;
			x += dx[d];
			y += dy[d];
			board[x][y] = true;
			ArrayList<Integer> directions = new ArrayList<>();
			directions.add(d);
			for(int i = 1;i <= g;i++) {
				int end = directions.size() - 1;
				for(int j = end; j >= 0;j--) {
					int dir = directions.get(j);
					dir = (dir + 1) % 4;
					x += dx[dir];
					y += dy[dir];
					board[x][y] = true;
					directions.add(dir);
				}
			}
		}
		
		int result = 0;
		// 정사각형 개수 세기
		for(int i = 0;i < 100;i++) {
			for(int j = 0;j < 100;j++) {
				if(board[i][j]) {
					if(i + 1 < 100 && j +  1 < 100) {
						if(board[i + 1][j] && board[i][j + 1] && board[i + 1][j + 1]) result++;
					}
				}
			}
		}
		
		System.out.println(result);
	}

}