public class stack{

    private provider CurrentProvider;

    private stack next;
    static private int length=0;

    public stack(){
        next=null;
    }


    public void addToStack(provider AProvider){
        stack temp = this.next;
        stack.length++;

        stack first = new stack();
        first.CurrentProvider = AProvider;
        first.next=temp;
        this.next=first;
    }


    public int getStackLength(){
        return length;
    }

    public boolean isEmpty(){
        return this.next == null;
    }

    public provider getFromStack(){
        stack first = this.next;
        length--;
        this.next=first.next;

        return first.CurrentProvider;
    }
}