public class QuickSort  {
    private project[] values;
    private int number;

    public void sort(project[] value) {
        // check for empty or null array
        if (value ==null || value.length==0){
            return;
        }
        values = value;
        number = values.length;
        quicksort(0, number - 1);
    }

    private void quicksort(int low, int high) {
        int i = low, j = high;
        // Get the pivot element from the middle of the list
        int pivot = (values[low + (high-low)/2]).getPenaltyForItem();

        // Divide into two lists
        while (i <= j) {
            // If the current value from the left list is smaller than the pivot
            // element then get the next element from the left list
            while ((values[i]).getPenaltyForItem() > pivot) {
                i++;
            }
            // If the current value from the right list is larger than the pivot
            // element then get the next element from the right list
            while ((values[j]).getPenaltyForItem() < pivot) {
                j--;
            }

            // If we have found a value in the left list which is larger than
            // the pivot element and if we have found a value in the right list
            // which is smaller than the pivot element then we exchange the
            // values.
            // As we are done we can increase i and j
            if (i <= j) {
                exchange(i, j);
                i++;
                j--;
            }
        }
        // Recursion
        if (low < j)
            quicksort(low, j);
        if (i < high)
            quicksort(i, high);
    }

    private void exchange(int i, int j) {
        project temp = values[i];
        values[i] = values[j];
        values[j] = temp;
    }
}