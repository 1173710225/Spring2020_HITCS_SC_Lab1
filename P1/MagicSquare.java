package P1;

import java.io.*;

public class MagicSquare {

	private BufferedReader in;

	boolean isLegalMagicSquare(String fileName) {
		String filePath = "src\\P1\\txt\\" + fileName; // 引号内字符串表示文件输入路径
		String line; // 声明读取文件的每行
		int i = 0, j = 0; // 分别表示矩阵的行、列
		int len = 0; // 声明方形矩阵的边长
		int sum = 0; // 声明数组第一行之和
		try {
			in = new BufferedReader(new FileReader(filePath));
			line = in.readLine(); // 读取第一行信息
			if (line.contains("\\t")) { // 判断是否含有'\t'
				System.out.println(fileName + " is not spliy by \t");
				return false;
			}
			String[] st = line.split("\t");
			len = st.length; // 令第一行的数据个数作为矩阵的边长
			int[][] sq = new int[len][len]; // 二维数组模拟Magicsquare
			while (line != null) {
				String[] str = line.split("\t");
				if (len != str.length) {
					System.out.println(fileName + " is not Square");
					return false;
				} // 判断是否与第一行等长
				for (j = 0; j < str.length; j++) {
					if (Double.valueOf(str[j]) % 1 == 0)
						sq[i][j] = Integer.valueOf(str[j]);
					else {
						System.out.println(fileName + " is not Integer");
						return false;
					} // 为i行数组赋值
				}
				line = in.readLine(); // 读取下一行
				i++;
			}
			in.close();
			if (i != len) {
				System.out.println(fileName + "Is not Square");
				return false;
			} // 判断行数是否等于每一行的长度
			for (j = 0; j < len; j++) {
				sum += sq[0][j];
			} // 以第一行之和作为比较依据
			int sum3 = 0, sum4 = 0; // 两个对角线之和
			for (i = 0; i < len; i++) {
				int sum1 = 0;
				int sum2 = 0;
				for (j = 0; j < len; j++) {
					sum1 += sq[i][j];
					sum2 += sq[j][i];
				}
				if (sum1 != sum || sum2 != sum)
					return false;
				sum3 += sq[i][i];
				sum4 += sq[i][len - i - 1];
			}
			if (sum3 != sum || sum4 != sum)
				return false;
		} catch (IOException iox) {
			System.out.println("Problem reading " + fileName);
		}
		return true;
	}

	public static boolean generateMagicSquare(int n) {
		if (n % 2 != 1) {
			System.out.println("Wrong n!");
			return false;
		}

		int magic[][] = new int[n][n];
		int row = 0, col = n / 2, i, j, square = n * n; // col此时表示矩阵的最中间的一列

		for (i = 1; i <= square; i++) {
			magic[row][col] = i;
			/*
			 * 首个数字 1 填入到第一行最中间列magic[0][n/2] 当刚输入进数组的数i是n整除的， row++ 否则 row =
			 * (row-1+n)%n ,col = (col+1)%n
			 */

			if (i % n == 0)
				row++;
			else {
				if (row == 0)
					row = n - 1;
				else
					row--;
				if (col == (n - 1))
					col = 0;
				else
					col++;
			}
		}
		try {
			String outfilePath = "src\\P1\\txt\\6.txt";
			BufferedWriter out = new BufferedWriter(new FileWriter(outfilePath)); // 写入文件

			for (i = 0; i < n; i++) {
				for (j = 0; j < n; j++) {
					System.out.print(magic[i][j] + "\t");
					out.write(magic[i][j] + "\t");
				}
				System.out.println();
				out.write("\n");
			}
			out.close();
		} catch (IOException iox) {
			System.out.println("Problem writing 6.txt");
		}
		return true;
	}

	public static void main(String[] args) {
		MagicSquare ms = new MagicSquare(); // 创建实例对象调用非static方法（实例方法）
		System.out.println(ms.isLegalMagicSquare("1.txt"));
		System.out.println(ms.isLegalMagicSquare("2.txt"));
		System.out.println(ms.isLegalMagicSquare("3.txt"));
		System.out.println(ms.isLegalMagicSquare("4.txt"));
		System.out.println(ms.isLegalMagicSquare("5.txt"));
		System.out.println(generateMagicSquare(11));
		System.out.println(ms.isLegalMagicSquare("6.txt"));
	}
}
