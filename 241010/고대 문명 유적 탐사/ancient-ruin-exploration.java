import java.io.*;
import java.util.*;

public class Main {
    static class Point implements Comparable<Point> {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public int compareTo(Point o) {
            if(this.y == o.y) return o.x - this.x;
            return this.y - o.y;
        }
    }
    static int K, M, map[][], pieces[], result, pieceIdx;
    static int dx[] = {1, -1, 0, 0}, dy[] = {0, 0, 1, -1};
    static Point center;
    static int dfsCnt, maxCost, angle;
    static boolean visited[][], visitedForRemove[][], flag;
    static ArrayList<Point> removeList = new ArrayList<>(), tempList;

    static void rotate(int x, int y) {
        // 회전할 3X3 배열 생성
        int temp[][] = new int[3][3];
        int newMap[][] = new int[3][3];
        int r = 0;
        for(int i = x - 1;i <= x + 1;i++) {
            int c = 0;
            for(int j = y - 1;j <= y + 1;j++) {
                newMap[r][c] = map[i][j];
                temp[r][c++] = map[i][j];
            }
            r++;
        }

        // 회전
        for(int i = 0;i < 3;i++) {
            for(int j = 0;j < 3;j++) {
                newMap[i][j] = temp[2 - j][i];
            }
        }

        // map 배열에 돌린 배열 넣기
        r = 0;
        for(int i = x - 1;i <= x + 1;i++) {
            int c = 0;
            for(int j = y - 1;j <= y + 1;j++) map[i][j] = newMap[r][c++];
            r++;
        }
    }

    static void dfs(int x, int y, int relicType) {
        visited[x][y] = true;
        dfsCnt++;
        for(int i = 0;i < 4;i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if(nx < 0 || ny < 0 || nx >= 5 || ny >= 5 || visited[nx][ny] || map[nx][ny] != relicType) continue;
            dfs(nx, ny, relicType);
        }
    }
    static void dfsForRemove(int x, int y, int relicType) {
        visitedForRemove[x][y] = true;
        tempList.add(new Point(x, y));
        for(int i = 0;i < 4;i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];

            if(nx < 0 || ny < 0 || nx >= 5 || ny >= 5 || visitedForRemove[nx][ny] || map[nx][ny] != relicType) continue;
            dfsForRemove(nx, ny, relicType);
        }
    }

    static void removeRelics() {
        visited = new boolean[5][5];
        int cost = 0;
        for(int i = 0;i < 5;i++) {
            for(int j = 0;j < 5;j++) {
                if(!visited[i][j]) {
                    dfsCnt = 0;
                    dfs(i, j, map[i][j]);
                    if(dfsCnt >= 3) {
                        cost += dfsCnt;
                        flag = true;
                        tempList = new ArrayList<>();
                        visitedForRemove = new boolean[5][5];
                        dfsForRemove(i, j, map[i][j]);
                        removeList.addAll(removeList.size(), tempList);
                    }
                }
            }
        }
        result += cost;
    }

    // 유물 확인
    static void findRelics(int x, int y, int radius) {
        visited = new boolean[5][5];
        int cost = 0;
        for(int i = 0;i < 5;i++) {
            for(int j = 0;j < 5;j++) {
                if(!visited[i][j]) {
                    dfsCnt = 0;
                    dfs(i, j, map[i][j]);
                    if(dfsCnt >= 3) {
                        cost += dfsCnt;
                    }
                }
            }
        }

        if(cost != 0) {
            flag = true;
            if(cost > maxCost) {
                maxCost = cost;
                angle = radius;
                center = new Point(x, y);
            } else if(cost == maxCost) {
                if(radius < angle) {
                    angle = radius;
                    center = new Point(x, y);
                } else if(radius == angle) {
                    if(y < center.y) {
                        center = new Point(x, y);
                    } else if(y == center.y) {
                        if(x < center.x) {
                            center = new Point(x, y);
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        String str[] = br.readLine().split(" ");
        K = Integer.parseInt(str[0]);
        M = Integer.parseInt(str[1]);

        map = new int[5][5];
        pieces = new int[M];
        for(int i = 0;i < 5;i++) {
            str = br.readLine().split(" ");
            for(int j = 0;j < 5;j++) map[i][j] = Integer.parseInt(str[j]);
        }
        str = br.readLine().split(" ");
        for(int i = 0;i < M;i++) pieces[i] = Integer.parseInt(str[i]);

        for(int k = 0;k < K;k++) {
            // 획득할 수 있는 유물을 발견하면 true
            flag = false;
            // 턴마다 출력할 값 초기화
            result = 0;
            maxCost = 0;
            removeList = new ArrayList<>();

            // 회전 선택
            angle = 360;
            center = new Point(5, 5);
            for(int i = 1;i < 4;i++) {
                for(int j = 1;j < 4;j++) {
                    // 90도 회전 후 확인
                    rotate(i, j);
                    // 유물 여부 확인
                    findRelics(i, j,90);
                    // 180도 회전 후 확인
                    rotate(i, j);
                    //유물 여부 확인;
                    findRelics(i, j,180);
                    //270도 회전 후 확인
                    rotate(i, j);
                    //유물 여부 확인
                    findRelics(i, j,270);
                    //원상 복구
                    rotate(i, j);
                }
            }
            if(!flag) break;

            for(int i = 0;i < angle / 90;i++) rotate(center.x, center.y);

            // 새로운 유물 채우기
            removeRelics();
            Collections.sort(removeList);
            for(Point p : removeList) {
                map[p.x][p.y] = pieces[pieceIdx++];
            }

            removeList.clear();

            while(true) {
                flag = false;
                removeRelics();
                if(!flag) break;
                Collections.sort(removeList);
                for(Point p : removeList) {
                    map[p.x][p.y] = pieces[pieceIdx++];
                }
                removeList.clear();
            }

            sb.append(result).append(" ");
        }
        System.out.println(sb.toString());
    }
}