package P1;

import java.io.*;

public class MagicSquare {

	private BufferedReader in;

	boolean isLegalMagicSquare(String fileName) {
		String filePath = "src\\P1\\txt\\" + fileName; // �������ַ�����ʾ�ļ�����·��
		String line; // ������ȡ�ļ���ÿ��
		int i = 0, j = 0; // �ֱ��ʾ������С���
		int len = 0; // �������ξ���ı߳�
		int sum = 0; // ���������һ��֮��
		try {
			in = new BufferedReader(new FileReader(filePath));
			line = in.readLine(); // ��ȡ��һ����Ϣ
			if (line.contains("\\t")) { // �ж��Ƿ���'\t'
				System.out.println(fileName + " is not spliy by \t");
				return false;
			}
			String[] st = line.split("\t");
			len = st.length; // ���һ�е����ݸ�����Ϊ����ı߳�
			int[][] sq = new int[len][len]; // ��ά����ģ��Magicsquare
			while (line != null) {
				String[] str = line.split("\t");
				if (len != str.length) {
					System.out.println(fileName + " is not Square");
					return false;
				} // �ж��Ƿ����һ�еȳ�
				for (j = 0; j < str.length; j++) {
					if (Double.valueOf(str[j]) % 1 == 0)
						sq[i][j] = Integer.valueOf(str[j]);
					else {
						System.out.println(fileName + " is not Integer");
						return false;
					} // Ϊi�����鸳ֵ
				}
				line = in.readLine(); // ��ȡ��һ��
				i++;
			}
			in.close();
			if (i != len) {
				System.out.println(fileName + "Is not Square");
				return false;
			} // �ж������Ƿ����ÿһ�еĳ���
			for (j = 0; j < len; j++) {
				sum += sq[0][j];
			} // �Ե�һ��֮����Ϊ�Ƚ�����
			int sum3 = 0, sum4 = 0; // �����Խ���֮��
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
		int row = 0, col = n / 2, i, j, square = n * n; // col��ʱ��ʾ��������м��һ��

		for (i = 1; i <= square; i++) {
			magic[row][col] = i;
			/*
			 * �׸����� 1 ���뵽��һ�����м���magic[0][n/2] ����������������i��n�����ģ� row++ ���� row =
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
			BufferedWriter out = new BufferedWriter(new FileWriter(outfilePath)); // д���ļ�

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
		MagicSquare ms = new MagicSquare(); // ����ʵ��������÷�static������ʵ��������
		System.out.println(ms.isLegalMagicSquare("1.txt"));
		System.out.println(ms.isLegalMagicSquare("2.txt"));
		System.out.println(ms.isLegalMagicSquare("3.txt"));
		System.out.println(ms.isLegalMagicSquare("4.txt"));
		System.out.println(ms.isLegalMagicSquare("5.txt"));
		System.out.println(generateMagicSquare(11));
		System.out.println(ms.isLegalMagicSquare("6.txt"));
	}
}
