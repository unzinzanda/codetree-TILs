import java.io.*;
import java.util.*;

/*
    과정
    1. 좌회전
    2. 갈 수 있다면 가고 못가면 좌회전
    3. 모든 방향을 확인했는데 못가면 원래 방향 유지하며 후진
    4. 후진하려는데 뒷공간이 인도이면 종료

    dir : 좌회전하려면 -1 방향
    map : 0 이면 도로, 1이면 인도, 2이면 방문한 도로
*/
public class Main {
    static int n, m, map[][], x, y, dir;
    static int dx[] = {-1, 0, 1, 0}, dy[] = {0, 1, 0, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str[] = br.readLine().split(" ");

        n = Integer.parseInt(str[0]);
        m = Integer.parseInt(str[1]);
        map = new int[n][m];

        str = br.readLine().split(" ");
        x = Integer.parseInt(str[0]);
        y = Integer.parseInt(str[1]);
        dir = Integer.parseInt(str[2]);

        for(int i = 0;i < n;i++) {
            str = br.readLine().split(" ");
            for(int j = 0;j < m;j++) {
                map[i][j] = Integer.parseInt(str[j]);
            }
        }
        
        map[x][y] = 2;
        int answer = 1;
        while(true) {
            // 이동하였다면 true로 바꾸기
            boolean flag = false;
            for(int i = 0;i < 4;i++) {
                // 좌회전
                dir = (dir - 1 + 4) % 4;
                
                // 갈 수 있나 확인
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(map[nx][ny] == 1 || map[nx][ny] == 2) continue;

                flag = true;
                x = nx;
                y = ny;

                map[x][y] = 2;
                answer++;
                break;
            }

            if(!flag) {
                int nx = x + dx[(dir + 2) % 4];
                int ny = y + dy[(dir + 2) % 4];

                if(map[nx][ny] == 1) break;
                else {
                    x = nx;
                    y = ny;
                }
            }
        }

        System.out.println(answer);
    }
}