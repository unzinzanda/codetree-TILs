import java.io.*;
import java.util.*;

public class Main {

    static int n, T, board[][];
    static int dx[] = {0, 0, 1, -1, 1, 1, -1, -1};
    static int dy[] = {1, -1, 0, 0, 1, -1, 1, -1};
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str[] = br.readLine().split(" ");

        n = Integer.parseInt(str[0]);
        T = Integer.parseInt(str[1]);

        board = new int[n][n];

        for(int i = 0;i < n;i++) {
            str = br.readLine().split(" ");
            for(int j = 0;j < n;j++) {
                board[i][j] = Integer.parseInt(str[j]);
            }
        }

        for(int t = 0;t < T;t++) {
            for(int cnt = 1;cnt <= n * n;cnt++) {
                int x = 0, y = 0;
                boolean flag = false;
                for(x = 0;x < n;x++) {
                    for(y = 0;y < n;y++) {
                        if(board[x][y] == cnt) {
                            flag = true;
                            break;
                        }
                    }
                    if(flag) break;
                }

                int moveX = 0, moveY = 0, maxValue = 0;
                for(int i = 0;i < 8;i++) {
                    int nx = x + dx[i];
                    int ny = y + dy[i];

                    if(nx < 0 || ny < 0 || nx >= n || ny >= n || maxValue > board[nx][ny]) continue;

                    moveX = nx;
                    moveY = ny;
                    maxValue = board[nx][ny];
                }

                board[moveX][moveY] = cnt;
                board[x][y] = maxValue;
            }
        }

        for(int i = 0;i < n;i++) {
            for(int j = 0;j < n;j++) System.out.print(board[i][j] + " ");
            System.out.println();
        }
    }
}