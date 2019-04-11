package Main;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TO {


    public static void setTimeoutSync(Runnable runnable, int delay) {
        try {
            Thread.sleep(delay);
            System.out.println("oi");
            runnable.run();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void main(String args[]) {
//        setTimeoutSync(() -> System.out.println("test"), 3000);

        

    }

}
