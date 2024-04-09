import java.io.*;
import java.util.*;
/*
    현재 칸보다 큰 숫자로 이동(t회)
    이동할 수 있는 칸이 많으면 우선순위 높은 곳으로 이동(우선순위 상 > 하 > 좌 > 우)
    이동 못하면 정지
    같은 칸에 구슬 여러 개 안착 -> 모두 깨짐

    최종적으로 남는 구슬의 수

    한 단계에 움직이는 구슬 -> BFS
*/

public class Main {
    static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str[] = br.readLine().split(" ");
        int n = Integer.parseInt(str[0]);
        int m = Integer.parseInt(str[1]);
        int T = Integer.parseInt(str[2]);

        int board[][] = new int[n][n];
        for(int i = 0;i < n;i++) {
            str = br.readLine().split(" ");
            for(int j = 0;j < n;j++) board[i][j] = Integer.parseInt(str[j]);
        }

        int dx[] = {-1, 1, 0, 0}, dy[] = {0, 0, -1, 1};
        Queue<Point> q = new LinkedList<>();

        for(int i = 0;i < m;i++) {
            str = br.readLine().split(" ");
            q.add(new Point(Integer.parseInt(str[0]) - 1, Integer.parseInt(str[1]) - 1));
        }

        for(int t = 0; t < T;t++) {
            int crash[][] = new int[n][n];
            
            int size = q.size();

            for(int i = 0;i < size;i++) {
                Point temp = q.remove();
                Point move = new Point(temp.x, temp.y);

                for(int j = 0;j < 4;j++) {
                    int nx = temp.x + dx[j];
                    int ny = temp.y + dy[j];

                    if(nx < 0 || ny < 0 || nx >= n || ny >= n || board[move.x][move.y] >= board[nx][ny]) continue;
                    move = new Point(nx, ny);
                }

                crash[move.x][move.y]++;
                if(move.x == temp.x && move.y == temp.y) continue;
                q.add(move);
            }

            size = q.size();
            for(int s = 0;s < size;s++) {
                Point temp = q.remove();
                if(crash[temp.x][temp.y] == 1) q.add(temp);
                else {
                    m -= crash[temp.x][temp.y];
                    crash[temp.x][temp.y] = 0;
                }
            }
        }

        System.out.println(m);
    }  
}