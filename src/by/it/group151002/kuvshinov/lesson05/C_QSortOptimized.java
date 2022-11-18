package by.it.group151002.kuvshinov.lesson05;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
Видеорегистраторы и площадь 2.
Условие то же что и в задаче А.
        По сравнению с задачей A доработайте алгоритм так, чтобы
        1) он оптимально использовал время и память:
            - за стек отвечает элиминация хвостовой рекурсии,
            - за сам массив отрезков - сортировка на месте
            - рекурсивные вызовы должны проводиться на основе 3-разбиения
        2) при поиске подходящих отрезков для точки реализуйте метод бинарного поиска
        для первого отрезка решения, а затем найдите оставшуюся часть решения
        (т.е. отрезков, подходящих для точки, может быть много)
    Sample Input:
    2 3
    0 5
    7 10
    1 6 11
    Sample Output:
    1 0 0
*/


public class C_QSortOptimized {

    //отрезок
    private class Segment implements Comparable<Segment>{
        int start;
        int stop;

        Segment(int start, int stop){
            if (start > stop) {
                this.stop = start;
                this.start = stop;
            } else {
                this.start = start;
                this.stop = stop;
            }
        }
        @Override
        public int compareTo(Segment o) {
            //подумайте, что должен возвращать компаратор отрезков
            if (this.start > o.start){
                return 1;
            } else if (this.start < o.start){
                return -1;
            }
            return 0;
        }
    }


    public int[] getAccessory2(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        int m = scanner.nextInt();
        int[] points = new int[m];
        int[] result = new int[m];
        for (int i = 0; i < n; i++)
            segments[i] = new Segment(scanner.nextInt(), scanner.nextInt());
        for (int i = 0; i < m; i++)
            points[i] = scanner.nextInt();
        qSort(segments, 0, segments.length - 1);
        for (int i = 0; i < points.length; i++) {
            int index = binarySearch(segments, points[i]);
            if (index == -1)
                result[i] = 0;
            else {
                int count = 1, j = index - 1;
                while (j >= 0 && segments[j].start == segments[index].start) {
                    count++;
                    j--;
                }
                j = index + 1;
                while (j < segments.length && segments[j].start == segments[index].start) {
                    count++;
                    j++;
                }
                result[i] = count;
            }
        }
        return result;
    }

    public int binarySearch(Segment[] segments, int point) {
        int l = 0, r = segments.length - 1, m;
        while (l <= r) {
            m = (l + r) / 2;
            if (segments[m].start > point)
                r = m - 1;
            if (segments[m].stop >= point && segments[m].start <= point)
                return m;
            else
                l = m + 1;
        }
        return -1;
    }

    public int[] partition(Segment[] segments, int l, int r) {
        int core = l + (int) (Math.random() * (r - l + 1));
        int coreStart = segments[core].start;
        int i = l, j = r, k = l;
        while (k <= j) {
            if (segments[k].start > coreStart) {
                changePos(segments, k, j--);
            } else if (segments[k].start < coreStart) {
                changePos(segments, i++, k++);
            } else
                k++;
        }
        return new int[] { i, j };
    }

    public void qSort(Segment[] segments, int l, int r) {
        while (l < r) {
            int[] cores = partition(segments, l, r);
            if (cores[0] - l < r - cores[1]) {
                qSort(segments, l, cores[0] - 1);
                l = cores[1] + 1;
            } else {
                qSort(segments, cores[1] + 1, r);
                r = cores[0] - 1;
            }
        }
    }

    public void changePos(Segment[] segments, int i, int j){
        Segment buff = segments[i];
        segments[i] = segments[j];
        segments[j] = buff;
    }


    public static void main(String[] args) throws FileNotFoundException {
        String root = System.getProperty("user.dir") + "/src/";
        InputStream stream = new FileInputStream(root + "by/it/group151002/kuvshinov/lesson05/dataC.txt");
        C_QSortOptimized instance = new C_QSortOptimized();
        int[] result=instance.getAccessory2(stream);
        for (int index:result){
            System.out.print(index+" ");
        }
    }

}