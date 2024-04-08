import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        int n = Integer.parseInt(br.readLine());
        int client[] = new int[n];

        String str[] = br.readLine().split(" ");
        for(int i = 0;i < n;i++) {
            client[i] = Integer.parseInt(str[i]);
        }

        str = br.readLine().split(" ");
        int leader = Integer.parseInt(str[0]);
        int follower = Integer.parseInt(str[1]);

        int answer = n;
        for(int i = 0;i < n;i++) {
            client[i] -= leader;
            if(client[i] > 0) {
                answer += client[i] / follower;
                if(client[i] % follower != 0) answer += 1;
            }
        }

        System.out.println(answer);
    }
}