import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int work[][] = new int[n][2];
        String str[];
        for(int i = 0;i < n;i++) {
            str = br.readLine().split(" ");
            work[i][0] = Integer.parseInt(str[0]);
            work[i][1] = Integer.parseInt(str[1]);
        }

        int dp[] = new int[n + 1];

        for(int i = 0;i < n;i++) {
            dp[i + work[i][0]] = Math.max(dp[i] + work[i][1], dp[i + work[i][0]]);
        }

        System.out.println(dp[n]);
    }
}