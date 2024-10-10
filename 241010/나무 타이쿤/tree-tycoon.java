import java.util.*;
import java.io.*;

public class Main {
    static class Point {
        int x;
        int y;
        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static int N, M, map[][];
    static int dx[] = {0, -1, -1, -1, 0, 1, 1, 1}, dy[] = {1, 1, 0, -1, -1, -1, 0, 1};
    static ArrayList<Point> nutrition = new ArrayList<>();
    static boolean isAdd[][];
    static void moveNut(int d, int p) {
        int size = nutrition.size();

        for(int i = size - 1;i >= 0;i--) {
            Point temp = nutrition.remove(i);
            temp.x = (temp.x + dx[d] * p) % N;
            temp.y = (temp.y + dy[d] * p) % N;
            if(temp.x < 0) temp.x += N;
            if(temp.y < 0) temp.y += N;

            nutrition.add(temp);
        }
    }

    static void addNut() {
        for(Point p : nutrition) {
            isAdd[p.x][p.y] = true;
            map[p.x][p.y] += 1;
        }
    }

    static void growTree() {
        for(Point p : nutrition) {
            int cnt = 0;
            for(int i = 1;i < 8;i += 2) {
                int nx = p.x + dx[i];
                int ny = p.y + dy[i];

                if(nx < 0 || ny < 0 || nx >= N || ny >= N || map[nx][ny] <= 0) continue;

                cnt++;
            }

            map[p.x][p.y] += cnt;
        }
    }

    static void cutTree() {
        for(int i = 0;i < N;i++) {
            for(int j = 0;j < N;j++) {
                if(!isAdd[i][j] && map[i][j] >= 2) {
                    map[i][j] -= 2;
                    nutrition.add(new Point(i, j));
                }
            }
        }
    }

    static void print() {
        for(int i = 0;i < N;i++) {
            for(int j = 0;j < N;j++) {
                System.out.print(map[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str[] = br.readLine().split(" ");
        N = Integer.parseInt(str[0]);
        M = Integer.parseInt(str[1]);

        map = new int[N][N];
        for(int i = 0;i < N;i++) {
            str = br.readLine().split(" ");
            for(int j = 0;j < N;j++) map[i][j] = Integer.parseInt(str[j]);
        }

        // 초기 영양제 위치를 리스트에 추가
        for(int i = N - 2;i < N;i++) {
            for(int j = 0;j < 2;j++) nutrition.add(new Point(i, j));
        }

        for(int m = 0;m < M;m++) {
            isAdd = new boolean[N][N];
            str = br.readLine().split(" ");

            // 영양제 위치 이동
            moveNut(Integer.parseInt(str[0]) - 1, Integer.parseInt(str[1]));

            // 영양제 투여 -> 1차 성장
            addNut();

            // 대각선에 위치한 높이 1 이상의 나무 카운트 후 2차 성장
            growTree();
            nutrition.clear();

            // 영양제 투여하지 않은 나무 중 높이 2이상이면 2만큼 베기 -> 영양제 배치
            cutTree();
        }

        int result = 0;
        for(int i = 0;i < N;i++) {
            for(int j = 0;j < N;j++) result += map[i][j];
        }

        System.out.println(result);
    }
}