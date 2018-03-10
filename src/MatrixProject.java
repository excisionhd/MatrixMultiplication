/***
 * file: MatrixProject.java
 * author: Amir Sotoodeh
 * class: CS 331 - Design and Analysis of Algorithms
 *
 * assignment: Project 1
 * date last modified: 2/14/18
 *
 * purpose: Compare the performance of three matrix multiplication algorithms.
 *
 **/
import java.util.Random;

public class MatrixProject {

    public static void main(String[] args) {
        final int FINALSIZE = 4;
        Random r = new Random();
        int[][] matrix1 = new int[FINALSIZE][FINALSIZE];
        int[][] matrix2 = new int[FINALSIZE][FINALSIZE];

        for (int i = 0; i<matrix1.length;i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                matrix1[i][j] = r.nextInt(5);
                matrix2[i][j] = r.nextInt(5);
            }
        }

        //ANALYSIS OF STANDARD MATRIX MULTIPLICATION
        long startTime = System.nanoTime();
        int[][] result = multiply(matrix1, matrix2);
        long stopTime = System.nanoTime();
        System.out.println("Standard Matrix Multiplication:");
        System.out.println("Time taken: " + (stopTime - startTime) + " nano seconds.");

        //PRINT MATRIX
        for (int i = 0; i < result.length; i++) { // aRow
            for (int j = 0; j < result.length; j++) { // bColumn
                System.out.print(result[i][j]+" ");
            }
            System.out.println();
        }


        //ANALYSIS OF DIVIDE AND CONQUER MULTIPLICATION
        startTime = System.nanoTime();
        int[][] result2 = DivideAndConquer(matrix1,matrix2);
        stopTime = System.nanoTime();
        System.out.println("Divide and Conquer Matrix Multiplication:");
        System.out.println("Time taken: "+ (stopTime - startTime) +" nano seconds.");

        //PRINT MATRIX
        for (int i = 0; i < result2.length; i++) { // aRow
            for (int j = 0; j < result2.length; j++) { // bColumn
                System.out.print(result[i][j]+" ");
            }
            System.out.println();
        }


        //ANALYSIS OF STRASSEN MULTIPLICATION
        startTime = System.nanoTime();
        int[][] result3 = Strassen(matrix1,matrix2);
        stopTime = System.nanoTime();
        System.out.println("Strassen Matrix Multiplication");
        System.out.println("Time taken: "+ (stopTime - startTime) +" nano seconds.");

