//Stack.push()
public class Stack {

    private int top ;
    private int element ;
    private int size ;
    private String traceFile ;

    public Stack ( ) {
        top = 0;
        element = 0;
        size = 10;
    }

    public Stack ( String tf ) {
        top = 0;
        element = 0;
        size = 10;
        traceFile = tf;
    }

    public void method1 ( ) {
    }

    public void setSize ( int s ) {
        if ( s > 0 ) {
            size = s;
        }
    }

    public void method2 ( ) {
    }

    //replace > with !=
    public void push ( int el ) {
        if ( top < size && el != 50 ) {
            element = el;
            top = top + 1;
        }
    }
    public void method3 ( ) {
    }

    public int pop ( ) {
        int element = 0;
        if ( top > 2 ) {
            if ( size > 25 ) {
                top = top - 1;
                size = size - 1;
                element = size + top;
            }
        }
        return element;
    }

    public void method4 ( ) {
    }

    public int isEmpty ( ) {
        int result = 0;
        if ( top == 0 ) {
            result = 1;
        }
        return result;
    }

    public void method5 ( ) {
    }

    public void method6 ( ) {
    }

    public int onTop ( ) {
        int element = 5;
        if ( top > 0 ) {
            element = element * top;
        }
        return element;
    }

    public void method7 ( ) {
    }

    public int clear ( ) {
        if ( top > 2 ) {
            if ( size > 25 ) {
                size = 0;
                top = top - top;
            }
        }
        return top;
    }

    public void method8 ( ) {
    }

    public void method9 ( ) {
    }

    public void method10 ( ) {
    }

}