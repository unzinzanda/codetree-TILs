import java.io.*;
/*
    현재 칸보다 큰 숫자로 이동(t회)
    이동할 수 있는 칸이 많으면 우선순위 높은 곳으로 이동(우선순위 상 > 하 > 좌 > 우)
    이동 못하면 정지
    같은 칸에 구슬 여러 개 안착 -> 모두 깨짐

    최종적으로 남는 구슬의 수

    한 단계에 움직이는 구슬 -> BFS
*/

public class Main {
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
        int count[][] = new int[n][n];

        for(int i = 0;i < m;i++) {
            str = br.readLine().split(" ");
            count[Integer.parseInt(str[0]) - 1][Integer.parseInt(str[1]) - 1] = 1;
        }


        for(int t = 0; t < T;t++) {
            int nextCount[][] = new int[n][n];

            // 구슬 옮기기
            for(int i = 0;i < n;i++) {
                for(int j = 0;j < n;j++) {
                    if(count[i][j] == 1) {
                        int moveX = i, moveY = j;
                        int maxNumber = -1;

                        for(int k = 0;k < 4;k++) {
                            int nx = i + dx[k];
                            int ny = j + dy[k];

                            if(nx < 0 || ny < 0 || nx >= n || ny >= n || board[nx][ny] <= maxNumber) continue;
                            
                            maxNumber = board[nx][ny];
                            moveX = nx;
                            moveY = ny;
                        }

                        nextCount[moveX][moveY] += 1;
                    }
                }
            }

            // 충돌한 구슬 지우고 count로 복사
            for(int i = 0;i < n;i++) {
                for(int j = 0;j < n;j++) {
                    if(nextCount[i][j] > 1) {
                        m -= nextCount[i][j];
                        nextCount[i][j] = 0;
                    }
                    count[i][j] = nextCount[i][j];
                }
            }
        }

        System.out.println(m);
    }  
}