        //PRINT MATRIX
        for (int i = 0; i < result3.length; i++) { // aRow
            for (int j = 0; j < result3.length; j++) { // bColumn
                System.out.print(result[i][j]+" ");
            }
            System.out.println();
        }


    }

    //STANDARD MATRIX MULTIPLICATION
    public static int[][] multiply(int[][] A, int[][] B) {

        //         ROW COL        ROW COL
        // EXAMPLE: 2 x 2 multiply 2 x 3
        // AColumn must be equal to BRow to perform multiplication.
        int aRows = A.length;
        int aColumns = A[0].length;
        int bRows = B.length;
        int bColumns = B[0].length;

        if (aColumns != bRows) {
            System.out.println("Cannot perform multiplication due to compatibility error.");
            System.exit(0);
        }

        int[][] C = new int[aRows][bColumns];
        for (int i = 0; i < aRows; i++) {
            for (int j = 0; j < bColumns; j++) {
                C[i][j] = 0;
            }
        }

        for (int i = 0; i < aRows; i++) { // aRow
            for (int j = 0; j < bColumns; j++) { // bColumn
                for (int k = 0; k < aColumns; k++) { // aColumn
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }

    public static int[][] DivideAndConquer(int[][] A, int[][] B){

        return  DCRecursion(
                A, B, 0, 0,
                0,0, A.length);

    }

    public static int[][] DCRecursion(int[][] A, int[][] B, int rowA, int colA, int rowB, int colB, int size){
        //INITIALIZE NEW ARRAY
        int[][] C= new int[size][size];

        //BASE CASE
        if(size==1)
            C[0][0]= A[rowA][colA]*B[rowB][colB];

        //RECURSIVELY DIVIDE INTO 4 SUBSECTIONS
        else{
            int newSize= size/2;
            //SUBSECTION C11
            transfer(C, DCRecursion(A, B, rowA, colA, rowB, colB, newSize), DCRecursion(A, B, rowA, colA+newSize, rowB+ newSize, colB, newSize), 0, 0);
            //SUBSECTION C12
            transfer(C, DCRecursion(A, B, rowA, colA, rowB, colB + newSize, newSize), DCRecursion(A, B, rowA, colA+newSize, rowB+ newSize, colB+newSize, newSize), 0, newSize);
            //SUBSECTION C21
            transfer(C, DCRecursion(A, B, rowA+ newSize, colA, rowB, colB, newSize), DCRecursion(A, B, rowA+ newSize, colA+newSize, rowB+ newSize, colB, newSize), newSize, 0);
            //SUBSECTION C22
            transfer(C, DCRecursion(A, B, rowA+ newSize, colA, rowB, colB+newSize, newSize), DCRecursion(A, B, rowA+ newSize, colA+newSize, rowB+ newSize, colB+newSize, newSize), newSize, newSize);
        }
        return C;
    }

    //HELPER FUNCTION TO COMPUTE AND TRANSFER VALUES
    private static void transfer(int[][] C, int[][]A, int[][]B,int rowC, int colC){
        int len=A.length;
        for(int i =0; i<len; i++){
            for(int j=0; j<len; j++)
                C[i+rowC][j+colC]=A[i][j]+B[i][j];
        }

    }

    //STRASSEN IMPLEMENTATION FOLLOWING PSEUDOCODE
    public static int[][] Strassen(int[][] a, int[][] b)
    {
        int len = a.length;
        int[][] R = new int[len][len];

        // OUTPUT AxB
        if(len==1)
            R[0][0] = a[0][0]*b[0][0];

        //COMPUTE ALL VALUES a11,b11 ..., a22,b22
        else
        {
            //INITIALIZE 8 SUBARRAYS
            int[][] a11 = new int[len/2][len/2];
            int[][] a12 = new int[len/2][len/2];
            int[][] a21 = new int[len/2][len/2];
            int[][] a22 = new int[len/2][len/2];
            int[][] b11 = new int[len/2][len/2];
            int[][] b12 = new int[len/2][len/2];
            int[][] b21 = new int[len/2][len/2];
            int[][] b22 = new int[len/2][len/2];

            //DIVIDE AND PLACE INTO NEW SUBARRAYS
            separate(a,a11,0,0);
            separate(a,a12,0,len/2);
            separate(a,a21,len/2,0);
            separate(a,a22,len/2,len/2);
            separate(b,b11,0,0);
            separate(b,b12,0,len/2);
            separate(b,b21,len/2,0);
            separate(b,b22,len/2,len/2);

            //COMPUTE 7 RECURSIVE CALLS USING HELPER FUNCTIONS
            int[][] M1 = Strassen(add(a11, a22), add(b11, b22));
            int[][] M2 = Strassen(add(a21, a22), b11);
            int[][] M3 = Strassen(a11, sub(b12, b22));
            int[][] M4 = Strassen(a22, sub(b21, b11));
            int[][] M5 = Strassen(add(a11, a12), b22);
            int[][] M6 = Strassen(sub(a21, a11), add(b11, b12));
            int[][] M7 = Strassen(sub(a12, a22), add(b21, b22));

            //COMPUTE ARITHMETIC
            int [][] C11 = add(sub(add(M1, M4), M5), M7);
            int [][] C12 = add(M3, M5);
            int [][] C21 = add(M2, M4);
            int [][] C22 = add(sub(add(M1, M3), M2), M6);

            //PUT BACK TOGETHER FOR FINAL RESULT
            fuse(C11, R, 0 , 0);
            fuse(C12, R, 0 , len/2);
            fuse(C21, R, len/2, 0);
            fuse(C22, R, len/2, len/2);


        }
        return R;
    }

    //FUNCTION COMPUTES ADDITION ARITHMETIC
    public static int[][] add(int[][] a,int[][] b)
    {
        int n = a.length;
        int[][] c = new int[n][n];
        for(int i = 0;i<n;i++)
        {
            for(int j=0;j<n;j++)
                c[i][j] = a[i][j] + b[i][j];
        }
        return c;
    }

    //FUNCTION COMPUTES SUBTRACTION ARITHMETIC (copy of add)
    public static int[][] sub(int[][] a,int[][] b)
    {
        int n = a.length;
        int[][] c = new int[n][n];
        for(int i = 0;i<n;i++)
        {
            for(int j=0;j<n;j++)
                c[i][j] = a[i][j] - b[i][j];
        }
        return c;
    }

    //FUNCTION SEPARATES ORIGINAL ARRAY INTO SUBSECTIONS
    public static void separate(int[][]A,int[][]C,int i,int j)
    {
        int i2 = i;
        for(int i1=0;i1< C.length;i1++)
        {
            int j2 = j;
            for(int j1=0; j1<C.length;j1++)
            {
                C[i1][j1] = A[i2][j2];
                j2++;
            }
            i2++;
        }
    }

    //FUNCTION COMBINES MATRICES
    public static void fuse(int[][]A,int[][]C,int i,int j)
    {
        int i2 = i;
        for(int i1=0;i1< A.length;i1++)
        {
            int j2 = j;
            for(int j1=0; j1< A.length;j1++)
            {
                C[i2][j2] = A[i1][j1];
                j2++;
            }
            i2++;
        }
    }



}
