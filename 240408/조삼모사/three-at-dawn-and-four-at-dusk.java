import java.io.*;
/*
    조합하고 최솟값
*/
public class Main {
    static int n, r;
    static int work[][];
    static boolean isSelected[];
    static int answer = Integer.MAX_VALUE;

    static void comb(int idx, int cnt) {
        if(cnt == r) {
            int morning = 0, night = 0;
            for(int i = 0;i < n;i++) {
                for(int j = 0;j < n;j++) {
                    if(!isSelected[i] && !isSelected[j]) morning += work[i][j];
                    else if(isSelected[i] && isSelected[j]) night += work[i][j];
                }
            }

            answer = Math.min(answer, Math.abs(morning - night));

            return;
        }

        for(int i = idx;i < n;i++) {
            if(!isSelected[i]) {
                isSelected[i] = true;
                comb(i + 1, cnt + 1);
                isSelected[i] = false;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());

        work = new int[n][n];
        isSelected = new boolean[n];
        String str[];
        for(int i = 0;i < n;i++) {
            str = br.readLine().split(" ");
            for(int j = 0;j < n;j++) work[i][j] = Integer.parseInt(str[j]);
        }
        r = n / 2;
        comb(0, 0);

        System.out.println(answer);
    }
}