import java.io.*;

public class Main {
    static int n, numbers[], minRes = Integer.MAX_VALUE, maxRes = Integer.MIN_VALUE;
    static char oper[];
    static boolean isSelected[];
    static int order[];

    static void perm(int cnt) {
        if(cnt == n - 1) {
            int result = numbers[0];
            int idx = 1;
            for(int i = 0;i < n - 1;i++) {
                if(oper[order[i]] == '+') result += numbers[idx++];
                else if(oper[order[i]] == '-') result -= numbers[idx++];
                else result *= numbers[idx++];
            }

            minRes = Math.min(minRes, result);
            maxRes = Math.max(maxRes, result);
            return;
        }

        for(int i = 0;i < n - 1;i++) {
            if(!isSelected[i]) {
                isSelected[i] = true;
                order[cnt] = i;
                perm(cnt + 1);
                isSelected[i] = false;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        numbers = new int[n];
        oper = new char[n - 1];
        isSelected = new boolean[n - 1];
        order = new int[n - 1];

        String str[] = br.readLine().split(" ");
        for(int i = 0;i < n;i++) numbers[i] = Integer.parseInt(str[i]);

        str = br.readLine().split(" ");
        int idx = 0;
        for(int i = 0;i < Integer.parseInt(str[0]);i++) {
            oper[idx++] = '+';
        }
        for(int i = 0;i < Integer.parseInt(str[1]);i++) {
            oper[idx++] = '-';
        }
        for(int i = 0;i < Integer.parseInt(str[2]);i++) {
            oper[idx++] = '*';
        }


        perm(0);

        System.out.println(minRes + " " + maxRes);
    }
}